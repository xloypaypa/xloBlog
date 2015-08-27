package config;

import model.db.DBClient;
import org.dom4j.Element;

import java.util.*;

/**
 * Created by xlo on 15-8-23.
 * it's the db config
 */
public class DBConfig implements ConfigInterface {
    protected String host = "127.0.0.1";
    protected int port = 27017;
    protected Map<String, String> collections;

    protected DBConfig() {
        this.collections = new HashMap<>();
    }

    @Override
    public void init() throws Exception {
        Element root = ConfigInterface.getRootElement("/db.xml");
        List node = root.elements();
        for (Object now : node) {
            Element element = (Element) now;
            if (element.getName().equals("host")) this.host = element.getText();
            else if (element.getName().equals("port")) this.port = Integer.valueOf(element.getText());
            else if (element.getName().equals("collection")) {
                this.collections.put(element.attributeValue("type"), element.getText());
            }
        }
    }

    @Override
    public void reload() throws Exception {
        this.init();
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getCollectionName(Class<? extends DBClient> type) {
        return this.collections.get(type.getName());
    }

    public HashMap<String, String> getCollections() {
        return new HashMap<>(this.collections);
    }
}
