package uz.shahzod.combinedenchants;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(CombinedEnchantsMod.MODID)
public class CombinedEnchantsMod {

    public static final String MODID = "combinedenchants";

    public CombinedEnchantsMod() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
    }
}
