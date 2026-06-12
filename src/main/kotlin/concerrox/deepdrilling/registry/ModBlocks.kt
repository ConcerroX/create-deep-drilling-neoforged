package concerrox.deepdrilling.registry

import com.simibubi.create.AllBlocks
import com.simibubi.create.AllItems
import com.simibubi.create.api.stress.BlockStressValues
import com.simibubi.create.foundation.data.CreateRegistrate
import com.simibubi.create.foundation.data.ModelGen.customItemModel
import com.simibubi.create.foundation.data.SharedProperties
import com.simibubi.create.foundation.data.TagGen.axeOrPickaxe
import com.simibubi.create.foundation.data.TagGen.pickaxeOnly
import com.tterrag.registrate.builders.BlockBuilder
import com.tterrag.registrate.providers.DataGenContext
import com.tterrag.registrate.providers.RegistrateBlockstateProvider
import com.tterrag.registrate.providers.RegistrateRecipeProvider
import com.tterrag.registrate.util.entry.BlockEntry
import com.tterrag.registrate.util.nullness.NonNullBiConsumer
import com.tterrag.registrate.util.nullness.NonNullFunction
import concerrox.deepdrilling.content.drill.core.DrillCoreBlock
import concerrox.deepdrilling.content.drill.head.DrillHeadBlock
import concerrox.deepdrilling.content.drill.head.DrillHeadModelHelper
import concerrox.deepdrilling.registrate
import net.minecraft.client.renderer.RenderType
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.ShapedRecipeBuilder.shaped
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import java.util.function.Supplier

object ModBlocks {

    internal fun register() {}

    val DRILL_CORE = block("drill_core", ::DrillCoreBlock) {
        initialProperties(SharedProperties::stone)
        properties(BlockBehaviour.Properties::noOcclusion)
        renderType(RenderType::cutout)
        transform(axeOrPickaxe())
        blockstate(existingFileFacing("block/drill_core/block"))
        stressImpact(16.0)
        recipe { context, provider ->
            shaped(RecipeCategory.MISC, context.get()).pattern("IHI").pattern("HGH").pattern("IHI")
                .define('I', AllItems.PRECISION_MECHANISM).define('H', AllItems.ELECTRON_TUBE)
                .define('G', AllBlocks.FLYWHEEL)
                .unlockedBy("has_ingredient", RegistrateRecipeProvider.has(AllItems.PRECISION_MECHANISM)).save(provider)
        }
        item().transform(customItemModel())
    }

//	public static final BlockEntry<BlankModuleBlock> BLANK_MODULE = DrillMod.REGISTRATE
//			.block("blank_module", BlankModuleBlock::new)
//			.properties(BlockBehaviour.Properties::noOcclusion)
//			.transform(axeOrPickaxe())
//			.lang("Blank Module")
//			.blockstate((c, p) -> BlockstateHelper.existingFilePillar(c, p, "block/blank_module/block"))
//			.item()
//			.recipe((c, p) -> ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, c.get(), 1)
//					.requires(AllBlocks.SHAFT)
//					.requires(AllBlocks.ANDESITE_CASING)
//					.requires(AllItems.IRON_SHEET)
//					.unlockedBy("has_ingredient", RegistrateRecipeProvider.has(DRILL))
//					.save(p))
//			.transform(ModelGen.customItemModel())
//			.register();

//	public static final BlockEntry<CollectorModuleBlock> COLLECTOR = DrillMod.REGISTRATE
//			.block("collection_filter", CollectorModuleBlock::new)
//			.properties(BlockBehaviour.Properties::noOcclusion)
//			.transform(axeOrPickaxe())
//			.lang("Collection Filter")
//			.blockstate((c, p) -> BlockstateHelper.existingFilePillar(c, p, "block/collection_filter/block"))
//			.transform(BlockStressDefaults.setImpact(2))
//			.item()
//			.recipe((c, p) -> ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, c.get(), 1)
//					.requires(BLANK_MODULE)
//					.requires(AllBlocks.ANDESITE_FUNNEL)
//					.unlockedBy("has_ingredient", RegistrateRecipeProvider.has(BLANK_MODULE))
//					.save(p))
//			.transform(ModelGen.customItemModel())
//			.register();
//
//	public static final BlockEntry<OverclockModuleBlock> DRILL_OVERCLOCK = DrillMod.REGISTRATE
//			.block("drill_overclock", OverclockModuleBlock::new)
//			.addLayer(() -> RenderType::cutout)
//			.lang("Drill Overclock")
//			.blockstate((c, p) -> BlockstateHelper.existingFilePillar(c, p, "block/drill_overclock/block"))
//			.properties(BlockBehaviour.Properties::noOcclusion)
//			.transform(pickaxeOnly())
//			.transform(BlockStressDefaults.setImpact(8))
//			.item()
//			.recipe((c, p) -> ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, c.get(), 1)
//					.requires(BLANK_MODULE)
//					.requires(AllBlocks.COGWHEEL)
//					.requires(AllBlocks.BRASS_CASING)
//					.requires(AllItems.PRECISION_MECHANISM)
//					.unlockedBy("has_ingredient", RegistrateRecipeProvider.has(BLANK_MODULE))
//					.save(p))
//			.transform(ModelGen.customItemModel())
//			.register();
//
//	public static final BlockEntry<SludgePumpModuleBlock> SLUDGE_PUMP = DrillMod.REGISTRATE
//			.block("sludge_pump", SludgePumpModuleBlock::new)
//			.properties(BlockBehaviour.Properties::noOcclusion)
//			.lang("Sludge Pump")
//			.blockstate((c, p) -> BlockstateHelper.existingFilePillar(c, p, "block/sludge_pump/block"))
//			.transform(axeOrPickaxe())
//			.transform(BlockStressDefaults.setImpact(4))
//			.item()
//			.recipe((c, p) -> ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, c.get(), 1)
//					.requires(BLANK_MODULE)
//					.requires(AllBlocks.FLUID_TANK)
//					.requires(AllBlocks.COPPER_CASING)
//					.requires(AllBlocks.MECHANICAL_PUMP)
//					.unlockedBy("has_ingredient", RegistrateRecipeProvider.has(BLANK_MODULE))
//					.save(p))
//			.transform(ModelGen.customItemModel())
//			.register();
//
//	public static final BlockEntry<OreNodeBlock>
//
//	CRIMSITE_NODE = DrillMod.REGISTRATE
//			.block("crimsite_node", OreNodeBlock::new)
//			.initialProperties(() -> Blocks.BEDROCK)
//			.loot((p, b) -> {})
//			.item()
//			.build()
//			.register(),
//
//	ASURINE_NODE = DrillMod.REGISTRATE
//			.block("asurine_node", OreNodeBlock::new)
//			.initialProperties(() -> Blocks.BEDROCK)
//			.loot((p, b) -> {})
//			.item()
//			.build()
//			.register(),
//	OCHRUM_NODE = DrillMod.REGISTRATE
//			.block("ochrum_node", OreNodeBlock::new)
//			.initialProperties(() -> Blocks.BEDROCK)
//			.loot((p, b) -> {})
//			.item()
//			.build()
//			.register(),
//	VERIDIUM_NODE = DrillMod.REGISTRATE
//			.block("veridium_node", OreNodeBlock::new)
//			.initialProperties(() -> Blocks.BEDROCK)
//			.loot((p, b) -> {})
//			.item()
//			.build()
//			.register();

