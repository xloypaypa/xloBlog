package config;

import org.dom4j.DocumentException;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by xlo on 2015/8/31.
 * it's the post config test
 */
public class PostConfigTest {
    @Test
    public void testLoad() throws DocumentException {
        PostConfig postConfig = new PostConfig();
        postConfig.init();
        for (PostConfig.PostInfo postInfo : postConfig.getPostInfo()) {
            assertNotNull(postInfo.getClassName());
            assertNotNull(postInfo.getName());
            assertNotNull(postInfo.getUrl());
        }
    }
}