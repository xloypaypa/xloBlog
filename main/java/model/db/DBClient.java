package model.db;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import config.ConfigManager;
import config.DBConfig;
import org.bson.Document;

/**
 * Created by xlo on 15-8-23.
 * it's handle mongo client
 */
public class DBClient {
    protected static MongoClient mongoClient;
    protected static DBConfig dbConfig
            = (DBConfig) ConfigManager.getConfigManager().getConfig(DBConfig.class);
    protected MongoCollection<Document> collection;

    public DBClient() {
        this.collection = getMongoDataBase("blog").getCollection(dbConfig.getCollectionName(this.getClass()));
    }

    public static MongoClient getMongoClient() {
        if (mongoClient == null) {
            loadClient();
        }
        return mongoClient;
    }

    public static MongoDatabase getMongoDataBase(String dbName) {
        return mongoClient.getDatabase(dbName);
    }

    private synchronized static void loadClient() {
        if (mongoClient != null) return;
        DBConfig dbConfig = (DBConfig) ConfigManager.getConfigManager().getConfig(DBConfig.class);
        mongoClient = new MongoClient(dbConfig.getHost(), dbConfig.getPort());
    }
}
