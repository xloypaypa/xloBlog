package net.post;

import model.config.PostConfig;
import net.server.serverSolver.RequestSolver;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.tool.LengthLimitReadServerSolver;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by xlo on 2015/9/9.
 * it's the auto post solver by clojure
 */
public class AutoPostSolver extends LengthLimitReadServerSolver {

    protected PostConfig.PostInfo postInfo;

    public AutoPostSolver(PostConfig.PostInfo postInfo) {
        this.postInfo = postInfo;
    }

    @Override
    public void solveMessage() throws Exception {
        Class<?> managerClass = getManagerClass();
        Object managerObject = getManagerObject(managerClass);
        List<Object> data = getData();
        Method method = getMethod(managerClass, data);
        method.invoke(managerObject, data.toArray());
    }

    protected Method getMethod(Class<?> managerClass, List<Object> data) throws NoSuchMethodException {
        String methodName = this.postInfo.getMethod();
        int size;
        if (!this.postInfo.getDataType().equals("object") && this.postInfo.needAccess()) {
            size = 3;
        } else if (!this.postInfo.getDataType().equals("object")) {
            size = 1;
        } else {
            size = data.size();
        }
        Class[] methodParamType = new Class[size];
        for (int i = 0; i < size - 1; i++) {
            methodParamType[i] = String.class;
        }
        switch (this.postInfo.getDataType()) {
            case "array":
                methodParamType[size - 1] = String[].class;
                break;
            case "file":
                methodParamType[size - 1] = byte[].class;
                break;
            default:
                methodParamType[size - 1] = String.class;
                break;
        }
        return managerClass.getMethod(methodName, methodParamType);
    }

    protected List<Object> getData() {
        List<Object> data = new LinkedList<>();
        if (this.postInfo.needAccess()) {
            String username = this.requestSolver.getRequestHeadReader().getMessage("Username");
            String password = this.requestSolver.getRequestHeadReader().getMessage("Password");
            data.add(username);
            data.add(password);
        }
        List<String> need = this.postInfo.getMethodData();
        List<String> defaultValue = this.postInfo.getDefaultValue();
        switch (this.postInfo.getDataType()) {
            case "file":
                data.add(this.originalMessage);
                break;
            case "array":
                JSONArray array = JSONArray.fromObject(this.message);
                List<Object> arrayData = new ArrayList<>();
                for (Object object : array) {
                    solveObject(arrayData, need, defaultValue, (JSONObject) object);
                }
                String[] strings = new String[arrayData.size()];
                for (int i = 0; i < strings.length; i++) {
                    strings[i] = (String) arrayData.get(i);
                }
                data.add(strings);
                break;
            default:
                if (need.size() != 0) {
                    JSONObject object = JSONObject.fromObject(this.message);
                    solveObject(data, need, defaultValue, object);
                }
                break;
        }
        return data;
    }

    private void solveObject(List<Object> data, List<String> need, List<String> defaultValue, JSONObject object) {
        for (int i = 0; i < need.size(); i++) {
            if (object.containsKey(need.get(i))) {
                data.add(object.getString(need.get(i)));
            } else if (defaultValue.get(i) != null) {
                data.add(defaultValue.get(i));
            } else {
                throw new NullPointerException();
            }
        }
    }

    protected Object getManagerObject(Class<?> managerClass) throws NoSuchMethodException, InstantiationException, IllegalAccessException, java.lang.reflect.InvocationTargetException {
        Class[] paramTypes = {RequestSolver.class};
        Object[] param = {this.requestSolver};
        Constructor<?> constructor = managerClass.getConstructor(paramTypes);
        return constructor.newInstance(param);
    }

    protected Class<?> getManagerClass() throws ClassNotFoundException {
        String managerName = this.postInfo.getManager();
        return Class.forName(managerName);
    }
}
