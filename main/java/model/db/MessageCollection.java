package model.db;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by xlo on 2015/8/31.
 * it's the message collection
 */
public class MessageCollection extends DBClient {

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
        FindIterable<Document> iterable = collection.find(new Document("_id", id));
        MongoCursor<Document> cursor = iterable.iterator();
        if (!cursor.hasNext()) return ;

        Document document = cursor.next();
        this.remove((ObjectId) document.get("_id"));
    }

    public DBData getMessage(String id) {
        lockCollection();
        FindIterable<Document> iterable = collection.find(new Document("_id", id));
        MongoCursor<Document> cursor = iterable.iterator();
        if (!cursor.hasNext()) return null;

        Document document = cursor.next();
        return addDocumentToUsing(document);
    }

    public DBData getMessageData(String id) {
        lockCollection();
        FindIterable<Document> iterable = collection.find(new Document("_id", id));
        MongoCursor<Document> cursor = iterable.iterator();
        if (!cursor.hasNext()) return null;

        Document document = cursor.next();
        DBData data = getDocumentNotUsing(document);
        unlockCollection();
        return data;
    }

    public List<DBData> findMessageData(Document document) {
        lockCollection();
        FindIterable<Document> iterable = collection.find(document);
        MongoCursor<Document> cursor = iterable.iterator();

        List<DBData> ans = new LinkedList<>();
        while (cursor.hasNext()) {
            ans.add(getDocumentNotUsing(cursor.next()));
        }
        unlockCollection();
        return ans;
    }
}
