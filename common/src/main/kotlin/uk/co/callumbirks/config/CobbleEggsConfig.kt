package uk.co.callumbirks.config

import com.google.gson.GsonBuilder
import net.minecraft.util.Identifier
import uk.co.callumbirks.CobbleEggs
import uk.co.callumbirks.item.Egg
import java.io.File
import java.io.FileReader
import java.io.PrintWriter

class CobbleEggsConfig {
    data class EggConfig(val blocksToHatch: Int, val pokemon: MutableSet<String>)

    val commonEgg = EggConfig(1_000, mutableSetOf("bidoof", "zigzagoon galarian"))
    val rareEgg = EggConfig(5_000, mutableSetOf("golett"))
    val epicEgg = EggConfig(10_000, mutableSetOf("charcadet"))
    val legendaryEgg = EggConfig(50_000, mutableSetOf("rayquaza"))
    val eventEgg = EggConfig(10_000, mutableSetOf())

    fun configForRarity(rarity: Egg.Rarity): EggConfig {
        return when (rarity) {
            Egg.Rarity.COMMON -> commonEgg
            Egg.Rarity.RARE -> rareEgg
            Egg.Rarity.EPIC -> epicEgg
            Egg.Rarity.LEGENDARY -> legendaryEgg
            Egg.Rarity.EVENT -> eventEgg
        }
    }

    companion object {
        fun speciesIdAndAspects(speciesString: String): Pair<Identifier?, Set<String>> {
            var aspects: Set<String> = setOf()
            var firstWord: String? = speciesString
            if (speciesString.contains(' ')) {
                var split = speciesString.split(' ')
                firstWord = split.firstOrNull()
                split = split.drop(1)
                aspects = split.toSet()
            }
            if (firstWord == null) {
                return Pair(null, setOf())
            }
            val species: Identifier? = if (firstWord.contains(':')) {
                Identifier.tryParse(firstWord)
            } else {
                Identifier("cobblemon", firstWord)
            }
            return Pair(species, aspects)
        }

        fun load(): CobbleEggsConfig {
            val gson = GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create()

            var config = CobbleEggsConfig()
            val configFile = File("config/${CobbleEggs.MOD_ID}/config.json")
            configFile.parentFile.mkdirs()

            if (configFile.exists()) {
                try {
                    val fileReader = FileReader(configFile)
                    config = gson.fromJson(fileReader, CobbleEggsConfig::class.java)
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