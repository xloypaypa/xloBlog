package config;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by xlo on 2015/8/20.
 * it's test config manager
 */
public class ConfigManagerTest {
    @Test
    public void testLoad() {
        assertTrue(((ShowFileTypeConfig)ConfigManager.getConfigManager().getConfig(ShowFileTypeConfig.class)).isShow("abc.html"));
    }
}