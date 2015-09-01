package control;

import com.mongodb.BasicDBList;
import config.LengthLimitConfig;
import model.db.BlogCollection;
import model.db.DBClient;
import model.event.Event;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.tool.WriteMessageServerSolver;
import org.bson.Document;
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

    public void addDocument(String username, String password, String title, String body) {
        Event event = new Event() {
            @Override
            public boolean run() {
                LengthLimitConfig lengthLimitConfig = LengthLimitConfig.getConfig();
                if (username == null || password == null || title == null || body == null) return false;
                if (title.length() > lengthLimitConfig.getLimit("documentTitle") || body.length() > lengthLimitConfig.getLimit("documentBody"))
                    return false;
                if (!accessConfig.isAccept(username, password)) return false;

                BlogCollection blogCollection = new BlogCollection();
                blogCollection.addDocument(username, title, body, new Date());
                return true;
            }
        };
        addSendMessage(event);
        event.submit();
    }

    public void addReply(String username, String password, String documentID, String reply) {
        Event event = new Event() {
            @Override
            public boolean run() {
                LengthLimitConfig lengthLimitConfig = LengthLimitConfig.getConfig();
                if (username == null || password == null || reply == null) return false;
                if (reply.length() > lengthLimitConfig.getLimit("documentBody"))
                    return false;
                if (!accessConfig.isAccept(username, password)) return false;

                BlogCollection blogCollection = new BlogCollection();
                DBClient.DBData document = blogCollection.getDocument(documentID);
                BasicDBList dbList; //TODO test this update
                if (document.object.containsKey("reply")) {
                    dbList = (BasicDBList) document.object.get("reply");
                } else {
                    dbList = new BasicDBList();
                }
                Document replyMap = new Document();
                replyMap.put("author", username);
                replyMap.put("data", new Date());
                replyMap.put("reply", reply);
                dbList.add(replyMap);
                document.object.put("reply", dbList);
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

    public void getDocumentList(String author) {
        new Event() {
            @Override
            public boolean run() {
                JSONObject object = new JSONObject();
                object.put("return", returnCodeConfig.getCode("not found"));
                sendWhileFail(new WriteMessageServerSolver(requestSolver, object));

                if (author == null) return false;
                BlogCollection blogCollection = new BlogCollection();
                List<DBClient.DBData> list = blogCollection.getDocumentDataByAuthor(author);

                JSONArray array = new JSONArray();
                for (DBClient.DBData now : list) {
                    JSONObject message = new JSONObject();
                    message.put("id", now.object.get("_id"));
                    message.put("title", now.object.get("title"));
                    message.put("author", now.object.get("author"));
                    message.put("time", now.object.get("time"));
                    message.put("preview", now.object.getString("body").substring(0, 100));
                    array.add(message);
                }

                sendWhileSuccess(new WriteMessageServerSolver(requestSolver, array));
                return true;
            }
        }.submit();
    }
}
