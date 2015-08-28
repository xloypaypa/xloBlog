package net.post.blog;

import control.BlogManager;
import net.sf.json.JSONObject;
import net.tool.LengthLimitReadServerSolver;

/**
 * Created by xlo on 2015/8/28.
 * it's the solver of get document
 */
public class GetDocumentReader extends LengthLimitReadServerSolver {
    @Override
    public void solveMessage() {
        JSONObject jsonObject = JSONObject.fromObject(this.message);
        String id = jsonObject.getString("id");
        BlogManager blogManager = new BlogManager(requestSolver);
        blogManager.getDocument(id);
    }
}
