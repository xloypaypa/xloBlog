package control;

import model.event.SendEvent;
import net.server.serverSolver.RequestSolver;

/**
 * Created by xlo on 2015/9/9.
 * it's the manager of mark user
 */
public class MarkManager extends Manager {
    public MarkManager(RequestSolver requestSolver) {
        super(requestSolver);
    }

    public void markUser(String username, String password, String aimUser) {
        SendEvent sendEvent = new SendEvent() {
            @Override
            public boolean run() throws Exception {
                return accessConfig.isAccept(username, password, this)
                        && (boolean) ManagerLogic.invoke(this.getClojureName(), username, password, aimUser);
            }
        };
        sendManager.addSendMessage(sendEvent);
        sendEvent.submit();
    }

    public void unMarkUser(String username, String password, String aimUser) {
        SendEvent sendEvent = new SendEvent() {
            @Override
            public boolean run() throws Exception {
                return accessConfig.isAccept(username, password, this)
                        && (boolean) ManagerLogic.invoke(this.getClojureName(), username, password, aimUser);
            }
        };
        sendManager.addSendMessage(sendEvent);
        sendEvent.submit();
    }

    public void isMarked(String username, String password, String aimUser) {
        SendEvent sendEvent = new SendEvent() {
            @Override
            public boolean run() throws Exception {
                return accessConfig.isAccept(username, password, this)
                        && (boolean) ManagerLogic.invoke(this.getClojureName(), username, password, aimUser, sendManager, this);
            }
        };
        sendManager.addFailMessage(sendEvent);
        sendEvent.submit();
    }

    public void getMarkedList(String username, String password) {
        SendEvent sendEvent = new SendEvent() {
            @Override
            public boolean run() throws Exception {
                return accessConfig.isAccept(username, password, this)
                        && (boolean) ManagerLogic.invoke(this.getClojureName(), username, password, sendManager, this);
//                if (!accessConfig.isAccept(username, password, this)) return false;
//
//                MarkUserCollection markUserCollection = new MarkUserCollection();
//                List<DBCollection.DBData> list = markUserCollection.find(new Document().append("from", username));
//
//                List<Map<String, Object>> ans = new LinkedList<>();
//                for (DBCollection.DBData now : list) {
//                    Map<String, Object> nowMap = new HashMap<>();
//                    nowMap.put("user", now.object.get("to"));
//                    ans.add(nowMap);
//                }
//                sendManager.addSuccessMessage(this, ans);
//                return true;
            }
        };
        sendManager.addFailMessage(sendEvent);
        sendEvent.submit();
    }
}
