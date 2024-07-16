package uk.co.callumbirks

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies
import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.util.party
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier
import org.slf4j.LoggerFactory
import uk.co.callumbirks.config.CobbleEggsConfig
import uk.co.callumbirks.item.Egg

object CobbleEggs {
    const val MOD_ID = "cobble_eggs"
    val LOGGER = LoggerFactory.getLogger(MOD_ID)
    val CONFIG = CobbleEggsConfig.load()
    lateinit var implementation: CobbleEggsImplementation

    fun init(implementation: CobbleEggsImplementation) {
        CobbleEggsItems.register()
        this.implementation = implementation
    }

    fun cobbleEggsResource(name: String): Identifier {
        return Identifier(MOD_ID, name)
    }

    fun getPokemon(name: String): Pokemon {
        return PokemonSpecies.random().create()
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
        speciesId?.let { id ->
            val species = PokemonSpecies.getByIdentifier(id) ?: return null
            val pokemon = species.create(1)
            pokemon.aspects = aspects
            pokemon.updateForm()
            return pokemon
        }
        return null
    }
}