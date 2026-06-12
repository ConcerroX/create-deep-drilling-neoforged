package concerrox.deepdrilling.content.drill.core

import com.simibubi.create.AllShapes
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock
import com.simibubi.create.foundation.block.IBE
import concerrox.deepdrilling.content.drill.head.DrillHeadBlock
import concerrox.deepdrilling.registry.ModBlockEntityTypes
import net.createmod.catnip.math.VoxelShaper
import net.createmod.catnip.placement.PlacementHelpers
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.BlockItem
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.DirectionProperty
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape

class DrillCoreBlock(properties: Properties) : DirectionalKineticBlock(properties), IBE<DrillCoreBlockEntity> {

    companion object {
        val SHAPE: VoxelShaper =
            AllShapes.Builder(box(0.0, 0.0, 0.0, 16.0, 6.0, 16.0)).add(box(2.0, 6.0, 2.0, 14.0, 15.0, 14.0))
                .forDirectional()
        val FACING: DirectionProperty = DirectionalKineticBlock.FACING
    }

    override fun getBlockEntityClass() = DrillCoreBlockEntity::class.java
    override fun getBlockEntityType(): BlockEntityType<DrillCoreBlockEntity> = ModBlockEntityTypes.DRILL_CORE.get()

    @Deprecated("Deprecated in Java")
    override fun use(
        state: BlockState, level: Level, pos: BlockPos, player: Player, hand: InteractionHand, hit: BlockHitResult
    ): InteractionResult {
        val helper = PlacementHelpers.get(DrillHeadBlock.PLACEMENT_HELPER_ID)
        val stack = player.getItemInHand(hand)

        return if (player.mayBuild() && helper.matchesItem(stack)) {
            helper.getOffset(player, level, state, pos, hit)
                .placeInWorld(level, stack.item as BlockItem, player, hand, hit)
        } else {
            super.use(state, level, pos, player, hand, hit)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
        return SHAPE.get(state.getValue(FACING))
    }

    override fun onNeighborChange(state: BlockState, level: LevelReader, pos: BlockPos, neighbor: BlockPos) {
        withBlockEntityDo(level, pos, DrillCoreBlockEntity::progressNextTick)
    }

    override fun getRotationAxis(state: BlockState): Direction.Axis {
        return state.getValue(FACING).axis
    }

    override fun hasShaftTowards(world: LevelReader, pos: BlockPos, state: BlockState, face: Direction): Boolean {
        return face.axis == state.getValue(FACING).axis
    }

}