package uk.co.callumbirks

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies
import com.cobblemon.mod.common.pokemon.IVs
import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.util.party
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier
import org.slf4j.LoggerFactory
import uk.co.callumbirks.config.CobbleEggsConfig
import uk.co.callumbirks.config.LootConfig
import uk.co.callumbirks.config.rollShiny
import uk.co.callumbirks.item.Egg

object CobbleEggs {
    const val MOD_ID = "cobble_eggs"
    val LOGGER = LoggerFactory.getLogger(MOD_ID)
    val CONFIG = CobbleEggsConfig.load()
    val LOOT_CONFIG = LootConfig.load()

    lateinit var implementation: CobbleEggsImplementation

    fun preInitialize(implementation: CobbleEggsImplementation) {
        CobbleEggs.implementation = implementation

        LOGGER.info("Initializing CobbleEggs ...")

        implementation.registerItems()
    }

    fun initialize() {
        CobbleEggsLoot.register()
        CobbleEggsEvents.register()
        when (implementation.environment()) {
            Environment.CLIENT -> CobbleEggsNetworking.registerClient()
            Environment.SERVER -> CobbleEggsNetworking.registerServer()
        }
    }

    fun cobbleEggsResource(name: String): Identifier {
        return Identifier(MOD_ID, name)
    }

    fun giveRandomPokemon(player: ServerPlayerEntity, rarity: Egg.Rarity): Boolean {
        val pokemon = getRandomPokemon(rarity)
        if (pokemon == null) {
            LOGGER.error("Failed to load random Pokemon from config for rarity ${rarity}!")
            return false
        }
        player.party().add(pokemon)
        // CobbleEggNetworking.playSoundToClient(user, cobblemonResource("poke_ball.capture_succeeded"), false)
        return true
    }

    private fun getRandomPokemon(rarity: Egg.Rarity): Pokemon? {
        val config = CONFIG.configForRarity(rarity)
        val speciesString = config.pokemon.randomOrNull() ?: return null
        val (speciesId, aspects) = CobbleEggsConfig.speciesIdAndAspects(speciesString)
        if (speciesId == null) {
            return null
        }
        val species = PokemonSpecies.getByIdentifier(speciesId) ?: return null
        val pokemon = species.create(1)
        if (CONFIG.settings.shinyRate > 0f) {
            pokemon.shiny = CONFIG.settings.shinyRate.rollShiny()
        }
        pokemon.aspects = aspects
        pokemon.updateForm()
        if (CONFIG.settings.perfectIVs > 0) {
            val ivs = IVs.createRandomIVs(CONFIG.settings.perfectIVs)
            for (iv in ivs) {
                pokemon.setIV(iv.key, iv.value)
            }
        }
        return pokemon
    }

}