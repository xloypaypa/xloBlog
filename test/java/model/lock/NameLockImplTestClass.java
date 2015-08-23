package model.lock;

import java.util.Vector;

/**
 * Created by xlo on 15-8-23.
 * it's the test class of name lock impl
 */
public class NameLockImplTestClass extends NameLockImpl {
    Vector<Long> order = new Vector<>();

    @Override
    protected void changeOwner(String name, Thread currentThread) {
        super.changeOwner(name, currentThread);
        if (currentThread.equals(nullThread)) {
            order.add(-Thread.currentThread().getId());
        } else {
            order.add(Thread.currentThread().getId());
        }
    }

    public static NameLockImplTestClass getNameLock() {
        if (nameLock == null) nameLock = new NameLockImplTestClass();
        return (NameLockImplTestClass) nameLock;
    }

    public Vector<Long> getOrder() {
        return order;
    }
}
