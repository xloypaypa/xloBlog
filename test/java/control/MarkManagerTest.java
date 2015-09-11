package control;

import model.db.DBCollection;
import model.db.MarkUserCollection;
import org.bson.Document;
import org.junit.After;
import org.junit.Test;
import testTool.Counter;

import static org.junit.Assert.*;

/**
 * Created by xlo on 2015/9/11.
 * it's the mark user manager testing code
 */
public class MarkManagerTest {

    public static void markUser(String username, String aimUser) throws InterruptedException {
        Counter counter = new Counter(1);
        MarkManagerNoSend markManager = new MarkManagerNoSend(counter);
        markManager.markUser(username, "pass", aimUser);

        while (counter.get() != 0) {
            Thread.sleep(500);
        }
    }

    public static void unMarkUser(String username, String aimUser) throws InterruptedException {
        Counter counter = new Counter(1);
        MarkManagerNoSend markManager = new MarkManagerNoSend(counter);
        markManager.unMarkUser(username, "pass", aimUser);

        while (counter.get() != 0) {
            Thread.sleep(500);
        }
    }

    @After
    public void tearDown() throws InterruptedException {
        UserManagerTest.remove("test user");
        UserManagerTest.remove("test aim");
        MarkUserCollection markUserCollection = new MarkUserCollection();
        assertEquals(0, markUserCollection.find(new Document("from", "test user")).size());
    }

    @Test
    public void testMarkUser() throws Exception {
        UserManagerTest.register("test user");
        UserManagerTest.register("test aim");

        markUser("test user", "test aim");

        MarkUserCollection markUserCollection = new MarkUserCollection();
        DBCollection.DBData data = markUserCollection.find(new Document("from", "test user")).get(0);
        assertEquals("test user", data.object.getString("from"));
        assertEquals("test aim", data.object.getString("to"));
    }

    @Test
    public void testUnMarkUser() throws Exception {
        testMarkUser();
        unMarkUser("test user", "test aim");
        MarkUserCollection markUserCollection = new MarkUserCollection();
        assertEquals(0, markUserCollection.find(new Document("from", "test user")).size());
    }

    @Test
    public void testIsMarked() throws Exception {
        testMarkUser();
        Counter counter = new Counter(1);
        MarkManagerNoSend markManager = new MarkManagerNoSend(counter);
        markManager.isMarked("test user", "pass", "test aim");
        while (counter.get() != 0) {
            Thread.sleep(500);
        }

        assertTrue(markManager.getManagerNoSend().getMessage().getBoolean("return"));
    }

    @Test
    public void testGetMarkedList() throws Exception {
        testMarkUser();
        Counter counter = new Counter(1);
        MarkManagerNoSend markManager = new MarkManagerNoSend(counter);
        markManager.getMarkedList("test user", "pass");
        while (counter.get() != 0) {
            Thread.sleep(500);
        }
        assertEquals(1, markManager.getManagerNoSend().getArray().size());
    }
}