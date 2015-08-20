package tool;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by xlo on 15-8-19.
 * it's test resource manager.
 */
public class ResourceManagerTest {
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
    public void testLoad() throws InterruptedException {
        int n = 10;
        Counter counter = new Counter(n);
        ResourceManager resourceManager = ResourceManager.getResourceManager();
        for (int i = 0; i < n; i++) {
            new Thread() {
                @Override
                public void run() {
                    resourceManager.getResource("/ForbiddenPage.html");
                    counter.add(-1);
                }
            }.start();
        }
        int now=0, all=1000, steep = 10;
        while (counter.get()!=0) {
            Thread.sleep(steep);
            now+=steep;
            if (now >= all) break;
        }
        assertEquals(0, counter.get());
    }

    @Test
    public void testLoadAgain() throws InterruptedException {
        int n = 1000;
        for (int i=0;i<n;i++) {
            testLoad();
            ResourceManager resourceManager = ResourceManager.getResourceManager();
            resourceManager.cache.clear();
            resourceManager.cacheStateMap.clear();
            resourceManager.waiting.clear();
        }
    }
}