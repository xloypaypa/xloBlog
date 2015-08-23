package model.lock;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xlo on 15-8-23.
 * it's the helper of lock
 */
public class LockHelper {
    protected List<String> locks = new ArrayList<>();

    public void addLock(String name) {
        this.locks.add(name);
    }

    public void sort() {
        this.locks.sort(String::compareTo);
    }

    public void lock() {
        NameLockImpl nameLock = NameLockImpl.getNameLock();
        locks.forEach(nameLock::lock);
    }

    public boolean tryLock() {
        NameLockImpl nameLock = NameLockImpl.getNameLock();
        for (int i = 0; i < this.locks.size(); i++) {
            if (!nameLock.tryLock(this.locks.get(i))) {
                for (int j = i - 1; i >= 0; j--) {
                    nameLock.unlock(this.locks.get(j));
                }
                return false;
            }
        }
        return true;
    }

    public boolean tryLock(long time) {
        NameLockImpl nameLock = NameLockImpl.getNameLock();
        for (int i = 0; i < this.locks.size(); i++) {
            if (!nameLock.tryLock(this.locks.get(i), time)) {
                for (int j = i - 1; i >= 0; j--) {
                    nameLock.unlock(this.locks.get(j));
                }
                return false;
            }
        }
        return true;
    }

    public void unlock() {
        NameLockImpl nameLock = NameLockImpl.getNameLock();
        locks.forEach(nameLock::unlock);
    }
}
