package control;

import model.db.CollectionGetter;
import model.db.DBCollection;
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
public class UserManagerTest extends TestClass {

    @After
    public void tearDown() throws Exception {
        removeUser();
    }

    public static void register(String username) throws InterruptedException {
        Counter counter = new Counter(1);
        register(counter, username);
        while (counter.get() != 0) {
            Thread.sleep(500);
        }
    }

    public static void register(Counter counter, String username) {
        UserManagerNoSend userAccessManager = new UserManagerNoSend(counter);
        userAccessManager.register(username, "pass");
    }

    public static void remove(String username) throws InterruptedException {
        Counter counter = new Counter(1);
        UserManager userManager = new UserManagerNoSend(counter);
        userManager.removeUser(username, "pass");
        while (counter.get() != 0) {
            Thread.sleep(500);
        }
    }

    public static void login() throws InterruptedException {
        Counter counter = new Counter(1);
        login(counter);
        while (counter.get() != 0) {
            Thread.sleep(500);
        }
    }

    public static void login(Counter counter) {
        UserManagerNoSend userAccessManager = new UserManagerNoSend(counter);
        userAccessManager.loginUser("test user", "pass");
    }

    protected void removeUser() {
        UserCollection userCollection = new UserCollection();
        userCollection.removeUser("test user");
        userCollection.submit();
    }

    @Test
    public void testRegister() throws InterruptedException {
        DBCollection.init();

        int n = 10;
        Counter counter = new Counter(n);
        for (int i = 0; i < n; i++) {
            final int finalI = i;
            new Thread() {
                @Override
                public void run() {
                    UserManagerNoSend userAccessManager = new UserManagerNoSend(counter);
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
            removeUser();
        }
        testRegister();
    }

    @Test
    public void testLogin() throws InterruptedException {
        Counter counter = new Counter(2);
        register(counter, "test user");

        while (counter.get() != 1) {
            Thread.sleep(500);
        }

        login(counter);

        while (counter.get() != 0) {
            Thread.sleep(500);
        }

        assertEquals(2, counter.getSuccess());
        assertEquals(0, counter.getFail());
    }

    @Test
    public void testLoginAgain() throws InterruptedException {
        int n = 10;
        for (int i = 0; i < n; i++) {
            testLogin();
            removeUser();
        }
        testLogin();
    }

    @Test
    public void testChangeUserAccess() throws InterruptedException {
        register("test user");
        Counter counter = new Counter(1);
        UserManagerNoSend userManager = new UserManagerNoSend(counter);
        userManager.changeUserAccess("test user", "pass", "test user", "test access", 2);

        while (counter.get() != 0) {
            Thread.sleep(500);
        }
        UserCollection userCollection = new UserCollection();
        assertEquals(2, userCollection.getUserData("test user").object.getInteger("test access", 0));
    }
}