package uk.co.callumbirks.item

import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World
import uk.co.callumbirks.CobbleEggs
import uk.co.callumbirks.util.getBlocksTravelled
import kotlin.math.max

class IncubatingEgg(private val incubator: Incubator, private val egg: Egg): Item(Item.Settings().maxCount(1)) {
    companion object {
        const val UPDATE_RATE = 20 // Update nbt steps data every x ticks
        var tickCounter = UPDATE_RATE
    }

    override fun inventoryTick(stack: ItemStack?, world: World?, entity: Entity?, slot: Int, selected: Boolean) {
        if (world?.isClient == true) {
            return
        }
        if (stack == null || entity == null || entity !is ServerPlayerEntity) {
            return
        }
        // TODO: This will count up once per tick for each item in the inventory
        if (tickCounter < UPDATE_RATE) {
            tickCounter += 1
            return
        }
        tickCounter = 0
        val steps = (entity.getBlocksTravelled() * incubator.stepsMultiplier).toInt()
        CobbleEggs.LOGGER.info("Player {} has done {} steps", entity.uuid, steps)
        if (stack.nbt == null) {
            stack.nbt = NbtCompound()
        }
        if (!stack.nbt!!.contains("steps_done")) {
            stack.nbt!!.putInt("steps_req", steps + egg.stepsRequired)
            stack.nbt!!.putBoolean("steps_done", false)
            stack.nbt!!.putInt("steps_progress", 0)
        }
        val stepsDone = stack.nbt!!.getBoolean("steps_done")
        if (stepsDone) {
            return
        }
        val stepsReq = stack.nbt!!.getInt("steps_req")
        val stepsStart = stepsReq - egg.stepsRequired
        stack.nbt!!.putInt("steps_progress", steps - stepsStart)
        if (steps > stepsReq) {
            stack.nbt!!.putBoolean("steps_done", true)
            // CobbleEggNetworking.playSoundToClient(entity, cobblemonResource("poke_ball.capture_started"), false)
        }
    }

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
        return if(CobbleEggs.giveRandomPokemon(user, egg.rarity)) {
            TypedActionResult.success(ItemStack.EMPTY)
        } else {
            TypedActionResult.fail(stack)
        }
    }

    override fun appendTooltip(
        stack: ItemStack?,
        world: World?,
        tooltip: MutableList<Text>?,
        context: TooltipContext?
    ) {
        if (tooltip == null || stack == null || stack.nbt == null || !stack.nbt!!.contains("steps_done")) {
            return
        }

        val stepsDone = stack.nbt!!.getBoolean("steps_done")
        if (stepsDone) {
            tooltip.add(Text.of("Ready to Hatch!"))
            return
        }

        var stepsProgress = stack.nbt!!.getInt("steps_progress")
        val stepsReq = stack.nbt!!.getInt("steps_req")
        val stepsDelta = stepsReq - egg.stepsRequired
        stepsProgress = max(0, stepsProgress - stepsDelta)
        tooltip.add(Text.of("Hatch Progress: ${stepsProgress}/${egg.stepsRequired} blocks"))
    }
}