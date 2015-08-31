package net.post.blog;

import control.BlogManager;
import net.sf.json.JSONObject;
import net.tool.LengthLimitReadServerSolver;

/**
 * Created by xlo on 2015/8/31.
 * it's the reader for reply document
 */
public class AddReplyReader extends LengthLimitReadServerSolver {
    @Override
    public void solveMessage() {
        String username = this.requestSolver.getRequestHeadReader().getMessage("Username");
        String password = this.requestSolver.getRequestHeadReader().getMessage("Password");
        JSONObject jsonObject = JSONObject.fromObject(this.message);
        String id = jsonObject.getString("id");
        String reply = jsonObject.getString("reply");

        BlogManager blogManager = new BlogManager(this.requestSolver);
        blogManager.addReply(username, password, id, reply);
    }
}
