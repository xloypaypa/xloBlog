package net.post.image;

import control.ImageManager;
import net.tool.LengthLimitReadServerSolver;

/**
 * Created by xlo on 2015/9/8.
 * it's the reader of image uploading
 */
public class UploadImageReader extends LengthLimitReadServerSolver {
    protected String type;

    public UploadImageReader(String type) {
        this.type = type;
    }

    @Override
    public boolean checkIP() {
        return true;
    }

    @Override
    public boolean buildIO() {
        return true;
    }

    @Override
    protected void trySolveMessage() {
        try {
            solveMessage();
        } catch (Exception e) {
            closeSocket();
        }
    }

    @Override
    public void solveMessage() {
        String username = this.requestSolver.getRequestHeadReader().getMessage("Username");
        String password = this.requestSolver.getRequestHeadReader().getMessage("Password");
        ImageManager imageManager = new ImageManager(requestSolver);
        imageManager.addImage(username, password, type, this.originalMessage);
    }
}
