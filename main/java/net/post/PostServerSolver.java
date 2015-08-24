package net.post;

import net.post.user.LoginServerSolverReader;
import net.post.user.RegisterServerSolverRead;
import server.serverSolver.RequestSolver;
import server.serverSolver.normalServer.DynamicServerSolver;
import tool.connection.event.ConnectionEvent;
import tool.connection.event.ConnectionEventManager;

/**
 * Created by xlo on 2015/8/20.
 * it's the server solver solve the command post
 */
public class PostServerSolver extends DynamicServerSolver {
    @Override
    public void buildAimSolver(RequestSolver requestSolver) {
        if (this.requestSolver.getRequestHeadReader().getUrl().getFile().equals("login")) {
            this.aimSolver = new LoginServerSolverReader() {
                public LoginServerSolverReader set(RequestSolver requestSolver) {
                    this.requestSolver = requestSolver;
                    return this;
                }
            }.set(requestSolver);
        } else if (this.requestSolver.getRequestHeadReader().getUrl().getFile().equals("register")) {
            this.aimSolver = new RegisterServerSolverRead() {
                public RegisterServerSolverRead set(RequestSolver requestSolver) {
                    this.requestSolver = requestSolver;
                    return this;
                }
            }.set(requestSolver);
        }

        ConnectionEventManager.getConnectionEventManager().addEventHandlerToItem(ConnectionEvent.connectEnd, this,
                (event, solver) ->ConnectionEventManager.getConnectionEventManager()
                        .invoke(ConnectionEvent.connectEnd, aimSolver));
    }
}
