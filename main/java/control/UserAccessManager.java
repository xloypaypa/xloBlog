package control;

import config.ConfigManager;
import config.ReturnCodeConfig;

/**
 * Created by xlo on 2015/8/21.
 * it's the user access manager
 */
public class UserAccessManager {
    private static UserAccessManager userAccessManager = new UserAccessManager();

    private UserAccessManager() {

    }

    public static UserAccessManager getUserAccessManager() {
        return userAccessManager;
    }

    public boolean userExist(String username) {
        return true;
    }

    public String checkUser(String username, String password) {
        ReturnCodeConfig returnCodeConfig = (ReturnCodeConfig) ConfigManager.getConfigManager().getConfig(ReturnCodeConfig.class);
        return returnCodeConfig.getCode("accept");
    }

    public String register(String username, String password) {
        ReturnCodeConfig returnCodeConfig = (ReturnCodeConfig) ConfigManager.getConfigManager().getConfig(ReturnCodeConfig.class);
        return returnCodeConfig.getCode("accept");
    }
}
