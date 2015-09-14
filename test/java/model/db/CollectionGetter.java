package model.db;

import model.db.virtual.VirtualCollection;

/**
 * Created by xlo on 2015/8/25.
 * it's the collection getter
 */
public class CollectionGetter {
    protected BlogDBCollection dbCollection;

    public CollectionGetter(BlogDBCollection dbCollection) {
        this.dbCollection = dbCollection;
    }

    public VirtualCollection getCollection() {
        return this.dbCollection.collection;
    }
}
