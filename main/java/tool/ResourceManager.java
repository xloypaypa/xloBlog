package tool;

import tool.ioAble.NormalByteIO;
import tool.streamConnector.NormalStreamConnector;
import tool.streamConnector.StreamConnector;
import tool.streamConnector.io.NormalStreamIONode;
import tool.streamConnector.io.StreamIONode;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by xlo on 2015/8/19.
 * it's the resource's manager
 * it's will cache the resource
 */
public class ResourceManager {
    protected Map<String, byte[]> resource;
    protected Map<String, CacheState> cached;
    protected ExecutorService executorService = Executors.newFixedThreadPool(5);

    private static ResourceManager resourceManager = new ResourceManager();

    private ResourceManager() {
        this.resource = new HashMap<>();
        this.cached = new HashMap<>();
    }

    public static ResourceManager getResourceManager() {
        return resourceManager;
    }

    public boolean haveResource(String path) {
        return checkResource(path);
    }

    public byte[] getResource(String path) {
        if (checkResource(path)) {
            if (this.isCached(path) == CacheState.END) {
                return this.resource.get(path);
            } else if (this.isCached(path) == CacheState.NONE) {
                this.loadResource(path);
                return this.resource.get(path);
            } else {
                //FIXME busy waiting
                while (this.isCached(path) == CacheState.START) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ignored) {
                    }
                }
                return this.resource.get(path);
            }
        } else {
            return null;
        }
    }

    private boolean checkResource(String path) {
        if (this.getClass().getResource(path) != null) {
            cacheResource(path);
            return true;
        } else {
            return false;
        }
    }

    private synchronized void cacheResource(String path) {
        if (this.cached.containsKey(path)) return ;
        if (!checkResource(path)) return;

        this.cached.put(path, CacheState.START);
        this.executorService.execute(() -> loadResource(path));
    }

    private synchronized CacheState isCached(String path) {
        if (!this.cached.containsKey(path)) {
            return CacheState.NONE;
        }
        return this.cached.get(path);
    }

    private void loadResource(String path) {
        this.cached.put(path, CacheState.START);
        StreamConnector streamConnector = new NormalStreamConnector();
        StreamIONode streamIONode = new NormalStreamIONode();
        ResourceIO resourceIO = new ResourceIO();
        resourceIO.setPath(path);
        resourceIO.buildIO();

        NormalByteIO normalByteIO = new NormalByteIO();
        normalByteIO.setInitValue(new byte[0]);
        normalByteIO.buildIO();

        streamIONode.setInputStream(resourceIO.getInputStream());
        streamIONode.addOutputStream(normalByteIO.getOutputStream());

        streamConnector.addMember(streamIONode);
        streamConnector.connect();

        this.resource.put(path, normalByteIO.getValue());
        this.cached.put(path, CacheState.END);
    }
}
