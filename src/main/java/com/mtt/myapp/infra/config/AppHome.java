package com.mtt.myapp.infra.config;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

import javax.annotation.PostConstruct;

import com.mtt.myapp.common.constant.SystemConstants;
import com.mtt.myapp.common.excpetion.ConfigurationException;
import com.mtt.myapp.common.excpetion.CustomRuntimeException;
import com.mtt.myapp.common.util.EncodingUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

/**
 * Class description.
 * Author: mavlarn
 */
@Component("appHome")
public class AppHome implements SystemConstants {

	private static final Logger LOG = LoggerFactory.getLogger(AppHome.class);

	private File directory;

	private static final String DEFAULT_HOME_FOLDER = ".myapp";

	@PostConstruct
	public void initHome() {
		resolveHome();
	}

	private void resolveHome() {
		String userHomeFromEnv = System.getenv("MYAPP_HOME");
		String userHomeFromProperty = System.getProperty("myapp.home");
		if (!StringUtils.equals(userHomeFromEnv, userHomeFromProperty)) {
			LOG.warn("The system home path is ambiguous:");
			LOG.warn("    System Environment:  MYAPP_HOME=" + userHomeFromEnv);
			LOG.warn("    Java System Property:  myapp.home=" + userHomeFromProperty);
			LOG.warn("    '" + userHomeFromProperty + "' is accepted.");
		}
		String userHome = StringUtils.defaultIfEmpty(userHomeFromProperty, userHomeFromEnv);
		File homeDirectory = (StringUtils.isNotEmpty(userHome)) ? new File(userHome) : new File(
				System.getProperty("user.home"), DEFAULT_HOME_FOLDER);
		LOG.info("System home directory:{}.", homeDirectory);

		this.directory = homeDirectory;

		if (!directory.exists()) {
			directory.mkdir();
		}
		// maybe there is new config file need copy.
		copyDefaultConfigurationFiles();
	}

	/**
	 * Copy default files.
	 *
	 * @throws java.io.IOException
	 *             occurs when there is no such a files.
	 */
	private void copyDefaultConfigurationFiles() {
		if (directory.exists() && !directory.canWrite()) {
			throw new ConfigurationException(String.format("App home directory %s is not writable.", directory),
					null);
		}
		try {
			copyFrom(new ClassPathResource("home_template").getFile(), false);
		} catch (IOException e) {
			LOG.error("Generate System config failed:{}.", e.getMessage());
		}
	}

	/**
	 * Get home directory.
	 *
	 * @return home directory
	 */
	public File getDirectory() {
		return directory;
	}

	/**
	 * Copy file from given location.
	 *
	 * @param from file location
	 * @param overwrite overwrite
	 */
	public void copyFrom(File from, boolean overwrite) {
		// Copy missing files
		try {
			for (File file : from.listFiles()) {
				if (!(new File(directory, file.getName()).exists())) {
					FileUtils.copyFileToDirectory(file, directory);
				} else {
					File orgConf = new File(directory, "org_conf");
					orgConf.mkdirs();
					FileUtils.copyFile(file, new File(orgConf, file.getName()));
				}
			}
		} catch (IOException e) {
			throw new CustomRuntimeException("Fail to copy files from " + from.getAbsolutePath(), e);
		}
	}

	/**
	 * Make sub directory on home directory.
	 *
	 * @param subPathName subpath name
	 */
	public void makeSubPath(String subPathName) {
		File subFile = new File(directory, subPathName);
		if (!subFile.exists()) {
			subFile.mkdir();
		}
	}

	/**
	 * Get the {@link java.util.Properties} named the given configuration file name.
	 *
	 * @param confFileName configuration file name
	 *
	 * @return loaded {@link java.util.Properties}
	 */
	public Properties getProperties(String confFileName) {
		try {
			File configFile = getSubFile(confFileName);
			if (configFile.exists()) {
				byte[] propByte = FileUtils.readFileToByteArray(configFile);
				String propString = EncodingUtil.getAutoDecodedString(propByte, "UTF-8");
				Properties prop = new Properties();
				prop.load(new StringReader(propString));
				return prop;
			} else {
				// default empty properties.
				return new Properties();
			}

		} catch (IOException e) {
			throw new CustomRuntimeException("Fail to load property file " + confFileName, e);
		}
	}

	/**
	 * Get sub {@link java.io.File} instance under home directory.
	 *
	 * @param subPathName subpath name
	 *
	 * @return {@link java.io.File}
	 */
	public File getSubFile(String subPathName) {
		return new File(directory, subPathName);
	}
}
