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
            public boolean run() throws Exception {
                return accessConfig.isAccept(username, password, this)
                        && (boolean) ManagerLogic.invoke(this.getClojureName(), username, password, message, aimUser);
            }
        };
        addSendMessage(event);
        event.submit();
    }

    public void getMessage(String username, String password, String id) {
        Event event = new Event() {
            @Override
            public boolean run() throws Exception {
                return accessConfig.isAccept(username, password, this)
                        && (boolean) ManagerLogic.invoke(this.getClojureName(), username, password, id, MessageManager.this, this);
            }
        };
        addFailMessage(event);
        event.submit();
    }

    public void readMessage(String username, String password, String id) {
        Event event = new Event() {
            @Override
            public boolean run() throws Exception {
                return accessConfig.isAccept(username, password, this)
                        && (boolean) ManagerLogic.invoke(this.getClojureName(), username, password, id);
            }
        };
        addSendMessage(event);
        event.submit();
    }

    public void getAllMessage(String username, String password) {
        Event event = new Event() {
            @Override
            public boolean run() throws Exception {
                return accessConfig.isAccept(username, password, this)
                        && (boolean) ManagerLogic.invoke(this.getClojureName(), username, password, username, MessageManager.this, this);
            }
        };
        addFailMessage(event);
        event.submit();
    }

    public void getUserAllMessage(String username, String password, String aim) {
        Event event = new Event() {
            @Override
            public boolean run() throws Exception {
                return accessConfig.isAccept(username, password, this)
                        && (boolean) ManagerLogic.invoke(this.getClojureName(), username, password, aim, MessageManager.this, this);
            }
        };
        addFailMessage(event);
        event.submit();
    }
}
