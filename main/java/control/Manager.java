package control;

import config.AccessConfig;
import config.ConfigManager;
import config.ReturnCodeConfig;
import model.event.Event;
import net.sf.json.JSONObject;
import net.tool.WriteMessageServerSolver;
import server.serverSolver.RequestSolver;

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

    protected void addSendMessage(Event event) {
        addSuccessMessage(event);
        addFailMessage(event);
    }

    protected void addSuccessMessage(Event event) {
        JSONObject object = new JSONObject();
        object.put("return", returnCodeConfig.getCode("accept"));
        event.sendWhileSuccess(new WriteMessageServerSolver(requestSolver, object));
    }

    protected void addFailMessage(Event event) {
        JSONObject object = new JSONObject();
        object.put("return", returnCodeConfig.getCode("forbidden"));
        event.sendWhileFail(new WriteMessageServerSolver(requestSolver, object));
    }
}
