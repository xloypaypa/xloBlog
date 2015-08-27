package model.lock;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by xlo on 15-8-23.
 * it's the name lock's impl
 */
public class NameLockImpl implements NameLock {
    protected final Map<String, Thread> owner;
    protected final Map<String, Integer> times;
    protected final Map<String, Set<Thread>> waiter;

    final protected static Thread nullThread = new Thread();

    protected static NameLockImpl nameLock;

    protected NameLockImpl() {
        this.owner = new HashMap<>();
        this.waiter = new HashMap<>();
        this.times = new HashMap<>();
    }

    public static NameLockImpl getNameLock() {
        if (nameLock == null) {
            synchronized (NameLockImpl.class) {
                if (nameLock == null) {
                    nameLock = new NameLockImpl();
                }
            }
        }
        return nameLock;
    }

    @Override
    public void lock(String name) {
        initName(name);
        Thread currentThread = Thread.currentThread();
        Thread wait;
        synchronized (this.owner.get(name)) {
            synchronized (this.waiter.get(name)) {
                if (!this.owner.get(name).equals(nullThread) && !this.owner.get(name).equals(currentThread)) {
                    wait = buildWaitThread();
                    this.waiter.get(name).add(wait);
                } else {
                    changeOwner(name, currentThread);
                    return;
                }
            }
        }
        try {
            wait.join();
        } catch (InterruptedException ignored) {
        }
        lock(name);
    }

    @Override
    public boolean tryLock(String name) {
        initName(name);
        Thread currentThread = Thread.currentThread();
        synchronized (this.owner.get(name)) {
            synchronized (this.waiter.get(name)) {
                if (!this.owner.get(name).equals(nullThread) && !this.owner.get(name).equals(currentThread)) {
                    return false;
                } else {
                    changeOwner(name, currentThread);
                    return true;
                }
            }
        }
    }

    @Override
    public boolean tryLock(String name, long time) {
        initName(name);
        Thread currentThread = Thread.currentThread();
        Thread wait;
        synchronized (this.owner.get(name)) {
            synchronized (this.waiter.get(name)) {
                if (!this.owner.get(name).equals(nullThread) && !this.owner.get(name).equals(currentThread)) {
                    wait = buildWaitThread();
                    this.waiter.get(name).add(currentThread);
                } else {
                    changeOwner(name, currentThread);
                    return true;
                }
            }
        }
        try {
            wait.join(time);
        } catch (InterruptedException ignored) {
        }
        return tryLock(name);
    }

    @Override
    public void unlock(String name) {
        initName(name);
        synchronized (this.owner.get(name)) {
            if (!this.owner.get(name).equals(Thread.currentThread())) return;
            synchronized (this.waiter.get(name)) {
                this.waiter.get(name).forEach(Thread::interrupt);
                this.waiter.get(name).clear();
                decLock(name);
            }
        }
    }

    protected void changeOwner(String name, Thread currentThread) {
        if (!this.owner.get(name).equals(currentThread)) {
            this.times.put(name, 1);
        } else {
            this.times.put(name, this.times.get(name) + 1);
        }
        this.owner.put(name, currentThread);
    }

    protected void ownerNull(String name) {
        this.owner.put(name, nullThread);
        this.times.put(name, 0);
    }

    protected void decLock(String name) {
        this.times.put(name, this.times.get(name) - 1);
        if (times.get(name) == 0 ){
            ownerNull(name);
        }
    }

    private synchronized void initName(String name) {
        if (!this.owner.containsKey(name)) {
            synchronized (this.owner) {
                synchronized (this.waiter) {
                    if (!this.owner.containsKey(name)) {
                        this.owner.put(name, nullThread);
                        this.times.put(name, 0);
                        this.waiter.put(name, new HashSet<>());
                    }
                }
            }
        }
    }

    private Thread buildWaitThread() {
        Thread wait = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(Long.MAX_VALUE);
                } catch (InterruptedException ignored) {
                }
            }
        };
        wait.start();
        return wait;
    }
}
