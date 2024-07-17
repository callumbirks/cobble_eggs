package uk.co.callumbirks.config

import com.cobblemon.mod.common.util.cobblemonResource
import com.google.gson.GsonBuilder
import net.minecraft.loot.LootTables
import net.minecraft.util.Identifier
import uk.co.callumbirks.CobbleEggs
import uk.co.callumbirks.CobbleEggs.cobbleEggsResource
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import net.minecraft.entity.mob.PhantomEntity

class LootConfig {
    class Entry(val table: String, val item: String, val weight: Int) {
        constructor(table: Identifier, item: Identifier, weight: Int) : this(table.toString(), item.toString(), weight)

        fun table(): Identifier {
            return Identifier.tryParse(table)
                ?: throw IllegalArgumentException("Invalid Identifier in cobble_eggs config loot.json")
        }

        fun item(): Identifier {
            return Identifier.tryParse(item)
                ?: throw IllegalArgumentException("Invalid Identifier in cobble_eggs config loot.json")
        }
    }

    val entries = mutableListOf(
        Entry(LootTables.FISHING_FISH_GAMEPLAY, cobbleEggsResource("egg_common"), 6),
        Entry(LootTables.FISHING_FISH_GAMEPLAY, cobbleEggsResource("egg_rare"), 1),
        Entry(LootTables.JUNGLE_TEMPLE_CHEST, cobbleEggsResource("egg_rare"), 8),
        Entry(LootTables.END_CITY_TREASURE_CHEST, cobbleEggsResource("egg_epic"), 20),
        Entry(LootTables.END_CITY_TREASURE_CHEST, cobbleEggsResource("egg_legendary"), 4),
        Entry(LootTables.ABANDONED_MINESHAFT_CHEST, cobbleEggsResource("egg_rare"), 15),
        Entry(LootTables.ABANDONED_MINESHAFT_CHEST, cobbleEggsResource("egg_epic"), 7),
        Entry(LootTables.BASTION_TREASURE_CHEST, cobbleEggsResource("egg_epic"), 8),
        Entry(LootTables.BASTION_TREASURE_CHEST, cobbleEggsResource("egg_legendary"), 2),
        Entry(LootTables.BASTION_BRIDGE_CHEST, cobbleEggsResource("egg_rare"), 6),
        Entry(LootTables.BASTION_HOGLIN_STABLE_CHEST, cobbleEggsResource("egg_rare"), 12),
        Entry(LootTables.ANCIENT_CITY_CHEST, cobbleEggsResource("egg_epic"), 13),
        Entry(LootTables.ANCIENT_CITY_CHEST, cobbleEggsResource("egg_legendary"), 3),
        Entry(LootTables.DESERT_PYRAMID_CHEST, cobbleEggsResource("egg_epic"), 11),
        Entry(LootTables.SIMPLE_DUNGEON_CHEST, cobbleEggsResource("egg_common"), 14),
        Entry(LootTables.SIMPLE_DUNGEON_CHEST, cobbleEggsResource("egg_rare"), 6),
        Entry(LootTables.SIMPLE_DUNGEON_CHEST, cobbleEggsResource("egg_epic"), 2),
        Entry(Identifier("minecraft", "entities/phantom"), cobbleEggsResource("egg_rare"), 1),
        Entry(cobblemonResource("ruins/gilded_chests/base"), cobbleEggsResource("egg_epic"), 1),
        Entry(cobbleEggsResource("ruins/guided_chests/ruins"), cobbleEggsResource("egg_epic"), 1),
    )

    companion object {
        fun load(): LootConfig {
            val gson = GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create()

            var config = LootConfig()
            val configFile = File("config/${CobbleEggs.MOD_ID}/loot.json")
            configFile.parentFile.mkdirs()

            if (configFile.exists()) {
                try {
                    val fileReader = FileReader(configFile)
                    config = gson.fromJson(fileReader, LootConfig::class.java)
                    fileReader.close()
                } catch (e: Exception) {
                    CobbleEggs.LOGGER.error("Error reading config file: ${e.message}")
                }
            }

            val pw = PrintWriter(configFile)
            gson.toJson(config, pw)
            pw.close()

            return config
        }
    }
}