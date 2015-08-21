package net.post.register;

import control.UserAccessManager;
import net.sf.json.JSONObject;
import net.tool.WriteServerSolver;
import server.serverSolver.RequestSolver;
import tool.head.writer.CustomReplyHeadWriter;
import tool.ioAble.NormalStringIO;
import tool.streamConnector.NormalStreamConnector;
import tool.streamConnector.StreamConnector;
import tool.streamConnector.io.NormalStreamIONode;
import tool.streamConnector.io.StreamIONode;

/**
 * Created by xlo on 2015/8/21.
 * * it's register server solver's writer
 */
public class RegisterServerSolverWrite extends WriteServerSolver {
    protected String username, password;
    private String message;

    public RegisterServerSolverWrite(RequestSolver requestSolver, Object... data) {
        super(requestSolver, data);
        this.username = (String) data[0];
        this.password = (String) data[1];
    }

    @Override
    protected boolean checkRequestExist() {
        return true;
    }

    @Override
    protected boolean checkRequestVisitable() {
        return true;
    }

    @Override
    protected boolean sendingHead() {
        buildReturnMessage();

        CustomReplyHeadWriter customReplyHeadWriter = this.requestSolver.getReplyHeadWriter();
        customReplyHeadWriter.setReply(200);
        customReplyHeadWriter.setVersion("HTTP/1.1");
        customReplyHeadWriter.setMessage("ok");
        customReplyHeadWriter.addMessage("Content-Length", "" + message.length());
        return customReplyHeadWriter.sendHead();
    }

    @Override
    protected boolean afterSendHead() {
        return true;
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

    private void buildReturnMessage() {
        JSONObject object = new JSONObject();
        UserAccessManager userAccessManager = UserAccessManager.getUserAccessManager();
        object.put("code", userAccessManager.register(this.username, this.password));
        this.message = object.toString();
    }
}
