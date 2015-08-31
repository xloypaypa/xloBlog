package net.post.message;

import control.MessageManager;
import net.sf.json.JSONObject;
import net.tool.LengthLimitReadServerSolver;

/**
 * Created by xlo on 2015/8/31.
 * it's the solver for a user send message to anther user
 */
public class SendMessageReader extends LengthLimitReadServerSolver {
    @Override
    public void solveMessage() {
        String username = this.requestSolver.getRequestHeadReader().getMessage("username");
        String password = this.requestSolver.getRequestHeadReader().getMessage("password");
        JSONObject jsonObject = JSONObject.fromObject(this.message);
        String aim = jsonObject.getString("aim");
        String message = jsonObject.getString("message");

        MessageManager messageManager = new MessageManager(requestSolver);
        messageManager.addMessage(username, password, message, aim);
    }
}
