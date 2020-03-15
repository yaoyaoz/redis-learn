import org.junit.Test;
import redis.clients.jedis.Jedis;

/**
 * 客户端连接redis 测试类
 *
 * Created by yaoyao on 2020-03-15.
 */
public class JedisTest {

    @Test
    public void testSet() {
        //1、连接redis
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        //2、操作redis
        jedis.set("name", "jedis-test");
        String name = jedis.get("name");
        System.out.println("name:" + name);
        //3、关闭连接
        jedis.close();
    }

}
