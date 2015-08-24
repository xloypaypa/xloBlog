package model.db;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import config.ConfigManager;
import config.ReturnCodeConfig;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xlo on 15-8-23.
 * it's the collection of user
 */
public class UserCollection extends DBClient {
    protected MongoCollection<Document> collection;
    protected static ReturnCodeConfig returnCodeConfig
            = (ReturnCodeConfig) ConfigManager.getConfigManager().getConfig(ReturnCodeConfig.class);

    public UserCollection() {
        this.collection = getMongoDataBase("blog").getCollection("username");
    }

    public boolean userExist(String username) {
        FindIterable iterable = collection.find(new Document("username", username));
        MongoCursor cursor = iterable.iterator();
        return cursor.hasNext();
    }

    public String checkUser(String username, String password) {
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        FindIterable iterable = collection.find(new Document(map));
        MongoCursor cursor = iterable.iterator();
        if (cursor.hasNext()) {
            return returnCodeConfig.getCode("accept");
        } else {
            return returnCodeConfig.getCode("forbidden");
        }
    }

    public String register(String username, String password) {
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        collection.insertOne(new Document(map));
        return returnCodeConfig.getCode("accept");
    }
}
