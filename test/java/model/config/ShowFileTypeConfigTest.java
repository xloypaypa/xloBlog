package model.config;

import model.lock.TestClass;
import org.dom4j.DocumentException;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by xlo on 2015/8/20.
 * it's test show file config
 */
public class ShowFileTypeConfigTest extends TestClass {
    @Test
    public void testLoad() throws DocumentException {
        ShowFileTypeConfig showFileTypeConfig = new ShowFileTypeConfig();
        showFileTypeConfig.init();
        assertTrue(showFileTypeConfig.isShow("abc.html"));
        assertTrue(showFileTypeConfig.isShow("abc.js"));
        assertTrue(showFileTypeConfig.isShow("abc.css"));
        assertFalse(showFileTypeConfig.isShow("abc.java"));
    }
}