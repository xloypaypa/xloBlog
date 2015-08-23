package config;

import org.dom4j.DocumentException;
import org.dom4j.Element;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by xlo on 15-8-23.
 * it's config name lock
 */
public class LockConfig implements ConfigInterface {
    protected Set<String> locks;

    protected LockConfig() {
        this.locks = new HashSet<>();
    }

    @Override
    public void init() throws DocumentException {
        Element root = ConfigInterface.getRootElement("/lockName.xml");
        List node = root.elements();
        for (Object now : node) {
            Element element = (Element) now;
            this.locks.add(element.getText());
        }
    }

    @Override
    public void reload() throws DocumentException {
        this.locks.clear();
        init();
    }

    public Set<String> getLocks() {
        return new HashSet<>(locks);
    }
}
