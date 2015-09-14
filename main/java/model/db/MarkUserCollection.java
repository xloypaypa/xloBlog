package model.db;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by xlo on 2015/9/2.
 * it's the collection of user mark another user
 */
public class MarkUserCollection extends BlogDBCollection {

    public void markUser(String username, String aimUser) {
        lockCollection();
        Document document = new Document().append("from", username).append("to", aimUser);
        this.insert(document);
        unlockCollection();
    }

    public void removeMark(String username, String aimUser) {
        lockCollection();
        List<Document> iterable = collection.find(new Document().append("from", username).append("to", aimUser));
        Iterator<Document> cursor = iterable.iterator();
        if (!cursor.hasNext()) return ;

        Document document = cursor.next();
        this.remove((ObjectId) document.get("_id"));
    }

    public DBData getMark(String username, String aimUser) {
        lockCollection();
        List<Document> iterable = collection.find(new Document().append("from", username).append("to", aimUser));
        Iterator<Document> cursor = iterable.iterator();
        if (!cursor.hasNext()) return null;

        return addDocumentToUsing(cursor.next());
    }

    public DBData getMarkData(String username, String aimUser) {
        lockCollection();
        List<Document> iterable = collection.find(new Document().append("from", username).append("to", aimUser));
        Iterator<Document> cursor = iterable.iterator();
        if (!cursor.hasNext()) return null;

        DBData ans = getDocumentNotUsing(cursor.next());
        unlockCollection();
        return ans;
    }

    public List<DBData> find(Document document) {
        lockCollection();
        List<DBData> ans = new LinkedList<>();
        List<Document> iterable = collection.find(document);

        ans.addAll(iterable.stream().map(this::getDocumentNotUsing).collect(Collectors.toList()));
        unlockCollection();
        return ans;
    }
}
