package concerrox.deepdrilling.registry

import concerrox.deepdrilling.content.drill.orenode.OreNode
import concerrox.deepdrilling.id
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey

object ModRegistries {

    val ORE_NODE: ResourceKey<Registry<OreNode>> = ResourceKey.createRegistryKey(id("ore_node"))

}