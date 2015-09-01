package model.db;

import org.bson.Document;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by xlo on 2015/8/31.
 * it's the message collection
 */
public class MessageCollection extends DBClient {

    public void addMessage(String username, String author, String message, Date date) {
        lockCollection();
        lock(username);
        Document document = new Document();
        document.put("_id", author + "." + username + "." + date.toString());
        document.put("username", username);
        document.put("author", author);
        document.put("time", date);
        document.put("message", message);
        document.put("read", false);
        this.insert(document);
        unlock(username);
        unlockCollection();
    }

    public DBData getMessage(String id) {
        lockCollection();
        List<Document> iterable = collection.find(new Document("_id", id));
        Iterator<Document> cursor = iterable.iterator();
        if (!cursor.hasNext()) return null;

        Document document = cursor.next();
        return addDocumentToUsing(document);
    }

    public DBData getMessageData(String id) {
        lockCollection();
        List<Document> iterable = collection.find(new Document("_id", id));
        Iterator<Document> cursor = iterable.iterator();
        if (!cursor.hasNext()) return null;

        Document document = cursor.next();
        DBData data = getDocumentNotUsing(document);
        unlockCollection();
        return data;
    }

    public List<DBData> getUserMessageData(String username) {
        lockCollection();
        lock(username);
        List<Document> iterable = collection.find(new Document("username", username));
        Iterator<Document> cursor = iterable.iterator();

        List<DBData> ans = new LinkedList<>();
        while (cursor.hasNext()) {
            ans.add(getDocumentNotUsing(cursor.next()));
        }
        unlock(username);
        unlockCollection();
        return ans;
    }

    public void lock(String username) {
        super.lock(this.lockName + "." + username);
    }

    public void unlock(String username) {
        super.unlock(this.lockName + "." + username);
    }
}
