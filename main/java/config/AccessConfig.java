package config;

import javafx.util.Pair;
import model.db.DBClient;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xlo on 15-8-30.
 * it's the config of access
 */
public class AccessConfig implements ConfigInterface {
    protected Map<String, Map<String, Pair<String, Integer>>> access;

    protected AccessConfig() {
        this.access = new HashMap<>();
    }

    public static AccessConfig getConfig() {
        return (AccessConfig) ConfigManager.getConfigManager().getConfig(AccessConfig.class);
    }

    @Override
    public void init() throws Exception {
        Element root = ConfigInterface.getRootElement("/access.xml");
        for (Object now : root.elements()) {
            Element element = (Element) now;
            for (Object kidObject : element.elements()) {
                Element method = (Element) kidObject;
                addAccess(element.attributeValue("name"), method.attributeValue("name"), method.attributeValue("filed"),
                        Integer.valueOf(method.getText()));
            }
        }
    }

    private void addAccess(String name, String method, String filed, Integer value) {
        if (!this.access.containsKey(name)) {
            this.access.put(name, new HashMap<>());
        }
        this.access.get(name).put(method, new Pair<>(filed, value));
    }

    @Override
    public void reload() throws Exception {
        this.access.clear();
        init();
    }

    public Pair<String, Integer> getAccessNeed() {
        String name = Thread.currentThread().getStackTrace()[2].getClassName();
        String method = Thread.currentThread().getStackTrace()[2].getMethodName();
        return checkClassAndMethod(name, method);
    }

    public boolean isAccept(DBClient.DBData data) {
        String name = Thread.currentThread().getStackTrace()[2].getClassName();
        String method = Thread.currentThread().getStackTrace()[2].getMethodName();
        Pair<String, Integer> need = checkClassAndMethod(name, method);
        if (need == null) return true;
        int have;
        if (data.object.containsKey(need.getKey())) {
            have = data.object.getInteger(need.getKey());
        } else {
            have = 0;
        }
        return have >= need.getValue();
    }

    protected Pair<String, Integer> checkClassAndMethod(String name, String method) {
        if (!this.access.containsKey(name)) {
            return null;
        } else if (!this.access.get(name).containsKey(method)) {
            return null;
        } else {
            return this.access.get(name).get(method);
        }
    }
}
