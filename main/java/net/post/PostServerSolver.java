package net.post;

import net.AimSolverChooser;
import net.post.user.LoginServerSolverReader;
import net.post.user.RegisterServerSolverRead;
import server.serverSolver.RequestSolver;
import server.serverSolver.normalServer.AbstractServerSolver;
import server.serverSolver.normalServer.DynamicServerSolver;
import tool.connection.event.ConnectionEventManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xlo on 2015/8/20.
 * it's the server solver solve the command post
 */
public class PostServerSolver extends DynamicServerSolver {
    public static Map<String, AimSolverChooser> chooserMap = new HashMap<>();

    public PostServerSolver() {
        chooserMap.put("login", new AimSolverChooser() {
            @Override
            public boolean isThisSolver(RequestSolver requestSolver) {
                return requestSolver.getRequestHeadReader().getUrl().getFile().equals("login");
            }

            @Override
            public AbstractServerSolver getSolver(RequestSolver requestSolver) {
                return new LoginServerSolverReader() {
                    public LoginServerSolverReader set(RequestSolver requestSolver) {
                        this.requestSolver = requestSolver;
                        return this;
                    }
                }.set(requestSolver);
            }
        });

        chooserMap.put("register", new AimSolverChooser() {
            @Override
            public boolean isThisSolver(RequestSolver requestSolver) {
                return requestSolver.getRequestHeadReader().getUrl().getFile().equals("register");
            }

            @Override
            public AbstractServerSolver getSolver(RequestSolver requestSolver) {
                return new RegisterServerSolverRead() {
                    public RegisterServerSolverRead set(RequestSolver requestSolver) {
                        this.requestSolver = requestSolver;
                        return this;
                    }
                }.set(requestSolver);
            }
        });
    }

    @Override
    public void buildAimSolver(RequestSolver requestSolver) {
        for (Map.Entry<String, AimSolverChooser> now : chooserMap.entrySet()) {
            if (now.getValue().isThisSolver(this.requestSolver)) {
                this.aimSolver = now.getValue().getSolver(this.requestSolver);
                break;
            }
        }
        ConnectionEventManager.getConnectionEventManager().proxyItem(this, this.aimSolver);
    }
}
