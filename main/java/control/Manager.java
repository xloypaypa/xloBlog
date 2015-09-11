package control;

import config.AccessConfig;
import config.ConfigManager;
import config.ReturnCodeConfig;
import model.event.Event;
import net.sf.json.JSONObject;
import net.tool.WriteMessageServerSolver;
import server.serverSolver.RequestSolver;

import java.util.Map;

/**
 * Created by xlo on 2015/8/28.
 * it's the abstract manager
 */
public abstract class Manager {
    protected RequestSolver requestSolver;
    protected static ReturnCodeConfig returnCodeConfig = (ReturnCodeConfig) ConfigManager.getConfigManager().getConfig(ReturnCodeConfig.class);
    protected static AccessConfig accessConfig = (AccessConfig) ConfigManager.getConfigManager().getConfig(AccessConfig.class);

    public Manager(RequestSolver requestSolver) {
        this.requestSolver = requestSolver;
    }

    public void addSendMessage(Event event) {
        addSuccessMessage(event);
        addFailMessage(event);
    }

    public void addSuccessMessage(Event event) {
        String accept = "accept";
        JSONObject object = getJsonObjectAsReturn(accept);
        event.sendWhileSuccess(new WriteMessageServerSolver(requestSolver, object));
    }

    public void addSuccessMessage(Event event, Map<String, Object> message) {
        JSONObject object = getJsonObject(message);
        event.sendWhileSuccess(new WriteMessageServerSolver(requestSolver, object));
    }

    public void addSuccessMessage(Event event, String message) {
        JSONObject object = JSONObject.fromObject(message);
        event.sendWhileSuccess(new WriteMessageServerSolver(requestSolver, object));
    }

    public void addFailMessage(Event event) {
        String forbidden = "forbidden";
        JSONObject object = getJsonObjectAsReturn(forbidden);
        event.sendWhileFail(new WriteMessageServerSolver(requestSolver, object));
    }

    public void addFailMessage(Event event, Map<String, Object> message) {
        JSONObject object = getJsonObject(message);
        event.sendWhileFail(new WriteMessageServerSolver(requestSolver, object));
    }

    public void addFailMessage(Event event, String message) {
        JSONObject object = JSONObject.fromObject(message);
        event.sendWhileFail(new WriteMessageServerSolver(requestSolver, object));
    }

    protected JSONObject getJsonObject(Map<String, Object> message) {
        JSONObject object = new JSONObject();
        object.putAll(message);
        return object;
    }

    protected JSONObject getJsonObjectAsReturn(String forbidden) {
        JSONObject object = new JSONObject();
        object.put("return", returnCodeConfig.getCode(forbidden));
        return object;
    }
}
