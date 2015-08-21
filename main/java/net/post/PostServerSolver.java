package net.post;

import server.serverSolver.RequestSolver;
import server.serverSolver.normalServer.DynamicServerSolver;

/**
 * Created by xlo on 2015/8/20.
 * it's the server solver solve the command post
 */
public class PostServerSolver extends DynamicServerSolver {
    @Override
    public void buildAimSolver(RequestSolver requestSolver) {
        if (this.requestSolver.getRequestHeadReader().getUrl().getFile().equals("login")) {
            this.aimSolver = new LoginServerSolver() {
                public LoginServerSolver set(RequestSolver requestSolver) {
                    this.requestSolver = requestSolver;
                    return this;
                }
            }.set(requestSolver);
        }
    }
}
