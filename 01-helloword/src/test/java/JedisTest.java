import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.Map;

/**
 * 客户端连接redis 测试类
 *
 * Created by yaoyao on 2020-03-15.
 */
public class JedisTest {

    @Test
    public void testSet() {
        //1、连接redis
        Jedis jedis = new Jedis("10.128.123.108", 6379);
        //2、操作redis
        jedis.set("name", "jedis-test");
        String name = jedis.get("name");
        System.out.println("name:" + name);

        //指定数据库
        jedis.select(2);
        String name2 = jedis.get("atomicNumberTest-20200517");
        System.out.println("name2:" + name2);

        //获取hash中 所有的field value
        jedis.select(4);
        Map<String, String> result = jedis.hgetAll("SNOW_FLAKE_SERIAL_CODE");
        System.out.println("result:" + result);

        //3、关闭连接
        jedis.close();
    }

}
