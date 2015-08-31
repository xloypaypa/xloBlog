package config;

import javafx.util.Pair;
import org.junit.Test;

import java.util.Set;

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
        Set<Pair<String, Integer>> value = accessConfig.checkClassAndMethod("control.BlogManager", "addDocument");
        for (Pair<String, Integer> now : value) {
            if (now.getKey().equals("access")) {
                int ans = now.getValue();
                assertEquals(1, ans);
            }
        }
    }
}