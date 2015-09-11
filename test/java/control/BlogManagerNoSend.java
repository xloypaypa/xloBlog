package control;

import model.event.Event;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import testTool.Counter;

import java.util.List;
import java.util.Map;

/**
 * Created by xlo on 2015/9/2.
 * it's the blog manager who not send reply
 */
public class BlogManagerNoSend extends BlogManager {
    protected Counter counter;
    protected JSONObject message;
    protected JSONArray array;

    public BlogManagerNoSend(Counter counter) {
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
    public void getDocument(String id) {
        Event event = new Event() {
            @Override
            public boolean run() throws Exception {
                return (boolean) ManagerLogic.invoke("control.BlogManager$getDocument", id, BlogManagerNoSend.this, this, returnCodeConfig);
            }
        };
        addSendMessage(event);
        event.submit();
    }

    @Override
    public void getAuthorTypeDocumentList(String author, String type) {
        Event event = new Event() {
            @Override
            public boolean run() throws Exception {
                return (boolean) ManagerLogic.invoke("control.BlogManager$getAuthorTypeDocumentList", author, type, BlogManagerNoSend.this, this, returnCodeConfig);
            }
        };
        addSendMessage(event);
        event.submit();
    }

    @Override
    public void getTypeDocumentList(String type) {
        Event event = new Event() {
            @Override
            public boolean run() throws Exception {
                return (boolean) ManagerLogic.invoke("control.BlogManager$getTypeDocumentList", type, BlogManagerNoSend.this, this, returnCodeConfig);
            }
        };
        addSendMessage(event);
        event.submit();
    }

    @Override
    public void getAuthorDocumentList(String author) {
        Event event = new Event() {
            @Override
            public boolean run() throws Exception {
                return (boolean) ManagerLogic.invoke("control.BlogManager$getAuthorDocumentList", author, BlogManagerNoSend.this, this, returnCodeConfig);
            }
        };
        addSendMessage(event);
        event.submit();
    }
}
