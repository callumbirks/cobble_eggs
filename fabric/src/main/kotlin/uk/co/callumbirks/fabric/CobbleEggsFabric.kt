package uk.co.callumbirks.fabric

import net.fabricmc.api.ModInitializer
import uk.co.callumbirks.CobbleEggs

class CobbleEggsFabric : ModInitializer {
    override fun onInitialize() {
        CobbleEggs.init(CobbleEggsFabricImplementation())
    }
}