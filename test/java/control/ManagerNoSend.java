package control;

import model.event.Event;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import testTool.Counter;

import java.util.List;
import java.util.Map;

/**
 * Created by xlo on 2015/9/11.
 * it's the manager no send
 */
public class ManagerNoSend extends Manager {
    protected Counter counter;
    protected JSONObject message;
    protected JSONArray array;

    public ManagerNoSend(Counter counter) {
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
    public void addSuccessMessage(Event event) {
        event.actionWhileSuccess(new Event() {
            @Override
            public boolean run() {
                counter.addSuccess(1);
                counter.add(-1);
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
                counter.add(-1);
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
                counter.add(-1);
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
                counter.add(-1);
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
                counter.add(-1);
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
                counter.add(-1);
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
                counter.add(-1);
                setMessage(JSONObject.fromObject(message));
                return true;
            }
        });
    }
}
