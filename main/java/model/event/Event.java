package model.event;

import net.tool.WriteServerSolver;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by xlo on 15-8-23.
 * it's the event for db
 */
public abstract class Event {
    protected Set<Event> successEvent, failEvent, commitEvent;
    protected Set<WriteServerSolver> successSend, failSend, commitSend;

    public Event() {
        this.successEvent = new HashSet<>();
        this.successSend = new HashSet<>();
        this.failEvent = new HashSet<>();
        this.failSend = new HashSet<>();
        this.commitEvent = new HashSet<>();
        this.commitSend = new HashSet<>();
    }

    public void submit() {
        EventRunner.getEventRunner().submitAEvent(this);
    }

    public boolean call() {
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
        if (res) {
            this.successEvent.forEach(Event::submit);
            this.successSend.forEach(WriteServerSolver::run);
        } else {
            this.failEvent.forEach(Event::submit);
            this.failSend.forEach(WriteServerSolver::run);
        }
        this.commitEvent.forEach(Event::submit);
        this.commitSend.forEach(WriteServerSolver::run);
        return res;
    }

    public void actionWhileSuccess(Event event) {
        this.successEvent.add(event);
    }

    public void actionWhileFail(Event event) {
        this.failEvent.add(event);
    }

    public void actionWhileCommit(Event event) {
        this.commitEvent.add(event);
    }

    public void sendWhileSuccess(WriteServerSolver serverSolver) {
        this.successSend.add(serverSolver);
    }

    public void sendWhileFail(WriteServerSolver serverSolver) {
        this.failSend.add(serverSolver);
    }

    public void sendWhileCommit(WriteServerSolver serverSolver) {
        this.commitSend.add(serverSolver);
    }

    public abstract void lock();

    public abstract void checkPoint();

    public abstract boolean process();

    public abstract void rollback();

    public abstract void unlock();
}
