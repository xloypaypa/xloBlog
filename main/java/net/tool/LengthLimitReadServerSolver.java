package net.tool;

import log.LogManager;
import tool.connection.event.ConnectionEvent;
import tool.connection.event.ConnectionEventManager;
import tool.ioAble.NormalStringIO;
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
    protected NormalStringIO stringIO;
    protected long length;

    public LengthLimitReadServerSolver() {
        ConnectionEventManager.getConnectionEventManager().addEventHandlerToItem(ConnectionEvent.connectEnd, this,
                (event, solver) -> {
                    solveMessage();
                    if (stringIO != null) stringIO.close();
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
        this.stringIO = new NormalStringIO();
        this.stringIO.setInitValue("");
        return this.stringIO.buildIO();
    }

    @Override
    public void connect() {
        StreamConnector connector = new NormalStreamConnector();

        if (length > 0) {
            StreamIONode ioNode = new LengthLimitStreamIONode(length);
            ioNode.setInputStream(this.requestSolver.getSocketIoBuilder().getInputStream());
            ioNode.addOutputStream(this.stringIO.getOutputStream());
            connector.addMember(ioNode);

            connector.connect();

            this.message = this.stringIO.getValue();
        } else {
            this.message = "";
        }

        LogManager.getLogManager().writeLog("blog read", this.message);
    }

    public abstract void solveMessage();
}
