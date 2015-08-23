package config;

import org.dom4j.Element;

import java.util.List;

/**
 * Created by xlo on 15-8-23.
 * it's the db config
 */
public class DBConfig implements ConfigInterface {
    protected String host = "127.0.0.1";
    protected int port = 27017;

    protected DBConfig() {

    }

    @Override
    public void init() throws Exception {
        Element root = ConfigInterface.getRootElement("/db.xml");
        List node = root.elements();
        for (Object now : node) {
            Element element = (Element) now;
            if (element.getName().equals("host")) this.host = element.getText();
            else if (element.getName().equals("port")) this.port = Integer.valueOf(element.getText());
        }
    }

    @Override
    public void reload() throws Exception {
        this.init();
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
}
