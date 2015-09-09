package config;

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
        Element root = ConfigInterface.getRootElement("/post.xml");
        List node = root.elements();
        for (Object now : node) {
            Element element = (Element) now;
            PostInfo post = new PostInfo();

            post.data = new ArrayList<>();
            post.methodDataType = new ArrayList<>();
            post.methodData = new ArrayList<>();

            post.name = element.attributeValue("name");
            post.url = element.attributeValue("url");
            post.manager = element.attributeValue("manager");
            post.access = element.attributeValue("access").equals("true");
            for (Object kid : element.elements()) {
                Element data = (Element) kid;
                if (data.getName().equals("data")) {
                    post.data.add(data.getText());
                } else {
                    post.method = data.attributeValue("name");
                    for (Object methodKid : data.elements()) {
                        Element methodData = (Element) methodKid;
                        post.methodDataType.add(methodData.attributeValue("type"));
                        post.methodData.add(methodData.getText());
                    }
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
        private List<String> data;
        private String method;
        private List<String> methodDataType;
        private List<String> methodData;


        public String getName() {
            return name;
        }

        public String getUrl() {
            return url;
        }

        public String getManager() {
            return manager;
        }

        public boolean getAccess() {
            return access;
        }

        public List<String> getData() {
            return new ArrayList<>(data);
        }

        public String getMethod() {
            return method;
        }

        public List<String> getMethodDataType() {
            return new ArrayList<>(methodDataType);
        }

        public List<String> getMethodData() {
            return new ArrayList<>(methodData);
        }
    }
}