package uk.co.callumbirks.item

import com.cobblemon.mod.common.util.cobblemonResource
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
import uk.co.callumbirks.CobbleEggsNetworking

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
            CobbleEggsNetworking.PlaySoundToClient.send(user, cobblemonResource("poke_ball.capture_succeeded"))
            val uses =
                if (stack.nbt!!.contains("incubator_uses")) stack.nbt!!.getInt("incubator_uses") + 1 else Incubator.MAX_USES
            if (uses == Incubator.MAX_USES) {
                TypedActionResult.success(ItemStack.EMPTY)
            } else {
                val usedIncubator = ItemStack(incubator)
                usedIncubator.nbt!!.putInt("incubator_uses", uses)
                TypedActionResult.success(usedIncubator)
            }
        } else {
            TypedActionResult.fail(stack)
        }
    }

    override fun inventoryTick(stack: ItemStack?, world: World?, entity: Entity?, slot: Int, selected: Boolean) {
        if (stack == null || world == null || world.isClient) return
        val nbt = stack.orCreateNbt
        if (!nbt.contains("steps_progress")) return

        val stepsProgress = nbt.getInt("steps_progress")
        val progressPct = (stepsProgress * 100) / (stepsRequired())
        stack.damage = 100 - progressPct
    }

    /// Update the steps/NBT data for this IncubatingEgg.
    /// Returns TRUE if this IncubatingEgg just finished hatching, FALSE otherwise.
    fun addNbtSteps(stack: ItemStack, stepsDelta: Int): Boolean {
        if (stack.nbt == null) {
            stack.nbt = NbtCompound()
        }
        if (!stack.nbt!!.contains("steps_done")) {
            stack.nbt!!.putBoolean("steps_done", false)
            stack.nbt!!.putInt("steps_progress", 0)
            return false
        }
        val stepsDone = stack.nbt!!.getBoolean("steps_done")
        if (stepsDone) {
            return false
        }
        val stepsProgress = stack.nbt!!.getInt("steps_progress")
        stack.nbt!!.putInt("steps_progress", stepsProgress + stepsDelta)
        return if (stepsProgress >= stepsRequired()) {
            stack.nbt!!.putBoolean("steps_done", true)
            true
        } else {
            false
        }
    }

    private fun stepsRequired(): Int {
        return (egg.stepsRequired / incubator.stepsMultiplier).toInt()
    }
}