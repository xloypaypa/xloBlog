package control;

import model.event.Event;
import testTool.Counter;

/**
 * Created by xlo on 2015/8/25.
 * it's not send return
 */
public class UserAccessManagerNoSend extends UserAccessManager {
    protected Counter counter;

    public UserAccessManagerNoSend(Counter counter) {
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
    }
}
