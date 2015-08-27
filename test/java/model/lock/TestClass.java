package model.lock;

import org.junit.AfterClass;

/**
 * Created by xlo on 15-8-27.
 * it's the test class
 */
public class TestClass {
    @AfterClass
    public static void clear() {
        NameLockImpl.nameLock = null;
    }
}
