package concerrox.deepdrilling

import concerrox.deepdrilling.registry.ModBlockEntityTypes
import concerrox.deepdrilling.registry.ModBlocks
import concerrox.deepdrilling.registry.ModPartialModels
import com.simibubi.create.foundation.data.CreateRegistrate
import concerrox.deepdrilling.event.ModEventHandler
import net.createmod.catnip.lang.Lang
import net.minecraft.resources.ResourceLocation
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.fml.DistExecutor
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext

internal val Minecraft = net.minecraft.client.Minecraft.getInstance()
internal fun id(path: String) = ResourceLocation.fromNamespaceAndPath(CreateDeepDrilling.MOD_ID, path)
internal fun registrate() = CreateDeepDrilling.REGISTRATE
internal fun langBuilder() = Lang.builder(CreateDeepDrilling.MOD_ID)

@Mod(CreateDeepDrilling.MOD_ID)
class CreateDeepDrilling {

    companion object {
        const val MOD_ID = "deepdrilling"
        internal val REGISTRATE = CreateRegistrate.create(MOD_ID)
    }

    init {
        @Suppress("removal", "DEPRECATION") val eventBus = FMLJavaModLoadingContext.get().modEventBus
        eventBus.register(ModEventHandler)
        registerRegistrate(eventBus)

        ModBlocks.register()
        ModBlockEntityTypes.register()

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT) {
            Runnable { ModPartialModels.register() }
        }

//        FluidsImpl.init();
//        OreNodeStructureImpl.init();
//        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
//            DrillModClientForge.setupEvents(BUS);
//        });
    }

    private fun registerRegistrate(eventBus: IEventBus) {
        //        DrillMod.REGISTRATE.setTooltipModifierFactory(item -> new ItemDescription.Modifier(item, TooltipHelper.Palette.STANDARD_CREATE)
//                .andThen(TooltipModifier.mapNull(KineticStats.create(item)))
//                .andThen(TooltipModifier.mapNull(DrillHeadTooltipsForge.create(item)))
//        );
        REGISTRATE.registerEventListeners(eventBus)
    }

}