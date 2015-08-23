package model.lock;

/**
 * Created by xlo on 15-8-23.
 * it's the lock lock by name
 */
public interface NameLock {

    void lock(String name);

    boolean tryLock(String name);

    boolean tryLock(String name, long time);

    void unlock(String name);
}
