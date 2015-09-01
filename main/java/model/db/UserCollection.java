package model.db;

import org.bson.Document;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by xlo on 15-8-23.
 * it's the collection of user
 */
public class UserCollection extends DBClient {

    public void registerUser(String username, String password) {
        lockUser(username);
        Map<String, Object> data = new HashMap<>();
        data.put("username", username);
        data.put("password", password);
        data.put("access", 0);
        this.insert(new Document(data));
        unlockUser(username);
    }

    public DBData getUser(String username) {
        lockUser(username);
        List<Document> iterable = collection.find(new Document("username", username));
        Iterator<Document> cursor = iterable.iterator();
        if (!cursor.hasNext()) return null;

        Document document = cursor.next();
        return addDocumentToUsing(document);
    }

    public DBData getUserData(String username) {
        lockUser(username);
        List<Document> iterable = collection.find(new Document("username", username));
        Iterator<Document> cursor = iterable.iterator();
        if (!cursor.hasNext()) return null;

        Document document = cursor.next();
        DBData ans = getDocumentNotUsing(document);
        unlockUser(username);
        return ans;
    }

    public void removeUser(String username) {
        lockUser(username);
        List<Document> iterable = collection.find(new Document("username", username));
        Iterator<Document> cursor = iterable.iterator();

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
