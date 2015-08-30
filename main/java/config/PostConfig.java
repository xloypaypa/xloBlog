package config;

import org.dom4j.DocumentException;
import org.dom4j.Element;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by xlo on 2015/8/28.
 * it's config post's solver
 */
public class PostConfig implements ConfigInterface {
    protected List<PostInfo> postInfo;

    protected PostConfig() {
        this.postInfo = new LinkedList<>();
    }

    @Override
    public void init() throws DocumentException {
        Element root = ConfigInterface.getRootElement("/post.xml");
        List node = root.elements();
        for (Object now : node) {
            Element element = (Element) now;
            PostInfo post = new PostInfo();
            post.name = element.attributeValue("name");
            post.url = element.attributeValue("url");
            post.className = element.getText();
            postInfo.add(post);
        }
    }

    @Override
    public void reload() throws DocumentException {
        this.postInfo.clear();
        init();
    }

    public List<PostInfo> getPostInfo() {
        return postInfo;
    }

    public class PostInfo {
        private String name;
        private String url;
        private String className;

        public String getClassName() {
            return className;
        }

        public String getUrl() {
            return url;
        }

        public String getName() {
            return name;
        }
    }
}