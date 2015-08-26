package net;

import server.serverSolver.RequestSolver;
import server.serverSolver.normalServer.AbstractServerSolver;

/**
 * Created by xlo on 2015/8/26.
 * it's the chooser of {@code DynamicServerSolver} to choose aim server solver
 */
public interface AimSolverChooser {
    boolean isThisSolver(RequestSolver requestSolver);

    AbstractServerSolver getSolver(RequestSolver requestSolver);
}
