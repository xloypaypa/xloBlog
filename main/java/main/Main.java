package main;

import net.CommandServerSolver;
import server.Server;
import tool.connection.event.ConnectionEvent;
import tool.connection.event.ConnectionEventManager;

import javax.swing.*;

/**
 * Created by xlo on 2015/8/19.
 * it's the main class
 */
public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("blog");
        frame.setBounds(100, 100, 300, 300);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(null);
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(0, 0, 200, 200);
        frame.add(panel);

        JButton jButton = new JButton("start");
        jButton.setBounds(0, 0, 200, 200);
        jButton.addActionListener(e -> {
            panel.remove(jButton);
            panel.repaint();
            new Thread() {
                @Override
                public void run() {
                    Server server = Server.getNewServer();
                    server.setSolverBuilder(CommandServerSolver::new);
                    server.getInstance(8000);
                    server.accept();
                    System.out.println("end");
                }
            }.start();
        });
        panel.add(jButton);
        frame.setVisible(true);

        ConnectionEventManager.getConnectionEventManager().addEventHandler(ConnectionEvent.connectFail, (event, solver) -> System.out.println(event));

        Server server = Server.getNewServer();
        server.setSolverBuilder(CommandServerSolver::new);
        server.getInstance(8001);
        server.accept();
    }
}
