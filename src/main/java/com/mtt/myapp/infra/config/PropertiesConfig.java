package com.mtt.myapp.infra.config;

import java.util.Properties;

import com.mtt.myapp.common.constant.SystemConstants;
import com.mtt.myapp.common.util.PropertiesWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;

/**
 * Class description.
 * Author: mavlarn
 * Date: 14-3-17
 */
//@Configuration
//@ComponentScan("com.mtt.myapp")
//@DependsOn("appHome")
public class PropertiesConfig implements SystemConstants {

	//@Autowired
	private AppHome appHome;

	private PropertiesWrapper systemProperties;

	private PropertiesWrapper databaseProperties;

	private PropertiesWrapper announceProperties;

//	@Bean
//	@DependsOn("appHome")
	public PropertySourcesPlaceholderConfigurer properties() {
		PropertySourcesPlaceholderConfigurer config = new PropertySourcesPlaceholderConfigurer();
		MutablePropertySources propertiesSource = new MutablePropertySources();

		Properties sysProperties = appHome.getProperties("system");
		this.systemProperties = new PropertiesWrapper(sysProperties);
		PropertiesPropertySource sysPS = new PropertiesPropertySource("system", sysProperties);
		propertiesSource.addFirst(sysPS);

		Properties dbProperties = appHome.getProperties("database");
		this.databaseProperties = new PropertiesWrapper(dbProperties);
		PropertiesPropertySource dbPS = new PropertiesPropertySource("database", dbProperties);
		propertiesSource.addLast(dbPS);

		Properties announceProperties = appHome.getProperties("announce");
		this.announceProperties = new PropertiesWrapper(announceProperties);
		PropertiesPropertySource announcePS = new PropertiesPropertySource("announce", announceProperties);
		propertiesSource.addLast(announcePS);

		config.setPropertySources(propertiesSource);

		// this needs properties config, so init here.
		return config;
	}

//	@Bean(name  = "databaseProperties")
//	public PropertiesWrapper getDatabaseProperties() {
//		return this.databaseProperties;
//	}
//
//	@Bean(name  = "databaseProperties")
//	public PropertiesWrapper getSystemProperties() {
//		return systemProperties;
//	}
//
//	@Bean(name  = "announceProperties")
//	public PropertiesWrapper getAnnounceProperties() {
//		return announceProperties;
//	}

}
