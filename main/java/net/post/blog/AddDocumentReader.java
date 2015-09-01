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
        String username = this.requestSolver.getRequestHeadReader().getMessage("Username");
        String password = this.requestSolver.getRequestHeadReader().getMessage("Password");
        JSONObject jsonObject = JSONObject.fromObject(this.message);
        String title = jsonObject.getString("title");
        String body = jsonObject.getString("body");
        String type = "default";
        if (jsonObject.containsKey("type")) {
            type = jsonObject.getString("type");
        }

        BlogManager blogManager = new BlogManager(this.requestSolver);
        blogManager.addDocument(username, password, title, body, type);
    }
}
