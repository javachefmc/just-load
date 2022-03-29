package trufflez.justload.config;

import com.mojang.datafixers.util.Pair;

public class Configs {
    public static SimpleConfig CONFIG;
    private static ConfigProvider configs;
    
    public static String WORLD_NAME;
    public static boolean LOAD_LAST_ON_ERROR;
    public static boolean CREATE_WORLD_IF_NONE;
    
    public static void init() {
        configs = new ConfigProvider();
        createConfigs();
        
        CONFIG = SimpleConfig.of("JustLoad").provider(configs).request();
        
        assignConfigs();
    }

    private static void createConfigs() {
        configs.append("\n# Just Load\n# by TrufflezMC\n");
        configs.append("\n# Specify a world name to load automatically: [otherwise, last played world will load]");
        configs.addKeyValuePair(new Pair<>("worldName", ""));
        configs.append("\n# Load last played world if the above world is not found? [otherwise, go to title screen] (default=true)");
        configs.addKeyValuePair(new Pair<>("loadLastOnError", true));
        configs.append("\n# Enter world creation if no worlds exist? (default=true)");
        configs.addKeyValuePair(new Pair<>("createWorldIfNone", true));
    }
    
    private static void assignConfigs() {
        WORLD_NAME = CONFIG.getOrDefault("worldName", "");
        LOAD_LAST_ON_ERROR = CONFIG.getOrDefault("loadLastOnError", true);
        CREATE_WORLD_IF_NONE = CONFIG.getOrDefault("createWorldIfNone", true);
    }
}
