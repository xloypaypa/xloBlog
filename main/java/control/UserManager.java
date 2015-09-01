package control;

import com.mongodb.BasicDBList;
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
public class UserManager extends Manager {

    public UserManager(RequestSolver requestSolver) {
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
                userCollection.lockCollection();
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

    public void markUser(String username, String password, String aimUser) {
        Event event = new Event() {
            @Override
            public boolean run() {
                if (aimUser == null) return false;
                if (!accessConfig.isAccept(username, password)) return false;

                UserCollection userCollection = new UserCollection();
                DBClient.DBData user = userCollection.getUser(username);
                BasicDBList dbList; //TODO test this update
                if (user.object.containsKey("mark")) {
                    dbList = (BasicDBList) user.object.get("mark");
                } else {
                    dbList = new BasicDBList();
                }
                dbList.add(aimUser);
                user.object.put("mark", dbList);
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
                if (!accessConfig.isAccept(username, password)) return false;

                UserCollection userCollection = new UserCollection();
                DBClient.DBData user = userCollection.getUser(username);
                BasicDBList dbList; //TODO test this update
                if (user.object.containsKey("mark")) {
                    dbList = (BasicDBList) user.object.get("mark");
                } else {
                    dbList = new BasicDBList();
                }
                for (Object object : dbList) {
                    if (object.equals(aimUser)) {
                        dbList.remove(object);
                    }
                }
                user.object.put("mark", dbList);
                return true;
            }
        };
        addSendMessage(event);
        event.submit();
    }

    public void acceptUserRegister(String username, String password, String aimUsername, String accessType, int access) {
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
                aimUser.object.put(accessType, access);
                return true;
            }
        };
        addSendMessage(event);
        event.submit();
    }
}
