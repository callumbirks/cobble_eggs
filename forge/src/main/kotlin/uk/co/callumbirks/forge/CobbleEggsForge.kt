package uk.co.callumbirks.forge

import dev.architectury.platform.forge.EventBuses
import net.minecraft.item.ItemGroup
import net.minecraft.registry.RegistryKeys
import net.minecraft.server.MinecraftServer
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.fml.DistExecutor
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import net.minecraftforge.fml.loading.FMLEnvironment
import net.minecraftforge.registries.RegisterEvent
import net.minecraftforge.server.ServerLifecycleHooks
import thedarkcolour.kotlinforforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.forge.MOD_CONTEXT
import uk.co.callumbirks.*

@Mod(CobbleEggs.MOD_ID)
class CobbleEggsForge : CobbleEggsImplementation {
    init {
        with(MOD_BUS) {
            addListener(this@CobbleEggsForge::initialize)
            CobbleEggs.preInitialize(this@CobbleEggsForge)
        }
    }

    fun initialize(event: FMLCommonSetupEvent) {
        CobbleEggs.LOGGER.info("Initializing CobbleEggs Forge...")
        CobbleEggs.initialize()
    }

    override val modAPI = ModAPI.FORGE

    override fun registerItems() {
        with(MOD_BUS) {
            addListener<RegisterEvent> { event ->
                event.register(CobbleEggsItems.registryKey) { helper ->
                    CobbleEggsItems.register { identifier, item -> helper.register(identifier, item) }
                }
            }
            addListener<RegisterEvent> { event ->
                event.register(RegistryKeys.ITEM_GROUP) { helper ->
                    CobbleEggsItemGroups.register { holder ->
                        val itemGroup = ItemGroup.builder()
                            .displayName(holder.displayName)
                            .icon(holder.displayIconProvider)
                            .entries(holder.entryCollector)
                            .build()
                        helper.register(holder.key, itemGroup)
                        itemGroup
                    }
                }
            }
        }
    }

    override fun environment(): Environment {
        return if (FMLEnvironment.dist.isClient) Environment.CLIENT else Environment.SERVER
    }

    override fun server(): MinecraftServer? = ServerLifecycleHooks.getCurrentServer()
}