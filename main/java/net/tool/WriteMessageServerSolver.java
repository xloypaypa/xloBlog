package net.tool;

import log.LogManager;
import server.serverSolver.RequestSolver;
import tool.head.writer.CustomReplyHeadWriter;
import tool.ioAble.NormalStringIO;
import tool.streamConnector.NormalStreamConnector;
import tool.streamConnector.StreamConnector;
import tool.streamConnector.io.NormalStreamIONode;
import tool.streamConnector.io.StreamIONode;

/**
 * Created by xlo on 2015/8/24.
 * it's the server solver who write message to client
 */
public class WriteMessageServerSolver extends WriteServerSolver {
    protected String message;

    public WriteMessageServerSolver(RequestSolver requestSolver, Object... data) {
        super(requestSolver, data);
        this.message = (String) data[0];
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

        LogManager.getLogManager().writeLog("blog write", this.message);
    }
}
