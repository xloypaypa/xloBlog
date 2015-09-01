package net.post.user;

import control.UserManager;
import net.sf.json.JSONObject;
import net.tool.LengthLimitReadServerSolver;

/**
 * Created by xlo on 2015/8/21.
 * it's register server solver's reader
 */
public class RegisterReader extends LengthLimitReadServerSolver {
    @Override
    public void solveMessage() {
        String username, password;
        JSONObject jsonObject = JSONObject.fromObject(this.message);
        username = jsonObject.getString("username");
        password = jsonObject.getString("password");

        UserManager userManager = new UserManager(this.requestSolver);
        userManager.register(username, password);
    }
}
