package trufflez.justload.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.GenericDirtMessageScreen;
import net.minecraft.network.chat.TranslatableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import trufflez.justload.config.Configs;

@Environment(EnvType.CLIENT)
public class JustLoadClient implements ClientModInitializer {
    public static final String MODID = "justload";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
    
    @Override
    public void onInitializeClient() {
        Configs.init();
        LOGGER.info("Just Load is installed.");
    }
    
    // This is called by JustLoadEntrypoint mixin
    public static void init() {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.forceSetScreen(new GenericDirtMessageScreen(new TranslatableComponent("selectWorld.data_read")));
    }
}
