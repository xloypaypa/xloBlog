package control;

import org.junit.Test;
import testTool.Counter;

import static org.junit.Assert.*;

/**
 * Created by xlo on 2015/9/2.
 * it's the testing code for blog manager
 */
public class BlogManagerTest {

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
    }

    @Test
    public void testAddReply() throws Exception {
    }

    @Test
    public void testGetDocument() throws Exception {

    }

    @Test
    public void testAddReader() throws Exception {

    }

    @Test
    public void testGetAuthorTypeDocumentList() throws Exception {

    }

    @Test
    public void testGetTypeDocumentList() throws Exception {

    }

    @Test
    public void testGetAuthorDocumentList() throws Exception {

    }

    @Test
    public void testSendDocumentList() throws Exception {

    }
}