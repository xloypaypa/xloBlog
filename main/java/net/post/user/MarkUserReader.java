package net.post.user;

import control.UserManager;
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
        UserManager userManager = new UserManager(requestSolver);
        userManager.markUser(username, password, aimUser);
    }
}
