package net.post.user;

import control.UserManager;
import net.tool.LengthLimitReadServerSolver;

/**
 * Created by xlo on 2015/8/21.
 * it's the server solver check login message
 */
public class LoginReader extends LengthLimitReadServerSolver {
    @Override
    public void solveMessage() {
        UserManager userManager = new UserManager(this.requestSolver);
        String username = this.requestSolver.getRequestHeadReader().getMessage("Username");
        String password = this.requestSolver.getRequestHeadReader().getMessage("Password");
        userManager.loginUser(username, password);
    }
}
