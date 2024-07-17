package uk.co.callumbirks.item

import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World
import uk.co.callumbirks.CobbleEggs

class IncubatingEgg(private val incubator: Incubator, private val egg: Egg) :
    Item(Settings().maxCount(1).maxDamage(100)) {

    override fun use(world: World?, user: PlayerEntity?, hand: Hand?): TypedActionResult<ItemStack> {
        if (world == null || world.isClient || user == null || user !is ServerPlayerEntity) {
            if (user != null) {
                val stack = user.mainHandStack
                return TypedActionResult.pass(stack)
            }
            return TypedActionResult.pass(ItemStack.EMPTY)
        }
        val stack = user.mainHandStack
        if (stack.item !is IncubatingEgg || stack.nbt == null || !stack.nbt!!.contains("steps_done")) {
            return TypedActionResult.pass(stack)
        }
        val stepsDone = stack.nbt!!.getBoolean("steps_done")
        if (!stepsDone) {
            return TypedActionResult.pass(stack)
        }
        return if (CobbleEggs.giveRandomPokemon(user, egg.rarity)) {
            val uses = if (stack.nbt!!.contains("incubator_uses")) stack.nbt!!.getInt("incubator_uses") + 1 else 1
            if (uses == Incubator.MAX_USES) {
                TypedActionResult.success(ItemStack.EMPTY)
            } else {
                val usedIncubator = ItemStack(incubator)
                val nbt = usedIncubator.orCreateNbt
                nbt.putInt("incubator_uses", uses)
                usedIncubator.damage = uses
                TypedActionResult.success(usedIncubator)
            }
        } else {
            TypedActionResult.fail(stack)
        }
    }

    override fun inventoryTick(stack: ItemStack?, world: World?, entity: Entity?, slot: Int, selected: Boolean) {
        if (stack == null) return
        val nbt = stack.orCreateNbt
        if (!nbt.contains("steps_progress")) return

        val stepsProgress = nbt.getInt("steps_progress")
        val progressPct = (stepsProgress * 100) / (stepsRequired())
        stack.damage = 100 - progressPct
    }

    fun updateNbtData(stack: ItemStack, blocksTravelled: Int) {
        if (stack.nbt == null) {
            stack.nbt = NbtCompound()
        }
        if (!stack.nbt!!.contains("steps_done")) {
            stack.nbt!!.putInt("steps_req", blocksTravelled + stepsRequired())
            stack.nbt!!.putBoolean("steps_done", false)
            stack.nbt!!.putInt("steps_progress", 0)
            return
        }
        val stepsDone = stack.nbt!!.getBoolean("steps_done")
        if (stepsDone) {
            return
        }
        val stepsReq = stack.nbt!!.getInt("steps_req")
        val stepsStart = stepsReq - stepsRequired()
        stack.nbt!!.putInt("steps_progress", blocksTravelled - stepsStart)
        if (blocksTravelled > stepsReq) {
            stack.nbt!!.putBoolean("steps_done", true)
            // CobbleEggNetworking.playSoundToClient(entity, cobblemonResource("poke_ball.capture_started"), false)
        }
    }

    private fun stepsRequired(): Int {
        return (egg.stepsRequired / incubator.stepsMultiplier).toInt()
    }
}