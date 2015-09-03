package control;

import config.LengthLimitConfig;
import model.db.*;
import model.event.Event;
import org.bson.Document;
import server.serverSolver.RequestSolver;

import java.util.List;

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

    public void removeUser(String username, String password) {
        Event event = new Event() {
            @Override
            public boolean run() {
                if (!accessConfig.isAccept(username, password, this)) return false;

                UserCollection userCollection = new UserCollection();
                userCollection.removeUser(username);

                BlogCollection blogCollection = new BlogCollection();
                List<DBClient.DBData> listData = blogCollection.findDocumentListData(new Document().append("author", username));
                for (DBClient.DBData now : listData) {
                    blogCollection.removeDocument(now.object.get("_id").toString());
                }

                MarkUserCollection markUserCollection = new MarkUserCollection();
                for (DBClient.DBData now : markUserCollection.find(new Document().append("from", username))) {
                    markUserCollection.removeMark(now.object.getString("from"), now.object.getString("to"));
                }
                for (DBClient.DBData now : markUserCollection.find(new Document().append("to", username))) {
                    markUserCollection.removeMark(now.object.getString("from"), now.object.getString("to"));
                }

                MessageCollection messageCollection = new MessageCollection();
                for (DBClient.DBData now : markUserCollection.find(new Document().append("username", username))) {
                    messageCollection.removeMessage(now.object.getString("_id"));
                }
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

    public void acceptUserRegister(String username, String password, String aimUsername, String accessType, int access) {
        Event event = new Event() {
            @Override
            public boolean run() {
                if (aimUsername == null) return false;
                UserCollection userCollection = new UserCollection();
                if (!accessConfig.isAccept(username, password, this)) return false;
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
