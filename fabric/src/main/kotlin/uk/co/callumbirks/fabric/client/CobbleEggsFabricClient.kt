package uk.co.callumbirks.fabric.client

import net.fabricmc.api.ClientModInitializer
import uk.co.callumbirks.CobbleEggsNetworking

class CobbleEggsFabricClient : ClientModInitializer {
    override fun onInitializeClient() {
        CobbleEggsNetworking.registerClient()
    }
}