package net;

import server.serverSolver.RequestSolver;
import tool.head.writer.CustomReplyHeadWriter;
import tool.streamConnector.NormalStreamConnector;
import tool.streamConnector.StreamConnector;
import tool.streamConnector.io.NormalStreamIONode;
import tool.streamConnector.io.StreamIONode;

/**
 * Created by xlo 2015/8/19.
 * every normal server solver in blog should extends it
 */
public interface ShowResourcePage {
    static boolean checkResource(String path) {
        return ShowResourcePage.class.getResource(path) != null;
    }

    static void send404(boolean html, RequestSolver requestSolver) {
        CustomReplyHeadWriter replyHeadWriter = requestSolver.getReplyHeadWriter();
        replyHeadWriter.setReply(404);
        replyHeadWriter.setMessage("not found");
        replyHeadWriter.setVersion("HTTP/1.1");
        if (!html) {
            replyHeadWriter.sendHead();
        } else if (replyHeadWriter.sendHead()) {
            replyHeadWriter.addMessage("Content-Type", "text/html;charset=utf-8");
            StreamConnector streamConnector = new NormalStreamConnector();
            StreamIONode streamIONode = new NormalStreamIONode();
            streamIONode.setInputStream(ShowResourcePage.class.getResourceAsStream("/NotFoundPage.html"));
            streamIONode.addOutputStream(requestSolver.getSocketIoBuilder().getOutputStream());
            streamConnector.addMember(streamIONode);
            streamConnector.connect();
        }
    }

    static void send403(boolean html, RequestSolver requestSolver) {
        CustomReplyHeadWriter replyHeadWriter = requestSolver.getReplyHeadWriter();
        replyHeadWriter.setReply(403);
        replyHeadWriter.setMessage("You don't have purview for this file");
        replyHeadWriter.setVersion("HTTP/1.1");
        if (!html) {
            replyHeadWriter.sendHead();
        } else if (replyHeadWriter.sendHead()) {
            replyHeadWriter.addMessage("Content-Type", "text/html;charset=utf-8");
            StreamConnector streamConnector = new NormalStreamConnector();
            StreamIONode streamIONode = new NormalStreamIONode();
            streamIONode.setInputStream(ShowResourcePage.class.getResourceAsStream("/ForbiddenPage.html"));
            streamIONode.addOutputStream(requestSolver.getSocketIoBuilder().getOutputStream());
            streamConnector.addMember(streamIONode);
            streamConnector.connect();
        }
    }
}
