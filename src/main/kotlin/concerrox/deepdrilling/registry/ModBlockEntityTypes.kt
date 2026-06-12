package concerrox.deepdrilling.registry

import com.simibubi.create.content.kinetics.base.OrientedRotatingVisual
import com.simibubi.create.foundation.data.CreateBlockEntityBuilder
import com.simibubi.create.foundation.data.CreateRegistrate
import com.tterrag.registrate.builders.BlockEntityBuilder
import com.tterrag.registrate.util.entry.BlockEntityEntry
import com.tterrag.registrate.util.nullness.NonNullFunction
import concerrox.deepdrilling.content.drill.core.DrillCoreBlockEntity
import concerrox.deepdrilling.content.drill.core.DrillCoreRenderer
import concerrox.deepdrilling.registrate
import dev.engine_room.flywheel.lib.visualization.SimpleBlockEntityVisualizer
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.world.level.block.entity.BlockEntity

object ModBlockEntityTypes {

    internal fun register() {}

    val DRILL_CORE = blockEntityType("drill_core", ::DrillCoreBlockEntity) {
        visual(OrientedRotatingVisual.of(ModPartialModels.DRILL_CORE_SHAFT), renderNormally = false)
        validBlocks(ModBlocks.DRILL_CORE)
        renderer(::DrillCoreRenderer)
    }

    private fun <BE : BlockEntity> blockEntityType(
        name: String,
        builder: BlockEntityBuilder.BlockEntityFactory<BE>,
        action: CreateBlockEntityBuilder<BE, CreateRegistrate>.() -> BlockEntityBuilder<BE, CreateRegistrate>
    ): BlockEntityEntry<BE> {
        val builder = registrate().blockEntity(name, builder)
        return action(builder).register()
    }

    private fun <BE : BlockEntity> CreateBlockEntityBuilder<BE, CreateRegistrate>.visual(
        visualBuilder: SimpleBlockEntityVisualizer.Factory<BE>, renderNormally: Boolean
    ): CreateBlockEntityBuilder<BE, CreateRegistrate> {
        return visual({ visualBuilder }, renderNormally)
    }

    private fun <BE : BlockEntity> BlockEntityBuilder<BE, CreateRegistrate>.renderer(
        rendererBuilder: NonNullFunction<BlockEntityRendererProvider.Context, BlockEntityRenderer<in BE>>
    ): BlockEntityBuilder<BE, CreateRegistrate> {
        return renderer { rendererBuilder }
    }

}