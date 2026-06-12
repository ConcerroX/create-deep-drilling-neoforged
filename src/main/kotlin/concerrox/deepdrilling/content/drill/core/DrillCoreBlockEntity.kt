package concerrox.deepdrilling.content.drill.core

import com.simibubi.create.content.kinetics.base.BlockBreakingKineticBlockEntity
import com.simibubi.create.content.kinetics.base.KineticBlockEntity
import concerrox.deepdrilling.content.drill.DrillData
import concerrox.deepdrilling.registry.ModRegistries
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtUtils
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.Mth
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.sqrt

class DrillCoreBlockEntity(type: BlockEntityType<DrillCoreBlockEntity>, pos: BlockPos, state: BlockState) :
    KineticBlockEntity(type, pos, state) {

    private val breakerId = BlockBreakingKineticBlockEntity.NEXT_BREAKER_ID.incrementAndGet().unaryMinus()
    private var ticksUntilProgress = 0 // increments destroyProgress at 0, is -1 if not progressing at all
    private var destroyProgress = 0

    private var breakingPosition = BlockPos(0, 0, 0)
    private var drillData: DrillData? = null

    @Deprecated("Use progressNextTick() instead")
    private val modules = mutableListOf<Module>()

//    // { modifierType: [(distance from core, associated BE, modifier)] }
//    private val modifiers: HashMap<Modifier.Type?, MutableList<Truple<Int?, BlockEntity?, Modifier?>>> =
//        HashMap<Modifier.Type?, MutableList<Truple<Int?, BlockEntity?, Modifier?>>>()

    @Deprecated("Use progressNextTick() instead")
    val isStalled: Boolean
        get() = ticksUntilProgress < 0

    @Deprecated("Use progressNextTick() instead")
    fun progressNextTick() {
        ticksUntilProgress = 1
    }

    val drillHeadPosition: BlockPos
        get() = blockPos.relative(blockState.getValue(DrillCoreBlock.FACING))

    fun findBreakingPosition(): BlockPos = blockPos.relative(blockState.getValue(DrillCoreBlock.FACING), 2)

    fun createDrillData() {

    }

//    fun ifDrillHeadDo(lambda: Consumer<DrillHeadBE>) {
//        val drillHead: DrillHeadBE = getDrillHead()
//        if (drillHead != null) {
//            lambda.accept(drillHead)
//        }
//    }
//
//    // this is awful :3
//    fun <T> applyModifiers(baseValue: T?, type: Modifier.Type<T?>?): T? {
//        var value = baseValue
//        for (modifier in modifiers.getOrDefault(type, mutableListOf<Truple<Int?, BlockEntity?, Modifier?>?>())) {
//            value = modifier.getC().modifier.apply(this, getDrillHead(), modifier.getB(), baseValue, value) as T?
//        }
//        return value
//    }

    // ticks per mining operation (will be affected by rounding)
    fun calculateSpeed(): Double {
        if (getSpeed() == 0f) return -1.0
        // 10 seconds at 256 rpm, 20s at 64, 40s at 16
        val baseSpeed = (20 * 10) / sqrt(abs(getSpeed() / 256.0))
        return max(0.0, baseSpeed)//TODO:applyModifiers(baseSpeed, ModifierTypes.SPEED))
    }
//
//    fun calculateDamage(): Double {
//        return max(applyModifiers<Double?>(1.0, ModifierTypes.DAMAGE)!!, 0.0)
//    }
//
//    val weightMultipliers: DrillHeadStats.WeightMultipliers?
//        get() = applyModifiers<DrillHeadStats.WeightMultipliers?>(
//            DrillHeadStats.WeightMultipliers.ONE, ModifierTypes.RESOURCE_WEIGHT
//        )

    fun ticksPerProgress(): Int {
        val destroySpeed = calculateSpeed()
        if (destroySpeed < 0) return -1
        return Mth.clamp(0.05 * destroySpeed, 1.0, 160.0).toInt()
    }
//
//    var drillHead: DrillHeadBE? = null
//    fun getDrillHead(): DrillHeadBE? {
//        if (drillHead == null && level!!.getBlockEntity(this.drillHeadPosition) is DrillHeadBE) {
//            drillHead = levelDrillHead
//            findModules()
//        }
//        return drillHead
//    }
//
//    fun removeDrillHead() {
//        drillHead = null
//        findModules()
//    }

    fun canDrill(state: BlockState): Boolean {
        return true
        //!state.liquid() && !state.isAir && getDrillHead() != null && OreNodes.get(state.block)
        //  .hasTables() && applyModifiers(true, ModifierTypes.CAN_FUNCTION)
    }

    fun getDrops(level: ServerLevel): MutableList<ItemStack> {
//        val node = OreNodes.get(level.getBlockState(breakingPosition).block)
//        val type: OreNode.LOOT_TYPE? = this.weightMultipliers.pick(level.random)
//
//        val lootTable = node.getTable(level, type)
//        // todo: proper lootcontextparam
//        val lootParams = LootParams.Builder(level).run {
//            withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(breakingPosition))
//            create(LootContextParamSets.ARCHAEOLOGY)
//        }
//        val drops = mutableListOf<ItemStack>()
//        var dropCount = 1.0//: Double = applyModifiers<Double?>(1.0, ModifierTypes.FORTUNE)!!
//        while (dropCount-- > level.random.nextFloat()) {
//            drops.addAll(lootTable.getRandomItems(lootParams))
//        }
        return mutableListOf()
    }

    fun mineBlock(level: ServerLevel) {
//        findModules()

        val drops = /*applyModifiers(*/getDrops(level)//, ModifierTypes.OUTPUT_LIST)
        for (drop in drops) {
            level.addFreshEntity(ItemEntity(level, blockPos.x + 0.5, blockPos.y + 0.5, blockPos.z + 0.5, drop))
        }
    }

//    fun findModules() {
//        val pos = MutableBlockPos()
//        pos.set(getBlockPos())
//        val dir: Direction = getBlockState().getValue<T?>(DrillCore.FACING).getOpposite()
//
//        modules.clear()
//        modifiers.clear()
//
//        val uniqueNames: MutableSet<ResourceLocation?> = HashSet<ResourceLocation?>()
//
//        for (zingusValue in 0..<searchDist) {
//            pos.move(dir)
//            val candidate = level!!.getBlockEntity(pos)
//
//            if (candidate is ModuleBE && candidate.getModuleAxis() === getBlockState().getValue<T?>(DrillCore.FACING)
//                    .getAxis()
//            ) {
//                if (Collections.disjoint(uniqueNames, candidate.getMutuallyExclusiveNames())) {
//                    uniqueNames.addAll(candidate.getMutuallyExclusiveNames())
//
//                    modules.add(candidate)
//                    for (modifier in candidate.getModifiers()) {
//                        modifiers.computeIfAbsent(modifier.type) { k: Modifier.Type? -> ArrayList<Truple<Int?, BlockEntity?, Modifier?>>() }
//                            .add(Truple(zingusValue, candidate, modifier))
//                    }
//                }
//            } else {
//                break
//            }
//        }
//        if (getDrillHead() != null) {
//            for (modifier in getDrillHead().getModifiers()) {
//                modifiers.computeIfAbsent(modifier.type) { k: Modifier.Type? -> ArrayList<Truple<Int?, BlockEntity?, Modifier?>>() }
//                    .add(Truple(-1, getDrillHead(), modifier))
//            }
//        }
//
//        for (modifierInstances in modifiers.values) {
//            modifierInstances.sort(Modifier.modifierComparator)
//        }
//    }

    override fun addToGoggleTooltip(tooltip: MutableList<Component?>?, isPlayerSneaking: Boolean): Boolean {
        var `val` = super.addToGoggleTooltip(tooltip, isPlayerSneaking)
        // this value is no longer getting synced to the client properly :/
        /*if (getSpeed() != 0) {
            int totalTicks = ticksPerProgress() * 10;
            Lang.text("Drilling every:").style(ChatFormatting.GRAY)
                    .forGoggles(tooltip);
            String time = LangNumberFormat.format(totalTicks / 20f);
            Lang.builder().space()
                    .text(time)
                        .style(ChatFormatting.AQUA)
                    .add(Lang.text(Objects.equals(time, "1") ? " second" : " seconds")
                        .style(ChatFormatting.DARK_GRAY))
                    .forGoggles(tooltip);
            val = true;
        }*/
//        if (!modules.isEmpty() && isPlayerSneaking) {
//            Lang.text("Attached Modules:").style(ChatFormatting.GRAY).forGoggles(tooltip)
//            for (module in modules) {
//                Lang.builder().space().add(module.getName()).style(ChatFormatting.GRAY).forGoggles(tooltip)
//            }
//            `val` = true
//        }

        return `val`
    }

//    override fun onSpeedChanged(previousSpeed: Float) {
//        super.onSpeedChanged(previousSpeed)
//        if (this.isStalled) progressNextTick()
//        ticksUntilProgress = min(ticksUntilProgress, ticksPerProgress())
//    }

    override fun lazyTick() {
        super.lazyTick()
        if (this.isStalled) progressNextTick()
    }

    override fun invalidate() {
        super.invalidate()
        if (!level!!.isClientSide && destroyProgress != 0) level!!.destroyBlockProgress(breakerId, breakingPosition, -1)
    }

    override fun tick() {
        super.tick()

        val serverLevel = level
        if (serverLevel !is ServerLevel || getSpeed() == 0f || ticksUntilProgress < 0) return

        breakingPosition = findBreakingPosition()
        val breakingState = serverLevel.getBlockState(breakingPosition)

        if (!canDrill(breakingState)) {
            if (destroyProgress > 0) {
                destroyProgress = 0
                serverLevel.destroyBlockProgress(breakerId, breakingPosition, -1)
            }
            return
        }

        if (ticksUntilProgress-- == 0) {
            ticksUntilProgress = ticksPerProgress()
            destroyProgress++
//            modules.forEach { m: Module -> m.progressBreaking(this) }
            if (destroyProgress >= 10) {
                destroyProgress = 0
//                modules.forEach { m: Module -> m.blockBroken(this) }
                mineBlock(serverLevel)
//                ifDrillHeadDo { drillHead: DrillHeadBE? -> drillHead.applyDamage(calculateDamage()) }
                serverLevel.destroyBlockProgress(breakerId, breakingPosition, -1)
            } else {
                serverLevel.destroyBlockProgress(breakerId, breakingPosition, destroyProgress)
            }
        }
    }

    override fun write(compound: CompoundTag, clientPacket: Boolean) {
        compound.putInt("Progress", destroyProgress)
        compound.putInt("NextTick", ticksUntilProgress)
        if (breakingPosition != null) compound.put("Breaking", NbtUtils.writeBlockPos(breakingPosition))
        super.write(compound, clientPacket)
    }

    override fun read(compound: CompoundTag, clientPacket: Boolean) {
        destroyProgress = compound.getInt("Progress")
        ticksUntilProgress = compound.getInt("NextTick")
        if (compound.contains("Breaking")) breakingPosition = NbtUtils.readBlockPos(compound.getCompound("Breaking"))
        super.read(compound, clientPacket)
    }

    companion object {
        var searchDist: Int = 5
    }

}