package net.tool;

import server.serverSolver.normalServer.NormalKeepAliveServerSolver;
import tool.connection.event.ConnectionEvent;
import tool.connection.event.ConnectionEventManager;

/**
 * Created by xlo on 2015/8/21.
 * it's the server solver read some message and invoke writer server solver
 */
public abstract class ReadServerSolver extends NormalKeepAliveServerSolver {
    protected WriterBuilder writerBuilder;

    @Override
    public void startNextServerSolver() {
        ConnectionEventManager.getConnectionEventManager().addEventHandlerToItem(ConnectionEvent.connectSuccess, this,
                (event, solver) -> this.writerBuilder.build().run());
        ConnectionEventManager.getConnectionEventManager().addEventHandlerToItem(ConnectionEvent.connectFail, this,
                (event, solver) -> closeSocket());
    }

    public void setWriterBuilder(WriterBuilder writerBuilder) {
        this.writerBuilder = writerBuilder;
    }
}
