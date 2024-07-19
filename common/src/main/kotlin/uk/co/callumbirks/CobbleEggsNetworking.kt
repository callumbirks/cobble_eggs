package uk.co.callumbirks

import dev.architectury.networking.NetworkManager
import dev.architectury.networking.NetworkManager.NetworkReceiver
import io.netty.buffer.Unpooled
import net.minecraft.network.PacketByteBuf
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.sound.SoundEvent
import net.minecraft.util.Identifier
import uk.co.callumbirks.CobbleEggs.cobbleEggsResource

object CobbleEggsNetworking {
    private val registeredPackets = arrayListOf<Packet>()

    val PLAY_SOUND_TO_CLIENT =
        registerPacket("packets/s2c_play_sound", PlaySoundToClient, toClient = true, toServer = false)

    data class Packet(
        val identifier: Identifier, val receiver: NetworkReceiver, val toServer: Boolean, val toClient: Boolean
    ) {
        fun sendToServer(buf: PacketByteBuf) {
            if (!toServer) {
                CobbleEggs.LOGGER.warn("Attempted to send client-only packet '{}' to Server!", identifier)
                return
            }
            NetworkManager.sendToServer(identifier, buf)
        }

        fun sendToClient(target: ServerPlayerEntity, buf: PacketByteBuf) {
            if (!toClient) {
                CobbleEggs.LOGGER.warn("Attempted to send server-only packet '{}' to Client!", identifier)
                return
            }
            NetworkManager.sendToPlayer(target, identifier, buf)
        }
    }

    private fun registerPacket(
        identifier: String, receiver: NetworkReceiver, toServer: Boolean, toClient: Boolean
    ): Packet {
        val packet = Packet(cobbleEggsResource(identifier), receiver, toServer, toClient)
        registeredPackets.add(packet)
        return packet
    }

    fun registerServer() {
        for (packet in registeredPackets.stream().filter { it.toServer }) {
            NetworkManager.registerReceiver(NetworkManager.Side.C2S, packet.identifier, packet.receiver)
        }
    }

    fun registerClient() {
        for (packet in registeredPackets.stream().filter { it.toClient }) {
            NetworkManager.registerReceiver(NetworkManager.Side.S2C, packet.identifier, packet.receiver)
        }
    }

    object PlaySoundToClient : NetworkReceiver {
        override fun receive(buf: PacketByteBuf?, context: NetworkManager.PacketContext?) {
            if (buf == null || context == null) return
            val soundId = buf.readIdentifier()
            val soundEvent = SoundEvent.of(soundId)
            context.player.playSound(soundEvent, 1f, 1f)
        }

        fun send(player: ServerPlayerEntity, soundId: Identifier) {
            val buf = PacketByteBuf(Unpooled.buffer())
            buf.writeIdentifier(soundId)
            PLAY_SOUND_TO_CLIENT.sendToClient(player, buf)
        }
    }
}