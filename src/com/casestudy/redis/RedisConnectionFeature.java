package com.casestudy.redis;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

public class RedisConnectionFeature implements Feature {

  @Override
  public boolean configure(final FeatureContext context) {
      context.register(new RedisConnectionBinder());
      return true;
  }
}
