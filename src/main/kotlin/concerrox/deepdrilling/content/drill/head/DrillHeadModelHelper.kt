package concerrox.deepdrilling.content.drill.head

import com.tterrag.registrate.providers.DataGenContext
import com.tterrag.registrate.providers.RegistrateBlockstateProvider
import net.minecraft.world.level.block.Block
import net.minecraftforge.client.model.generators.ConfiguredModel

object DrillHeadModelHelper {

    fun createBlockStateAndModel(
        context: DataGenContext<Block, DrillHeadBlock>,
        stateProvider: RegistrateBlockstateProvider,
        name: String,
        headTexture: String
    ) {
        stateProvider.getVariantBuilder(context.get()).forAllStates { _ ->
            ConfiguredModel.builder()
                .modelFile(stateProvider.models().getBuilder("block/$name").texture("particle", headTexture))
                .build()
        }
        ConfiguredModel.builder().modelFile(
            stateProvider.models().withExistingParent(name, stateProvider.modLoc("block/drill_head/base_drill_head"))
                .texture("0", headTexture)
        )
    }

}