    val ANDESITE_DRILL_HEAD = block("andesite_drill_head", ::DrillHeadBlock) {
        properties(BlockBehaviour.Properties::noOcclusion)
        renderType(RenderType::cutout)
        transform(pickaxeOnly())
        blockstate { ctx, prov ->
            DrillHeadModelHelper.createBlockStateAndModel(ctx, prov, "andesite_drill_head", "minecraft:block/anvil")
        }
        stressImpact(4.0)/*
                        .transform(DrillHeadStats.setDurability(durability))
                .transform(DrillHeadStats.setSpeedModifier(miningTimeModifier))
                .transform(DrillHeadStats.setLootWeightMultiplier(earthWeight, commonWeight, rareWeight))
         */
        lang("Andesite Drill Head")
        item().build()
    }

    private fun <B : Block> block(
        name: String,
        builder: NonNullFunction<BlockBehaviour.Properties, B>,
        action: BlockBuilder<B, CreateRegistrate>.() -> BlockBuilder<B, CreateRegistrate>
    ): BlockEntry<B> {
        val builder = registrate().block(name, builder)
        return action(builder).register()
    }

//    private fun <B : Block> BlockBuilder<B, CreateRegistrate>.transform2(
//        transformation: NonNullFunction<BlockBuilder<B, CreateRegistrate>, BlockBuilder<B, CreateRegistrate>>
//    ): BlockBuilder<B, CreateRegistrate> {
//        return transform<Block, B, CreateRegistrate, BlockBuilder<B, CreateRegistrate>>(transformation)
//    }

    @Suppress("removal", "DEPRECATION")
    private fun <B : Block> BlockBuilder<B, CreateRegistrate>.renderType(renderType: Supplier<RenderType>): BlockBuilder<B, CreateRegistrate> {
        return addLayer { renderType }
    }

    private fun <B : Block> BlockBuilder<B, CreateRegistrate>.stressImpact(impact: Double): BlockBuilder<B, CreateRegistrate> {
        return onRegister { BlockStressValues.IMPACTS.register(it) { impact } }
    }

    private fun <T : Block> existingFileFacing(path: String): NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> {
        return NonNullBiConsumer { context, provider ->
            provider.directionalBlock(context.get(), provider.models().getExistingFile(provider.modLoc(path)))
        }
    }

}
