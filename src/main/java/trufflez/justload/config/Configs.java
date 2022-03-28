package trufflez.justload.config;

import com.mojang.datafixers.util.Pair;

public class Configs {
    public static SimpleConfig CONFIG;
    private static ConfigProvider configs;
    
    public static boolean LOAD_LAST_PLAYED_WORLD;
    
    public static void init() {
        configs = new ConfigProvider();
        createConfigs();
        
        CONFIG = SimpleConfig.of("JustLoad").provider(configs).request();
        
        assignConfigs();
    }

    private static void createConfigs() {
        configs.append("\n# Just Load\n# by TrufflezMC\n");
        configs.append("\n# Load last played world (default=true)");
        configs.addKeyValuePair(new Pair<>("loadLastPlayedWorld", true));
    }
    
    private static void assignConfigs() {
        LOAD_LAST_PLAYED_WORLD = CONFIG.getOrDefault("removeToastsSFX", true);
    }
}
