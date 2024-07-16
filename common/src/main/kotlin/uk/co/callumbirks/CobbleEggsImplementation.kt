package uk.co.callumbirks

import net.minecraft.server.network.ServerPlayerEntity
import uk.co.callumbirks.item.Egg

interface CobbleEggsImplementation {
    /// Give the player a random Pokémon from the pool of the given egg rarity.
    fun giveRandomPokemon(player: ServerPlayerEntity, rarity: Egg.Rarity): Boolean
}