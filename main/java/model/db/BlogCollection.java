package model.db;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by xlo on 2015/8/28.
 * it's the collection of blog
 */
public class BlogCollection extends DBClient {

    public void addDocument(String author, String title, String body) {
        Document document = new Document();
        document.put("author", author);
        document.put("title", title);
        document.put("body", body);
        document.put("time", new Date());
        this.insert(document);
    }

    public DBData getDocument(String id) {
        lockCollection();
        FindIterable<Document> iterable = collection.find(new Document("_id", new ObjectId(id)));
        MongoCursor<Document> cursor = iterable.iterator();
        if (!cursor.hasNext()) return null;

        Document document = cursor.next();
        return addDocumentToUsing(document);
    }

    public DBData getDocumentData(String id) {
        lockCollection();
        FindIterable<Document> iterable = collection.find(new Document("_id", new ObjectId(id)));
        MongoCursor<Document> cursor = iterable.iterator();
        if (!cursor.hasNext()) return null;

        Document document = cursor.next();
        DBData ans = getDocumentNotUsing(document);
        unlockCollection();
        return ans;
    }

    public List<DBData> getDocumentDataByAuthor(String author) {
        lockCollection();
        FindIterable<Document> iterable = collection.find(new Document("author", author));
        MongoCursor<Document> cursor = iterable.iterator();
        if (!cursor.hasNext()) return null;

        List<DBData> ans = new LinkedList<>();
        while (cursor.hasNext()) {
            Document document = cursor.next();
            ans.add(getDocumentNotUsing(document));
        }
        unlockCollection();
        return ans;
    }

    public void lockItem(String id) {
        lockType("id", id);
    }

    public void unlockItem(String id) {
        unlockType("id", id);
    }

    private void lockType(String type, String value) {
        unlock(this.lockName + "." + type + "." + value);
    }

    private void unlockType(String type, String value) {
        unlock(this.lockName + "." + type + "." + value);
    }
}
