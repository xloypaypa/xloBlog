package model;

/**
 * Created by xlo on 15-8-23.
 * it's the event for db
 */
public interface Event {
    void lock();

    void checkPoint();

    boolean process();

    void rollback();

    void unlock();

    default void submit() {
        EventRunner.getEventRunner().submitAEvent(this);
    }

    default boolean call() {
        boolean res = false;
        lock();
        checkPoint();
        try {
            if (process()) {
                res = true;
            }
        } catch (Exception ignored) {

        } finally {
            if (!res) rollback();
            unlock();
        }
        return res;
    }
}
