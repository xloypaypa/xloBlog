package model.config;

import org.dom4j.DocumentException;
import org.dom4j.Element;

import java.util.ArrayList;
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

    public static PostConfig getConfig() {
        return (PostConfig) ConfigManager.getConfigManager().getConfig(PostConfig.class);
    }

    @Override
    public void init() throws DocumentException {
        Element root = ConfigInterface.getRootElement(ConfigManager.configPathConfig.getConfigFilePath(this.getClass()));
        List node = root.elements();
        for (Object now : node) {
            Element element = (Element) now;
            PostInfo post = new PostInfo();

            post.methodData = new ArrayList<>();
            post.defaultValue = new ArrayList<>();

            post.name = element.attributeValue("name");
            post.url = element.attributeValue("url");
            post.manager = element.attributeValue("manager");
            post.access = element.attributeValue("access").equals("true");
            for (Object kid : element.elements()) {
                Element data = (Element) kid;
                post.method = data.attributeValue("name");
                post.array = data.attributeValue("model", "object").equals("array");
                for (Object methodKid : data.elements()) {
                    Element methodData = (Element) methodKid;
                    String defaultValue = methodData.attributeValue("default");
                    post.defaultValue.add(defaultValue);
                    post.methodData.add(methodData.getText());
                }
            }
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
        private String manager;
        private boolean access;
        private String method;
        private boolean array;
        private List<String> methodData;
        private List<String> defaultValue;

        public String getName() {
            return name;
        }

        public String getUrl() {
            return url;
        }

        public String getManager() {
            return manager;
        }

        public boolean needAccess() {
            return access;
        }

        public String getMethod() {
            return method;
        }

        public boolean isArray() {
            return this.array;
        }

        public List<String> getMethodData() {
            return new ArrayList<>(methodData);
        }

        public List<String> getDefaultValue() {
            return new ArrayList<>(defaultValue);
        }
    }
}