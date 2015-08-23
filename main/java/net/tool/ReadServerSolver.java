package net.tool;

import server.serverSolver.normalServer.NormalKeepAliveServerSolver;
import tool.connection.event.ConnectionEvent;
import tool.connection.event.ConnectionEventManager;

/**
 * Created by xlo on 2015/8/21.
 * it's the server solver read some message and invoke writer server solver
 */
public abstract class ReadServerSolver extends NormalKeepAliveServerSolver {

    @Override
    public void startNextServerSolver() {
    }
}
