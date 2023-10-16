package elocindev.yosby.forge;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(YOSBY.MODID)
public class YOSBY {
    public static final String MODID = "yosby";
    private static final Logger LOGGER = LogUtils.getLogger();

    public YOSBY() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
      LOGGER.info("YOSBY is brought to you by the \"Association for Options Rights\" gang.");
    }
}