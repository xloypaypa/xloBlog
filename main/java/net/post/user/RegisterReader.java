package net.post.user;

import control.UserAccessManager;
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

        UserAccessManager userAccessManager = new UserAccessManager(this.requestSolver);
        userAccessManager.register(username, password);
    }
}
