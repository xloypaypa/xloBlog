package net.get;

import config.ConfigManager;
import config.ShowFileTypeConfig;
import server.serverSolver.RequestSolver;
import server.serverSolver.normalServer.DynamicServerSolver;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by xlo on 2015/8/20.
 * it's the server solver check the server action is download or show
 */
public class GetServerSolver extends DynamicServerSolver {
    @Override
    public void buildAimSolver(RequestSolver requestSolver) {
        try {
            String path = URLDecoder.decode(requestSolver.getRequestHeadReader().getUrl().getFile(), "UTF-8");
            ShowFileTypeConfig showFileTypeConfig = (ShowFileTypeConfig) ConfigManager.getConfigManager().getConfig(ShowFileTypeConfig.class);
            if (showFileTypeConfig.isShow(path)) {
                this.aimSolver = new ShowItemNormalServerSolver(){
                    public ShowItemNormalServerSolver set(RequestSolver requestSolver) {
                        this.requestSolver = requestSolver;
                        return this;
                    }
                }.set(requestSolver);
            } else {
                this.aimSolver = new AllDownloadServerSolver(){
                    public AllDownloadServerSolver set(RequestSolver requestSolver) {
                        this.requestSolver = requestSolver;
                        return this;
                    }
                }.set(requestSolver);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
