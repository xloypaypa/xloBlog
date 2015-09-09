package net.post.image;

import model.db.ImageCollection;
import net.tool.WriteServerSolver;
import server.serverSolver.RequestSolver;
import tool.head.writer.CustomReplyHeadWriter;

import java.io.IOException;

/**
 * Created by xlo on 2015/9/8.
 * it's the solver send image
 */
public class SendImage extends WriteServerSolver {
    protected String id;
    protected ImageCollection imageCollection;

    public SendImage(RequestSolver requestSolver, String id) {
        super(requestSolver);
        imageCollection = new ImageCollection();
        this.id = id;
    }

    @Override
    protected boolean checkRequestExist() {
        return true;
    }

    @Override
    protected boolean checkRequestVisitable() {
        return true;
    }

    @Override
    protected boolean sendingHead() {
        CustomReplyHeadWriter customReplyHeadWriter = this.requestSolver.getReplyHeadWriter();
        customReplyHeadWriter.setReply(200);
        customReplyHeadWriter.setVersion("HTTP/1.1");
        customReplyHeadWriter.setMessage("ok");
        customReplyHeadWriter.addMessage("Content-Length", "" + imageCollection.getLength(id));
        return customReplyHeadWriter.sendHead();
    }

    @Override
    protected boolean afterSendHead() {
        return true;
    }

    @Override
    public void connect() {
        try {
            imageCollection.getImage(id, requestSolver.getSocketIoBuilder().getOutputStream());
        } catch (IOException e) {
            closeSocket();
        }
    }
}
