//package com.mtt.myapp.common.model;
//
//
//import static com.google.common.base.Preconditions.checkNotNull;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.StringReader;
//import java.util.Properties;
//
//import com.mtt.myapp.common.constant.SystemConstants;
//import com.mtt.myapp.common.excpetion.ConfigurationException;
//import com.mtt.myapp.common.excpetion.CustomRuntimeException;
//import com.mtt.myapp.common.util.EncodingUtil;
//import org.apache.commons.io.FileUtils;
//
///**
// * Home class which enable you to easily access resources in Home directory.
// *
// * @author Mavlarn
// * @since 1.0
// */
//public class Home implements SystemConstants {
//
//	private final File directory;
//
//	private static final String LOG_FILE_PATH = "logs";
//
//	/**
//	 * Constructor.
//	 *
//	 * @param directory home directory
//	 */
//	public Home(File directory) {
//		this(directory, true);
//	}
//
//	/**
//	 * Constructor.
//	 *
//	 * @param directory home directory
//	 * @param create create the directory if not exists
//	 */
//	public Home(File directory, boolean create) {
//		checkNotNull(directory, "directory should not be null");
//		if (create) {
//			directory.mkdir();
//		}
//		if (directory.exists() && !directory.canWrite()) {
//			throw new ConfigurationException(String.format("App home directory %s is not writable.", directory),
//					null);
//		}
//		this.directory = directory;
//	}
//
//	/**
//	 * Get home directory.
//	 *
//	 * @return home directory
//	 */
//	public File getDirectory() {
//		return directory;
//	}
//
//	/**
//	 * Copy file from given location.
//	 *
//	 * @param from file location
//	 * @param overwrite overwrite
//	 */
//	public void copyFrom(File from, boolean overwrite) {
//		// Copy missing files
//		try {
//			for (File file : from.listFiles()) {
//				if (!(new File(directory, file.getName()).exists())) {
//					FileUtils.copyFileToDirectory(file, directory);
//				} else {
//					File orgConf = new File(directory, "org_conf");
//					orgConf.mkdirs();
//					FileUtils.copyFile(file, new File(orgConf, file.getName()));
//				}
//			}
//		} catch (IOException e) {
//			throw new CustomRuntimeException("Fail to copy files from " + from.getAbsolutePath(), e);
//		}
//	}
//
//	/**
//	 * Make sub directory on home directory.
//	 *
//	 * @param subPathName subpath name
//	 */
//	public void makeSubPath(String subPathName) {
//		File subFile = new File(directory, subPathName);
//		if (!subFile.exists()) {
//			subFile.mkdir();
//		}
//	}
//
//	/**
//	 * Get the {@link java.util.Properties} named the given configuration file name.
//	 *
//	 * @param confFileName configuration file name
//	 *
//	 * @return loaded {@link java.util.Properties}
//	 */
//	public Properties getProperties(String confFileName) {
//		try {
//			File configFile = getSubFile(confFileName);
//			if (configFile.exists()) {
//				byte[] propByte = FileUtils.readFileToByteArray(configFile);
//				String propString = EncodingUtil.getAutoDecodedString(propByte, "UTF-8");
//				Properties prop = new Properties();
//				prop.load(new StringReader(propString));
//				return prop;
//			} else {
//				// default empty properties.
//				return new Properties();
//			}
//
//		} catch (IOException e) {
//			throw new CustomRuntimeException("Fail to load property file " + confFileName, e);
//		}
//	}
//
//	/**
//	 * Get sub {@link java.io.File} instance under home directory.
//	 *
//	 * @param subPathName subpath name
//	 *
//	 * @return {@link java.io.File}
//	 */
//	public File getSubFile(String subPathName) {
//		return new File(directory, subPathName);
//	}
//
//}
