package net.post.login;

import config.ConfigManager;
import config.ReturnCodeConfig;
import control.UserAccessManager;
import net.sf.json.JSONObject;
import safeList.SafeModelManager;
import server.serverSolver.NormalRequestSolver;
import server.serverSolver.RequestSolver;
import server.serverSolver.normalServer.AbstractServerSolver;
import tool.connection.event.ConnectionEvent;
import tool.connection.event.ConnectionEventManager;
import tool.head.writer.CustomReplyHeadWriter;
import tool.ioAble.NormalStringIO;
import tool.streamConnector.NormalStreamConnector;
import tool.streamConnector.StreamConnector;
import tool.streamConnector.io.NormalStreamIONode;
import tool.streamConnector.io.StreamIONode;

/**
 * Created by xlo on 2015/8/21.
 * it's the server solver check login message
 */
public class LoginServerSolver extends AbstractServerSolver {
    protected RequestSolver requestSolver;
    private String message;

    public LoginServerSolver() {
        ConnectionEventManager.getConnectionEventManager().addEventHandlerToItem(ConnectionEvent.connectEnd, this,
                (event, solver) -> closeSocket());
    }

    @Override
    public boolean checkIP() {
        return SafeModelManager.getSafeModelManager().accept("server-ip", this.socket.getInetAddress().getHostAddress());
    }

    @Override
    public boolean readRequest() {
        return this.requestSolver.getRequestHeadReader().readHead();
    }

    @Override
    public boolean checkAccept() {
        return true;
    }

    @Override
    public boolean sendReply() {
        buildReturnMessage();

        CustomReplyHeadWriter customReplyHeadWriter = this.requestSolver.getReplyHeadWriter();
        customReplyHeadWriter.setReply(200);
        customReplyHeadWriter.setVersion("HTTP/1.1");
        customReplyHeadWriter.setMessage("ok");
        customReplyHeadWriter.addMessage("Content-Length", "" + message.length());
        return customReplyHeadWriter.sendHead();
    }

    @Override
    public void connect() {
        StreamConnector connector = new NormalStreamConnector();
        StreamIONode ioNode = new NormalStreamIONode();

        NormalStringIO ioBuilder = new NormalStringIO();
        ioBuilder.setInitValue(message);
        if (!ioBuilder.buildIO()) return ;

        ioNode.setInputStream(ioBuilder.getInputStream());
        ioNode.addOutputStream(this.requestSolver.getSocketIoBuilder().getOutputStream());
        connector.addMember(ioNode);
        connector.connect();
        ioBuilder.close();
    }

    @Override
    public boolean buildIO() {
        this.requestSolver = new NormalRequestSolver();
        this.requestSolver.getSocketIoBuilder().setSocket(this.socket);
        return this.requestSolver.getSocketIoBuilder().buildIO();
    }

    private void buildReturnMessage() {
        String username, password;
        username = this.requestSolver.getRequestHeadReader().getMessage("username");
        password = this.requestSolver.getRequestHeadReader().getMessage("password");
        UserAccessManager userAccessManager = UserAccessManager.getUserAccessManager();
        ReturnCodeConfig returnCodeConfig = (ReturnCodeConfig) ConfigManager.getConfigManager().getConfig(ReturnCodeConfig.class);

        JSONObject jsonObject = new JSONObject();
        if (username == null || password == null) {
            jsonObject.put("return", returnCodeConfig.getCode("error head"));
        } else {
            jsonObject.put("return", userAccessManager.checkUser(username, password));
        }
        this.message = jsonObject.toString();
    }
}
