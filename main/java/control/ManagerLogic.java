package control;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xlo on 2015/9/10.
 * it's the manager logic
 */
public class ManagerLogic {
    protected static final Map<String, Object> logicObject = new HashMap<>();
    protected static final Map<String, Method> logicFun = new HashMap<>();

    public static void put(String name, Object object, int parameterNum) throws NoSuchMethodException {
        synchronized (logicObject) {
            logicObject.put(name, object);
            Class<?>[] objects = new Class[parameterNum];
            for (int i = 0; i < parameterNum; i++) {
                objects[i] = Object.class;
            }
            Method method = object.getClass().getMethod("invoke", objects);
            method.setAccessible(true);
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
}
