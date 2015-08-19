package net;

import safeList.SafeModelManager;
import server.serverSolver.normalServer.NormalServerSolver;
import tool.connection.event.ConnectionEvent;
import tool.connection.event.ConnectionEventHandler;
import tool.connection.event.ConnectionEventManager;
import tool.connection.solver.ConnectionSolver;
import tool.head.writer.CustomReplyHeadWriter;
import tool.head.writer.ReplyHeadWriter;
import tool.ioAble.FileIOBuilder;
import tool.ioAble.NormalFileIO;
import tool.streamConnector.NormalStreamConnector;
import tool.streamConnector.StreamConnector;
import tool.streamConnector.io.NormalStreamIONode;
import tool.streamConnector.io.StreamIONode;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by xlo on 2015/8/19.
 * it's the server solver who show the file end with html, css, js and so on.
 */
public class ShowItemServerSolver extends NormalServerSolver {
    protected FileIOBuilder fileIOBuilder;
    protected File file;

    public ShowItemServerSolver() {
        ConnectionEventManager.getConnectionEventManager().addEventHandlerToItem(ConnectionEvent.connectEnd, this,
                (s, connectionSolver) -> closeSocket());
    }

    @Override
    protected boolean checkRequestExist() {
        try {
            String path = URLDecoder.decode(this.requestSolver.getRequestHeadReader().getUrl().getFile(), "UTF-8");
            this.file = new File("."+ path);
            if (!this.file.exists() || !this.file.isFile()) {
                send404();
                return false;
            }
            return true;
        } catch (IOException e) {
            send404();
            return false;
        }
    }

    @Override
    protected boolean checkRequestVisitable() {
        if (!SafeModelManager.getSafeModelManager().accept("file", this.file.getAbsolutePath())) {
            send403();
            return false;
        }
        return true;
    }

    @Override
    protected boolean sendingHead() {
        CustomReplyHeadWriter replyHeadWriter = this.requestSolver.getReplyHeadWriter();
        replyHeadWriter.setReply(200);
        replyHeadWriter.setMessage("OK");
        replyHeadWriter.setVersion("HTTP/1.1");
        replyHeadWriter.addMessage("Content-Length", "" + file.length());
        if (this.file.getName().endsWith(".html")) {
            replyHeadWriter.addMessage("Content-Type", "text/html;charset=utf-8");
        }
        return replyHeadWriter.sendHead();
    }

    @Override
    protected boolean afterSendHead() {
        this.fileIOBuilder = new NormalFileIO();
        this.fileIOBuilder.setFile(this.file.getAbsolutePath());
        return this.fileIOBuilder.buildIO();
    }

    public void connect() {
        StreamConnector streamConnector = new NormalStreamConnector();
        StreamIONode streamIONode = new NormalStreamIONode();
        streamIONode.setInputStream(this.fileIOBuilder.getInputStream());
        streamIONode.addOutputStream(this.requestSolver.getSocketIoBuilder().getOutputStream());
        streamConnector.addMember(streamIONode);
        streamConnector.connect();
    }

    private void send404() {
        boolean html = this.file.getName().endsWith("html");
        CustomReplyHeadWriter replyHeadWriter = this.requestSolver.getReplyHeadWriter();
        replyHeadWriter.setReply(404);
        replyHeadWriter.setMessage("not found");
        replyHeadWriter.setVersion("HTTP/1.1");
        if (html) {
            replyHeadWriter.addMessage("Content-Type", "text/html;charset=utf-8");
        }
        if (replyHeadWriter.sendHead() && html) {
            StreamConnector streamConnector = new NormalStreamConnector();
            StreamIONode streamIONode = new NormalStreamIONode();
            streamIONode.setInputStream(this.getClass().getResourceAsStream("/NotFoundPage.html"));
            streamIONode.addOutputStream(this.requestSolver.getSocketIoBuilder().getOutputStream());
            streamConnector.addMember(streamIONode);
            streamConnector.connect();
        }
    }

    private void send403() {
        boolean html = this.file.getName().endsWith("html");
        CustomReplyHeadWriter replyHeadWriter = this.requestSolver.getReplyHeadWriter();
        replyHeadWriter.setReply(403);
        replyHeadWriter.setMessage("You don't have purview for this file");
        replyHeadWriter.setVersion("HTTP/1.1");
        if (html) {
            replyHeadWriter.addMessage("Content-Type", "text/html;charset=utf-8");
        }
        if (replyHeadWriter.sendHead() && html) {
            StreamConnector streamConnector = new NormalStreamConnector();
            StreamIONode streamIONode = new NormalStreamIONode();
            streamIONode.setInputStream(this.getClass().getResourceAsStream("/ForbiddenPage.html"));
            streamIONode.addOutputStream(this.requestSolver.getSocketIoBuilder().getOutputStream());
            streamConnector.addMember(streamIONode);
            streamConnector.connect();
        }
    }
}
