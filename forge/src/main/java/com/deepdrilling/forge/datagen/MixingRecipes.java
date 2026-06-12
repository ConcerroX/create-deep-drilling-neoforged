package com.deepdrilling.forge.datagen;

import com.deepdrilling.DrillMod;
import com.deepdrilling.fluid.Fluids;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.foundation.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Blocks;

public class MixingRecipes extends ProcessingRecipeGen {
    // we love fabrics unique fluid api it is very cool
    GeneratedRecipe SLUDGE_NETHERRACK = create(DrillMod.id("sludge_netherrack"), b -> b.require(Fluids.getSludge(), 250)
            .require(AllItems.CINDER_FLOUR)
            .output(Blocks.NETHERRACK)
            .requiresHeat(HeatCondition.HEATED));

    public MixingRecipes(PackOutput output) {
        super(output);
    }

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return AllRecipeTypes.MIXING;
    }
}
