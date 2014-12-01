package com.fcs.redis.redisson;

import java.util.concurrent.ConcurrentMap;

import org.redisson.Config;
import org.redisson.Redisson;

/**
 * @author fcs
 * Redisson Example
 */
public class RedissonTest {
	public static void main(String[] args) {
		//1.初始化
		Config config = new Config();
		config.setConnectionPoolSize(10);
		config.addAddress("127.0.0.1:6379");
		Redisson redisson = Redisson.create(config);
		System.out.println("redis连接接成功。。。。。");
		
		//2.测试concurrentMap,put时候就会同步到redis中
		ConcurrentMap<String, String> map = redisson.getMap("firstMap");
		map.put("changshengfeng", "男");
		map.put("yongtaoliu", "男");
		map.put("qiaozhu", "女");
		
		ConcurrentMap resultMap = redisson.getMap("firstMap");
		System.out.println("resultMap == "+resultMap.keySet());
		//关闭连接
		redisson.shutdown();
	}
}
