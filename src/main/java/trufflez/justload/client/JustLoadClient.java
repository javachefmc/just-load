package trufflez.justload.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import trufflez.justload.config.Configs;

import java.io.File;
import java.nio.file.Path;

@Environment(EnvType.CLIENT)
public class JustLoadClient implements ClientModInitializer {
    public static final String MODID = "justload";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    /* 
        minecraft.loadLevel(String) does not check for version compatibility or even level existence!
        While JustLoadClient.loadWorld(File) is only called if the level probably exists (directory + level.dat exists),
        it does not check for game version or potential level corruption like Minecraft actually does. 
     */
    
    @Override
    public void onInitializeClient() {
        Configs.init();
    }
    
    // This is called by JustLoadEntrypoint mixin
    public static void init() {
        LOGGER.info("JustLoad is initializing.");
        
        Minecraft minecraft = Minecraft.getInstance();
        
        if (Configs.WORLD_NAME.isBlank()) {    // If WorldName is empty
            
            loadLastPlayedWorld();
            
        } else {                               // If WorldName contains more than whitespace

            LOGGER.info("Attempting to load world from config...");
            
            // Check if world exists
            Path path = minecraft.gameDirectory.toPath().resolve("saves");
            
            File file = path.resolve(Configs.WORLD_NAME).toFile();
            
            if (file.exists()) { // Config has valid path
                if (file.isDirectory()) { // Config path is folder

                    File levelFile = new File(file, "level.dat");
                    if (levelFile.exists()) { // Check if this folder is probably a world save
                        
                        loadWorld(file);
                        
                    }
                    
                } else {
                    LOGGER.info("Error while reading config: world name is not of a directory.");
                    
                    if (Configs.LOAD_LAST_ON_ERROR) {
                        
                        loadLastPlayedWorld();
                        
                    } else {
                        LOGGER.info("Redirecting to title screen.");
                        LOGGER.info("JustLoad complete.");
                    }
                }
            } else {
                LOGGER.info("Error while reading config: world name returns invalid path.");
                
                if (Configs.LOAD_LAST_ON_ERROR) {
                    
                    loadLastPlayedWorld();
                    
                } else {
                    LOGGER.info("Redirecting to title screen.");
                    LOGGER.info("JustLoad complete.");
                }
            }
        }
    }
    
    public static void loadLastPlayedWorld() {
        // Load last played world
        LOGGER.info("Attempting to load last played world...");
        
        Minecraft minecraft = Minecraft.getInstance();
        Path path = minecraft.gameDirectory.toPath().resolve("saves");
        
        File worldFile = null;
        
        File[] files = path.toFile().listFiles();
        int numberOfFiles;
        numberOfFiles = files.length; // This may be 0.
        
        boolean noWorlds = true;
        
        for(int i = 0; i < numberOfFiles; ++i) { // Loop through all files
            File file = files[i];
            if (file.isDirectory()) { // If this is a folder
                
                File levelFile = new File(file, "level.dat");
                if (levelFile.exists()) { // Check if this folder is probably a world save
                    noWorlds = false;
                    // Because I don't know how to validate this level.dat file, I will just attempt to load this world
                    worldFile = file;
                    break;
                }
            }
        }

        if(noWorlds) { // No worlds were found
            LOGGER.info("No worlds found.");
            if(Configs.CREATE_WORLD_IF_NONE) {
                LOGGER.info("Redirecting to world creation screen.");
                
                minecraft.setScreen(CreateWorldScreen.createFresh(minecraft.screen));
                
            } else {

                LOGGER.info("JustLoad complete.");
                
            }
        } else { // World was found
            if(worldFile != null) {
                
                loadWorld(worldFile);
                
            } else {
                LOGGER.warn("Internal error while loading world.");
            }
        }
    }
    
    public static void loadWorld(File file) {
        Minecraft minecraft = Minecraft.getInstance();
        String filename = file.getName();
        LOGGER.info("Attempting to load world: " + filename);
        LOGGER.info("Any errors from here are handed off to Minecraft.");
        
        // TODO: protect this method call a little bit more. See my blurb above
        
        minecraft.loadLevel(filename);
    }
}
