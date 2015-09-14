package model.config;

import model.script.ForceCacheScriptManager;
import model.script.ScriptManager;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by xlo on 2015/9/10.
 * it's the clojure manager
 */
public class ClojureConfig implements ConfigInterface {
    public static ClojureConfig getConfig() {
        return (ClojureConfig) ConfigManager.getConfigManager().getConfig(ClojureConfig.class);
    }

    @Override
    public void init() throws DocumentException, IOException {
        Element root = ConfigInterface.getRootElement("/clojure.xml");
        ScriptManager scriptManager = ForceCacheScriptManager.getForceCacheScriptManager();
        List node = root.elements();
        for (Object now : node) {
            Element element = (Element) now;
            String path = element.getText();
            File file = new File("." + path);
            if (file.exists()) {
                path = "." + path;
            }
            scriptManager.runScript(path, true);
        }
    }

    @Override
    public void reload() throws DocumentException, IOException {
        init();
    }
}
