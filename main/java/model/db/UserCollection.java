package model.db;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by xlo on 15-8-23.
 * it's the collection of user
 */
public class UserCollection extends DBCollection {

    public void registerUser(String username, String password) {
        lockCollection();
        Map<String, Object> data = new HashMap<>();
        data.put("username", username);
        data.put("password", password);
        this.insert(new Document(data));
        unlockCollection();
    }

    public DBData getUser(String username) {
        lockCollection();
        List<Document> iterable = collection.find(new Document("username", username));
        Iterator<Document> cursor = iterable.iterator();
        if (!cursor.hasNext()) return null;

        Document document = cursor.next();
        return addDocumentToUsing(document);
    }

    public DBData getUserData(String username) {
        lockCollection();
        List<Document> iterable = collection.find(new Document("username", username));
        Iterator<Document> cursor = iterable.iterator();
        if (!cursor.hasNext()) return null;

        Document document = cursor.next();
        DBData ans = getDocumentNotUsing(document);
        unlockCollection();
        return ans;
    }

    public void removeUser(String username) {
        lockCollection();
        List<Document> iterable = collection.find(new Document("username", username));
        Iterator<Document> cursor = iterable.iterator();
        if (!cursor.hasNext()) return ;

        Document document = cursor.next();
        this.remove((ObjectId) document.get("_id"));
    }

    public List<DBData> findUserData(Document document) {
        lockCollection();
        List<DBData> ans = new LinkedList<>();
        List<Document> iterable = collection.find(document);
        ans.addAll(iterable.stream().map(this::getDocumentNotUsing).collect(Collectors.toList()));
        unlockCollection();
        return ans;
    }
}
