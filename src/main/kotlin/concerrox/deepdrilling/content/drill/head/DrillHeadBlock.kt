package concerrox.deepdrilling.content.drill.head

import com.simibubi.create.AllShapes
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock
import com.simibubi.create.foundation.block.IBE
import concerrox.deepdrilling.content.drill.core.DrillCoreBlock
import concerrox.deepdrilling.registry.ModBlocks
import net.createmod.catnip.ghostblock.GhostBlocks
import net.createmod.catnip.placement.IPlacementHelper
import net.createmod.catnip.placement.PlacementHelpers
import net.createmod.catnip.placement.PlacementOffset
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.DirectionProperty
import net.minecraft.world.level.storage.loot.LootParams
import net.minecraft.world.level.storage.loot.parameters.LootContextParams
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape
import java.util.function.Predicate

class DrillHeadBlock(properties: Properties, val durability: Int) : DirectionalKineticBlock(properties),
    IBE<DrillHeadBlockEntity> {

    companion object {
        val FACING: DirectionProperty = DirectionalKineticBlock.FACING
        val PLACEMENT_HELPER_ID = PlacementHelpers.register(PlacementHelper())
        private val SHAPER = AllShapes.Builder(box(1.0, -1.0, 1.0, 15.0, 17.0, 15.0)).forDirectional()
    }

    override fun getRotationAxis(state: BlockState): Direction.Axis = state.getValue(FACING).axis
    override fun getBlockEntityClass() = DrillHeadBlockEntity::class.java

    override fun getBlockEntityType(): BlockEntityType<out DrillHeadBlockEntity> {
        TODO("Not yet implemented")
    }

    @Deprecated("Deprecated in Java")
    override fun getDrops(state: BlockState, builder: LootParams.Builder): MutableList<ItemStack> {
        var builder = builder
        val be = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY)
        if (be is DrillHeadBlockEntity) {
//            builder = builder.withDynamicDrop(DrillHeadBlock.DRILL_DATA) { stackBuilder ->
//                stackBuilder.accept(be.writeItemNbt(state.block.asItem().defaultInstance))
//            }
        }
        return super.getDrops(state, builder)
    }

    override fun setPlacedBy(
        worldIn: Level, pos: BlockPos, state: BlockState, placer: LivingEntity?, stack: ItemStack
    ) {
        super.setPlacedBy(worldIn, pos, state, placer, stack)
        withBlockEntityDo(worldIn, pos) {
            it.readItemNbt(stack)
//            it.coreBlockEntity?.findModules()
        }
    }

    override fun hasShaftTowards(world: LevelReader, pos: BlockPos, state: BlockState, face: Direction): Boolean {
        return face == state.getValue(FACING).opposite
    }

    @Deprecated("Deprecated in Java")
    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
        return SHAPER.get(state.getValue(FACING))
    }

    @Suppress("DEPRECATION")
    @Deprecated("Deprecated in Java")
    override fun canSurvive(state: BlockState, level: LevelReader, pos: BlockPos): Boolean {
        val facing = state.getValue(FACING)
        val corePos = pos.relative(facing.opposite)
        val coreState = level.getBlockState(corePos)
        return super.canSurvive(state, level, pos) && coreState.`is`(ModBlocks.DRILL_CORE.get()) && coreState.getValue(
            DrillCoreBlock.FACING
        ) == facing
    }

    @Suppress("DEPRECATION")
    @Deprecated("Deprecated in Java")
    override fun neighborChanged(
        state: BlockState, level: Level, pos: BlockPos, block: Block, fromPos: BlockPos, isMoving: Boolean
    ) {
        super.neighborChanged(state, level, pos, block, fromPos, isMoving)
        val facing = state.getValue(FACING)
        if (!level.isClientSide && fromPos == pos.relative(facing.opposite) && !canSurvive(state, level, pos)) {
            level.destroyBlock(pos, true)
        }
    }

    @Suppress("DEPRECATION")
    override fun getStateForPlacement(context: BlockPlaceContext): BlockState? {
        val state = super.getStateForPlacement(context)
        if (state != null && !canSurvive(state, context.level, context.clickedPos)) return null
        return state
    }

    // todo: figure out how the fuck blocks like shafts and cogs render
    //  seriously - I had to do a lot more tomfoolery than create seems to do and
    //  the PlacementHelper ghost doesn't even render at all
    private class PlacementHelper : IPlacementHelper {

        override fun getItemPredicate() = Predicate { stack: ItemStack ->
            val item = stack.item
            item is BlockItem && item.block is DrillHeadBlock
        }

        override fun getStatePredicate() = Predicate { state: BlockState ->
            state.block is DrillCoreBlock
        }

        override fun getOffset(
            player: Player, world: Level, state: BlockState, pos: BlockPos, ray: BlockHitResult
        ): PlacementOffset {
            val coreFacing = state.getValue(DrillCoreBlock.FACING)
            return if (world.getBlockState(pos.relative(coreFacing)).canBeReplaced()) {
                PlacementOffset.success(pos.relative(coreFacing)) { state -> state.setValue(FACING, coreFacing) }
            } else {
                PlacementOffset.fail()
            }
        }

        override fun renderAt(
            pos: BlockPos, state: BlockState, ray: BlockHitResult, offset: PlacementOffset
        ) {
            if (!offset.hasGhostState()) return
            GhostBlocks.getInstance().showGhostState(this, offset.transform.apply(offset.ghostState))
                .at(offset.getBlockPos()).breathingAlpha()
        }

    }

}