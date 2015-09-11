package control;

import model.db.DBCollection;
import model.db.MessageCollection;
import model.event.Event;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.tool.WriteMessageServerSolver;
import server.serverSolver.RequestSolver;
import testTool.Counter;

import java.util.List;
import java.util.Map;

/**
 * Created by xlo on 2015/9/11.
 * it's the message manager who not send reply
 */
public class MessageManagerNoSend extends MessageManager {
    protected Counter counter;
    protected JSONObject message;
    protected JSONArray array;

    public MessageManagerNoSend(Counter counter) {
        super(null);
        this.counter = counter;
    }

    public JSONObject getMessage() {
        return message;
    }

    public JSONArray getArray() {
        return array;
    }

    private void setMessage(JSONObject message) {
        this.message = message;
    }

    private void setArray(JSONArray array) {
        this.array = array;
    }

    @Override
    public void addSendMessage(Event event) {
        event.actionWhileCommit(new Event() {
            @Override
            public boolean run() {
                counter.add(-1);
                return true;
            }
        });
        super.addSendMessage(event);
    }

    @Override
    public void addSuccessMessage(Event event) {
        event.actionWhileSuccess(new Event() {
            @Override
            public boolean run() {
                counter.addSuccess(1);
                return true;
            }
        });
    }

    @Override
    public void addSuccessMessage(Event event, Map<String, Object> message) {
        event.actionWhileSuccess(new Event() {
            @Override
            public boolean run() {
                counter.addSuccess(1);
                setMessage(getJsonObject(message));
                return true;
            }
        });
    }

    @Override
    public void addSuccessMessage(Event event, String message) {
        event.actionWhileSuccess(new Event() {
            @Override
            public boolean run() {
                counter.addSuccess(1);
                setMessage(JSONObject.fromObject(message));
                return true;
            }
        });
    }

    @Override
    public void addSuccessMessage(Event event, List<Map<String, Object>> message) {
        event.actionWhileSuccess(new Event() {
            @Override
            public boolean run() {
                counter.addSuccess(1);
                setArray(getJsonObject(message));
                return true;
            }
        });
    }

    @Override
    public void addFailMessage(Event event) {
        event.actionWhileFail(new Event() {
            @Override
            public boolean run() {
                counter.addFail(1);
                return true;
            }
        });
    }

    @Override
    public void addFailMessage(Event event, Map<String, Object> message) {
        event.actionWhileFail(new Event() {
            @Override
            public boolean run() {
                counter.addFail(1);
                setMessage(getJsonObject(message));
                return true;
            }
        });
    }

    @Override
    public void addFailMessage(Event event, String message) {
        event.actionWhileFail(new Event() {
            @Override
            public boolean run() {
                counter.addFail(1);
                setMessage(JSONObject.fromObject(message));
                return true;
            }
        });
    }

    @Override
    public void addMessage(String username, String password, String message, String aimUser) {
        Event event = new Event() {
            @Override
            public boolean run() throws Exception {
                return accessConfig.isAccept(username, password, this)
                        && (boolean) ManagerLogic.invoke("control.MessageManager$addMessage", username, password, message, aimUser);
            }
        };
        addSendMessage(event);
        event.submit();
    }

    @Override
    public void getMessage(String username, String password, String id) {
        Event event = new Event() {
            @Override
            public boolean run() throws Exception {
                return accessConfig.isAccept(username, password, this)
                        && (boolean) ManagerLogic.invoke("control.MessageManager$getMessage", username, password, id, MessageManagerNoSend.this, this);
            }
        };
        addSendMessage(event);
        event.submit();
    }

    @Override
    public void readMessage(String username, String password, String id) {
        Event event = new Event() {
            @Override
            public boolean run() throws Exception {
                return accessConfig.isAccept(username, password, this)
                        && (boolean) ManagerLogic.invoke("control.MessageManager$readMessage", username, password, id);
            }
        };
        addSendMessage(event);
        event.submit();
    }

    @Override
    public void getAllMessage(String username, String password) {
        Event event = new Event() {
            @Override
            public boolean run() throws Exception {
                return accessConfig.isAccept(username, password, this)
                        && (boolean) ManagerLogic.invoke("control.MessageManager$getAllMessage", username, password, username, MessageManagerNoSend.this, this);
            }
        };
        addSendMessage(event);
        event.submit();
    }

    @Override
    public void getUserAllMessage(String username, String password, String aim) {
        Event event = new Event() {
            @Override
            public boolean run() throws Exception {
                return accessConfig.isAccept(username, password, this)
                        && (boolean) ManagerLogic.invoke("control.MessageManager$getAllMessage", username, password, aim, MessageManagerNoSend.this, this);
            }
        };
        addSendMessage(event);
        event.submit();
    }
}
