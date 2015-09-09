package net.post.mark;

import control.MarkManager;
import net.sf.json.JSONObject;
import net.tool.LengthLimitReadServerSolver;

/**
 * Created by xlo on 2015/9/9.
 * it's the reader for check is marked
 */
public class IsMarkedReader extends LengthLimitReadServerSolver {
    @Override
    public void solveMessage() {
        String username = this.requestSolver.getRequestHeadReader().getMessage("Username");
        String password = this.requestSolver.getRequestHeadReader().getMessage("Password");
        JSONObject jsonObject = JSONObject.fromObject(this.message);
        String aimUser = jsonObject.getString("aimUser");
        MarkManager markManager = new MarkManager(requestSolver);
        markManager.isMarked(username, password, aimUser);
    }
}
