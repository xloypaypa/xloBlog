package model.db;

import model.db.virtual.VirtualCollection;

/**
 * Created by xlo on 2015/8/25.
 * it's the collection getter
 */
public class CollectionGetter {
    protected DBClient dbClient;

    public CollectionGetter(DBClient dbClient) {
        this.dbClient = dbClient;
    }

    public VirtualCollection getCollection() {
        return this.dbClient.collection;
    }
}
