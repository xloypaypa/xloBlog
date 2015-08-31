package config;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by xlo on 15-8-30.
 * it's the test of access config
 */
public class AccessConfigTest {
    @Test
    public void testLoad() throws Exception {
        AccessConfig accessConfig = new AccessConfig();
        accessConfig.init();
        int value = accessConfig.checkClassAndMethod("control.BlogManager", "addBlog").getValue();
        assertEquals(1, value);
    }
}