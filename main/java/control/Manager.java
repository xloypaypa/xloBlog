package control;

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

    public Manager(RequestSolver requestSolver) {
        this.requestSolver = requestSolver;
    }
}
