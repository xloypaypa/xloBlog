package net.post.blog;

import control.BlogManager;
import net.sf.json.JSONObject;
import net.tool.LengthLimitReadServerSolver;

/**
 * Created by xlo on 2015/9/1.
 * it's the solver of add reader
 */
public class AddReaderReader extends LengthLimitReadServerSolver {
    @Override
    public void solveMessage() {
        JSONObject jsonObject = JSONObject.fromObject(this.message);
        String id = jsonObject.getString("id");
        BlogManager blogManager = new BlogManager(requestSolver);
        blogManager.addReader(id);
    }
}
