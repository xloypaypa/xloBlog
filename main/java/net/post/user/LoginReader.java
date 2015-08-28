package net.post.user;

import control.UserAccessManager;
import net.tool.LengthLimitReadServerSolver;

/**
 * Created by xlo on 2015/8/21.
 * it's the server solver check login message
 */
public class LoginReader extends LengthLimitReadServerSolver {
    @Override
    public void solveMessage() {
        UserAccessManager userAccessManager = new UserAccessManager(this.requestSolver);
        String username = this.requestSolver.getRequestHeadReader().getMessage("username");
        String password = this.requestSolver.getRequestHeadReader().getMessage("password");
        userAccessManager.loginUser(username, password);
    }
}
