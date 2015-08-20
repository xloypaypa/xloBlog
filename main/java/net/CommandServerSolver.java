package net;

import net.get.GetServerSolver;
import net.post.PostServerSolver;
import server.serverSolver.RequestSolver;
import server.serverSolver.normalServer.DynamicServerSolver;

/**
 * Created by xlo on 2015/8/20.
 * it's check the command is get or post
 */
public class CommandServerSolver extends DynamicServerSolver {
    @Override
    public void buildAimSolver(RequestSolver requestSolver) {
        if (requestSolver.getRequestHeadReader().getCommand().equals("GET")) {
            GetServerSolver getServerSolver = new GetServerSolver() {
                public GetServerSolver set(RequestSolver requestSolver) {
                    this.requestSolver = requestSolver;
                    return this;
                }
            }.set(requestSolver);
            getServerSolver.buildAimSolver(requestSolver);
            this.aimSolver = getServerSolver;
        } else {
            PostServerSolver postServerSolver = new PostServerSolver() {
                public PostServerSolver set(RequestSolver requestSolver) {
                    this.requestSolver = requestSolver;
                    return this;
                }
            }.set(requestSolver);
            postServerSolver.buildAimSolver(requestSolver);
            this.aimSolver = postServerSolver;
        }
    }
}
