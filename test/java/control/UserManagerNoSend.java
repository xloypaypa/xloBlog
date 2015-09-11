package control;

import model.db.MarkUserCollection;
import model.event.Event;
import net.sf.json.JSONObject;
import net.tool.WriteMessageServerSolver;
import testTool.Counter;

/**
 * Created by xlo on 2015/8/25.
 * it's not send return
 */
public class UserManagerNoSend extends UserManager {
    protected Counter counter;

    public UserManagerNoSend(Counter counter) {
        super(null);
        this.counter = counter;
    }

    @Override
    public void addSendMessage(Event event) {
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
    public void addSuccessMessage(Event event) {
        event.actionWhileSuccess(new Event() {
            @Override
            public boolean run() {
                counter.addSuccess(1);
                return true;
            }
        });
    }

    @Override
    public void addFailMessage(Event event) {
        event.actionWhileFail(new Event() {
            @Override
            public boolean run() {
                counter.addFail(1);
                return true;
            }
        });
    }

    public void markUser(String username, String password, String aimUser) {
        Event event = new Event() {
            @Override
            public boolean run() {
                if (aimUser == null) return false;
                if (!accessConfig.isAccept(username, password, this)) return false;

                MarkUserCollection markUserCollection = new MarkUserCollection();
                if (markUserCollection.getMarkData(username, aimUser) != null) return false;
                markUserCollection.markUser(username, aimUser);
                return true;
            }
        };
        addSendMessage(event);
        event.submit();
    }

    public void unMarkUser(String username, String password, String aimUser) {
        Event event = new Event() {
            @Override
            public boolean run() {
                if (aimUser == null) return false;
                if (!accessConfig.isAccept(username, password, this)) return false;

                MarkUserCollection markUserCollection = new MarkUserCollection();
                if (markUserCollection.getMarkData(username, aimUser) == null) return false;
                markUserCollection.removeMark(username, aimUser);
                return true;
            }
        };
        addSendMessage(event);
        event.submit();
    }

    public void isMarked(String username, String password, String aimUser) {
        Event event = new Event() {
            @Override
            public boolean run() {
                if (aimUser == null) return false;
                if (!accessConfig.isAccept(username, password, this)) return false;

                MarkUserCollection markUserCollection = new MarkUserCollection();
                boolean ans = markUserCollection.getMarkData(username, aimUser) != null;

                JSONObject object = new JSONObject();
                object.put("return", ans);
                this.sendWhileSuccess(new WriteMessageServerSolver(requestSolver, object));

                return true;
            }
        };
        addFailMessage(event);
        event.submit();
    }
}
