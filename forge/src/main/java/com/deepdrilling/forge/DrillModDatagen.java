package com.deepdrilling.forge;

import com.deepdrilling.DrillMod;
import com.tterrag.registrate.providers.RegistrateDataProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DrillMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DrillModDatagen {

    public void onInitializeDataGenerator(GatherDataEvent event) {
//        ExistingFileHelper helper = new ExistingFileHelper(Set.of(Paths.get(System.getProperty("existing_resources"))), Set.of("create"), false, null, null);

        var generator = event.getGenerator();
        var lookupProvider = event.getLookupProvider();
        var packOutput = generator.getPackOutput();

//        DrillMod.REGISTRATE.(pack, helper);
//        DrillMod.REGISTRATE.addDataGenerator()
//        generator.addProvider(event.includeClient(), new RegistrateDataProvider(DrillMod.REGISTRATE, DrillMod.MOD_ID, event));
//        DrillMod.REGISTRATE.addLang("itemGroup", DrillMod.id("main"), "Deep Drilling");
//        DrillMod.REGISTRATE.addDataGenerator(ProviderType.LANG, LangStuff::register);
//
//        event.getGenerator()
//                .addProvider(event.includeClient(), new DrillSequencedRecipes(event.getGenerator().getPackOutput()));
//        event.getGenerator()
//                .addProvider(event.includeClient(), new MixingRecipes(event.getGenerator().getPackOutput()));

        /*
        TODO datagen maybe idk
         - basic crafting recipes lol
         - ore node + loot table (!!)
         - configured + placed features
         - biome tags
         */
    }
}
