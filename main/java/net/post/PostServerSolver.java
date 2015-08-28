package net.post;

import config.ConfigManager;
import config.PostConfig;
import net.AimSolverChooser;
import net.tool.LengthLimitReadServerSolver;
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
    protected static PostConfig postConfig = (PostConfig) ConfigManager.getConfigManager().getConfig(PostConfig.class);

    public PostServerSolver() {
        for (PostConfig.PostInfo postInfo : postConfig.getPostInfo()) {
            chooserMap.put(postInfo.getName(), new AimSolverChooser() {
                @Override
                public boolean isThisSolver(RequestSolver requestSolver) {
                    return requestSolver.getRequestHeadReader().getUrl().getFile().equals(postInfo.getUrl());
                }

                @Override
                public AbstractServerSolver getSolver(RequestSolver requestSolver) {
                    try {
                        LengthLimitReadServerSolver lengthLimitReadServerSolver =
                                (LengthLimitReadServerSolver) Class.forName(postInfo.getClassName()).newInstance();
                        return lengthLimitReadServerSolver.setRequestSolver(requestSolver);
                    } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            });
        }
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
