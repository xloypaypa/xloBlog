package model.db.virtual;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xlo on 2015/9/1.
 * it's the virtual db
 */
public class VirtualDB {

    private static Map<String, VirtualDB> dbMap = new HashMap<>();

    protected Map<String, VirtualCollection> collectionMap = new HashMap<>();

    private VirtualDB() {

    }

    public synchronized static VirtualDB getDatabase(String name) {
        if (!dbMap.containsKey(name)) {
            dbMap.put(name, new VirtualDB());
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
