package config;

import org.dom4j.Element;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by xlo on 2015/8/19.
 * it's the manager of config
 */
public class ConfigManager implements ConfigInterface {
    private volatile static ConfigManager configManager = null;
    private volatile static boolean needInit = true;

    protected Set<ConfigInterface> configInterfaces;

    private ConfigManager() {
        init();
    }

    public static ConfigManager getConfigManager() {
        if (configManager == null) {
            synchronized (ConfigManager.class) {
                if (configManager == null) {
                    configManager = new ConfigManager();
                }
            }
        }
        return configManager;
    }

    public ConfigInterface getConfig(Class<? extends ConfigInterface> config) {
        if (needInit) init();
        for (ConfigInterface configInterface : this.configInterfaces) {
            if (configInterface.getClass().equals(config)) {
                return configInterface;
            }
        }
        return null;
    }

    public void reloadConfig(Class<? extends ConfigInterface> config) {
        if (needInit) init();
        for (ConfigInterface now : this.configInterfaces) {
            if (!now.getClass().equals(config)) continue;
            try {
                now.reload();
            } catch (Exception ignored) {
            }
        }
    }

    @Override
    public void init() {
        this.configInterfaces = new HashSet<>();
        try {
            Element root = ConfigInterface.getRootElement("/configs.xml");
            List node = root.elements();
            for (Object now : node) {
                Element element = (Element) now;
                try {
                    ConfigInterface configInterface = (ConfigInterface) Class.forName(element.getText()).newInstance();
                    configInterface.init();
                    this.configInterfaces.add(configInterface);
                } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            needInit = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void reload() {
        this.configInterfaces.clear();
        this.init();
        for (ConfigInterface now : this.configInterfaces) {
            try {
                now.reload();
            } catch (Exception ignored) {
            }
        }
    }
}
