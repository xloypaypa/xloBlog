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
        PostConfig.PostInfo postInfo = postConfig.getPostInfo().get(1);
        assertNotNull(postInfo);
        assertEquals("/register", postInfo.getUrl());
        assertEquals("control.UserManager", postInfo.getManager());
        assertFalse(postInfo.getAccess());
        assertEquals("register", postInfo.getMethod());
        assertEquals("username", postInfo.getMethodData().get(0));
    }
}