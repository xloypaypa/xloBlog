package net.post;

import config.PostConfig;
import net.sf.json.JSONObject;
import net.tool.LengthLimitReadServerSolver;
import server.serverSolver.RequestSolver;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

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
        List<String> data = getData();
        Method method = getMethod(managerClass, data);
        method.invoke(managerObject, data.toArray());
    }

    protected Method getMethod(Class<?> managerClass, List<String> data) throws NoSuchMethodException {
        String methodName = this.postInfo.getMethod();
        Class[] methodParamType = new Class[data.size()];
        for (int i = 0; i < data.size(); i++) {
            methodParamType[i] = String.class;
        }
        return managerClass.getMethod(methodName, methodParamType);
    }

    protected List<String> getData() {
        List<String> data = new LinkedList<>();
        if (this.postInfo.getAccess()) {
            String username = this.requestSolver.getRequestHeadReader().getMessage("Username");
            String password = this.requestSolver.getRequestHeadReader().getMessage("Password");
            data.add(username);
            data.add(password);
        }
        List<String> need = this.postInfo.getMethodData();
        if (need.size() != 0) {
            JSONObject object = JSONObject.fromObject(this.message);
            data.addAll(need.stream().map(object::getString).collect(Collectors.toList()));
        }
        return data;
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
