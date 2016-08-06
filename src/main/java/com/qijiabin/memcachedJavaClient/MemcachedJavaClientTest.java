package com.qijiabin.memcachedJavaClient;

import com.whalin.MemCached.MemCachedClient;
import com.whalin.MemCached.SockIOPool;

/**
 * ========================================================
 * 日 期：2016年8月6日 下午3:27:26
 * 作 者：jackson
 * 版 本：1.0.0
 * 类说明：Memcached-Java-Client 使用示例
 * TODO
 * ========================================================
 * 修订日期     修订人    描述
 */
public class MemcachedJavaClientTest {

	protected static MemCachedClient client = new MemCachedClient();

	static {
		/**
		 * 设置缓存服务器列表，当使用分布式缓存的时，可以指定多个缓存服务器。
		 */
		String[] servers = { "127.0.0.1:11211", "127.0.0.1:11211" };
		/**
		 * 设置服务器权重
		 */
		Integer[] weights = { 3, 2 };
		/**
		 * 创建一个Socked连接池实例
		 * 这个类用来创建管理客户端和服务器通讯连接池，客户端主要的工作包括数据通讯、服务器定位、hash码生成等都是由这个类完成的
		 * 获得连接池的单态方法。这个方法有一个重载方法getInstance( String poolName )，每个poolName只构造一个SockIOPool实例。
		 * 缺省构造的poolName是default。
		 */
		SockIOPool pool = SockIOPool.getInstance();
		/**
		 * 向连接池设置服务器和权重
		 */
		pool.setServers(servers);
		pool.setWeights(weights);
		/**
		 * 设置容错开关设置为TRUE，当当前socket不可用时，程序会自动查找可用连接并返回，否则返回NULL，默认状态是true，建议保持默认
		 */
	    pool.setFailover( true );
	    /**
	     * 设置开始时每个cache服务器的可用连接数
	     */
	    pool.setInitConn( 10 ); 
	    /**
	     * 设置每个服务器最少可用连接数
	     */
	    pool.setMinConn( 5 );
	    /**
	     * 设置每个服务器最大可用连接数
	     */
	    pool.setMaxConn( 250 );
	    /**
	     * 设置连接池维护线程的睡眠时间
	     * 设置为0，维护线程不启动
	     * 维护线程主要通过log输出socket的运行状况，监测连接数目及空闲等待时间等参数以控制连接创建和关闭。
	     */
	    pool.setMaintSleep( 30 );
	    /**
	     * 设置是否使用Nagle算法，因为我们的通讯数据量通常都比较大（相对TCP控制数据）而且要求响应及时，因此该值需要设置为false（默认是true）
	     */
	    pool.setNagle( false );
	    /**
	     * 设置socket的读取等待超时值
	     */
	    pool.setSocketTO( 3000 );
	    /**
	     * 设置连接心跳监测开关。
	     * 设为true则每次通信都要进行连接是否有效的监测，造成通信次数倍增，加大网络负载，因此该参数应该在对HA要求比较高的场合设为TRUE，默认状态是false。
	     */
	    pool.setAliveCheck( true );
	    /**
	     * 设置完pool参数后最后调用该方法，启动pool。
	     */
	    pool.initialize();
	}

	public static void main(String[] args) {
		 /**
	     * 存储一个username，值为刘德华，存储成功返回true
	     */
	    boolean success = client.set("username", "刘德华");
	    System.out.println(success);
	    
	    /**
	     * 从缓存中获取一个key为username的数据
	     */
	    Object o = client.get("username");
	    System.out.println(o);
	    
	    /**
	     * 定义一个p对象，Persion类必须实现Serializable接口
	     */
	    Person p = new Person();
	    p.setId(1);
	    p.setName("周杰伦");
	    
	    /**
	     * 缓存一个p对象
	     */
	    client.set("p1", p);
	    
	    /**
	     * 获取缓存的p对象
	     */
	    Person p1 = (Person) client.get("p1");
	    System.out.println(p1.getName());
	    
	    /**
	     * 调用add方法添加一个key为p1的对象，值123是不能添加进缓存的，因为p1已经添加过一次了
	     */
	    client.add("p1", 123);//错误！无法更新p1的值
	    
	    System.out.println(client.get("p1"));//还是person对象
	    
	    /**
	     * 使用set方法可以更新p1
	     */
	    client.set("p1", 123);
	    System.out.println(client.get("p1"));//输出123
	    
	    /**
	     * 使用replace方法可以更新p1
	     */
	    client.replace("p1", 456);
	    System.out.println(client.get("p1"));//输出456
	    
	    /**
	     * 使用replace方法可以更新p2,缓存中不含有key为p2的数据，无法更新，不会添加
	     */
	    client.replace("p2", 456);
	    System.out.println(client.get("p2"));//输出null
	    
	    /**
	     * 删除key为p1的缓存数据
	     */
	    client.delete("p1");
	    System.out.println(client.get("p1"));//输出null
	}
}