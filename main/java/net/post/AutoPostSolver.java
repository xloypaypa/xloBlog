package net.post;

import config.PostConfig;
import net.tool.LengthLimitReadServerSolver;
import script.ForceCacheScriptManager;
import script.ScriptManager;

import java.io.IOException;

/**
 * Created by xlo on 2015/9/9.
 * it's the auto post solver by clojure
 */
public class AutoPostSolver extends LengthLimitReadServerSolver {

    protected PostConfig.PostInfo postInfo;

    public AutoPostSolver(PostConfig.PostInfo postInfo) {
        this.postInfo = postInfo;
    }

    @Override
    public void solveMessage() {
        ScriptManager scriptManager = ForceCacheScriptManager.getForceCacheScriptManager();
        try {
            //TODO set clojure script path
            PostInfoSolver postInfoSolver = (PostInfoSolver) scriptManager.runScript("", true);
            postInfoSolver.solveInfo(postInfo, this.message, this.requestSolver);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
