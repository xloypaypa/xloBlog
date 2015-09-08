package model.db;

import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import org.bson.types.ObjectId;

import java.io.OutputStream;

/**
 * Created by xlo on 2015/9/8.
 * it's the collection of image
 */
public class ImageCollection extends OtherDB {

    public String insertImage(String type, byte[] image) {
        lockCollection();
        ObjectId objectId = new ObjectId();
        String filename = objectId.toString() + "." + type;
        GridFS gridFS = getGridFS();
        GridFSInputFile gridFSInputFile = gridFS.createFile(image);
        gridFSInputFile.setFilename(filename);
        gridFSInputFile.save();
        unlockCollection();
        return filename;
    }

    public byte[] getImage(String id, OutputStream outputStream) throws Exception {
        GridFS gridFS = getGridFS();
        GridFSDBFile gridFSDBFile = gridFS.findOne(id);
        gridFSDBFile.writeTo(outputStream);
        return null;
    }

    public void removeImage(String id) {
        lockCollection();
        GridFS gfsPhoto = getGridFS();
        gfsPhoto.remove(gfsPhoto.findOne(id));
        unlockCollection();
    }

    private GridFS getGridFS() {
        return new GridFS(db, dbCollection.getName());
    }
}
