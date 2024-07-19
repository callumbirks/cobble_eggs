package uk.co.callumbirks.fabric

import net.fabricmc.api.EnvType
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.MinecraftClient
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.server.MinecraftServer
import uk.co.callumbirks.*

object CobbleEggsFabric : CobbleEggsImplementation {
    fun initialize() {
        CobbleEggs.preInitialize(this)
        CobbleEggs.initialize()
    }

    private var server: MinecraftServer? = null

    override val modAPI = ModAPI.FABRIC

    override fun environment(): Environment {
        return when (FabricLoader.getInstance().environmentType) {
            EnvType.CLIENT -> Environment.CLIENT
            EnvType.SERVER -> Environment.SERVER
            else -> throw IllegalStateException("Fabric implementation cannot resolve environment yet")
        }
    }

    override fun registerItems() {
        CobbleEggsItems.register { identifier, item -> Registry.register(CobbleEggsItems.registry, identifier, item) }
        CobbleEggsItemGroups.register { provider ->
            Registry.register(
                Registries.ITEM_GROUP, provider.key, FabricItemGroup.builder()
                    .displayName(provider.displayName)
                    .icon(provider.displayIconProvider)
                    .entries(provider.entryCollector)
                    .build()
            )
        }
    }

    override fun server(): MinecraftServer? =
        if (this.environment() == Environment.CLIENT) MinecraftClient.getInstance().server else this.server
}