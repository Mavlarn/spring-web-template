package com.mtt.myapp.infra.config;


import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DynamicCacheConfigTest {

	@Autowired
	private DynamicCacheConfig cacheConfig;

	@Test
	public void testDynamicCacheConfig() {


		assertThat(cacheConfig.dynamicCacheManager()).isNotNull();
	}


}
