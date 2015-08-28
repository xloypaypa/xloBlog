package model.lock;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xlo on 15-8-23.
 * it's the name lock's impl
 */
public class NameLockImpl implements NameLock {
    protected final Map<String, Thread> owner;
    protected final Map<String, Integer> times;
    protected final Map<String, Thread> waiter;
    protected final Map<String, String> names;

    final protected static Thread nullThread = new Thread();

    protected static NameLockImpl nameLock;

    protected NameLockImpl() {
        this.owner = new HashMap<>();
        this.waiter = new HashMap<>();
        this.times = new HashMap<>();
        this.names = new HashMap<>();
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
        synchronized (this.names.get(name)) {
            if (!this.owner.get(name).equals(nullThread) && !this.owner.get(name).equals(currentThread)) {
                wait = this.waiter.get(name);
            } else {
                changeOwner(name, currentThread);
                return;
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
        synchronized (this.names.get(name)) {
            if (!this.owner.get(name).equals(nullThread) && !this.owner.get(name).equals(currentThread)) {
                return false;
            } else {
                changeOwner(name, currentThread);
                return true;
            }
        }
    }

    @Override
    public boolean tryLock(String name, long time) {
        initName(name);
        Thread currentThread = Thread.currentThread();
        Thread wait;
        synchronized (this.names.get(name)) {
            if (!this.owner.get(name).equals(nullThread) && !this.owner.get(name).equals(currentThread)) {
                wait = this.waiter.get(name);
            } else {
                changeOwner(name, currentThread);
                return true;
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
        synchronized (this.names.get(name)) {
            if (!this.owner.get(name).equals(Thread.currentThread())) return;
            decLock(name);
        }
    }

    protected void changeOwner(String name, Thread currentThread) {
        if (!this.owner.get(name).equals(currentThread)) {
            this.times.put(name, 1);
            this.waiter.put(name, buildWaitThread());
        } else {
            this.times.put(name, this.times.get(name) + 1);
        }
        this.owner.put(name, currentThread);
    }

    protected void ownerNull(String name) {
        this.owner.put(name, nullThread);
        this.times.put(name, 0);
        this.waiter.get(name).interrupt();
    }

    protected void decLock(String name) {
        this.times.put(name, this.times.get(name) - 1);
        if (times.get(name) == 0) {
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
                        this.names.put(name, name);
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
