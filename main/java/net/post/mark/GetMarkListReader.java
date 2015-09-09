package net.post.mark;

import control.MarkManager;
import net.tool.LengthLimitReadServerSolver;

/**
 * Created by xlo on 2015/9/9.
 * it's the reader for getting mark list
 */
public class GetMarkListReader extends LengthLimitReadServerSolver {
    @Override
    public void solveMessage() {
        String username = this.requestSolver.getRequestHeadReader().getMessage("Username");
        String password = this.requestSolver.getRequestHeadReader().getMessage("Password");
        MarkManager markManager = new MarkManager(requestSolver);
        markManager.getMarkedList(username, password);
    }
}
