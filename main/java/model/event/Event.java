package model.event;

import control.Manager;
import model.db.DBClient;
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
    protected String className, methodName;

    public Event() {
        this.successEvent = new HashSet<>();
        this.successSend = new HashSet<>();
        this.failEvent = new HashSet<>();
        this.failSend = new HashSet<>();
        this.commitEvent = new HashSet<>();
        this.commitSend = new HashSet<>();
        this.className = "";
        this.methodName = "";

        for (int i = 0; i < Thread.currentThread().getStackTrace().length; i++) {
            String nowName = Thread.currentThread().getStackTrace()[i].getClassName();
            String nowMethod = Thread.currentThread().getStackTrace()[i].getMethodName();
            try {
                if (Manager.class.isAssignableFrom(Class.forName(nowName))) {
                    this.className = nowName;
                    this.methodName = nowMethod;
                    break;
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
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

    public void submit() {
        EventRunner.getEventRunner().submitAEvent(this);
    }

    public boolean call() {
        boolean ans;
        try {
            ans = run();
        } catch (Exception e) {
            e.printStackTrace();
            ans = false;
        }

        if (ans) {
            DBClient.submitUsing();
            this.successEvent.forEach(Event::submit);
            this.successSend.forEach(WriteServerSolver::run);
        } else {
            DBClient.releaseUsing();
            this.failEvent.forEach(Event::submit);
            this.failSend.forEach(WriteServerSolver::run);
        }
        this.commitEvent.forEach(Event::submit);
        this.commitSend.forEach(WriteServerSolver::run);
        return ans;
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getClojureName() {
        return this.className + "$" + this.methodName;
    }

    public abstract boolean run() throws Exception;
}