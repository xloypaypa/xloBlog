package model.db;

import com.mongodb.BasicDBList;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by xlo on 15-8-23.
 * it's the collection of user
 */
public class UserCollection extends DBClient {

    public void registerUser(String username, String password) {
        lockCollection();
        Map<String, Object> data = new HashMap<>();
        data.put("username", username);
        data.put("password", password);
        data.put("access", 0);
        this.insert(new Document(data));
        unlockCollection();
    }

    public DBData getUser(String username) {
        lockCollection();
        FindIterable<Document> iterable = collection.find(new Document("username", username));
        MongoCursor<Document> cursor = iterable.iterator();
        if (!cursor.hasNext()) return null;

        Document document = cursor.next();
        return addDocumentToUsing(document);
    }

    public DBData getUserData(String username) {
        lockCollection();
        FindIterable<Document> iterable = collection.find(new Document("username", username));
        MongoCursor<Document> cursor = iterable.iterator();
        if (!cursor.hasNext()) return null;

        Document document = cursor.next();
        DBData ans = getDocumentNotUsing(document);
        unlockCollection();
        return ans;
    }

    public void removeUser(String username) {
        lockCollection();
        FindIterable<Document> iterable = collection.find(new Document("username", username));
        MongoCursor<Document> cursor = iterable.iterator();
        if (!cursor.hasNext()) return ;

        Document document = cursor.next();
        this.remove((ObjectId) document.get("_id"));
    }

    public List<DBData> findWhoMarkedUser(String username) {
        lockCollection();
        List<DBData> ans = new LinkedList<>();
        FindIterable<Document> iterable = collection.find();
        for (Document anIterable : iterable) {
            BasicDBList dbList = (BasicDBList) anIterable.get("mark");
            if (dbList.contains(username)) {
                ans.add(getDocumentNotUsing(anIterable));
            }
        }
        unlockCollection();
        return ans;
    }

    public List<DBData> findUserData(Document document) {
        lockCollection();
        List<DBData> ans = new LinkedList<>();
        FindIterable<Document> iterable = collection.find(document);
        for (Document anIterable : iterable) {
            ans.add(getDocumentNotUsing(anIterable));
        }
        unlockCollection();
        return ans;
    }
}
