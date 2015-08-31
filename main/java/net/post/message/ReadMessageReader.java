package net.post.message;

import control.MessageManager;
import net.sf.json.JSONObject;
import net.tool.LengthLimitReadServerSolver;

/**
 * Created by xlo on 2015/8/31.
 * it's the solver change the read flag of message
 */
public class ReadMessageReader extends LengthLimitReadServerSolver {
    @Override
    public void solveMessage() {
        String username = this.requestSolver.getRequestHeadReader().getMessage("username");
        String password = this.requestSolver.getRequestHeadReader().getMessage("password");
        JSONObject jsonObject = JSONObject.fromObject(this.message);
        String id = jsonObject.getString("id");

        MessageManager messageManager = new MessageManager(requestSolver);
        messageManager.readMessage(username, password, id);
    }
}
