package net.post.image;

import control.ImageManager;
import net.sf.json.JSONObject;
import net.tool.LengthLimitReadServerSolver;

/**
 * Created by xlo on 2015/9/8.
 * it's the add image reader
 */
public class AddImageReader extends LengthLimitReadServerSolver {
    @Override
    public void solveMessage() {
        String username = this.requestSolver.getRequestHeadReader().getMessage("Username");
        String password = this.requestSolver.getRequestHeadReader().getMessage("Password");
        JSONObject jsonObject = JSONObject.fromObject(this.message);
        String type = jsonObject.getString("type");
        ImageManager imageManager = new ImageManager(this.requestSolver);
        imageManager.checkAddImage(username, password, type);
    }
}
