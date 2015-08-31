package config;

import javafx.util.Pair;
import model.db.DBClient;
import model.db.UserCollection;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by xlo on 15-8-30.
 * it's the config of access
 */
public class AccessConfig implements ConfigInterface {
    protected Map<String, Map<String, Set<Pair<String, Integer>>>> access;

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
        if (!this.access.get(name).containsKey(method)) {
            this.access.get(name).put(method, new HashSet<>());
        }
        this.access.get(name).get(method).add(new Pair<>(filed, value));
    }

    @Override
    public void reload() throws Exception {
        this.access.clear();
        init();
    }

    public Set<Pair<String, Integer>> getAccessNeed() {
        String name = Thread.currentThread().getStackTrace()[getAimPos()].getClassName();
        String method = Thread.currentThread().getStackTrace()[getAimPos()].getMethodName();
        return checkClassAndMethod(name, method);
    }

    public boolean isAccept(DBClient.DBData data) {
        Set<Pair<String, Integer>> need = getAccessNeed();
        if (need == null) return true;
        for (Pair<String, Integer> now : need) {
            int have;
            if (data.object.containsKey(now.getKey())) {
                have = data.object.getInteger(now.getKey());
            } else {
                have = 0;
            }
            if (have >= now.getValue()) return true;
        }
        return false;
    }

    public boolean isAccept(String username, String password) {
        if (username == null || password == null) return false;
        UserCollection userCollection = new UserCollection();
        DBClient.DBData data = userCollection.getUserData(username);
        return data != null && data.object.get("password").equals(password) && isAccept(data);
    }

    protected Set<Pair<String, Integer>> checkClassAndMethod(String name, String method) {
        if (!this.access.containsKey(name)) {
            return null;
        } else if (!this.access.get(name).containsKey(method)) {
            return null;
        } else {
            return this.access.get(name).get(method);
        }
    }

    private int getAimPos() {
        for (int i = 0; i < Thread.currentThread().getStackTrace().length; i++) {
            if (!Thread.currentThread().getStackTrace()[i].getClassName().equals(this.getClass().getName())) {
                return i;
            }
        }
        return -1;
    }
}
