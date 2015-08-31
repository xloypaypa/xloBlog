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
    protected Map<String, String> collections, dbOfCollectoin;
    protected Set<String> dbs;

    protected DBConfig() {
        this.collections = new HashMap<>();
        this.dbOfCollectoin = new HashMap<>();
        this.dbs = new HashSet<>();
    }

    public static DBConfig getConfig() {
        return (DBConfig) ConfigManager.getConfigManager().getConfig(DBConfig.class);
    }

    @Override
    public void init() throws Exception {
        Element root = ConfigInterface.getRootElement("/db.xml");
        for (Object now : root.elements()) {
            Element element = (Element) now;
            if (element.getName().equals("host")) this.host = element.getText();
            else if (element.getName().equals("port")) this.port = Integer.valueOf(element.getText());
            else {
                String dbName = element.attributeValue("name");
                dbs.add(dbName);
                for (Object collection : element.elements()) {
                    initCollection((Element) collection, dbName);
                }
            }
        }
    }

    private void initCollection(Element element, String dbName) {
        String collectionType = element.attributeValue("type");
        String collectionName = element.getText();
        this.dbOfCollectoin.put(collectionName, dbName);
        this.collections.put(collectionType, collectionName);
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

    public String getDBofCollection(String collectionName) {
        return this.dbOfCollectoin.get(collectionName);
    }

    public Set<String> getDbs() {
        return new HashSet<>(this.dbs);
    }

    public HashMap<String, String> getCollections() {
        return new HashMap<>(this.collections);
    }
}
