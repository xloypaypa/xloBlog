package net.tool;

import log.LogManager;
import server.serverSolver.RequestSolver;
import tool.connection.event.ConnectionEvent;
import tool.connection.event.ConnectionEventManager;
import tool.ioAble.NormalByteIO;
import tool.streamConnector.NormalStreamConnector;
import tool.streamConnector.StreamConnector;
import tool.streamConnector.io.LengthLimitStreamIONode;
import tool.streamConnector.io.StreamIONode;

/**
 * Created by xlo on 2015/8/24.
 * it's length limit read server solver
 */
public abstract class LengthLimitReadServerSolver extends ReadServerSolver {
    protected String message;
    protected byte[] originalMessage;
    protected NormalByteIO byteIO;
    protected long length;

    public LengthLimitReadServerSolver() {
        ConnectionEventManager.getConnectionEventManager().addEventHandlerToItem(ConnectionEvent.connectEnd, this,
                (event, solver) -> {
                    trySolveMessage();
                    if (byteIO != null) byteIO.close();
                });
    }

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
        this.byteIO = new NormalByteIO();
        this.byteIO.setInitValue(new byte[0]);
        return this.byteIO.buildIO();
    }

    @Override
    public void connect() {
        StreamConnector connector = new NormalStreamConnector();

        if (length > 0) {
            StreamIONode ioNode = new LengthLimitStreamIONode(length);
            ioNode.setInputStream(this.requestSolver.getSocketIoBuilder().getInputStream());
            ioNode.addOutputStream(this.byteIO.getOutputStream());
            connector.addMember(ioNode);

            connector.connect();

            this.originalMessage = this.byteIO.getValue();
        } else {
            this.originalMessage = new byte[0];
        }

        LogManager.getLogManager().writeLog("blog read", this.message);
    }

    public LengthLimitReadServerSolver setRequestSolver(RequestSolver requestSolver) {
        this.requestSolver = requestSolver;
        return this;
    }

    protected void trySolveMessage() {
        try {
            this.message = new String(this.originalMessage);
            solveMessage();
        } catch (Exception e) {
            closeSocket();
        }
    }

    public abstract void solveMessage();
}
