package net.post.login;

import control.UserAccessManager;
import net.tool.ReadServerSolver;
import server.serverSolver.RequestSolver;

/**
 * Created by xlo on 2015/8/21.
 * it's the server solver check login message
 */
public class LoginServerSolverReader extends ReadServerSolver {
    protected RequestSolver requestSolver;
    private String message;
    protected long length;

    @Override
    protected boolean checkRequestExist() {
        return true;
    }

    @Override
    protected boolean checkRequestVisitable() {
        String message = this.requestSolver.getRequestHeadReader().getMessage("Content-Length");
        try {
            this.length = Long.valueOf(message);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    protected boolean sendingHead() {
        return true;
    }

    @Override
    protected boolean afterSendHead() {
        return true;
    }

    @Override
    public void connect() {
        UserAccessManager userAccessManager = new UserAccessManager(this.requestSolver);
        String username = this.requestSolver.getRequestHeadReader().getMessage("username");
        String password = this.requestSolver.getRequestHeadReader().getMessage("password");
        userAccessManager.loginUser(username, password);
    }

//    @Override
//    public boolean checkIP() {
//        return SafeModelManager.getSafeModelManager().accept("server-ip", this.socket.getInetAddress().getHostAddress());
//    }
//
//    @Override
//    public boolean readRequest() {
//        return this.requestSolver.getRequestHeadReader().readHead();
//    }
//
//    @Override
//    public boolean checkAccept() {
//        return true;
//    }
//
//    @Override
//    public boolean sendReply() {
//        buildReturnMessage();
//
//        CustomReplyHeadWriter customReplyHeadWriter = this.requestSolver.getReplyHeadWriter();
//        customReplyHeadWriter.setReply(200);
//        customReplyHeadWriter.setVersion("HTTP/1.1");
//        customReplyHeadWriter.setMessage("ok");
//        customReplyHeadWriter.addMessage("Content-Length", "" + message.length());
//        return customReplyHeadWriter.sendHead();
//    }
//
//    @Override
//    public void connect() {
//        StreamConnector connector = new NormalStreamConnector();
//        StreamIONode ioNode = new NormalStreamIONode();
//
//        NormalStringIO ioBuilder = new NormalStringIO();
//        ioBuilder.setInitValue(message);
//        if (!ioBuilder.buildIO()) return ;
//
//        ioNode.setInputStream(ioBuilder.getInputStream());
//        ioNode.addOutputStream(this.requestSolver.getSocketIoBuilder().getOutputStream());
//        connector.addMember(ioNode);
//        connector.connect();
//        ioBuilder.close();
//    }
//
//    @Override
//    public boolean buildIO() {
//        this.requestSolver = new NormalRequestSolver();
//        this.requestSolver.getSocketIoBuilder().setSocket(this.socket);
//        return this.requestSolver.getSocketIoBuilder().buildIO();
//    }
//
//    private void buildReturnMessage() {
//        String username, password;
//        username = this.requestSolver.getRequestHeadReader().getMessage("username");
//        password = this.requestSolver.getRequestHeadReader().getMessage("password");
//        UserAccessManager userAccessManager = UserAccessManager.getUserAccessManager();
//        ReturnCodeConfig returnCodeConfig = (ReturnCodeConfig) ConfigManager.getConfigManager().getConfig(ReturnCodeConfig.class);
//
//        JSONObject jsonObject = new JSONObject();
//        if (username == null || password == null) {
//            jsonObject.put("return", returnCodeConfig.getCode("error head"));
//        } else {
//            jsonObject.put("return", userAccessManager.loginUser(username, password));
//        }
//        this.message = jsonObject.toString();
//    }
}
