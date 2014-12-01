package com.fcs.redis.jedis;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import redis.clients.jedis.Jedis;
import org.junit.*;
/**
 * @author WH141006P
 * test about jedis
 * Dec 1, 2014
 */
public class JedisTest {
	private static Jedis jedis;
	
	@Before
	public void setup(){
		jedis = new Jedis("127.0.0.1", 6379);
		System.out.println("Redis服务器已连接....");
//		jedis.auth("admin");   //权限验证
	}
	
	/**
	 * redis 存储字符串
	 */
	@Test
	public void testString(){
		//添加数据
		jedis.set("name", "fcs");
		System.out.println(jedis.get("name"));//获取结果
		
		jedis.append("name", "is handsome");//拼接
		
		jedis.del("name");//删除某个键
		System.out.println(jedis.get("name"));
		
		jedis.mset("name","changsheng","age","22","qq","646653132");//设置多个键值对
		jedis.incr("age");//加1操作   在投票中可能用的上
		System.out.println(jedis.get("name")+"--"+jedis.get("age")+"--"+jedis.get("qq"));
	}
	
	/**
	 * 操作List
	 */
	@Test
	public void testList(){
		jedis.del("java framework");
		System.out.println(jedis.lrange("java framework", 0, -1));
		//先向key java framework存放三条数据
		jedis.lpush("java framework", "spring");
		jedis.lpush("java framework", "struts");
		jedis.lpush("java framework", "hibernate");
		//再取出所有数据jedis.lrange是按范围取出  第一个是key  第二个是其实位置  第三个是结束位置
		System.out.println(jedis.lrange("java framework", 0, -1));
		
		jedis.del("java framework");
		jedis.rpush("java framework", "spring");
		jedis.rpush("java framework", "struts");
		jedis.rpush("java framework", "hibernate");
		//再取出所有数据jedis.lrange是按范围取出  第一个是key  第二个是其实位置  第三个是结束位置
		System.out.println(jedis.lrange("java framework", 0, -1));
		
	}
	
	/**
	 * 操作Set
	 */
	@Test
	public void testSet(){
		jedis.sadd("haha", "why");
		jedis.sadd("haha", "you");
		jedis.sadd("haha", "so");
		jedis.sadd("haha", "diao");
		jedis.sadd("haha", "?");
		//移除
		jedis.srem("haha", "?");
		System.out.println("判断?是不是haha集合的元素:"+jedis.sismember("haha", "?"));
		System.out.println("获取所有加入的value："+jedis.smembers("haha"));
		System.out.println("返回给定集合名的一个随机的value："+jedis.srandmember("haha"));
		System.out.println("返回集合的元素个数："+jedis.scard("haha"));
		
	}
	
	/**
	 * redis 操作map
	 */
	@Test
	public void testmap(){
		Map<String,String> map = new HashMap<String, String>();
		map.put("name", "小露");
		map.put("sex", "男");
		map.put("email", "haha@fcs.com");
		jedis.hmset("user", map);//相当于给map再取一个名字
		
		List<String> rsmap = jedis.hmget("user", "name","sex");//后面是一个可变参数列表  去某个map中的一些key代表的值
		System.out.println(rsmap);
		
		//删除map中的某个键值
		jedis.hdel("user", "email");
		System.out.println("删除后----email"+jedis.hmget("user", "email"));
		System.out.println("是否存在key为user的记录:"+jedis.exists("user"));
		System.out.println("key为user的map中存放的值的个数:"+jedis.hlen("user"));
		System.out.println("返回map对象中所有的key:"+jedis.hkeys("user"));
		System.out.println("返回map对象中所有的value:"+jedis.hvals("user"));
		
		//使用迭代器
		Iterator<String> iter = jedis.hkeys("user").iterator();
		System.out.println("***************使用迭代器***************");
		while(iter.hasNext()){
			String key = iter.next();//每次向后越过一个对象
			System.out.println(key+":"+jedis.hmget("user", key));//迭代key   根据key再取值value
		}

	}
	
	/**
	 * 这里在前面执行完之后直接再去拿值   试试这些进驻内存的数据是否还在
	 * 可以把服务器端关掉再重启    再直接运行这个方法看看
	 * 如果还有数据就说明该数据库自动完成了持久化     它有默认的持久化机制
	 */
	@Test
	public void testNoSet(){
		Iterator<String> iter = jedis.hkeys("user").iterator();
		System.out.println("***************使用迭代器***************");
		while(iter.hasNext()){
			String key = iter.next();//每次向后越过一个对象
			System.out.println(key+":"+jedis.hmget("user", key));//迭代key   根据key再取值value
		}
	}

//	@AfterClass   测试整个类时可以用    会关闭服务器端程序
//	public static void close(){
//		jedis.shutdown();//不能用@After   不然每次执行完一个方法都会关闭服务器
//		System.out.println("连接已关闭.....");
//	}
	
}
