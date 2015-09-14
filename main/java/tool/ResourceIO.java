package tool;

import model.tool.ioAble.AbstractIOAble;

/**
 * Created by xlo on 2015/8/19.
 * it's the io builder use resource firstly.
 * if there are no resource use local file.
 */
public class ResourceIO extends AbstractIOAble {
    protected String path;

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public boolean buildIO() {
        this.inputStream = this.getClass().getResourceAsStream(path);
        if (this.inputStream != null) {
            this.outputStream = null;
            return true;
        } else {
            return false;
        }
    }
}
