package model.db;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import config.ConfigManager;
import config.DBConfig;
import model.db.virtual.VirtualCollection;
import model.db.virtual.VirtualDB;
import model.lock.NameLockImpl;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.*;

/**
 * Created by xlo on 15-8-23.
 * it's handle mongo client
 */
public abstract class DBClient {
    protected static MongoClient mongoClient;
    protected volatile static Map<Thread, Set<DBClient>> usingDB;
    private volatile static Map<String, VirtualDB> databaseMap;
    private volatile static boolean needInit = true;
    protected static DBConfig dbConfig
            = (DBConfig) ConfigManager.getConfigManager().getConfig(DBConfig.class);
    protected static MongoDatabase blog;

    public synchronized static void init() {
        if (databaseMap != null) return ;
//        if (mongoClient != null) return;
        DBConfig dbConfig = (DBConfig) ConfigManager.getConfigManager().getConfig(DBConfig.class);
//        mongoClient = new MongoClient(dbConfig.getHost(), dbConfig.getPort());
        databaseMap = new HashMap<>();
        for (String now : dbConfig.getDbs()) {
//            databaseMap.put(now, mongoClient.getDatabase(now));
            databaseMap.put(now, VirtualDB.getDB(now));
        }
        usingDB = new HashMap<>();
        needInit = false;
    }

    public static void submitUsing() {
        if (usingDB.containsKey(Thread.currentThread())) {
            usingDB.get(Thread.currentThread()).forEach(DBClient::submit);
            usingDB.remove(Thread.currentThread());
        }
    }

    public static void releaseUsing() {
        if (usingDB.containsKey(Thread.currentThread())) {
            usingDB.get(Thread.currentThread()).forEach(DBClient::release);
            usingDB.remove(Thread.currentThread());
        }
    }

    private static VirtualDB getDatabase(String name) {
        return databaseMap.get(name);
    }

    protected VirtualCollection collection;
    protected String lockName;

    public DBClient() {
        if (needInit) init();
        String collectionName = dbConfig.getCollectionName(this.getClass());
        String dbName = dbConfig.getDBofCollection(collectionName);

        this.lockName = dbName + "." + collectionName;
        lockCollection();
        unlockCollection();

        this.collection = getDatabase(dbName).getCollection(collectionName);
        usingDB.put(Thread.currentThread(), new HashSet<>());
        usingDB.get(Thread.currentThread()).add(this);
    }

    public class DBData {
        public Document object;
        protected Document past;
        protected ObjectId id;
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
    protected volatile Vector<String> locks = new Vector<>();

    public void submit() {
        using.stream().filter(now -> !now.object.equals(now.past)).forEach(now
                -> collection.updateOne(new Document("_id", now.id),
                new Document("$set", now.object)));
        insert.forEach(collection::insertOne);
        remove.forEach(collection::deleteOne);
        release();
    }

    public void release() {
        this.using.clear();
        this.insert.clear();
        Vector <String> names = new Vector<>(locks);
        for (int i=names.size()-1;i>=0;i--) {
            unlock(names.get(i));
        }
        names.clear();
    }

    public void lockCollection() {
        NameLockImpl.getNameLock().lock(this.lockName);
        this.locks.add(this.lockName);
    }

    public void unlockCollection() {
        this.locks.remove(this.locks.lastIndexOf(this.lockName));
        NameLockImpl.getNameLock().unlock(this.lockName);
    }

    protected void lock(String name) {
        NameLockImpl.getNameLock().lock(name);
        this.locks.add(name);
    }

    protected void unlock(String name) {
        this.locks.remove(this.locks.lastIndexOf(name));
        NameLockImpl.getNameLock().unlock(name);
    }

    protected DBData addDocumentToUsing(Document document) {
        DBData ans = new DBData();
        ans.object = document;
        ans.past = new Document(document);
        ans.id = (ObjectId) document.get("_id");
        ans.object.remove("_id");
        this.using.add(ans);
        return ans;
    }

    protected DBData getDocumentNotUsing(Document document) {
        DBData ans = new DBData();
        ans.object = document;
        ans.past = new Document(document);
        ans.id = (ObjectId) document.get("_id");
        return ans;
    }

}
