package control;

import config.AccessConfig;
import config.ConfigManager;
import config.ReturnCodeConfig;
import model.event.Event;
import model.lock.NameLockImpl;
import net.sf.json.JSONObject;
import net.tool.WriteMessageServerSolver;
import server.serverSolver.RequestSolver;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xlo on 2015/8/28.
 * it's the abstract manager
 */
public abstract class Manager {
    protected RequestSolver requestSolver;
    protected static ReturnCodeConfig returnCodeConfig = (ReturnCodeConfig) ConfigManager.getConfigManager().getConfig(ReturnCodeConfig.class);
    protected static AccessConfig accessConfig = (AccessConfig) ConfigManager.getConfigManager().getConfig(AccessConfig.class);

    protected static final Map<String, Object> logicObject = new HashMap<>();
    protected static final Map<String, Method> logicFun = new HashMap<>();

    public Manager(RequestSolver requestSolver) {
        this.requestSolver = requestSolver;
    }

    public static void put(String name, Object object, int parameterNum) throws NoSuchMethodException {
        synchronized (logicObject) {
            System.out.println("put " + name);
            logicObject.put(name, object);
            Class<?>[] objects = new Class[parameterNum];
            for (int i = 0; i < parameterNum; i++) {
                objects[i] = Object.class;
            }
            Method method = object.getClass().getMethod("invoke", objects);
            logicFun.put(name, method);
        }
    }

    public static Object invoke(String name, Object... objects) throws Exception {
        Method method;
        Object object;
        synchronized (logicObject) {
            method = logicFun.get(name);
            object = logicObject.get(name);
        }
        return method.invoke(object, objects);
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
