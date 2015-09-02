package net.post.blog;

import control.BlogManager;
import net.sf.json.JSONObject;
import net.tool.LengthLimitReadServerSolver;

/**
 * Created by xlo on 2015/8/28.
 * it's get list of author's all document
 */
public class GetDocumentListOfAuthorAndTypeReader extends LengthLimitReadServerSolver {
    @Override
    public void solveMessage() {
        JSONObject jsonObject = JSONObject.fromObject(this.message);
        String author = jsonObject.getString("author");
        String type = jsonObject.getString("type");
        BlogManager blogManager = new BlogManager(requestSolver);
        blogManager.getAuthorTypeDocumentList(author, type);
    }
}
