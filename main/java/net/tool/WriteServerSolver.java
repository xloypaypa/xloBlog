package net.tool;

import server.serverSolver.RequestSolver;
import server.serverSolver.normalServer.NormalKeepAliveServerSolver;
import tool.connection.event.ConnectionEvent;
import tool.connection.event.ConnectionEventManager;

/**
 * Created by xlo on 2015/8/21.
 * it's the writer server solver
 */
public abstract class WriteServerSolver extends NormalKeepAliveServerSolver {
    protected Object[] data;

    public WriteServerSolver(RequestSolver requestSolver, Object... data) {
        this.data = data;
        this.requestSolver = requestSolver;
        this.firstConnect = false;
    }

    @Override
    public boolean readRequest() {
        return true;
    }

    @Override
    public void startNextServerSolver() {
        ConnectionEventManager.getConnectionEventManager().addEventHandlerToItem(ConnectionEvent.connectEnd, this,
                (event, solver) -> closeSocket());
    }
}
