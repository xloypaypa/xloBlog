package control;

import config.ConfigManager;
import config.ReturnCodeConfig;
import model.db.DBClient;
import model.db.UserCollection;
import model.event.Event;
import net.sf.json.JSONObject;
import net.tool.WriteMessageServerSolver;
import org.bson.Document;
import server.serverSolver.RequestSolver;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xlo on 2015/8/21.
 * it's the user access manager
 */
public class UserAccessManager {
    protected RequestSolver requestSolver;
    private static ReturnCodeConfig returnCodeConfig = (ReturnCodeConfig) ConfigManager.getConfigManager().getConfig(ReturnCodeConfig.class);

    public UserAccessManager(RequestSolver requestSolver) {
        this.requestSolver = requestSolver;
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
                UserCollection userCollection = new UserCollection();

                DBClient.DBData past = userCollection.getUserData(username);
                if (past != null) {
                    return false;
                }
                userCollection.lockCollection();
                userCollection.lockUser(username);
                Map<String, Object> data = new HashMap<>();
                data.put("username", username);
                data.put("password", password);
                userCollection.insert(new Document(data));
                return true;
            }
        };
        addSendMessage(event);
        event.submit();
    }

    protected void addSendMessage(Event event) {
        JSONObject object = new JSONObject();
        object.put("return", returnCodeConfig.getCode("accept"));
        event.sendWhileSuccess(new WriteMessageServerSolver(requestSolver, object));
        object.clear();
        object.put("return", returnCodeConfig.getCode("forbidden"));
        event.sendWhileFail(new WriteMessageServerSolver(requestSolver, object));
    }
}
