package com.casestudy.redis;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

public class RedisConnectionBinder extends AbstractBinder {

	@Override
	protected void configure() {
		bind(new RedisConnectionSetup()).to(RedisConnectionSetup.class);
	}

}
