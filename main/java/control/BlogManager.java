package control;

import model.db.BlogCollection;
import model.db.DBClient;
import model.db.UserCollection;
import model.event.Event;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.tool.WriteMessageServerSolver;
import org.bson.types.ObjectId;
import server.serverSolver.RequestSolver;

import java.util.List;

/**
 * Created by xlo on 2015/8/28.
 * it's the manager of blog
 */
public class BlogManager extends Manager {
    public BlogManager(RequestSolver requestSolver) {
        super(requestSolver);
    }

    public void addBlog(String username, String password, String title, String body) {
        Event event = new Event() {
            @Override
            public boolean run() {
                if (username == null || password == null || title == null || body == null) return false;
                UserCollection userCollection = new UserCollection();
                DBClient.DBData userData = userCollection.getUserData(username);
                if (userData == null) return false;
                if (!userData.object.get("password").equals(password)) return false;
                if (!accessConfig.isAccept(userData)) return false;

                BlogCollection blogCollection = new BlogCollection();
                blogCollection.addDocument(username, title, body);
                return true;
            }
        };
        addSendMessage(event);
        event.submit();
    }

    public void getDocument(String id) {
        new Event() {
            @Override
            public boolean run() {
                JSONObject object = new JSONObject();
                object.put("return", returnCodeConfig.getCode("not found"));
                sendWhileFail(new WriteMessageServerSolver(requestSolver, object));

                if (id == null) return false;

                BlogCollection blogCollection = new BlogCollection();
                DBClient.DBData data = blogCollection.getDocument(id);
                if (data == null) return false;

                object.clear();
                object.putAll(data.object);
                sendWhileSuccess(new WriteMessageServerSolver(requestSolver, object));
                return true;
            }
        }.submit();
    }

    public void getDocumentList(String author) {
        new Event() {
            @Override
            public boolean run() {
                JSONObject object = new JSONObject();
                object.put("return", returnCodeConfig.getCode("not found"));
                sendWhileFail(new WriteMessageServerSolver(requestSolver, object));

                if (author == null) return false;
                BlogCollection blogCollection = new BlogCollection();
                List<ObjectId> list = blogCollection.getIDsByAuthor(author);

                JSONArray array = new JSONArray();
                for (ObjectId id : list) {
                    JSONObject message = new JSONObject();
                    message.put("id", id.toHexString());
                    array.add(message);
                }

                sendWhileSuccess(new WriteMessageServerSolver(requestSolver, array));
                return true;
            }
        }.submit();
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
