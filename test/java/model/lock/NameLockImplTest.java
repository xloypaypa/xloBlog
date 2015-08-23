package model.lock;

import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.assertEquals;

/**
 * Created by xlo on 15-8-23.
 * it's test lock
 */
public class NameLockImplTest {
    static class Counter {
        int num;

        public Counter(int num) {
            this.num = num;
        }

        public synchronized void add(int value) {
            this.num += value;
        }

        public synchronized int get() {
            return this.num;
        }
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
        for (int i=0;i<n;i++) {
            testLock();
        }
    }

}