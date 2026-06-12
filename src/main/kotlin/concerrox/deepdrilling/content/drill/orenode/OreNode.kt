package concerrox.deepdrilling.content.drill.orenode

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.util.random.WeightedEntry
import net.minecraft.util.random.WeightedRandomList
import net.minecraft.world.level.block.Block
import java.util.Optional

typealias OreLootListing = WeightedRandomList<WeightedEntry.Wrapper<OreIngredient<*>>>

@JvmRecord
data class OreNode(
    val nodeBlock: Holder<Block>,
    val earthLootListing: Optional<OreLootListing>,
    val commonLootListing: Optional<OreLootListing>,
    val rareLootListing: Optional<OreLootListing>,
) {

    companion object {

        private val LOOT_LISTING_CODEC: Codec<WeightedRandomList<WeightedEntry.Wrapper<OreIngredient<*>>>> =
            WeightedRandomList.codec(WeightedEntry.Wrapper.codec(OreIngredient.CODEC))

        val CODEC: Codec<OreNode> = RecordCodecBuilder.create { builder ->
            builder.group(
                BuiltInRegistries.BLOCK.holderByNameCodec().fieldOf("node_block").forGetter(OreNode::nodeBlock),
                LOOT_LISTING_CODEC.optionalFieldOf("earth_loot").forGetter { it.earthLootListing },
                LOOT_LISTING_CODEC.optionalFieldOf("common_loot").forGetter { it.commonLootListing },
                LOOT_LISTING_CODEC.optionalFieldOf("rare_loot").forGetter { it.rareLootListing },
            ).apply(builder, ::OreNode)
        }

    }

}