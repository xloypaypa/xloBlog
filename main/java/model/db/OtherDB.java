package model.db;

import com.mongodb.*;
import config.ConfigManager;
import config.DBConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xlo on 2015/9/8.
 * it's the db not in config
 */
public abstract class OtherDB extends DBClient {

    protected DB db;
    protected com.mongodb.DBCollection dbCollection;

    private volatile static Map<String, DB> databaseMap;

    @SuppressWarnings("deprecation")
    public synchronized static void init() {
        if (mongoClient != null) return;
        DBConfig dbConfig = (DBConfig) ConfigManager.getConfigManager().getConfig(DBConfig.class);
        mongoClient = new MongoClient(dbConfig.getHost(), dbConfig.getPort());
        databaseMap = new HashMap<>();
        dbConfig.getDbs().stream().filter(now -> dbConfig.getDBType(now).equals("other"))
                .forEach(now -> databaseMap.put(now, mongoClient.getDB(now)));
    }

    public OtherDB() {
        String collectionName = dbConfig.getCollectionName(this.getClass());
        String dbName = dbConfig.getDBofCollection(collectionName);
        this.db = databaseMap.get(dbName);
        this.dbCollection = db.getCollection(collectionName);
    }
}
