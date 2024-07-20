package uk.co.callumbirks

import com.cobblemon.mod.common.util.cobblemonResource
import dev.architectury.event.events.common.TickEvent
import net.minecraft.server.MinecraftServer
import uk.co.callumbirks.item.IncubatingEgg
import uk.co.callumbirks.util.Pedometer

object CobbleEggsEvents : TickEvent.Server {
    fun register() {
        TickEvent.SERVER_PRE.register(this)
    }

    private const val UPDATE_RATE = 20
    private var tickCounter = UPDATE_RATE

    override fun tick(instance: MinecraftServer?) {
        if (instance == null) return
        if (tickCounter < UPDATE_RATE) {
            tickCounter += 1
            return
        }
        tickCounter = 0

        updateIncubatingEggs(instance)
    }

    private fun updateIncubatingEggs(instance: MinecraftServer) {
        for (player in instance.playerManager.playerList) {
            val stepsDelta = Pedometer.getStepsDelta(player)
            var justHatched = false
            for (stack in player.inventory.main + player.inventory.offHand) {
                if (stack.isEmpty || stack.item !is IncubatingEgg) {
                    continue
                }
                if ((stack.item as IncubatingEgg).addNbtSteps(stack, stepsDelta)) {
                    justHatched = true
                }
            }
            if (justHatched) {
                CobbleEggsNetworking.PlaySoundToClient.send(player, cobblemonResource("fossil_machine.finished"))
            }
        }
    }
}