package net.post.image;

import control.ImageManager;
import net.sf.json.JSONObject;
import net.tool.LengthLimitReadServerSolver;

/**
 * Created by xlo on 2015/9/8.
 * it's the reader of get image
 */
public class GetImageReader extends LengthLimitReadServerSolver {
    @Override
    public void solveMessage() {
        JSONObject jsonObject = JSONObject.fromObject(this.message);
        String id = jsonObject.getString("id");
        ImageManager imageManager = new ImageManager(requestSolver);
        imageManager.getImage(id);
    }
}
