package control;

import model.event.Event;
import testTool.Counter;

/**
 * Created by xlo on 2015/9/2.
 * it's the blog manager who not send reply
 */
public class BlogManagerNoSend extends BlogManager {
    protected Counter counter;

    public BlogManagerNoSend(Counter counter) {
        super(null);
        this.counter = counter;
    }

    @Override
    protected void addSendMessage(Event event) {
        event.actionWhileCommit(new Event() {
            @Override
            public boolean run() {
                counter.add(-1);
                return true;
            }
        });
        super.addSendMessage(event);
    }

    @Override
    protected void addSuccessMessage(Event event) {
        event.actionWhileSuccess(new Event() {
            @Override
            public boolean run() {
                counter.addSuccess(1);
                return true;
            }
        });
    }

    @Override
    protected void addFailMessage(Event event) {
        event.actionWhileFail(new Event() {
            @Override
            public boolean run() {
                counter.addFail(1);
                return true;
            }
        });
    }
}
