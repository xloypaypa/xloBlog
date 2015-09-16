package control;

import model.tool.ioAble.FileIOBuilder;
import model.tool.ioAble.NormalFileIO;
import model.tool.ioAble.NormalStringIO;
import model.tool.streamConnector.NormalStreamConnector;
import model.tool.streamConnector.StreamConnector;
import model.tool.streamConnector.io.NormalStreamIONode;
import model.tool.streamConnector.io.StreamIONode;
import net.sf.json.JSONObject;
import org.junit.After;
import org.junit.Test;
import testTool.Counter;

import java.io.File;

import static org.junit.Assert.*;

/**
 * Created by xlo on 2015/9/16.
 * it's the testing code for image manager
 */
public class ImageManagerTest {

    public static void uploadImage(String username, byte[] file) throws InterruptedException {
        Counter counter = new Counter(1);

        ImageManagerNoSend imageManager = new ImageManagerNoSend(counter);
        imageManager.uploadImage(username, "pass", file);

        while (counter.get() != 0) {
            Thread.sleep(500);
        }
    }

    @After
    public void tearDown() throws Exception {
        UserManagerTest.remove("test user");
    }

    @Test
    public void testImage() throws Exception {
        UserManagerTest.register("test user");

        byte[] bytes = "abc".getBytes();
        Counter counter = new Counter(1);
        ImageManagerNoSend imageManager = new ImageManagerNoSend(counter);
        imageManager.uploadImage("test user", "pass", bytes);
        while (counter.get() != 0) {
            Thread.sleep(500);
        }

        JSONObject object = imageManager.getManagerNoSend().getMessage();
        assertNotNull(object);

        counter = new Counter(1);
        imageManager = new ImageManagerNoSend(counter);
        imageManager.getImage(object.getString("return"));

        while (counter.get() != 0) {
            Thread.sleep(500);
        }

        File file = imageManager.getManagerNoSend().getFile();
        assertNotNull(file);
        assertTrue(file.exists());

        StreamConnector connector = new NormalStreamConnector();

        NormalStringIO stringIO = new NormalStringIO();
        stringIO.setInitValue("");
        stringIO.buildIO();

        FileIOBuilder  fileIOBuilder = new NormalFileIO();
        fileIOBuilder.setFile(file.getPath());
        fileIOBuilder.buildIO();

        StreamIONode streamIONode = new NormalStreamIONode();
        streamIONode.setInputStream(fileIOBuilder.getInputStream());
        streamIONode.addOutputStream(stringIO.getOutputStream());
        connector.addMember(streamIONode);

        connector.connect();

        fileIOBuilder.close();
        stringIO.close();

        assertEquals("abc", stringIO.getValue());

        while (!file.delete()) {
            Thread.sleep(500);
        }
    }
}