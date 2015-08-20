package config;

import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import tool.ResourceManager;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by xlo on 2015/8/20.
 * it's the class config show file or download file
 */
public class ShowFileTypeConfig implements ConfigInterface {
    protected Set<String> showFileNameEndWith = new HashSet<>();

    protected ShowFileTypeConfig() {

    }

    @Override
    public void init() {
        try {
            Element root = DocumentHelper.parseText(new String(ResourceManager.getResourceManager().getResource("/showFileType.xml"))).getRootElement();
            List node = root.elements();
            for (Object now : node) {
                Element element = (Element) now;
                this.showFileNameEndWith.add(element.getText());
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void reload() {
        this.showFileNameEndWith.clear();
        init();
    }

    public boolean isShow(String path) {
        return this.showFileNameEndWith.contains(path.substring(path.lastIndexOf('.') + 1));
    }
}
