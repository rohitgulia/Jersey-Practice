package com.casestudy.misc;

import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.server.ResourceConfig;

import com.casestudy.redis.RedisConnectionBinder;

@Provider
public class AppRegister extends ResourceConfig {

	public AppRegister() {
    packages("com.casestudy.misc.AppRegister");
    register(new RedisConnectionBinder());
 }
}