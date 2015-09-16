package model.db;

import model.lock.NameLockManager;

import java.io.File;
import java.io.IOException;

/**
 * Created by xlo on 2015/9/16.
 * it's the image collection
 */
public class ImageCollection extends DBFileTable {
    @Override
    public long count() {
        lockCollection();
        long ans = super.count();
        unlockCollection();
        return ans;
    }

    @Override
    public File find(String s) throws IOException {
        lockCollection();
        File ans = super.find(s);
        NameLockManager.getNameLockManager().getLock("file " + ans.getPath()).lock();
        unlockCollection();
        return ans;
    }

    @Override
    public String insert(byte[] bytes) {
        lockCollection();
        String ans = super.insert(bytes);
        unlockCollection();
        return ans;
    }

    @Override
    public void delete(String s) {
        lockCollection();
        super.delete(s);
        unlockCollection();
    }

    @Override
    public Long getFileLength(String s) {
        lockCollection();
        Long ans = super.getFileLength(s);
        unlockCollection();
        return ans;
    }
}
