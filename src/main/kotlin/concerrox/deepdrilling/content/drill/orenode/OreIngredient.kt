package concerrox.deepdrilling.content.drill.orenode

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.Dynamic
import com.mojang.serialization.JsonOps
import com.mojang.serialization.codecs.RecordCodecBuilder
import com.simibubi.create.foundation.fluid.FluidIngredient
import net.minecraft.world.item.crafting.Ingredient

sealed class OreIngredient<R>(val value: R) {

    class Item(ingredient: Ingredient) : OreIngredient<Ingredient>(ingredient)
    class Fluid(ingredient: FluidIngredient) : OreIngredient<FluidIngredient>(ingredient)

    // TODO: Mekanism compat
    class Chemical(ingredient: Ingredient) : OreIngredient<Ingredient>(ingredient)

    companion object {

        private val INGREDIENT_CODEC: Codec<Ingredient> = Codec.PASSTHROUGH.flatXmap({ dynamic ->
            try {
                DataResult.success(Ingredient.fromJson(dynamic.convert(JsonOps.INSTANCE).value))
            } catch (e: Exception) {
                DataResult.error { "Invalid ingredient: ${e.message}" }
            }
        }, { ingredient ->
            try {
                DataResult.success(Dynamic(JsonOps.INSTANCE, ingredient.toJson()))
            } catch (e: Exception) {
                DataResult.error { "Failed to encode ingredient: ${e.message}" }
            }
        })
        private val FLUID_INGREDIENT_CODEC: Codec<FluidIngredient> = Codec.PASSTHROUGH.flatXmap({ dynamic ->
            try {
                DataResult.success(FluidIngredient.deserialize(dynamic.convert(JsonOps.INSTANCE).value))
            } catch (e: Exception) {
                DataResult.error { "Invalid fluid ingredient: ${e.message}" }
            }
        }, { ingredient ->
            try {
                DataResult.success(Dynamic(JsonOps.INSTANCE, ingredient.serialize()))
            } catch (e: Exception) {
                DataResult.error { "Failed to encode fluid ingredient: ${e.message}" }
            }
        })


        private val ITEM_CODEC: Codec<Item> = RecordCodecBuilder.create { b ->
            b.group(INGREDIENT_CODEC.fieldOf("ingredient").forGetter { it.value }).apply(b, ::Item)
        }
        private val FLUID_CODEC: Codec<Fluid> = RecordCodecBuilder.create { b ->
            b.group(FLUID_INGREDIENT_CODEC.fieldOf("ingredient").forGetter { it.value }).apply(b, ::Fluid)
        }
        private val CHEMICAL_CODEC: Codec<Chemical> = RecordCodecBuilder.create { b ->
            b.group(INGREDIENT_CODEC.fieldOf("ingredient").forGetter { it.value }).apply(b, ::Chemical)
        }

        val CODEC: Codec<OreIngredient<*>> = Codec.STRING.dispatch("type", { oreIngredient ->
            when (oreIngredient) {
                is Item -> "item"
                is Fluid -> "fluid"
                is Chemical -> "chemical"
            }
        }, { type ->
            when (type) {
                "item" -> ITEM_CODEC
                "fluid" -> FLUID_CODEC
                "chemical" -> CHEMICAL_CODEC
                else -> throw IllegalArgumentException("Unknown OreIngredient type: $type")
            }
        })

    }

}
