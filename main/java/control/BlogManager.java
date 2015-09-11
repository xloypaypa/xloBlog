package control;

import model.db.BlogCollection;
import model.db.DBCollection;
import model.event.Event;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.tool.WriteMessageServerSolver;
import org.bson.Document;
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

    public void addDocument(String username, String password, String title, String body, String type) {
        Event event = new Event() {
            @Override
            public boolean run() throws Exception {
                return accessConfig.isAccept(username, password, this)
                        && (boolean) ManagerLogic.invoke(this.getClojureName(), username, password, title, body, type);
            }
        };
        addSendMessage(event);
        event.submit();
    }

    public void addReply(String username, String password, String documentID, String reply) {
        Event event = new Event() {
            @Override
            public boolean run() throws Exception {
                return accessConfig.isAccept(username, password, this)
                        && (boolean) ManagerLogic.invoke(this.getClojureName(), username, password, documentID, reply);
            }
        };
        addSendMessage(event);
        event.submit();
    }

    public void getDocument(String id) {
        new Event() {
            @Override
            public boolean run() throws Exception {
                return (boolean) ManagerLogic.invoke(this.getClojureName(), id, BlogManager.this ,this, returnCodeConfig);
//                JSONObject object = new JSONObject();
//                object.put("return", returnCodeConfig.getCode("not found"));
//                sendWhileFail(new WriteMessageServerSolver(requestSolver, object));
//
//                if (id == null) return false;
//
//                BlogCollection blogCollection = new BlogCollection();
//                DBCollection.DBData data = blogCollection.getDocumentData(id);
//                if (data == null) return false;
//                object = JSONObject.fromObject(data.object.toJson());
//                sendWhileSuccess(new WriteMessageServerSolver(requestSolver, object));
//                return true;
            }
        }.submit();
    }

    public void addReader(String id) {
        Event event = new Event() {
            @Override
            public boolean run() {
                BlogCollection blogCollection = new BlogCollection();
                DBCollection.DBData data = blogCollection.getDocument(id);
                data.object.put("reader", data.object.getInteger("reader", 0) + 1);
                return true;
            }
        };
        addSendMessage(event);
        event.submit();
    }

    public void getAuthorTypeDocumentList(String author, String type) {
        new Event() {
            @Override
            public boolean run() {
                JSONObject object = new JSONObject();
                object.put("return", returnCodeConfig.getCode("not found"));
                sendWhileFail(new WriteMessageServerSolver(requestSolver, object));

                if (author == null || type == null) return false;
                sendDocumentList(this, new Document().append("author", author).append("type", type));
                return true;
            }
        }.submit();
    }

    public void getTypeDocumentList(String type) {
        new Event() {
            @Override
            public boolean run() {
                JSONObject object = new JSONObject();
                object.put("return", returnCodeConfig.getCode("not found"));
                sendWhileFail(new WriteMessageServerSolver(requestSolver, object));

                if (type == null) return false;
                sendDocumentList(this, new Document().append("type", type));
                return true;
            }
        }.submit();
    }

    public void getAuthorDocumentList(String author) {
        new Event() {
            @Override
            public boolean run() {
                JSONObject object = new JSONObject();
                object.put("return", returnCodeConfig.getCode("not found"));
                sendWhileFail(new WriteMessageServerSolver(requestSolver, object));

                if (author == null) return false;
                sendDocumentList(this, new Document().append("author", author));
                return true;
            }
        }.submit();
    }

    protected void sendDocumentList(Event event, Document message) {
        BlogCollection blogCollection = new BlogCollection();
        List<DBCollection.DBData> list = blogCollection.findDocumentListData(message);

        JSONArray array = new JSONArray();
        for (DBCollection.DBData now : list) {
            JSONObject object = new JSONObject();
            object.put("id", now.object.get("_id").toString());
            object.put("title", now.object.get("title"));
            object.put("author", now.object.get("author"));
            object.put("time", now.object.get("time"));
            object.put("reader", now.object.getInteger("reader", 0));
            String body = now.object.getString("body");
            if (body.length() > 100) {
                body = body.substring(0, 100);
            }
            object.put("preview", body);
            array.add(object);
        }
        event.sendWhileSuccess(new WriteMessageServerSolver(requestSolver, array));
    }
}
