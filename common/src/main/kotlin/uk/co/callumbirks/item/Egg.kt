package uk.co.callumbirks.item

import net.minecraft.item.Item

class Egg(settings: Settings): Item(settings) {
    val stepsRequired: Int = settings.stepsRequired
    val rarity: Rarity = settings.rarity

    enum class Rarity {
        COMMON,
        RARE,
        EPIC,
        LEGENDARY,
        EVENT,
    }

    data class Settings(val stepsRequired: Int, val rarity: Rarity): Item.Settings()
}