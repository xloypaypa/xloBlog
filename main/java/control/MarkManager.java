package control;

import model.db.DBCollection;
import model.db.MarkUserCollection;
import model.event.Event;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.tool.WriteMessageServerSolver;
import org.bson.Document;
import server.serverSolver.RequestSolver;

import java.util.List;

/**
 * Created by xlo on 2015/9/9.
 * it's the manager of mark user
 */
public class MarkManager extends Manager {
    public MarkManager(RequestSolver requestSolver) {
        super(requestSolver);
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

    public void getMarkedList(String username, String password) {
        Event event = new Event() {
            @Override
            public boolean run() {
                if (!accessConfig.isAccept(username, password, this)) return false;

                MarkUserCollection markUserCollection = new MarkUserCollection();
                List<DBCollection.DBData> list = markUserCollection.find(new Document().append("from", username));

                JSONArray array = new JSONArray();
                for (DBCollection.DBData now : list) {
                    JSONObject object = new JSONObject();
                    object.put("user", now.object.get("to"));
                    array.add(object);
                }

                this.sendWhileSuccess(new WriteMessageServerSolver(requestSolver, array));

                return true;
            }
        };
        addFailMessage(event);
        event.submit();
    }
}
