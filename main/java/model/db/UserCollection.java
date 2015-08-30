package model.db;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xlo on 15-8-23.
 * it's the collection of user
 */
public class UserCollection extends DBClient {

    public void registerUser(String username, String password) {
        Map<String, Object> data = new HashMap<>();
        data.put("username", username);
        data.put("password", password);
        data.put("access", 0);
        this.insert(new Document(data));
    }

    public DBData getUser(String username) {
        lockUser(username);
        FindIterable<Document> iterable = collection.find(new Document("username", username));
        MongoCursor<Document> cursor = iterable.iterator();
        if (!cursor.hasNext()) return null;

        Document document = cursor.next();
        return addDocumentToUsing(document);
    }

    public DBData getUserData(String username) {
        lockUser(username);
        FindIterable<Document> iterable = collection.find(new Document("username", username));
        MongoCursor<Document> cursor = iterable.iterator();
        if (!cursor.hasNext()) return null;

        Document document = cursor.next();
        DBData ans = getDocumentNotUsing(document);
        unlockUser(username);
        return ans;
    }

    public void removeUser(String username) {
        lockUser(username);
        FindIterable<Document> iterable = collection.find(new Document("username", username));
        MongoCursor<Document> cursor = iterable.iterator();

        Document document = cursor.next();
        collection.deleteOne(document);
        unlockUser(username);
    }

    public void lockUser(String username) {
        lock(this.lockName + "." + username);
    }

    public void unlockUser(String username) {
        unlock(this.lockName + "." + username);
    }
}
