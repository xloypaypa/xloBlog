package tool;

import model.tool.ioAble.IOAble;
import model.tool.ioAble.NormalByteIO;
import model.tool.ioAble.NormalFileIO;
import model.tool.streamConnector.NormalStreamConnector;
import model.tool.streamConnector.StreamConnector;
import model.tool.streamConnector.io.NormalStreamIONode;
import model.tool.streamConnector.io.StreamIONode;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by xlo on 2015/8/19.
 * it's the resource's manager
 * it's will cache the resource
 */
public class ResourceManager {
    protected final Map<String, CacheState> cacheStateMap;
    protected final Map<String, Set<Thread>> waiting;
    protected Map<String, byte[]> cache;
    protected ExecutorService executorService = Executors.newFixedThreadPool(5);

    private volatile static ResourceManager resourceManager = new ResourceManager();

    private ResourceManager() {
        this.cacheStateMap = new HashMap<>();
        this.waiting = new HashMap<>();
        this.cache = new HashMap<>();
    }

    public static ResourceManager getResourceManager() {
        return resourceManager;
    }

    public boolean haveResource(String path) {
        if (this.checkResourceExist(path)) {
            cacheResource(path);
            return true;
        } else {
            return false;
        }
    }

    public byte[] getResource(String path) {
        if (!checkResourceExist(path)) return null;
        cacheResource(path);
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(Long.MAX_VALUE);
                } catch (InterruptedException ignored) {
                }
            }
        };
        synchronized (this.cacheStateMap) {
            if (this.cacheStateMap.get(path) == CacheState.END) {
                return this.cache.get(path);
            }
            synchronized (waiting) {
                thread.start();
                buildWaitingSet(path);
                waiting.get(path).add(thread);
            }
        }
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return this.cache.get(path);
    }

    private boolean checkResourceExist(String path) {
        return this.getClass().getResource(path) != null;
    }

    private void cacheResource(String path) {
        synchronized (this.cacheStateMap) {
            if (this.cacheStateMap.containsKey(path)) {
                return;
            }
            this.cacheStateMap.put(path, CacheState.START);
        }
        executorService.execute(() -> {
            loadResource(path);

            synchronized (cacheStateMap) {
                cacheStateMap.put(path, CacheState.END);
                synchronized (waiting) {
                    buildWaitingSet(path);
                    waiting.get(path).forEach(Thread::interrupt);
                }
            }
        });
    }

    private void buildWaitingSet(String path) {
        if (this.waiting.get(path) == null) {
            this.waiting.put(path, new HashSet<>());
        }
    }

    private void loadResource(String path) {
        IOAble ioAble;
        File file = new File("." + path);
        if (file.exists() && file.isFile()) {
            ioAble = new NormalFileIO();
            ((NormalFileIO) ioAble).setFile(file.getAbsolutePath());
            ioAble.buildIO();
        } else {
            ioAble = new ResourceIO();
            ((ResourceIO) ioAble).setPath(path);
            ioAble.buildIO();
        }

        NormalByteIO byteIO = new NormalByteIO();
        byteIO.setInitValue(new byte[0]);
        byteIO.buildIO();

        StreamConnector connector = new NormalStreamConnector();
        StreamIONode ioNode = new NormalStreamIONode();
        ioNode.setInputStream(ioAble.getInputStream());
        ioNode.addOutputStream(byteIO.getOutputStream());
        connector.addMember(ioNode);
        connector.connect();

        this.cache.put(path, byteIO.getValue());

        try {
            ioAble.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
