package control;

import config.AccessConfig;
import config.ConfigManager;
import config.ReturnCodeConfig;
import server.serverSolver.RequestSolver;

/**
 * Created by xlo on 2015/8/28.
 * it's the abstract manager
 */
public abstract class Manager {
    protected RequestSolver requestSolver;
    protected static ReturnCodeConfig returnCodeConfig = (ReturnCodeConfig) ConfigManager.getConfigManager().getConfig(ReturnCodeConfig.class);
    protected static AccessConfig accessConfig = (AccessConfig) ConfigManager.getConfigManager().getConfig(AccessConfig.class);

    public Manager(RequestSolver requestSolver) {
        this.requestSolver = requestSolver;
    }
}
