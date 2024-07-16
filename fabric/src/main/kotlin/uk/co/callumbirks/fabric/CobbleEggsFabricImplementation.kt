package uk.co.callumbirks.fabric

import net.minecraft.server.network.ServerPlayerEntity
import uk.co.callumbirks.CobbleEggsImplementation
import uk.co.callumbirks.item.Egg

class CobbleEggsFabricImplementation: CobbleEggsImplementation {
    override fun giveRandomPokemon(player: ServerPlayerEntity, rarity: Egg.Rarity): Boolean {
        //val pokemon = getRandomPokemon(rarity)
        //if (pokemon == null) {
        //    CobbleEggs.LOGGER.error("Failed to load random Pokemon from config for rarity ${rarity}!")
        //    return false
        //}
        //player.party().add(pokemon)
        //// CobbleEggNetworking.playSoundToClient(user, cobblemonResource("poke_ball.capture_succeeded"), false)
        return true
    }

    //companion object {
    //    private fun getRandomPokemon(rarity: Egg.Rarity): Pokemon? {
    //        val config = CobbleEggs.CONFIG.configForRarity(rarity)
    //        val speciesString = config.pokemon.randomOrNull() ?: return null
    //        val (speciesId, aspects) = CobbleEggsConfig.speciesIdAndAspects(speciesString)
    //        speciesId?.let { id ->
    //            val species = PokemonSpecies.getByIdentifier(id) ?: return null
    //            val pokemon = species.create(1)
    //            pokemon.aspects = aspects
    //            pokemon.updateForm()
    //            return pokemon
    //        }
    //        return null
    //    }
    //}
}