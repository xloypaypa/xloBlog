package control;

import model.db.ImageCollection;
import model.event.Event;
import net.post.image.SendImage;
import net.post.image.UploadImageReader;
import net.sf.json.JSONObject;
import net.tool.WriteMessageServerSolver;
import server.serverSolver.RequestSolver;
import tool.connection.event.ConnectionEventManager;

/**
 * Created by xlo on 2015/9/8.
 * it's the image manager
 */
public class ImageManager extends Manager {
    public ImageManager(RequestSolver requestSolver) {
        super(requestSolver);
    }

    public void checkAddImage(String username, String password, String type) {
        Event event = new Event() {
            @Override
            public boolean run() {
                return accessConfig.isAccept(username, password, this);
            }
        };
        addFailMessage(event);

        JSONObject object = new JSONObject();
        object.put("return", returnCodeConfig.getCode("accept"));
        event.sendWhileSuccess(new WriteMessageServerSolver(requestSolver, object) {
            @Override
            public void startNextServerSolver() {
                UploadImageReader uploadImageReader = new UploadImageReader(type);
                uploadImageReader.setRequestSolver(requestSolver);
                ConnectionEventManager.getConnectionEventManager().proxyItem(this, uploadImageReader);
                uploadImageReader.run();
            }
        });
        event.submit();
    }

    public void addImage(String username, String password, String type, byte[] body) {
        Event event = new Event() {
            @Override
            public boolean run() {
                if (!accessConfig.isAccept(username, password, this)) return false;
                ImageCollection imageCollection = new ImageCollection();
                imageCollection.insertImage(type, body);
                return true;
            }
        };
        addSendMessage(event);
        event.submit();
    }

    public void getImage(String id) {
        Event event = new Event() {
            @Override
            public boolean run() {
                return true;
            }
        };
        addFailMessage(event);
        event.sendWhileSuccess(new SendImage(requestSolver, id));
        event.submit();
    }
}
