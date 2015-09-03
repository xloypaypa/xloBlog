package control;

import config.LengthLimitConfig;
import model.db.BlogCollection;
import model.db.DBClient;
import model.db.MarkUserCollection;
import model.db.MessageCollection;
import model.event.Event;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.tool.WriteMessageServerSolver;
import org.bson.*;
import server.serverSolver.RequestSolver;

import java.util.Date;
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
            public boolean run() {
                LengthLimitConfig lengthLimitConfig = LengthLimitConfig.getConfig();
                if (username == null || password == null || title == null || body == null) return false;
                if (title.length() > lengthLimitConfig.getLimit("documentTitle") || body.length() > lengthLimitConfig.getLimit("documentBody"))
                    return false;
                if (!accessConfig.isAccept(username, password, this)) return false;

                BlogCollection blogCollection = new BlogCollection();
                blogCollection.addDocument(username, title, body, new Date(), type);

                MarkUserCollection markUserCollection = new MarkUserCollection();
                MessageCollection messageCollection = new MessageCollection();
                for (DBClient.DBData now : markUserCollection.find(new Document().append("to", username))) {
                    messageCollection.addMessage(now.object.getString("to"), username, title, new Date());
                }
                return true;
            }
        };
        addSendMessage(event);
        event.submit();
    }

    public void addReply(String username, String password, String documentID, String reply) {
        Event event = new Event() {
            @SuppressWarnings("unchecked")
            @Override
            public boolean run() {
                LengthLimitConfig lengthLimitConfig = LengthLimitConfig.getConfig();
                if (username == null || password == null || reply == null) return false;
                if (reply.length() > lengthLimitConfig.getLimit("documentBody"))
                    return false;
                if (!accessConfig.isAccept(username, password, this)) return false;

                BlogCollection blogCollection = new BlogCollection();
                DBClient.DBData document = blogCollection.getDocument(documentID);
                BsonArray list;
                if (document.object.containsKey("reply"))
                    list = new BsonArray((List<? extends BsonValue>) document.object.get("reply"));
                else {
                    list = new BsonArray();
                }
                BsonDocument replyMap = new BsonDocument();
                replyMap.put("author", new BsonString(username));
                replyMap.put("data", new BsonDateTime(new Date().getTime()));
                replyMap.put("reply", new BsonString(reply));
                list.add(replyMap);
                document.object.put("reply", list);
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
                DBClient.DBData data = blogCollection.getDocumentData(id);
                if (data == null) return false;
                object = JSONObject.fromObject(data.object.toJson());
                sendWhileSuccess(new WriteMessageServerSolver(requestSolver, object));
                return true;
            }
        }.submit();
    }

    public void addReader(String id) {
        Event event = new Event() {
            @Override
            public boolean run() {
                BlogCollection blogCollection = new BlogCollection();
                DBClient.DBData data = blogCollection.getDocument(id);
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
        List<DBClient.DBData> list = blogCollection.findDocumentListData(message);

        JSONArray array = new JSONArray();
        for (DBClient.DBData now : list) {
            JSONObject object = new JSONObject();
            object.put("id", now.object.get("_id").toString());
            object.put("title", now.object.get("title"));
            object.put("author", now.object.get("author"));
            object.put("time", now.object.get("time"));
            String body = now.object.getString("body");
            if (body.length()>100) {
                body = body.substring(0, 100);
            }
            object.put("preview", body);
            array.add(object);
        }
        event.sendWhileSuccess(new WriteMessageServerSolver(requestSolver, array));
    }
}
