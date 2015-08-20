package config;

import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import tool.ResourceManager;

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
            if (configInterface.getClass().equals(config)) {
                return configInterface;
            }
        }
        return null;
    }

    public void reloadConfig(Class<? extends ConfigInterface> config) {
        this.configInterfaces.stream().filter(configInterface ->
                configInterface.getClass().equals(config)).forEach(configInterface -> configInterface.reload());
    }

    @Override
    public void init() {
        try {
            Element root = DocumentHelper.parseText(new String(ResourceManager.getResourceManager().getResource("/configs.xml"))).getRootElement();
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
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void reload() {
        this.configInterfaces.clear();
        this.init();
        this.configInterfaces.forEach(ConfigInterface::reload);
    }
}
