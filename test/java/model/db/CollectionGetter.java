package model.db;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

/**
 * Created by xlo on 2015/8/25.
 * it's the collection getter
 */
public class CollectionGetter {
    protected DBCollection dbCollection;

    public CollectionGetter(DBCollection dbCollection) {
        this.dbCollection = dbCollection;
    }

    public MongoCollection<Document> getCollection() {
        return this.dbCollection.collection;
    }
}
