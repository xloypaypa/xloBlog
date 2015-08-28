package net.post.blog;

import control.BlogManager;
import net.sf.json.JSONObject;
import net.tool.LengthLimitReadServerSolver;

/**
 * Created by xlo on 2015/8/28.
 * it's the solver of add blog
 */
public class AddDocumentReader extends LengthLimitReadServerSolver {
    @Override
    public void solveMessage() {
        String username = this.requestSolver.getRequestHeadReader().getMessage("username");
        String password = this.requestSolver.getRequestHeadReader().getMessage("password");
        JSONObject jsonObject = JSONObject.fromObject(this.message);
        String title = jsonObject.getString("title");
        String body = jsonObject.getString("body");

        BlogManager blogManager = new BlogManager(this.requestSolver);
        blogManager.addBlog(username, password, title, body);
    }
}
