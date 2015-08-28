package config;

import model.db.UserCollection;
import model.lock.TestClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by xlo on 2015/8/27.
 * it's db config test
 */
public class DBConfigTest extends TestClass {
    @Test
    public void testLoad() throws Exception {
        DBConfig dbConfig = new DBConfig();
        dbConfig.init();
        assertEquals("user", dbConfig.getCollectionName(UserCollection.class));
        assertEquals("blog", dbConfig.getDBofCollection("user"));
    }
}