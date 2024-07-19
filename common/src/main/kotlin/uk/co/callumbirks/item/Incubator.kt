package uk.co.callumbirks.item

import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.StackReference
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ToolMaterial
import net.minecraft.recipe.Ingredient
import net.minecraft.screen.slot.Slot
import net.minecraft.sound.SoundEvent
import net.minecraft.util.ClickType
import net.minecraft.world.World
import uk.co.callumbirks.CobbleEggsItems

class Incubator(settings: Settings) : Item(settings) {
    companion object {
        const val MAX_USES = 3
    }

    val rarity = settings.rarity
    val stepsMultiplier = settings.stepsMultiplier

    data class Settings(val rarity: Rarity, val stepsMultiplier: Float) : Item.Settings() {
        init {
            maxDamage(MAX_USES)
        }
    }

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
        if (player == null || stack == null || otherStack == null || slot == null || otherStack.item !is Egg) {
            return false
        }
        val incubatingEgg = ItemStack(CobbleEggsItems.getIncubatingEgg(this, otherStack.item as Egg))

        // Create or carry over the incubator uses NBT data
        val incubatorNbt = stack.orCreateNbt
        val uses = if (incubatorNbt.contains("incubator_uses")) incubatorNbt.getInt("incubator_uses") else 0
        incubatingEgg.orCreateNbt.putInt("incubator_uses", uses)

        otherStack.count -= 1
        slot.stack.count -= 1
        if (slot.stack.isEmpty) {
            slot.stack = incubatingEgg
        } else {
            player.inventory.offerOrDrop(incubatingEgg)
        }

        return true
    }

    override fun inventoryTick(stack: ItemStack?, world: World?, entity: Entity?, slot: Int, selected: Boolean) {
        if (stack == null) return
        if (stack.nbt == null || !stack.nbt!!.contains("incubator_uses")) return
        val uses = stack.nbt!!.getInt("incubator_uses")
        stack.damage = uses
    }
}