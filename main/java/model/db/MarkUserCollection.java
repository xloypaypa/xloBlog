package model.db;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by xlo on 2015/9/2.
 * it's the collection of user mark another user
 */
public class MarkUserCollection extends DBCollection {

    public void markUser(String username, String aimUser) {
        lockCollection();
        Document document = new Document().append("from", username).append("to", aimUser);
        this.insert(document);
        unlockCollection();
    }

    public void removeMark(String username, String aimUser) {
        lockCollection();
        FindIterable<Document> iterable = collection.find(new Document().append("from", username).append("to", aimUser));
        MongoCursor<Document> cursor = iterable.iterator();
        if (!cursor.hasNext()) return ;

        Document document = cursor.next();
        this.remove((ObjectId) document.get("_id"));
    }

    public DBData getMark(String username, String aimUser) {
        lockCollection();
        FindIterable<Document> iterable = collection.find(new Document().append("from", username).append("to", aimUser));
        MongoCursor<Document> cursor = iterable.iterator();
        if (!cursor.hasNext()) return null;

        return addDocumentToUsing(cursor.next());
    }

    public DBData getMarkData(String username, String aimUser) {
        lockCollection();
        FindIterable<Document> iterable = collection.find(new Document().append("from", username).append("to", aimUser));
        MongoCursor<Document> cursor = iterable.iterator();
        if (!cursor.hasNext()) return null;

        DBData ans = getDocumentNotUsing(cursor.next());
        unlockCollection();
        return ans;
    }

    public List<DBData> find(Document document) {
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
