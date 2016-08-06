package com.qijiabin.spymemcached;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import net.spy.memcached.MemcachedClient;

/**
 * ========================================================
 * 日 期：2016年8月6日 下午3:35:11
 * 作 者：jackson
 * 版 本：1.0.0
 * 类说明：spymemcached使用示例
 * TODO
 * ========================================================
 * 修订日期     修订人    描述
 */
public class spymemcachedTest {

	private static MemcachedClient client;

	static{
		List<InetSocketAddress> addrs = new ArrayList<InetSocketAddress>();
	    addrs.add(new InetSocketAddress("127.0.0.1", 11211));
	    addrs.add(new InetSocketAddress("127.0.0.1", 11211));
	    try {

	        client=new MemcachedClient(addrs);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public static void main(String[] args) {
		/**
		 * 第一个参数：键（key）
		 * 第二个参数：过期时间（单位是秒）
		 * 第三个参数：要设置缓存中的对象（value），如果没有则插入，如果有则修改。
		 */
		client.set("foo", 1, "This is a test String");
		String bar = client.get("foo").toString();
		System.out.println(">>> " + bar);
	}
	
}
