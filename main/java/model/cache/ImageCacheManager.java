package model.cache;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xlo on 15-9-16.
 * it's the image cache manager
 */
public class ImageCacheManager extends CacheManager {
    protected Map<String, File> fileMap;

    private static ImageCacheManager imageCacheManager;

    private ImageCacheManager() {
        this.fileMap = new HashMap<>();
    }

    public static ImageCacheManager getImageCacheManager() {
        if (imageCacheManager == null) {
            synchronized (ImageCacheManager.class) {
                if (imageCacheManager == null) {
                    imageCacheManager = new ImageCacheManager();
                }
            }
        }
        return imageCacheManager;
    }

    @Override
    protected CacheObject newCacheObject(Object object) {
        return new WaitDeleteCacheObject(object, () -> {
            File file = new File(object.toString());
            while (file.exists() && !file.delete()) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {
                }
            }
        });
    }
}
