package control;

import config.LengthLimitConfig;
import model.db.DBClient;
import model.db.UserCollection;
import model.event.Event;
import org.bson.Document;
import server.serverSolver.RequestSolver;

/**
 * Created by xlo on 2015/8/21.
 * it's the user access manager
 */
public class UserAccessManager extends Manager {

    public UserAccessManager(RequestSolver requestSolver) {
        super(requestSolver);
    }

    public void loginUser(String username, String password) {
        Event event = new Event() {
            @Override
            public boolean run() {
                if (username == null || password == null) return false;
                UserCollection userCollection = new UserCollection();
                DBClient.DBData data = userCollection.getUserData(username);
                if (data == null) return false;
                Document document = data.object;
                return document.get("password").equals(password);
            }
        };
        addSendMessage(event);
        event.submit();
    }

    public void register(String username, String password) {
        Event event = new Event() {
            @Override
            public boolean run() {
                LengthLimitConfig lengthLimitConfig = LengthLimitConfig.getConfig();
                if (username == null || password == null) return false;
                if (username.length() > lengthLimitConfig.getLimit("username") || password.length() > lengthLimitConfig.getLimit("password"))
                    return false;

                UserCollection userCollection = new UserCollection();
                userCollection.lockUser(username);
                DBClient.DBData past = userCollection.getUserData(username);
                if (past != null) {
                    return false;
                }
                userCollection.registerUser(username, password);
                return true;
            }
        };
        addSendMessage(event);
        event.submit();
    }

    public void acceptUserRegister(String username, String password, String aimUsername, int access) {
        Event event = new Event() {
            @Override
            public boolean run() {
                if (aimUsername == null) return false;
                UserCollection userCollection = new UserCollection();
                if (!accessConfig.isAccept(username, password)) return false;
                DBClient.DBData aimUser = userCollection.getUser(aimUsername);
                if (aimUser == null) {
                    return false;
                }
                aimUser.object.put("access", access);
                return true;
            }
        };
        addSendMessage(event);
        event.submit();
    }
}
