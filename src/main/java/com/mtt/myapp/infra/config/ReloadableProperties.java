package com.mtt.myapp.infra.config;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

import com.mtt.myapp.common.excpetion.CustomRuntimeException;
import com.mtt.myapp.common.util.EncodingUtil;
import com.mtt.myapp.common.util.FileWatchdog;
import com.mtt.myapp.common.util.PropertiesWrapper;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class description.
 * Author: mavlarn
 */
public class ReloadableProperties {

	private static final Logger LOG = LoggerFactory.getLogger(ReloadableProperties.class);

	private FileWatchdog watchDog;

	private PropertiesWrapper properties;

	private static final PropertiesWrapper emptyProperties = new PropertiesWrapper(new Properties());

	public ReloadableProperties (final String filePath) {
		watchDog = new FileWatchdog(filePath) {
			@Override
			protected void doOnChange() {
				LOG.info("File {} is changed.", filePath);
				properties = loadProperties(filePath);
			}
		};
		watchDog.setName("WatchDog - announcement.conf");
		watchDog.setDelay(2000);
		watchDog.start();
	}

	private PropertiesWrapper loadProperties(String filePath) {
		try {
			File configFile = new File(filePath);
			if (configFile.exists()) {
				byte[] propByte = FileUtils.readFileToByteArray(configFile);
				String propString = EncodingUtil.getAutoDecodedString(propByte, "UTF-8");
				Properties prop = new Properties();
				prop.load(new StringReader(propString));
				return new PropertiesWrapper(prop);
			} else {
				// default empty properties.
				return emptyProperties;
			}

		} catch (IOException e) {
			throw new CustomRuntimeException("Fail to load property file " + filePath, e);
		}
	}

	public PropertiesWrapper get() {
		return this.properties;
	}
}
