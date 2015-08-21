package config;

import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import tool.ResourceManager;

/**
 * Created by xlo on 2015/8/20.
 * it's the interface of config
 */
public interface ConfigInterface {
    void init() throws  Exception;
    void reload() throws Exception;
    static Element getRootElement(String path) throws DocumentException {
        return DocumentHelper.parseText(new String(ResourceManager.getResourceManager().getResource(path))).getRootElement();
    }
}
