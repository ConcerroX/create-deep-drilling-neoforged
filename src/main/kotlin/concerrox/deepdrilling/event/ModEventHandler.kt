package concerrox.deepdrilling.event

import concerrox.deepdrilling.content.drill.orenode.OreNode
import concerrox.deepdrilling.registry.ModRegistries
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.registries.DataPackRegistryEvent

object ModEventHandler {

    @SubscribeEvent
    fun onNewDataPackRegistry(event: DataPackRegistryEvent.NewRegistry) {
        event.dataPackRegistry(ModRegistries.ORE_NODE, OreNode.CODEC, OreNode.CODEC)
    }

}