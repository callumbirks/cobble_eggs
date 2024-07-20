package uk.co.callumbirks

import dev.architectury.event.events.common.LootEvent
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.loot.LootManager
import net.minecraft.loot.LootPool
import net.minecraft.loot.entry.ItemEntry
import net.minecraft.util.Identifier

object CobbleEggsLoot : LootEvent.ModifyLootTable {
    private var loot: HashMap<Identifier, ArrayList<LootEntry>> = hashMapOf()

    data class LootEntry(val item: Item, val weight: Int)

    private fun registerLoot(tableIdentifier: Identifier, item: Item, weight: Int) {
        val list = loot.getOrPut(tableIdentifier) { arrayListOf() }
        list.add(LootEntry(item, weight))
    }

    fun register() {
        for (entry in CobbleEggs.LOOT_CONFIG.entries) {
            val item = CobbleEggsItems.byIdentifier(entry.item())
                ?: throw IllegalArgumentException(String.format("No known egg with identifier '{}'"))
            registerLoot(entry.table(), item, entry.weight)
            CobbleEggs.LOGGER.debug("Registering loot item '{}' in table '{}'", entry.item, entry.table)
        }
        for (lootEntry in loot) {
            val totalWeight =
                lootEntry.value.stream().map { it.weight }.reduce(0) { total, weight -> total + weight }
            if (totalWeight < 100) {
                registerLoot(lootEntry.key, Items.AIR, 100 - totalWeight)
            }
        }
        LootEvent.MODIFY_LOOT_TABLE.register(this)
        CobbleEggs.LOGGER.debug("Registered loot {}", loot)
    }

    override fun modifyLootTable(
        lootDataManager: LootManager?,
        id: Identifier?,
        context: LootEvent.LootTableModificationContext?,
        builtin: Boolean
    ) {
        if (!builtin || id == null || context == null) return

        loot[id]?.let { entries ->
            CobbleEggs.LOGGER.debug("Modifying loot table '{}' with entries {}", id, entries)
            var builder = LootPool.builder()
            for (entry in entries) {
                builder = builder.with(ItemEntry.builder(entry.item).weight(entry.weight))
            }
            context.addPool(builder)
        }
    }
}