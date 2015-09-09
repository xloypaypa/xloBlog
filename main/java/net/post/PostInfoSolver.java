package net.post;

import config.PostConfig;
import server.serverSolver.RequestSolver;

/**
 * Created by xlo on 2015/9/9.
 * it's the interface for clojure
 */
public interface PostInfoSolver {
    void solveInfo(PostConfig.PostInfo postInfo, String message, RequestSolver requestSolver);
}
