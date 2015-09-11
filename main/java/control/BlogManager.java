package control;

import model.db.BlogCollection;
import model.db.DBCollection;
import model.event.Event;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.tool.WriteMessageServerSolver;
import org.bson.Document;
import server.serverSolver.RequestSolver;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
            }
        }.submit();
    }

    public void addReader(String id) {
        Event event = new Event() {
            @Override
            public boolean run() throws Exception {
                return (boolean) ManagerLogic.invoke(this.getClojureName(), id);
            }
        };
        addSendMessage(event);
        event.submit();
    }

    public void getAuthorTypeDocumentList(String author, String type) {
        new Event() {
            @Override
            public boolean run() throws Exception {
                return (boolean) ManagerLogic.invoke(this.getClojureName(), author, type, BlogManager.this, this, returnCodeConfig);
            }
        }.submit();
    }

    public void getTypeDocumentList(String type) {
        new Event() {
            @Override
            public boolean run() throws Exception {
                return (boolean) ManagerLogic.invoke(this.getClojureName(), type, BlogManager.this, this, returnCodeConfig);
            }
        }.submit();
    }

    public void getAuthorDocumentList(String author) {
        new Event() {
            @Override
            public boolean run() throws Exception {
                return (boolean) ManagerLogic.invoke(this.getClojureName(), author, BlogManager.this, this, returnCodeConfig);
            }
        }.submit();
    }
}
