package model.db.virtual;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xlo on 2015/9/1.
 * it's the virtual db
 */
public class BlogVirtualDB {

    private static Map<String, BlogVirtualDB> dbMap = new HashMap<>();

    protected Map<String, VirtualCollection> collectionMap = new HashMap<>();

    private BlogVirtualDB() {

    }

    public synchronized static BlogVirtualDB getDatabase(String name) {
        if (!dbMap.containsKey(name)) {
            dbMap.put(name, new BlogVirtualDB());
        }
        return dbMap.get(name);
    }

    public synchronized VirtualCollection getCollection(String collectionName) {
        if (!this.collectionMap.containsKey(collectionName)) {
            this.collectionMap.put(collectionName, new VirtualCollection());
        }
        return this.collectionMap.get(collectionName);
    }
}
