package main;

import model.log.LogManager;
import model.log.NormalLog;
import model.script.ForceCacheScriptManager;
import model.values.SystemStrings;
import net.CommandServerSolver;
import net.get.DBImageSolver;
import net.server.Server;
import net.server.serverSolver.SolverBuilder;
import net.tool.connection.event.ConnectionEvent;
import net.tool.connection.event.ConnectionEventManager;

import java.io.File;
import java.util.concurrent.Executors;

/**
 * Created by xlo on 2015/8/19.
 * it's the main class
 */
public class Main {
    public static void main(String[] args) throws Exception {
        ForceCacheScriptManager.getForceCacheScriptManager();

        NormalLog normalLog = new NormalLog();
        File file = new File("./log.txt");
        normalLog.setPath(file.getAbsolutePath());
        LogManager.getLogManager().putLog(SystemStrings.readHead, normalLog);
        LogManager.getLogManager().putLog("blog read", normalLog);
        LogManager.getLogManager().putLog("blog write", normalLog);


        startServer(Integer.parseInt(args[0]), CommandServerSolver::new, Integer.parseInt(args[2]));
        startServer(Integer.parseInt(args[1]), DBImageSolver::new, Integer.parseInt(args[3]));

        ConnectionEventManager.getConnectionEventManager().addEventHandler(ConnectionEvent.connectFail, (event, solver) -> System.out.println(event));
    }

    private static void startServer(int port, SolverBuilder solverBuilder, int threadNum) {
        new Thread() {
            @Override
            public void run() {
                Server server = Server.getNewServer();
                server.changeThreadPool(Executors.newFixedThreadPool(threadNum));
                server.setSolverBuilder(solverBuilder);
                server.getInstance(port);
                server.accept();
            }
        }.start();
    }
}
