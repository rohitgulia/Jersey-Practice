package com.casestudy.redis;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

@WebListener
public class RedisConnectionSetup implements ServletContextListener{

	private static RedissonClient redisson;
	
	public RedissonClient getRedisson() {
		return redisson;
	}
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		if (redisson != null)
			redisson.shutdown();
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		Config config = new Config();
		config.useSingleServer().setAddress("redis://127.0.0.1:6379");
		config.setCodec(new org.redisson.codec.SerializationCodec());
		redisson = Redisson.create(config);
	}

}
