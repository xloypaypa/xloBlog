package control;

import model.db.BlogCollection;
import model.db.DBCollection;
import org.bson.BsonArray;
import org.bson.Document;
import org.junit.After;
import org.junit.Test;
import testTool.Counter;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by xlo on 2015/9/2.
 * it's the testing code for blog manager
 */
public class BlogManagerTest {

    @After
    public void tearDown() throws InterruptedException {
        Counter counter = new Counter(1);
        UserManager userManager = new UserManagerNoSend(counter);
        userManager.removeUser("test user", "pass");
        while (counter.get() != 0) {
            Thread.sleep(500);
        }
    }

    @Test
    public void testAddDocument() throws Exception {
        Counter counter = new Counter(1);
        BlogManagerNoSend blogManagerNoSend = new BlogManagerNoSend(counter);
        UserManagerTest.register();
        blogManagerNoSend.addDocument("test user", "pass", "title", "body", "default");

        while (counter.get() != 0) {
            Thread.sleep(500);
        }

        assertEquals(1, counter.getSuccess());

        BlogCollection collection = new BlogCollection();
        List<DBCollection.DBData> data = collection.findDocumentListData(new Document().append("author", "test user"));
        assertEquals(1, data.size());
    }

    @Test
    public void testAddReply() throws Exception {
        testAddDocument();
        int n = 10;
        for (int i = 0; i < n; i++) {
            Counter counter = new Counter(1);
            BlogManagerNoSend blogManagerNoSend = new BlogManagerNoSend(counter);
            BlogCollection collection = new BlogCollection();
            DBCollection.DBData data = collection.findDocumentListData(new Document().append("author", "test user")).get(0);
            if (data.object.get("_id") == null) {
                fail();
            }

            blogManagerNoSend.addReply("test user", "pass", data.object.get("_id").toString(), "reply");

            while (counter.get() != 0) {
                Thread.sleep(500);
            }
        }

        BlogCollection collection = new BlogCollection();
        List<DBCollection.DBData> listData = collection.findDocumentListData(new Document().append("author", "test user"));
        assertEquals(1, listData.size());
        DBCollection.DBData data = listData.get(0);
        assertEquals(10, ((BsonArray) data.object.get("reply")).size());
        collection.submit();
    }

    @Test
    public void testAddReader() throws Exception {
        testAddDocument();
        int n = 10;
        for (int i = 0; i < n; i++) {
            Counter counter = new Counter(1);
            BlogManagerNoSend blogManagerNoSend = new BlogManagerNoSend(counter);
            BlogCollection collection = new BlogCollection();
            DBCollection.DBData data = collection.findDocumentListData(new Document().append("author", "test user")).get(0);
            blogManagerNoSend.addReader(data.object.get("_id").toString());

            while (counter.get() != 0) {
                Thread.sleep(500);
            }
        }

        BlogCollection collection = new BlogCollection();
        DBCollection.DBData data = collection.findDocumentListData(new Document().append("author", "test user")).get(0);
        assertEquals(10, data.object.getInteger("reader", 0));
    }
}