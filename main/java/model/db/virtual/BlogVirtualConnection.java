package model.db.virtual;

import model.db.VirtualDBConnection;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xlo on 2015/9/14.
 * it's the virtual connection
 */
public class BlogVirtualConnection implements VirtualDBConnection {
    private static Map<String, BlogVirtualDB> dbMap = new HashMap<>();

    public synchronized BlogVirtualDB getDatabase(String name) {
        if (!dbMap.containsKey(name)) {
            dbMap.put(name, new BlogVirtualDB());
        }
        return dbMap.get(name);
    }
}
