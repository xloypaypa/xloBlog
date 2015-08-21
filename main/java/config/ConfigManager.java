package config;

import org.dom4j.DocumentException;
import org.dom4j.Element;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by xlo on 2015/8/19.
 * it's the manager of config
 */
public class ConfigManager implements ConfigInterface {
    private static ConfigManager configManager = new ConfigManager();

    protected Set<ConfigInterface> configInterfaces;

    private ConfigManager() {
        this.configInterfaces = new HashSet<>();
        init();
    }

    public static ConfigManager getConfigManager() {
        return configManager;
    }

    public ConfigInterface getConfig(Class<? extends ConfigInterface> config) {
        for (ConfigInterface configInterface : this.configInterfaces) {
            System.out.println(configInterface.getClass().getName());
            if (configInterface.getClass().equals(config)) {
                return configInterface;
            }
        }
        return null;
    }

    public void reloadConfig(Class<? extends ConfigInterface> config) {
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
        try {
            Element root = ConfigInterface.getRootElement("/configs.xml");
            List node = root.elements();
            for (Object now : node) {
                Element element = (Element) now;
                try {
                    ConfigInterface configInterface = (ConfigInterface) Class.forName("config." + element.getText()).newInstance();
                    configInterface.init();
                    this.configInterfaces.add(configInterface);
                } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
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
