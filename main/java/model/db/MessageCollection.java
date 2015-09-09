package model.db;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by xlo on 2015/8/31.
 * it's the message collection
 */
public class MessageCollection extends DBCollection {

    public void addMessage(String username, String author, String message, Date date) {
        lockCollection();
        Document document = new Document();
        document.put("username", username);
        document.put("author", author);
        document.put("time", date);
        document.put("message", message);
        document.put("read", false);
        this.insert(document);
        unlockCollection();
    }

    public void removeMessage(String id) {
        lockCollection();
        List<Document> iterable = collection.find(new Document("_id", id));
        Iterator<Document> cursor = iterable.iterator();
        if (!cursor.hasNext()) return ;

        Document document = cursor.next();
        this.remove((ObjectId) document.get("_id"));
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

    public List<DBData> findMessageData(Document document) {
        lockCollection();
        List<Document> iterable = collection.find(document);
        Iterator<Document> cursor = iterable.iterator();

        List<DBData> ans = new LinkedList<>();
        while (cursor.hasNext()) {
            ans.add(getDocumentNotUsing(cursor.next()));
        }
        unlockCollection();
        return ans;
    }
}
