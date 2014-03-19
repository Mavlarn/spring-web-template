package com.mtt.myapp.infra.config;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.IOException;

import javax.annotation.PostConstruct;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import com.mtt.myapp.common.constant.SystemConstants;
import com.mtt.myapp.common.util.PropertiesWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class AppConfig implements SystemConstants {

	private static final Logger LOG = LoggerFactory.getLogger(AppConfig.class);

	@Autowired
	private AppHome home;

	private ReloadableProperties systemProperties;

	private ReloadableProperties announceProperties;

	private ReloadableProperties databaseProperties;


	@PostConstruct
	public void initConfig() {
		initProperties();
		initLogger();
	}

	/**
	 * Set up the logger.
	 *
	 */
	protected void initLogger() {
		resetLoggerContext(isVerbose());
	}

	/**
	 * Reset default slf4j configuration, and set the log level.
	 * The 'logback.xml' can be put into app's home directory. Otherwise, "/logback/logback.xml" will be used.
	 *
	 */
	public void resetLoggerContext(boolean verbose) {
		final LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		final JoranConfigurator configurator = new JoranConfigurator();
		configurator.setContext(context);
		context.reset();
		context.putProperty("LOG_LEVEL", verbose ? "DEBUG" : "INFO");
		File logbackConf = home.getSubFile("logback.xml");
		try {
			if (!logbackConf.exists()) {
				logbackConf = new ClassPathResource("/logback/logback.xml").getFile();
				context.putProperty("LOG_DIRECTORY", home.getDirectory().getAbsolutePath());
				context.putProperty("LOG_FILE_NAME", "myapp");

			}
			configurator.doConfigure(logbackConf);
		} catch (JoranException e) {
			LOG.error(e.getMessage(), e);
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	private void initProperties() {
		this.systemProperties = new ReloadableProperties(home.getSubFile("system.conf").getAbsolutePath());
		this.announceProperties = new ReloadableProperties(home.getSubFile("announce.conf").getAbsolutePath());
		this.databaseProperties = new ReloadableProperties(home.getSubFile("database.conf").getAbsolutePath());
		this.databaseProperties.get().setProperty("DB_HOME", home.getDirectory().getAbsolutePath());
	}



	/**
	 * Check if user self-registration is enabled.
	 *
	 * @return true if enabled.
	 */
	public boolean isSignUpEnabled() {
		return systemProperties.get().getPropertyBoolean(PROP_SYSTEM_ALLOW_SIGN_UP, true);
	}

	public String getDefaultLanguage() {
		return systemProperties.get().getProperty(PROP_SYSTEM_DEFAULT_LANGUAGE, "zh");
	}

	public boolean isVerbose() {
		return "debug".equalsIgnoreCase(systemProperties.get().getProperty(PROP_SYSTEM_LOG_LEVEL));
	}

	/**
	 * Get announcement content.
	 *
	 * @return loaded from announcement.conf.
	 */
	public String getSystemAnnouncement() {
		return announceProperties.get().getProperty("system", null);
	}

	public String getUserAnnouncement() {
		return announceProperties.get().getProperty("user", null);
	}

	public PropertiesWrapper getDataBaseProperties() {
		checkNotNull(this.databaseProperties);
		return this.databaseProperties.get();
	}

	public PropertiesWrapper getSystemProperties() {
		checkNotNull(this.systemProperties);
		return this.systemProperties.get();
	}
}