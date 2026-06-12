package concerrox.deepdrilling.registry

import concerrox.deepdrilling.id
import dev.engine_room.flywheel.lib.model.baked.PartialModel

object ModPartialModels {

    internal fun register() {}

    val DRILL_CORE_SHAFT = block("drill_core/shaft")

    fun block(path: String): PartialModel = PartialModel.of(id("block/$path"))

}