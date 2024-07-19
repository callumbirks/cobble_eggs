package uk.co.callumbirks

import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier
import uk.co.callumbirks.CobbleEggs.cobbleEggsResource
import uk.co.callumbirks.item.Egg
import uk.co.callumbirks.item.IncubatingEgg
import uk.co.callumbirks.item.Incubator
import uk.co.callumbirks.platform.PlatformRegistry

object CobbleEggsItems : PlatformRegistry<Registry<Item>, RegistryKey<Registry<Item>>, Item>() {
    override val registry: Registry<Item> = Registries.ITEM
    override val registryKey: RegistryKey<Registry<Item>> = RegistryKeys.ITEM

    private val itemByIdentifier = hashMapOf<Identifier, Item>()

    val EGG_COMMON =
        create("egg_common", Egg(Egg.Settings(CobbleEggs.CONFIG.commonEgg.blocksToHatch, Egg.Rarity.COMMON)))
    val EGG_RARE =
        create("egg_rare", Egg(Egg.Settings(CobbleEggs.CONFIG.rareEgg.blocksToHatch, Egg.Rarity.RARE)))
    val EGG_EPIC =
        create("egg_epic", Egg(Egg.Settings(CobbleEggs.CONFIG.epicEgg.blocksToHatch, Egg.Rarity.EPIC)))
    val EGG_LEGENDARY =
        create("egg_legendary", Egg(Egg.Settings(CobbleEggs.CONFIG.legendaryEgg.blocksToHatch, Egg.Rarity.LEGENDARY)))
    val EGG_EVENT =
        create("egg_event", Egg(Egg.Settings(CobbleEggs.CONFIG.eventEgg.blocksToHatch, Egg.Rarity.EVENT)))

    val INCUBATOR_COMMON =
        create("incubator_common", Incubator(Incubator.Settings(Incubator.Rarity.COMMON, 1f)))
    val INCUBATOR_RARE =
        create("incubator_rare", Incubator(Incubator.Settings(Incubator.Rarity.RARE, 1.5f)))
    val INCUBATOR_EPIC =
        create("incubator_epic", Incubator(Incubator.Settings(Incubator.Rarity.EPIC, 2f)))

    val INCUBATING_EGG_COMMON_COMMON =
        create("incubating_egg_common_common", IncubatingEgg(INCUBATOR_COMMON, EGG_COMMON))
    val INCUBATING_EGG_COMMON_RARE =
        create("incubating_egg_common_rare", IncubatingEgg(INCUBATOR_COMMON, EGG_RARE))
    val INCUBATING_EGG_COMMON_EPIC =
        create("incubating_egg_common_epic", IncubatingEgg(INCUBATOR_COMMON, EGG_EPIC))
    val INCUBATING_EGG_COMMON_LEGENDARY =
        create("incubating_egg_common_legendary", IncubatingEgg(INCUBATOR_COMMON, EGG_LEGENDARY))
    val INCUBATING_EGG_COMMON_EVENT =
        create("incubating_egg_common_event", IncubatingEgg(INCUBATOR_COMMON, EGG_EVENT))

    val INCUBATING_EGG_RARE_COMMON =
        create("incubating_egg_rare_common", IncubatingEgg(INCUBATOR_RARE, EGG_COMMON))
    val INCUBATING_EGG_RARE_RARE =
        create("incubating_egg_rare_rare", IncubatingEgg(INCUBATOR_RARE, EGG_RARE))
    val INCUBATING_EGG_RARE_EPIC =
        create("incubating_egg_rare_epic", IncubatingEgg(INCUBATOR_RARE, EGG_EPIC))
    val INCUBATING_EGG_RARE_LEGENDARY =
        create("incubating_egg_rare_legendary", IncubatingEgg(INCUBATOR_RARE, EGG_LEGENDARY))
    val INCUBATING_EGG_RARE_EVENT =
        create("incubating_egg_rare_event", IncubatingEgg(INCUBATOR_RARE, EGG_EVENT))

    val INCUBATING_EGG_EPIC_COMMON =
        create("incubating_egg_epic_common", IncubatingEgg(INCUBATOR_EPIC, EGG_COMMON))
    val INCUBATING_EGG_EPIC_RARE =
        create("incubating_egg_epic_rare", IncubatingEgg(INCUBATOR_EPIC, EGG_RARE))
    val INCUBATING_EGG_EPIC_EPIC =
        create("incubating_egg_epic_epic", IncubatingEgg(INCUBATOR_EPIC, EGG_EPIC))
    val INCUBATING_EGG_EPIC_LEGENDARY =
        create("incubating_egg_epic_legendary", IncubatingEgg(INCUBATOR_EPIC, EGG_LEGENDARY))
    val INCUBATING_EGG_EPIC_EVENT =
        create("incubating_egg_epic_event", IncubatingEgg(INCUBATOR_EPIC, EGG_EVENT))

    override fun <E : Item> create(name: String, entry: E): E {
        val identifier = cobbleEggsResource(name)
        itemByIdentifier[identifier] = entry
        return super.create(name, entry)
    }

    fun byIdentifier(identifier: Identifier): Item? {
        return itemByIdentifier[identifier]
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