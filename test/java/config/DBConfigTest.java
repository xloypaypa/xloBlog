package config;

import model.db.UserCollection;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by xlo on 2015/8/27.
 * it's db config test
 */
public class DBConfigTest {
    @Test
    public void testLoad() throws Exception {
        DBConfig dbConfig = new DBConfig();
        dbConfig.init();
        assertEquals("user", dbConfig.getCollectionName(UserCollection.class));
    }
}