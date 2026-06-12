package concerrox.deepdrilling.content.drill.head

import com.simibubi.create.content.kinetics.base.KineticBlockEntity
import concerrox.deepdrilling.content.drill.core.DrillCoreBlockEntity
import concerrox.deepdrilling.langBuilder
import net.createmod.catnip.lang.LangNumberFormat
import net.minecraft.ChatFormatting
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

class DrillHeadBlockEntity(
    typeIn: BlockEntityType<DrillHeadBlockEntity>, pos: BlockPos, state: BlockState,
) : KineticBlockEntity(typeIn, pos, state) {

    companion object {
        const val DAMAGE_KEY = "Damage"
        const val UNBREAKABLE_KEY = "Unbreakable"
        const val ENCHANTMENTS_KEY = "Enchantments"
    }

    val coreBlockEntity
        get(): DrillCoreBlockEntity? {
            val be = level?.getBlockEntity(blockPos.relative(blockState.getValue(DrillHeadBlock.FACING).opposite))
            return be as? DrillCoreBlockEntity
        }

    private val maxDamage = (state.block as DrillHeadBlock).durability.toDouble()
    private var damage = 0.0
        set(value) {
            if (value >= maxDamage && !isUnbreakable && !hasMendingEnchantment) {
                level?.destroyBlock(blockPos, false)
            } else {
                field = value.coerceAtMost(maxDamage)
                setChanged()
                sendData()
            }
        }

    private var isUnbreakable = false
    private var enchantments: MutableMap<Enchantment, Int> = HashMap<Enchantment, Int>()

    private var unbreakingEnchantmentLevel = 0
    private var efficiencyEnchantmentLevel = 0
    private var fortuneEnchantmentLevel = 0
    private var hasMendingEnchantment = false

    private val isFunctional
        get() = if (hasMendingEnchantment) damage < maxDamage else true
    private val isEnchanted
        get() = unbreakingEnchantmentLevel > 0 || efficiencyEnchantmentLevel > 0 || fortuneEnchantmentLevel > 0 || hasMendingEnchantment

    fun applyDamage(amount: Int) {
        if (!isUnbreakable) damage += amount
    }

//    private static
//    val MODIFIER_FUNCTION: Modifier<Boolean?, com.deepdrilling.blockentities.drillhead.DrillHeadBE?> =
//        ModifierTypes.CAN_FUNCTION.create(
//            ({ core, head, be, base, prev -> prev && be.isFunctional() }), 0
//        )

//    private fun getSpeedModifier(): Double {
//        val name = BuiltInRegistries.BLOCK.getKey(getBlockState().getBlock())
//        val speedStat = DrillHeadStats.DRILL_SPEED_MODIFIERS.getOrDefault(name, 1.0)
//        return speedStat / (1 + efficiencyEnchantmentLevel.toDouble() / 5)
//    }

//    val MODIFIER_SPEED: Modifier<Double?, com.deepdrilling.blockentities.drillhead.DrillHeadBE?> =
//        ModifierTypes.SPEED.create(
//            ({ core, head, be, base, prev -> prev * be.getSpeedModifier() }), 1000
//        )

//    private fun getDamageModifier(): Double {
//        return 1.0 / (1 + unbreakingEnchantmentLevel)
//    }

//    val MODIFIER_DAMAGE: Modifier<Double?, com.deepdrilling.blockentities.drillhead.DrillHeadBE?> =
//        ModifierTypes.DAMAGE.create(
//            ({ core, head, be, base, prev -> prev * be.getDamageModifier() }), -1000
//        )

//    private fun getFortuneAmount(): Double {
//        return fortuneEnchantmentLevel.toDouble() / 3
//    }
//    private static
//    val MODIFIER_FORTUNE: Modifier<Double?, com.deepdrilling.blockentities.drillhead.DrillHeadBE?> =
//        ModifierTypes.FORTUNE.create(
//            ({ core, head, be, base, prev -> prev + be.getFortuneAmount() }), 1000
//        )

//    private fun getWeightMultipliers(): DrillHeadStats.WeightMultipliers {
//        val name = BuiltInRegistries.BLOCK.getKey(getBlockState().getBlock())
//        return DrillHeadStats.LOOT_WEIGHT_MULTIPLIER.getOrDefault(name, DrillHeadStats.WeightMultipliers.ONE)
//    }
//
//    val MODIFIER_WEIGHTS: Modifier<DrillHeadStats.WeightMultipliers?, com.deepdrilling.blockentities.drillhead.DrillHeadBE?> =
//        ModifierTypes.RESOURCE_WEIGHT.create(
//            ({ core, head, be, base, prev -> prev.mul(be.getWeightMultipliers()) }), 1000
//        )
//
//    fun getModifiers(): MutableList<Modifier?> {
//        return List.of<Modifier?>(
//            MODIFIER_FUNCTION, MODIFIER_SPEED, MODIFIER_DAMAGE, MODIFIER_FORTUNE, MODIFIER_WEIGHTS
//        )
//    }

    fun writeItemNbt(stack: ItemStack): ItemStack {
        val tag = stack.getOrCreateTag()
        tag.putInt(DAMAGE_KEY, damage.toInt())
        tag.putBoolean(UNBREAKABLE_KEY, isUnbreakable)
        EnchantmentHelper.setEnchantments(enchantments, stack)
        return stack
    }

    fun readItemNbt(stack: ItemStack) {
        val tag = stack.getOrCreateTag()
        isUnbreakable = tag.getBoolean(UNBREAKABLE_KEY)
        enchantments = EnchantmentHelper.getEnchantments(stack)
        updateEnchantments()
        damage = tag.getInt(DAMAGE_KEY).toDouble()
    }

    private fun updateEnchantments() {
        unbreakingEnchantmentLevel = enchantments.getOrDefault(Enchantments.UNBREAKING, 0)
        efficiencyEnchantmentLevel = enchantments.getOrDefault(Enchantments.BLOCK_EFFICIENCY, 0)
        fortuneEnchantmentLevel = enchantments.getOrDefault(Enchantments.BLOCK_FORTUNE, 0)
        hasMendingEnchantment = enchantments.getOrDefault(Enchantments.MENDING, 0) > 0
    }

    override fun remove() {
//        coreBlockEntity?.removeDrillHead()
        super.remove()
    }


    override fun addToGoggleTooltip(tooltip: MutableList<Component>, isPlayerSneaking: Boolean): Boolean {
        super.addToGoggleTooltip(tooltip, isPlayerSneaking)
        if (isUnbreakable) return true

        langBuilder().text("Head Durability:").style(ChatFormatting.GRAY).forGoggles(tooltip)

        val damageRatio = damage / maxDamage
        val damageFmt = if (damageRatio < 0.25) {
            ChatFormatting.GREEN
        } else if (damageRatio < 0.5) {
            ChatFormatting.YELLOW
        } else if (damageRatio < 0.75) {
            ChatFormatting.GOLD
        } else {
            ChatFormatting.RED
        }

        langBuilder().space().text(LangNumberFormat.format(maxDamage - damage)).style(damageFmt)
            .add(langBuilder().text(" / ${maxDamage.toInt()}").style(ChatFormatting.DARK_GRAY)).forGoggles(tooltip)
        return true
    }

    override fun read(compound: CompoundTag, clientPacket: Boolean) {
        super.read(compound, clientPacket)
        damage = compound.getDouble(DAMAGE_KEY)
        isUnbreakable = compound.getBoolean(UNBREAKABLE_KEY)
        enchantments = EnchantmentHelper.deserializeEnchantments(
            compound.getList(ENCHANTMENTS_KEY, CompoundTag.TAG_COMPOUND.toInt())
        )
        updateEnchantments()
    }

    override fun write(compound: CompoundTag, clientPacket: Boolean) {
        super.write(compound, clientPacket)

        compound.putDouble(DAMAGE_KEY, damage)
        compound.putBoolean(UNBREAKABLE_KEY, isUnbreakable)

        val enchantmentsTag = ListTag()
        for (entry in enchantments.entries) {
            val enchantment = entry.key
            enchantmentsTag.add(
                EnchantmentHelper.storeEnchantment(EnchantmentHelper.getEnchantmentId(enchantment), entry.value)
            )
        }
        compound.put(ENCHANTMENTS_KEY, enchantmentsTag)
    }

}