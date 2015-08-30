package control;

import model.db.CollectionGetter;
import model.db.DBClient;
import model.db.UserCollection;
import model.lock.TestClass;
import org.bson.Document;
import org.junit.After;
import org.junit.Test;
import testTool.Counter;

import static org.junit.Assert.assertEquals;

/**
 * Created by xlo on 2015/8/25.
 * it's the test code of user register
 */
public class UserRegisterTest extends TestClass {

    @After
    public void tearDown() throws Exception {
        UserCollection userCollection = new UserCollection();
        userCollection.removeUser("test user");
    }

    @Test
    public void testRegister() throws InterruptedException {
        DBClient.init();

        int n = 10;
        Counter counter = new Counter(n);
        for (int i = 0; i < n; i++) {
            final int finalI = i;
            new Thread() {
                @Override
                public void run() {
                    UserAccessManagerNoSend userAccessManager = new UserAccessManagerNoSend(counter);
                    userAccessManager.register("test user", "pass " + finalI);
                }
            }.start();
        }

        while (counter.get() != 0) {
            Thread.sleep(500);
        }

        CollectionGetter collectionGetter = new CollectionGetter(new UserCollection());
        assertEquals(1, collectionGetter.getCollection().count(new Document("username", "test user")));
    }

    @Test
    public void testRegisterAgain() throws InterruptedException {
        int n = 10;
        for (int i = 0; i < n; i++) {
            testRegister();
        }
    }
}