package control;

import model.event.Event;
import net.server.serverSolver.RequestSolver;

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
            public boolean run() throws Exception {
                return (boolean) ManagerLogic.invoke(this.getClojureName(), username, password);
            }
        };
        sendManager.addSendMessage(event);
        event.submit();
    }

    public void register(String username, String password) {
        Event event = new Event() {
            @Override
            public boolean run() throws Exception {
                return (boolean) ManagerLogic.invoke(this.getClojureName(), username, password);
            }
        };
        sendManager.addSendMessage(event);
        event.submit();
    }

    public void removeUser(String username, String password) {
        Event event = new Event() {
            @Override
            public boolean run() throws Exception {
                return accessConfig.isAccept(username, password, this)
                        && (boolean) ManagerLogic.invoke(this.getClojureName(), username, password);
            }
        };
        sendManager.addSendMessage(event);
        event.submit();
    }

    public void changeUserAccess(String username, String password, String aimUsername, String accessType, int access) {
        Event event = new Event() {
            @Override
            public boolean run() throws Exception {
                return accessConfig.isAccept(username, password, this)
                        && (boolean) ManagerLogic.invoke(this.getClojureName(), username, password, aimUsername, accessType, access);
            }
        };
        sendManager.addSendMessage(event);
        event.submit();
    }

    public void setMotto(String username, String password, String motto) {
        Event event = new Event() {
            @Override
            public boolean run() throws Exception {
                return accessConfig.isAccept(username, password, this)
                        && (boolean) ManagerLogic.invoke(this.getClojureName(), username, password, motto);
            }
        };
        sendManager.addSendMessage(event);
        event.submit();
    }

    public void getMotto(String username) {
        Event event = new Event() {
            @Override
            public boolean run() throws Exception {
                return (boolean) ManagerLogic.invoke(this.getClojureName(), username, sendManager, this);
            }
        };
        sendManager.addFailMessage(event);
        event.submit();
    }
}
