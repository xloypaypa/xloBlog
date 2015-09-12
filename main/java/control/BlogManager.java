package control;

import model.event.Event;
import server.serverSolver.RequestSolver;

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
        sendManager.addSendMessage(event);
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
        sendManager.addSendMessage(event);
        event.submit();
    }

    public void getDocument(String id) {
        new Event() {
            @Override
            public boolean run() throws Exception {
                return (boolean) ManagerLogic.invoke(this.getClojureName(), id, sendManager, this, returnCodeConfig);
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
        sendManager.addSendMessage(event);
        event.submit();
    }

    public void getAuthorTypeDocumentList(String author, String type, final String page) {
        new Event() {
            @Override
            public boolean run() throws Exception {
                return (boolean) ManagerLogic.invoke(this.getClojureName(), author, type, page, sendManager, this, returnCodeConfig);
            }
        }.submit();
    }

    public void getTypeDocumentList(String type, String page) {
        new Event() {
            @Override
            public boolean run() throws Exception {
                return (boolean) ManagerLogic.invoke(this.getClojureName(), "type", type, page, sendManager, this, returnCodeConfig);
            }
        }.submit();
    }

    public void getAuthorDocumentList(String author, String page) {
        new Event() {
            @Override
            public boolean run() throws Exception {
                return (boolean) ManagerLogic.invoke(this.getClojureName(), "author", author, page, sendManager, this, returnCodeConfig);
            }
        }.submit();
    }
}
