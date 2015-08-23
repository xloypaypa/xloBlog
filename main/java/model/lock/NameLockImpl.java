package model.lock;

import config.ConfigManager;
import config.LockConfig;

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
    protected final Map<String, Set<Thread>> waiter;

    final protected static Thread nullThread = new Thread();

    protected static NameLockImpl nameLock;

    protected NameLockImpl() {
        this.owner = new HashMap<>();
        this.waiter = new HashMap<>();
        initFromConfig();
    }

    private void initFromConfig() {
        LockConfig lockConfig = (LockConfig) ConfigManager.getConfigManager().getConfig(LockConfig.class);
        for (String name : lockConfig.getLocks()) {
            this.owner.put(name, nullThread);
            this.waiter.put(name, new HashSet<>());
        }
    }

    public static NameLockImpl getNameLock() {
        if (nameLock == null) nameLock = new NameLockImpl();
        return nameLock;
    }

    @Override
    public void lock(String name) {
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
        synchronized (this.owner.get(name)) {
            if (!this.owner.get(name).equals(Thread.currentThread())) return;
            synchronized (this.waiter.get(name)) {
                this.waiter.get(name).forEach(Thread::interrupt);
                this.waiter.get(name).clear();
                changeOwner(name, nullThread);
            }
        }
    }

    protected void changeOwner(String name, Thread currentThread) {
        this.owner.put(name, currentThread);
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
