package uk.co.callumbirks

import com.cobblemon.mod.common.util.cobblemonResource
import com.ibm.icu.text.DisplayContext
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemGroup.EntryCollector
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryKey
import net.minecraft.text.Text

object CobbleEggsItemGroups {
    private val ALL = arrayListOf<ItemGroupHolder>()

    private val ITEMS_KEY = this.create("items", ItemEntries) { ItemStack(CobbleEggsItems.EGG_LEGENDARY) }

    fun register(consumer: (holder: ItemGroupHolder) -> ItemGroup) {
        ALL.forEach(consumer::invoke)
    }

    private fun create(name: String, entryCollector: EntryCollector, displayIconProvider: () -> ItemStack): RegistryKey<ItemGroup> {
        val key = RegistryKey.of(Registries.ITEM_GROUP.key, cobblemonResource(name))
        ALL += ItemGroupHolder(key, displayIconProvider, entryCollector, Text.of("CobbleEggs $name"))
        return key
    }

    data class ItemGroupHolder(
        val key: RegistryKey<ItemGroup>,
        val displayIconProvider: () -> ItemStack,
        val entryCollector: EntryCollector,
        val displayName: Text,
    )

    private object ItemEntries : EntryCollector {
        override fun accept(displayContext: ItemGroup.DisplayContext?, entries: ItemGroup.Entries?) {
            if (entries == null) return
            entries.add(CobbleEggsItems.EGG_COMMON)
            entries.add(CobbleEggsItems.EGG_RARE)
            entries.add(CobbleEggsItems.EGG_EPIC)
            entries.add(CobbleEggsItems.EGG_LEGENDARY)
            entries.add(CobbleEggsItems.EGG_EVENT)
            entries.add(CobbleEggsItems.INCUBATOR_COMMON)
            entries.add(CobbleEggsItems.INCUBATOR_RARE)
            entries.add(CobbleEggsItems.INCUBATOR_EPIC)
        }

    }
}