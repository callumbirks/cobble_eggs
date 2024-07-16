package uk.co.callumbirks.item

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.StackReference
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.screen.slot.Slot
import net.minecraft.util.ClickType
import uk.co.callumbirks.CobbleEggsItems

class Incubator(settings: Settings): Item(settings) {
    val rarity = settings.rarity
    val stepsMultiplier = settings.stepsMultiplier

    data class Settings(val rarity: Rarity, val stepsMultiplier: Float) : Item.Settings()
    enum class Rarity {
        COMMON,
        RARE,
        EPIC,
    }

    override fun onClicked(
        stack: ItemStack?,
        otherStack: ItemStack?,
        slot: Slot?,
        clickType: ClickType?,
        player: PlayerEntity?,
        cursorStackReference: StackReference?
    ): Boolean {
        if (player == null || otherStack == null || slot == null || otherStack.item !is Egg) {
            return false
        }
        val incubatingEgg = CobbleEggsItems.getIncubatingEgg(this, otherStack.item as Egg)
        if (slot.stack.count > 1) {
            slot.stack.count -= 1
            player.inventory.offerOrDrop(ItemStack(incubatingEgg))
        } else {
            slot.stack = ItemStack(incubatingEgg)
        }
        otherStack.count -= 1
        return true
    }
}