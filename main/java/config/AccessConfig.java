package config;

import model.db.DBClient;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xlo on 15-8-30.
 * it's the config of access
 */
public class AccessConfig implements ConfigInterface {
    protected Map<String, Map<String, Integer>> access;

    protected AccessConfig() {
        this.access = new HashMap<>();
    }

    @Override
    public void init() throws Exception {
        Element root = ConfigInterface.getRootElement("/access.xml");
        for (Object now : root.elements()) {
            Element element = (Element) now;
            for (Object kidObject : element.elements()) {
                Element method = (Element) kidObject;
                addAccess(element.attributeValue("name"), method.attributeValue("name"),
                        Integer.valueOf(method.getText()));
            }
        }
    }

    private void addAccess(String name, String method, Integer value) {
        if (!this.access.containsKey(name)) {
            this.access.put(name, new HashMap<>());
        }
        this.access.get(name).put(method, value);
    }

    @Override
    public void reload() throws Exception {
        this.access.clear();
        init();
    }

    public int getAccessNeed() {
        String name = Thread.currentThread().getStackTrace()[2].getClassName();
        String method = Thread.currentThread().getStackTrace()[2].getMethodName();
        return checkClassAndMethod(name, method);
    }

    public boolean isAccept(DBClient.DBData data) {
        String name = Thread.currentThread().getStackTrace()[2].getClassName();
        String method = Thread.currentThread().getStackTrace()[2].getMethodName();
        int need = checkClassAndMethod(name, method);
        int have;
        if (data.object.containsKey("access")) {
            have = data.object.getInteger("access");
        } else {
            have = 0;
        }
        return have >= need;
    }

    protected int checkClassAndMethod(String name, String method) {
        if (!this.access.containsKey(name)) {
            return 0;
        } else if (!this.access.get(name).containsKey(method)) {
            return 0;
        } else {
            return this.access.get(name).get(method);
        }
    }
}
