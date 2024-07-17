package uk.co.callumbirks

import dev.architectury.registry.registries.DeferredRegister
import net.minecraft.item.Item
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier
import uk.co.callumbirks.CobbleEggs.cobbleEggsResource
import uk.co.callumbirks.item.Egg
import uk.co.callumbirks.item.IncubatingEgg
import uk.co.callumbirks.item.Incubator

object CobbleEggsItems {
    private val REGISTRY = DeferredRegister.create(CobbleEggs.MOD_ID, RegistryKeys.ITEM)
    private val byIdentifier = hashMapOf<Identifier, Item>()
    private val reverseMap = hashMapOf<Item, Identifier>()

    val EGG_COMMON    = register("egg_common")
        { Egg(Egg.Settings(CobbleEggs.CONFIG.commonEgg.blocksToHatch, Egg.Rarity.COMMON)) }
    val EGG_RARE      = register("egg_rare")
        { Egg(Egg.Settings(CobbleEggs.CONFIG.rareEgg.blocksToHatch, Egg.Rarity.RARE)) }
    val EGG_EPIC      = register("egg_epic")
        { Egg(Egg.Settings(CobbleEggs.CONFIG.epicEgg.blocksToHatch, Egg.Rarity.EPIC)) }
    val EGG_LEGENDARY = register("egg_legendary")
        { Egg(Egg.Settings(CobbleEggs.CONFIG.legendaryEgg.blocksToHatch, Egg.Rarity.LEGENDARY)) }
    val EGG_EVENT     = register("egg_event")
        { Egg(Egg.Settings(CobbleEggs.CONFIG.eventEgg.blocksToHatch, Egg.Rarity.EVENT)) }

    val INCUBATOR_COMMON =
        register("incubator_common") { Incubator(Incubator.Settings(Incubator.Rarity.COMMON, 1f)) }
    val INCUBATOR_RARE   =
        register("incubator_rare")   { Incubator(Incubator.Settings(Incubator.Rarity.RARE, 1.5f)) }
    val INCUBATOR_EPIC   =
        register("incubator_epic")   { Incubator(Incubator.Settings(Incubator.Rarity.EPIC, 2f)) }

    // Common Incubator, Common Egg
    val INCUBATING_EGG_COMMON_COMMON =
        register("incubating_egg_common_common")    { IncubatingEgg(INCUBATOR_COMMON, EGG_COMMON) }
    // Common, Rare
    val INCUBATING_EGG_COMMON_RARE =
        register("incubating_egg_common_rare")      { IncubatingEgg(INCUBATOR_COMMON, EGG_RARE) }
    // Common, Epic
    val INCUBATING_EGG_COMMON_EPIC =
        register("incubating_egg_common_epic")      { IncubatingEgg(INCUBATOR_COMMON, EGG_EPIC) }
    // Common, Legendary
    val INCUBATING_EGG_COMMON_LEGENDARY =
        register("incubating_egg_common_legendary") { IncubatingEgg(INCUBATOR_COMMON, EGG_LEGENDARY) }
    // Common, Event
    val INCUBATING_EGG_COMMON_EVENT =
        register("incubating_egg_common_event")     { IncubatingEgg(INCUBATOR_COMMON, EGG_EVENT) }
    // Rare Incubator, Common Egg
    val INCUBATING_EGG_RARE_COMMON =
        register("incubating_egg_rare_common")      { IncubatingEgg(INCUBATOR_RARE, EGG_COMMON) }
    // Rare, Rare
    val INCUBATING_EGG_RARE_RARE =
        register("incubating_egg_rare_rare")        { IncubatingEgg(INCUBATOR_RARE, EGG_RARE) }
    // Rare, Epic
    val INCUBATING_EGG_RARE_EPIC =
        register("incubating_egg_rare_epic")        { IncubatingEgg(INCUBATOR_RARE, EGG_EPIC) }
    // Rare, Legendary
    val INCUBATING_EGG_RARE_LEGENDARY =
        register("incubating_egg_rare_legendary")   { IncubatingEgg(INCUBATOR_RARE, EGG_LEGENDARY) }
    // Rare, Event
    val INCUBATING_EGG_RARE_EVENT =
        register("incubating_egg_rare_event")       { IncubatingEgg(INCUBATOR_RARE, EGG_EVENT) }
    // Epic Incubator, Common Egg
    val INCUBATING_EGG_EPIC_COMMON =
        register("incubating_egg_epic_common")      { IncubatingEgg(INCUBATOR_EPIC, EGG_COMMON) }
    // Epic, Rare
    val INCUBATING_EGG_EPIC_RARE =
        register("incubating_egg_epic_rare")        { IncubatingEgg(INCUBATOR_EPIC, EGG_RARE) }
    // Epic, Epic
    val INCUBATING_EGG_EPIC_EPIC =
        register("incubating_egg_epic_epic")        { IncubatingEgg(INCUBATOR_EPIC, EGG_EPIC) }
    // Epic, Legendary
    val INCUBATING_EGG_EPIC_LEGENDARY =
        register("incubating_egg_epic_legendary")   { IncubatingEgg(INCUBATOR_EPIC, EGG_LEGENDARY) }
    // Epic, Event
    val INCUBATING_EGG_EPIC_EVENT =
        register("incubating_egg_epic_event")       { IncubatingEgg(INCUBATOR_EPIC, EGG_EVENT) }

    private fun <T> register(name: String, supplier: () -> T) : T where T: Item {
        val item = supplier()
        REGISTRY.register(name) { item }
        val identifier = cobbleEggsResource(name)
        byIdentifier[identifier] = item
        reverseMap[item] = identifier
        return item
    }

    fun byIdentifier(identifier: Identifier): Item? {
        return byIdentifier[identifier]
    }

    fun getIdentifier(item: Item): Identifier? {
        return reverseMap[item]
    }

    fun register() {
        REGISTRY.register()
    }

    fun getIncubatingEgg(incubator: Incubator, egg: Egg): IncubatingEgg {
        return when (incubator.rarity) {
            Incubator.Rarity.COMMON -> when (egg.rarity) {
                Egg.Rarity.COMMON -> INCUBATING_EGG_COMMON_COMMON
                Egg.Rarity.RARE -> INCUBATING_EGG_COMMON_RARE
                Egg.Rarity.EPIC -> INCUBATING_EGG_COMMON_EPIC
                Egg.Rarity.LEGENDARY -> INCUBATING_EGG_COMMON_LEGENDARY
                Egg.Rarity.EVENT -> INCUBATING_EGG_COMMON_EVENT
            }

            Incubator.Rarity.RARE -> when (egg.rarity) {
                Egg.Rarity.COMMON -> INCUBATING_EGG_RARE_COMMON
                Egg.Rarity.RARE -> INCUBATING_EGG_RARE_RARE
                Egg.Rarity.EPIC -> INCUBATING_EGG_RARE_EPIC
                Egg.Rarity.LEGENDARY -> INCUBATING_EGG_RARE_LEGENDARY
                Egg.Rarity.EVENT -> INCUBATING_EGG_RARE_EVENT
            }

            Incubator.Rarity.EPIC -> when (egg.rarity) {
                Egg.Rarity.COMMON -> INCUBATING_EGG_EPIC_COMMON
                Egg.Rarity.RARE -> INCUBATING_EGG_EPIC_RARE
                Egg.Rarity.EPIC -> INCUBATING_EGG_EPIC_EPIC
                Egg.Rarity.LEGENDARY -> INCUBATING_EGG_EPIC_LEGENDARY
                Egg.Rarity.EVENT -> INCUBATING_EGG_EPIC_EVENT
            }
        }
    }
}