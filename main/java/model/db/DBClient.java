package model.db;

import com.mongodb.MongoClient;
import config.ConfigManager;
import config.DBConfig;
import model.lock.NameLockImpl;

import java.util.Map;
import java.util.Set;
import java.util.Vector;

/**
 * Created by xlo on 2015/9/8.
 * it's the db client
 */
public abstract class DBClient {
    protected static MongoClient mongoClient;
    protected static DBConfig dbConfig
            = (DBConfig) ConfigManager.getConfigManager().getConfig(DBConfig.class);
    protected volatile static boolean needInit = true;
    protected volatile static Map<Thread, Set<DBClient>> usingDB;
    protected String lockName;
    protected volatile Vector<String> locks = new Vector<>();

    public DBClient() {
        if (needInit) {
            synchronized (DBClient.class) {
                if (needInit) {
                    DBCollection.init();
                    OtherDB.init();
                    needInit = false;
                }
            }
        }
        String collectionName = dbConfig.getCollectionName(this.getClass());
        String dbName = dbConfig.getDBofCollection(collectionName);

        this.lockName = dbName + "." + collectionName;
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

    public void submit() {
        release();
    }

    public void release() {
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
}
