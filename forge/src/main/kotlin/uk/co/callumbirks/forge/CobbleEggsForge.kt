package uk.co.callumbirks.forge

import dev.architectury.platform.forge.EventBuses
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.fml.DistExecutor
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import uk.co.callumbirks.CobbleEggs
import uk.co.callumbirks.CobbleEggsNetworking

@Mod(CobbleEggs.MOD_ID)
class CobbleEggsForge {
    init {
        EventBuses.registerModEventBus(CobbleEggs.MOD_ID, FMLJavaModLoadingContext.get().modEventBus)
        CobbleEggs.init()
        DistExecutor.safeRunWhenOn(Dist.CLIENT) { DistExecutor.SafeRunnable(CobbleEggsNetworking::registerClient) }
    }
}