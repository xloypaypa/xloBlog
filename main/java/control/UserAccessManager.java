package control;

import config.ConfigManager;
import config.ReturnCodeConfig;
import model.db.UserCollection;
import model.event.Event;
import model.lock.NameLockImpl;
import net.tool.WriteMessageServerSolver;
import server.serverSolver.RequestSolver;

/**
 * Created by xlo on 2015/8/21.
 * it's the user access manager
 */
public class UserAccessManager {
    protected RequestSolver requestSolver;
    private static ReturnCodeConfig returnCodeConfig = (ReturnCodeConfig) ConfigManager.getConfigManager().getConfig(ReturnCodeConfig.class);

    public UserAccessManager(RequestSolver requestSolver) {
        this.requestSolver = requestSolver;
    }

    public void loginUser(String username, String password) {
        new Event() {
            @Override
            public void lock() {
                NameLockImpl.getNameLock().lock("username");
            }

            @Override
            public void checkPoint() {

            }

            @Override
            public boolean process() {
                sendWhileFail(new WriteMessageServerSolver(requestSolver, returnCodeConfig.getCode("event fail")));
                if (username == null || password == null) {
                    return false;
                }

                UserCollection userCollection = new UserCollection();
                sendWhileSuccess(new WriteMessageServerSolver(requestSolver,
                        userCollection.checkUser(username, password)));
                return true;
            }

            @Override
            public void rollback() {

            }

            @Override
            public void unlock() {
                NameLockImpl.getNameLock().unlock("username");
            }
        }.submit();
    }

    public void register(String username, String password) {
        new Event() {
            @Override
            public void lock() {
                NameLockImpl.getNameLock().lock("username");
            }

            @Override
            public void checkPoint() {

            }

            @Override
            public boolean process() {
                sendWhileFail(new WriteMessageServerSolver(requestSolver, returnCodeConfig.getCode("event fail")));
                if (username == null || password == null) {
                    return false;
                }

                UserCollection userCollection = new UserCollection();
                if (userCollection.userExist(username)) {
                    sendWhileSuccess(new WriteMessageServerSolver(requestSolver, returnCodeConfig.getCode("conflict")));
                    return true;
                }
                sendWhileSuccess(new WriteMessageServerSolver(requestSolver, userCollection.register(username, password)));
                return true;
            }

            @Override
            public void rollback() {
            }

            @Override
            public void unlock() {
                NameLockImpl.getNameLock().unlock("username");
            }
        }.submit();
    }
}
