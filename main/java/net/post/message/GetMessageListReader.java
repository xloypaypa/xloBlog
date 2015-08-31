package net.post.message;

import control.MessageManager;
import net.tool.LengthLimitReadServerSolver;

/**
 * Created by xlo on 2015/8/31.
 * it's the solver for a user get his message list
 */
public class GetMessageListReader extends LengthLimitReadServerSolver {
    @Override
    public void solveMessage() {
        String username = this.requestSolver.getRequestHeadReader().getMessage("Username");
        String password = this.requestSolver.getRequestHeadReader().getMessage("Password");

        MessageManager messageManager = new MessageManager(requestSolver);
        messageManager.getAllMessage(username, password);
    }
}
