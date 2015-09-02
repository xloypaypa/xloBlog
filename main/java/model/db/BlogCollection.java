package model.db;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by xlo on 2015/8/28.
 * it's the collection of blog
 */
public class BlogCollection extends DBClient {

    public void addDocument(String author, String title, String body, Date date, String type) {
        lockCollection();
        Document document = new Document();
        document.put("author", author);
        document.put("title", title);
        document.put("body", body);
        document.put("time", date);
        document.put("type", type);
        this.insert(document);
        unlockCollection();
    }

    public void removeDocument(String id) {
        lockCollection();
        List<Document> iterable = collection.find(new Document("_id", new ObjectId(id)));
        Iterator<Document> cursor = iterable.iterator();
        if (!cursor.hasNext()) return ;

        Document document = cursor.next();
        this.remove((ObjectId) document.get("_id"));
    }

    public DBData getDocument(String id) {
        lockCollection();
        List<Document> iterable = collection.find(new Document("_id", new ObjectId(id)));
        Iterator<Document> cursor = iterable.iterator();
        if (!cursor.hasNext()) return null;

        Document document = cursor.next();
        return addDocumentToUsing(document);
    }

    public DBData getDocumentData(String id) {
        lockCollection();
        List<Document> iterable = collection.find(new Document("_id", new ObjectId(id)));
        Iterator<Document> cursor = iterable.iterator();
        if (!cursor.hasNext()) return null;

        Document document = cursor.next();
        DBData ans = getDocumentNotUsing(document);
        unlockCollection();
        return ans;
    }

    public List<DBData> findDocumentListData(Document message) {
        lockCollection();
        List<Document> iterable = collection.find(message);
        Iterator<Document> cursor = iterable.iterator();

        List<DBData> ans = new LinkedList<>();
        while (cursor.hasNext()) {
            Document document = cursor.next();
            ans.add(getDocumentNotUsing(document));
        }
        unlockCollection();
        return ans;
    }
}
