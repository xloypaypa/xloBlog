package control;

import model.event.SendEvent;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.tool.WriteFileServerSolver;
import testTool.Counter;

import java.io.File;
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
    protected File file;

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

    public File getFile() {
        return file;
    }

    private void setMessage(JSONObject message) {
        this.message = message;
    }

    private void setArray(JSONArray array) {
        this.array = array;
    }

    private void setFile(File file) {
        this.file = file;
    }

    @Override
    public void addSuccessMessage(SendEvent sendEvent) {
        sendEvent.actionWhileSuccess(new SendEvent() {
            @Override
            public boolean run() {
                counter.addSuccess(1);
                counter.add(-1);
                return true;
            }
        });
    }

    @Override
    public void addSuccessMessage(SendEvent sendEvent, Map<String, Object> message) {
        sendEvent.actionWhileSuccess(new SendEvent() {
            @Override
            public boolean run() {
                counter.addSuccess(1);
                setMessage(getJsonObject(message));
                counter.add(-1);
                return true;
            }
        });
    }

    @Override
    public void addSuccessMessage(SendEvent sendEvent, String message) {
        sendEvent.actionWhileSuccess(new SendEvent() {
            @Override
            public boolean run() {
                counter.addSuccess(1);
                setMessage(JSONObject.fromObject(message));
                counter.add(-1);
                return true;
            }
        });
    }

    @Override
    public void addSuccessMessage(SendEvent sendEvent, List<Map<String, Object>> message) {
        sendEvent.actionWhileSuccess(new SendEvent() {
            @Override
            public boolean run() {
                counter.addSuccess(1);
                setArray(getJsonObject(message));
                counter.add(-1);
                return true;
            }
        });
    }

    public void addSendFile(SendEvent sendEvent, String path) {
        sendEvent.actionWhileSuccess(new SendEvent() {
            @Override
            public boolean run() {
                counter.addSuccess(1);
                setFile(new File(path));
                counter.add(-1);
                return true;
            }
        });
    }

    @Override
    public void addFailMessage(SendEvent sendEvent) {
        sendEvent.actionWhileFail(new SendEvent() {
            @Override
            public boolean run() {
                counter.addFail(1);
                counter.add(-1);
                return true;
            }
        });
    }

    @Override
    public void addFailMessage(SendEvent sendEvent, Map<String, Object> message) {
        sendEvent.actionWhileFail(new SendEvent() {
            @Override
            public boolean run() {
                counter.addFail(1);
                setMessage(getJsonObject(message));
                counter.add(-1);
                return true;
            }
        });
    }

    @Override
    public void addFailMessage(SendEvent sendEvent, String message) {
        sendEvent.actionWhileFail(new SendEvent() {
            @Override
            public boolean run() {
                counter.addFail(1);
                setMessage(JSONObject.fromObject(message));
                counter.add(-1);
                return true;
            }
        });
    }
}
