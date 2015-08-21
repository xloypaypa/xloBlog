package control;

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

    public boolean checkUser(String username, String password) {
        return true;
    }
}
