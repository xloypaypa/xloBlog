package control;

import model.db.UserCollection;
import model.event.Event;
import model.lock.NameLockImpl;
import net.post.register.RegisterServerSolverWrite;
import server.serverSolver.RequestSolver;

/**
 * Created by xlo on 2015/8/21.
 * it's the user access manager
 */
public class UserAccessManager {
    protected RequestSolver requestSolver;

    public UserAccessManager(RequestSolver requestSolver) {
        this.requestSolver = requestSolver;
    }

    public void loginUser(String username, String password) {

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
                UserCollection userCollection = new UserCollection();
                sendWhileSuccess(new RegisterServerSolverWrite(requestSolver,
                        userCollection.register(username, password)));
                sendWhileFail(new RegisterServerSolverWrite(requestSolver, "db error"));
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
