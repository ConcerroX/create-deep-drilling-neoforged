package concerrox.deepdrilling.content.drill.core

import concerrox.deepdrilling.registry.ModPartialModels
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer
import net.createmod.catnip.render.CachedBuffers
import net.createmod.catnip.render.SuperByteBuffer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.world.level.block.state.BlockState

class DrillCoreRenderer(context: BlockEntityRendererProvider.Context) :
    KineticBlockEntityRenderer<DrillCoreBlockEntity>(context) {

    override fun getRotatedModel(be: DrillCoreBlockEntity, state: BlockState): SuperByteBuffer {
        return CachedBuffers.partialFacing(ModPartialModels.DRILL_CORE_SHAFT, state)
    }

}