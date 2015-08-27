package model.lock;

import org.junit.After;
import org.junit.Test;
import testTool.Counter;

import java.util.HashSet;

import static org.junit.Assert.assertEquals;

/**
 * Created by xlo on 15-8-23.
 * it's test lock
 */
public class NameLockImplTest extends TestClass {
    @After
    public void tearDown() {
        NameLockImplTestClass.getNameLock().order.clear();
    }

    @Test
    public void testLock() throws InterruptedException {
        NameLockImplTestClass nameLock = NameLockImplTestClass.getNameLock();
        nameLock.owner.put("name", NameLockImpl.nullThread);
        nameLock.waiter.put("name", new HashSet<>());

        int n = 10;
        Counter counter = new Counter(n);
        for (int i = 0; i < n; i++) {
            new Thread() {
                @Override
                public void run() {
                    nameLock.lock("name");

                    nameLock.unlock("name");
                    counter.add(-1);
                }
            }.start();
        }

        int now = 0, all = 1000, steep = 10;
        while (counter.get() != 0) {
            Thread.sleep(steep);
            now += steep;
            if (now >= all) break;
        }
        assertEquals(0, counter.get());

        long sum = 0;
        for (int i = 0; i < nameLock.order.size(); i += 2) {
            sum += nameLock.order.get(i) + nameLock.order.get(i + 1);
            assertEquals(0, sum);
        }
    }

    @Test
    public void testLockAgain() throws InterruptedException {
        int n = 500;
        for (int i = 0; i < n; i++) {
            testLock();
        }
    }

    @Test
    public void testReentrantLock() throws InterruptedException {
        NameLockImplTestClass nameLock = NameLockImplTestClass.getNameLock();
        nameLock.owner.put("name", NameLockImpl.nullThread);
        nameLock.waiter.put("name", new HashSet<>());

        int n = 10, m = 5;
        Counter counter = new Counter(n);
        for (int i = 0; i < n; i++) {
            new Thread() {
                @Override
                public void run() {
                    for (int j = 0; j < m; j++) {
                        nameLock.lock("name");
                    }

                    for (int j = 0; j < m; j++) {
                        nameLock.unlock("name");
                    }
                    counter.add(-1);
                }
            }.start();
        }

        int now = 0, all = 1000, steep = 10;
        while (counter.get() != 0) {
            Thread.sleep(steep);
            now += steep;
            if (now >= all) break;
        }
        assertEquals(0, counter.get());

        long sum = 0;
        for (int i = 0; i < nameLock.order.size(); i += 2 * m) {
            for (int j = 0; j < 2 * m; j++) {
                sum += nameLock.order.get(i + j);
            }
            assertEquals(0, sum);
        }
    }

    @Test
    public void testReentrantLockAgain() throws InterruptedException {
        int n = 500;
        for (int i = 0; i < n; i++) {
            testReentrantLock();
        }
    }

}