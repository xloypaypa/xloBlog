package control;

import model.db.MessageCollection;
import net.sf.json.JSONObject;
import org.bson.Document;
import org.junit.After;
import org.junit.Test;
import testTool.Counter;

import static org.junit.Assert.*;

/**
 * Created by xlo on 2015/9/11.
 * it's the testing code for message manager
 */
public class MessageManagerTest {

    public static void addMessage(String username, String aimUser, String message) throws InterruptedException {
        Counter counter = new Counter(1);
        MessageManagerNoSend messageManager = new MessageManagerNoSend(counter);
        messageManager.addMessage(username, "pass", message, aimUser);
        while (counter.get() != 0) {
            Thread.sleep(500);
        }
    }

    public static void readMessage(String username, String id) throws InterruptedException {
        Counter counter = new Counter(1);
        MessageManagerNoSend messageManager = new MessageManagerNoSend(counter);
        messageManager.readMessage(username, "pass", id);
        while (counter.get() != 0) {
            Thread.sleep(500);
        }
    }

    public static void removeMessage(String username, String id) throws InterruptedException {
        Counter counter = new Counter(1);
        MessageManagerNoSend messageManager = new MessageManagerNoSend(counter);
        messageManager.removeMessage(username, "pass", id);
        while (counter.get() != 0) {
            Thread.sleep(500);
        }
    }

    @After
    public void tearDown() throws InterruptedException {
        UserManagerTest.remove("test user");
        UserManagerTest.remove("test aim");
        MessageCollection messageCollection = new MessageCollection();
        assertEquals(0, messageCollection.findMessageData(new Document("username", "test aim")).size());
    }

    @Test
    public void testAddMessage() throws Exception {
        UserManagerTest.register("test user");
        UserManagerTest.register("test aim");
        addMessage("test user", "test aim", "message");

        MessageCollection messageCollection = new MessageCollection();
        assertEquals(1, messageCollection.findMessageData(new Document("username", "test aim")).size());
    }

    @Test
    public void testGetMessage() throws Exception {
        UserManagerTest.register("test user");
        UserManagerTest.register("test aim");
        addMessage("test user", "test aim", "message1");

        Counter counter = new Counter(1);
        MessageManagerNoSend messageManager = new MessageManagerNoSend(counter);
        MessageCollection messageCollection = new MessageCollection();
        messageManager.getMessage("test aim", "pass", messageCollection.findMessageData(new Document("username", "test aim")).get(0).object.get("_id").toString());
        while (counter.get() != 0) {
            Thread.sleep(500);
        }

        assertEquals("message1", messageManager.getManagerNoSend().getMessage().getString("message"));
    }

    @Test
    public void testReadMessage() throws Exception {
        UserManagerTest.register("test user");
        UserManagerTest.register("test aim");
        addMessage("test user", "test aim", "message");
        MessageCollection messageCollection = new MessageCollection();
        readMessage("test aim", messageCollection.findMessageData(new Document("username", "test aim")).get(0).object.get("_id").toString());

        assertTrue(messageCollection.findMessageData(new Document("username", "test aim")).get(0).object.getBoolean("read"));
    }

    @Test
    public void testGetAllMessage() throws Exception {
        UserManagerTest.register("test user");
        UserManagerTest.register("test aim");
        addMessage("test user", "test aim", "message12");
        addMessage("test user", "test aim", "message12");

        Counter counter = new Counter(1);
        MessageManagerNoSend messageManager = new MessageManagerNoSend(counter);
        messageManager.getAllMessage("test aim", "pass");
        while (counter.get() != 0) {
            Thread.sleep(500);
        }

        assertEquals(2, messageManager.getManagerNoSend().getArray().size());
        JSONObject object = (JSONObject) messageManager.getManagerNoSend().getArray().get(0);
        assertEquals("message12", object.getString("preview"));
    }

    @Test
    public void testGetUserAllMessage() throws Exception {
        UserManagerTest.register("test user");
        UserManagerTest.register("test aim");
        addMessage("test user", "test aim", "message12");
        addMessage("test user", "test aim", "message12");

        Counter counter = new Counter(1);
        MessageManagerNoSend messageManager = new MessageManagerNoSend(counter);
        messageManager.getUserAllMessage("test user", "pass", "test aim");
        while (counter.get() != 0) {
            Thread.sleep(500);
        }

        assertEquals(2, messageManager.getManagerNoSend().getArray().size());
        JSONObject object = (JSONObject) messageManager.getManagerNoSend().getArray().get(0);
        assertEquals("message12", object.getString("preview"));
    }

    @Test
    public void testRemoveMessage() throws Exception {
        testAddMessage();

        MessageCollection messageCollection = new MessageCollection();
        removeMessage("test aim", messageCollection.findMessageData(new Document("username", "test aim")).get(0).object.get("_id").toString());
        assertEquals(0, messageCollection.findMessageData(new Document("username", "test aim")).size());
    }
}