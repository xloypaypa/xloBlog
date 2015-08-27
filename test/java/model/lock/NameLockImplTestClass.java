package model.lock;

import java.util.Vector;

/**
 * Created by xlo on 15-8-23.
 * it's the test class of name lock impl
 */
public class NameLockImplTestClass extends NameLockImpl {
    protected Vector<Long> order = new Vector<>();

    @Override
    protected void changeOwner(String name, Thread currentThread) {
        super.changeOwner(name, currentThread);
        order.add(Thread.currentThread().getId());
    }

    @Override
    protected void decLock(String name) {
        super.decLock(name);
        order.add(-Thread.currentThread().getId());
    }

    public static NameLockImplTestClass getNameLock() {
        if (nameLock == null) nameLock = new NameLockImplTestClass();
        return (NameLockImplTestClass) nameLock;
    }

    public Vector<Long> getOrder() {
        return order;
    }
}
