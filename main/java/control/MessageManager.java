package control;

import config.LengthLimitConfig;
import model.db.DBCollection;
import model.db.MessageCollection;
import model.event.Event;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.tool.WriteMessageServerSolver;
import org.bson.Document;
import server.serverSolver.RequestSolver;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by xlo on 2015/8/31.
 * it's the message manager
 */
public class MessageManager extends Manager {
    public MessageManager(RequestSolver requestSolver) {
        super(requestSolver);
    }

    public void addMessage(String username, String password, String message, String aimUser) {
        Event event = new Event() {
            @Override
            public boolean run() {
                if (username == null || password == null || message == null || aimUser == null) return false;
                if (message.length() > LengthLimitConfig.getConfig().getLimit("message")) return false;
                if (!accessConfig.isAccept(username, password, this)) return false;

                MessageCollection messageCollection = new MessageCollection();
                messageCollection.addMessage(aimUser, username, message, new Date());
                return true;
            }
        };
        addSendMessage(event);
        event.submit();
    }

    public void getMessage(String username, String password, String id) {
        Event event = new Event() {
            @Override
            public boolean run() {
                if (id == null) return false;
                if (!accessConfig.isAccept(username, password, this)) return false;

                MessageCollection messageCollection = new MessageCollection();
                DBCollection.DBData data = messageCollection.getMessageData(id);
                JSONObject object = JSONObject.fromObject(data.object.toJson());
                sendWhileSuccess(new WriteMessageServerSolver(requestSolver, object));
                return true;
            }
        };
        addFailMessage(event);
        event.submit();
    }

    public void readMessage(String username, String password, String id) {
        Event event = new Event() {
            @Override
            public boolean run() {
                if (!accessConfig.isAccept(username, password, this)) return false;

                MessageCollection messageCollection = new MessageCollection();
                DBCollection.DBData data = messageCollection.getMessage(id);
                data.object.put("read", true);
                return true;
            }
        };
        addSendMessage(event);
        event.submit();
    }

    public void getAllMessage(String username, String password) {
        Event event = new Event() {
            @Override
            public boolean run() {
                if (!accessConfig.isAccept(username, password, this)) return false;

                MessageCollection messageCollection = new MessageCollection();
                List<DBCollection.DBData> dataList = messageCollection.findMessageData(new Document("username", username));
                JSONArray array = dataList.stream().map(data -> JSONObject.fromObject(data.object.toJson())).collect(Collectors.toCollection(JSONArray::new));
                sendWhileSuccess(new WriteMessageServerSolver(requestSolver, array));
                return true;
            }
        };
        addFailMessage(event);
        event.submit();
    }

    public void getUserAllMessage(String username, String password, String aim) {
        Event event = new Event() {
            @Override
            public boolean run() {
                if (!accessConfig.isAccept(username, password, this)) return false;

                MessageCollection messageCollection = new MessageCollection();
                List<DBCollection.DBData> dataList = messageCollection.findMessageData(new Document("username", aim));
                JSONArray array = dataList.stream().map(data -> JSONObject.fromObject(data.object.toJson())).collect(Collectors.toCollection(JSONArray::new));
                sendWhileSuccess(new WriteMessageServerSolver(requestSolver, array));
                return true;
            }
        };
        addFailMessage(event);
        event.submit();
    }
}
