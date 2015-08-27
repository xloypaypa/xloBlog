package model.db;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

/**
 * Created by xlo on 2015/8/25.
 * it's the collection getter
 */
public class CollectionGetter {
    protected DBClient dbClient;

    public CollectionGetter(DBClient dbClient) {
        this.dbClient = dbClient;
    }

    public MongoCollection<Document> getCollection() {
        return this.dbClient.collection;
    }
}
