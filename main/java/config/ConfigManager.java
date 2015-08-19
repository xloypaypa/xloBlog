package config;

/**
 * Created by xlo on 2015/8/19.
 * it's the manager of config
 */
public class ConfigManager {
    private static ConfigManager configManager = new ConfigManager();

    private ConfigManager() {
        
    }

    public static ConfigManager getConfigManager() {
        return configManager;
    }


}
