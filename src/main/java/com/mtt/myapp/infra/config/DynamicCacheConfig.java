package com.mtt.myapp.infra.config;

import java.io.IOException;
import java.io.InputStream;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.config.ConfigurationFactory;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

/**
 * Dynamic cache configuration. This get the control of EhCache configuration from Spring. Depending
 * on the system.conf, it creates local cache or dist cache.
 *
 * @author Mavlarn
 * @author Mavlarn
 * @since 3.1
 */
@Component
public class DynamicCacheConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(DynamicCacheConfig.class);

	@Autowired
	private AppConfig config;

	/**
	 * Create cache manager dynamically according to the configuration. Because we can't add a
	 * cluster peer provider dynamically.
	 *
	 * @return EhCacheCacheManager bean
	 */
	@SuppressWarnings("rawtypes")
	@Bean(name = "cacheManager")
	public EhCacheCacheManager dynamicCacheManager() {
		EhCacheCacheManager cacheManager = new EhCacheCacheManager();
		Configuration cacheManagerConfig;
		InputStream inputStream = null;
		try {
			inputStream = new ClassPathResource("ehcache.xml").getInputStream();
			cacheManagerConfig = ConfigurationFactory.parseConfiguration(inputStream);

			cacheManagerConfig.setName(getCacheName());
			CacheManager mgr = CacheManager.create(cacheManagerConfig);
			cacheManager.setCacheManager(mgr);

		} catch (IOException e) {
			LOGGER.error("Error while setting up cache", e);
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
		return cacheManager;
	}


	String getCacheName() {
		return "cacheManager";
	}

}
