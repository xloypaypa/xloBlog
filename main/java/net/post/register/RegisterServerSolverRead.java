package net.post.register;

import net.sf.json.JSONObject;
import net.tool.ReadServerSolver;
import tool.connection.event.ConnectionEvent;
import tool.connection.event.ConnectionEventManager;
import tool.ioAble.NormalStringIO;
import tool.streamConnector.NormalStreamConnector;
import tool.streamConnector.StreamConnector;
import tool.streamConnector.io.LengthLimitStreamIONode;
import tool.streamConnector.io.StreamIONode;

/**
 * Created by xlo on 2015/8/21.
 * it's register server solver's reader
 */
public class RegisterServerSolverRead extends ReadServerSolver {
    protected String username, password;
    protected NormalStringIO stringIO;
    protected long length;

    public RegisterServerSolverRead() {
        ConnectionEventManager.getConnectionEventManager().addEventHandlerToItem(ConnectionEvent.connectEnd, this,
                (event, solver) -> {
                    if (stringIO != null) stringIO.close();
                });
        this.setWriterBuilder(() -> new RegisterServerSolverWrite(requestSolver, username, password));
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

        StreamIONode ioNode = new LengthLimitStreamIONode(length);
        ioNode.setInputStream(this.requestSolver.getSocketIoBuilder().getInputStream());
        ioNode.addOutputStream(this.stringIO.getOutputStream());
        connector.addMember(ioNode);

        connector.connect();

        JSONObject jsonObject = JSONObject.fromObject(this.stringIO.getValue());
        this.username = jsonObject.getString("username");
        this.password = jsonObject.getString("password");
    }
}
