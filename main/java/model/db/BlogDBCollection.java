package model.db;

import model.db.virtual.VirtualCollection;
import model.db.virtual.BlogVirtualDB;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.*;

/**
 * Created by xlo on 15-8-23.
 * it's handle mongo client
 */
public abstract class BlogDBCollection extends BlogDBClient {

    private volatile static Map<String, BlogVirtualDB> databaseMap;

    public synchronized static void init() {
        databaseMap = new HashMap<>();
        dbConfig.getDbs().stream().filter(now -> dbConfig.getDBType(now).equals("default"))
                .forEach(now -> databaseMap.put(now, BlogVirtualDB.getDatabase(now)));
        usingDB = new HashMap<>();
    }

    private static BlogVirtualDB getDatabase(String name) {
        return databaseMap.get(name);
    }

    protected VirtualCollection collection;

    public BlogDBCollection() {
        super();
        String collectionName = dbConfig.getCollectionName(this.getClass());
        String dbName = dbConfig.getDBofCollection(collectionName);

        lockCollection();
        unlockCollection();

        this.collection = getDatabase(dbName).getCollection(collectionName);
        if (!usingDB.containsKey(Thread.currentThread())) {
            synchronized (BlogDBCollection.class) {
                if (!usingDB.containsKey(Thread.currentThread())) {
                    usingDB.put(Thread.currentThread(), new HashSet<>());
                }
            }
        }
        usingDB.get(Thread.currentThread()).add(this);
    }

    protected void insert(Document document) {
        this.insert.add(document);
    }
    protected void remove(ObjectId id) {
        this.remove.add(new Document("_id", id));
    }

    protected Set<DBData> using = new HashSet<>();
    protected Set<Document> insert = new HashSet<>();
    protected Set<Document> remove = new HashSet<>();

    @Override
    public void submit() {
        using.stream().filter(now -> !now.object.equals(now.past)).forEach(now
                -> collection.updateOne(new Document("_id", now.id),
                new Document("$set", now.object)));
        insert.forEach(collection::insertOne);
        remove.forEach(collection::deleteOne);
        release();
    }

    @Override
    public void release() {
        this.using.clear();
        this.insert.clear();
        Vector <String> names = new Vector<>(locks);
        for (int i=names.size()-1;i>=0;i--) {
            unlock(names.get(i));
        }
        names.clear();
    }

    protected DBData addDocumentToUsing(Document document) {
        DBData ans = new DBData();
        ans.object = new Document(document);
        ans.past = new Document(document);
        ans.id = (ObjectId) document.get("_id");
        ans.object.remove("_id");
        this.using.add(ans);
        return ans;
    }

    protected DBData getDocumentNotUsing(Document document) {
        DBData ans = new DBData();
        ans.object = new Document(document);
        ans.past = new Document(document);
        ans.id = (ObjectId) document.get("_id");
        return ans;
    }

    public class DBData {
        public Document object;
        protected Document past;
        protected ObjectId id;
    }

}
