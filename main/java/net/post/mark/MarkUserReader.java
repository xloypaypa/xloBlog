package net.post.mark;

import control.MarkManager;
import net.sf.json.JSONObject;
import net.tool.LengthLimitReadServerSolver;

/**
 * Created by xlo on 2015/9/1.
 * it's the solver of mark user
 */
public class MarkUserReader extends LengthLimitReadServerSolver {
    @Override
    public void solveMessage() {
        String username = this.requestSolver.getRequestHeadReader().getMessage("Username");
        String password = this.requestSolver.getRequestHeadReader().getMessage("Password");
        JSONObject jsonObject = JSONObject.fromObject(this.message);
        String aimUser = jsonObject.getString("aimUser");
        MarkManager markManager = new MarkManager(requestSolver);
        markManager.markUser(username, password, aimUser);
    }
}
