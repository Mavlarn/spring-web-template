package com.mtt.myapp.infra.config;

import java.util.Properties;

import javax.persistence.Entity;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.mtt.myapp.common.constant.SystemConstants;
import com.mtt.myapp.common.util.PropertiesWrapper;
import org.apache.commons.dbcp.BasicDataSource;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.jpa.support.MergingPersistenceUnitManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.MutablePersistenceUnitInfo;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitPostProcessor;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Dynamic datasource bean configuration.
 * 
 * @author Mavlarn
 * @since 1.0
 */
@Configuration
@EnableJpaRepositories("com.mtt.myapp")
@EnableAspectJAutoProxy
@EnableTransactionManagement
public class DatabaseConfig implements SystemConstants {
	private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseConfig.class);

	@Autowired
	private AppConfig config;

	/**
	 * Create the dataSource based on the database configuration.
	 * 
	 * @return dataSource
	 */
	@Bean(name = "dataSource", destroyMethod = "close")
	public BasicDataSource dataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		Database database = Database.getDatabase(config.getDataBaseProperties().getProperty("database", "H2",
				"[FATAL] Database type is not specified. In default, use H2."));
		database.setup(dataSource, config.getDataBaseProperties());
		return dataSource;
	}

	@Bean
	public EntityManagerFactory entityManagerFactory() {
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		String dbType = config.getDataBaseProperties().getProperty("database", "H2",
				"[FATAL] Database type is not specified. In default, use H2.");
		LOGGER.info("Using DB type:{}", dbType);
		Database database = Database.getDatabase(dbType);
		vendorAdapter.setDatabasePlatform(database.getDialect());
		vendorAdapter.setGenerateDdl(true);
		//vendorAdapter.setShowSql(true);


		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setJpaVendorAdapter(vendorAdapter);
		factory.setPackagesToScan("com.mtt.myapp");
		factory.setPersistenceUnitName("myapp");
		factory.setDataSource(dataSource());
		factory.afterPropertiesSet();
		factory.setPersistenceUnitManager(new MergingPersistenceUnitManager());

		return factory.getObject();
	}

		/*
		emf.setJpaProperties(new Properties() {
			{  // Hibernate Specific:
				setProperty("hibernate.hbm2ddl.auto", "create-drop");
				//setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
			}
		});
		*/

	/**
	 * Create the transactionManager.
	 *
	 * @return {@link org.springframework.orm.jpa.JpaTransactionManager}
	 */
	@Bean
	public JpaTransactionManager transactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory());
		transactionManager.setDataSource(dataSource());
		return transactionManager;
	}

}
