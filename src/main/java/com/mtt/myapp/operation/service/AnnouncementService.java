/* 
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */
package com.mtt.myapp.operation.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.mtt.myapp.infra.config.AppConfig;
import com.mtt.myapp.infra.config.AppHome;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Announcement operating service.
 * 
 * @author AlexQin
 * @since 3.1
 */
@Service
public class AnnouncementService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AnnouncementService.class);

	@Autowired
	private AppHome home;

	@Autowired
	private AppConfig config;

	/**
	 * Get announcement content.
	 * 
	 * @return file content.
	 */
	public String getSysAnnouncement() {
		return config.getSystemAnnouncement();
	}

	public String getUserAnnouncement() {
		return config.getUserAnnouncement();
	}

	@Cacheable("announce")
	public Map<String, String> getAnnouncement() {
		Map announce = new HashMap();
		announce.put("system", config.getSystemAnnouncement());
		announce.put("user", config.getUserAnnouncement());
		return announce;
	}

	public String getFileContent() {
		try {
			return FileUtils.readFileToString(home.getSubFile("announce.properties"), "UTF-8");
		} catch (IOException e) {
			LOGGER.error("Error while writing announcement file.");
			return "";
		}
	}

	/**
	 * Check whether the announcement needs display.
	 *
	 * @return true if it needs
	 */
	public boolean check() {
		// TODO: add some pattern in announcement for the display time logic.
		return true;
	}

	/**
	 * Save content to announcement.conf file.
	 * 
	 * @param content
	 *            file content.
	 * @return save successfully or not.
	 */
	@CacheEvict("announce")
	public boolean save(String content) {
		try {
			FileUtils.writeStringToFile(home.getSubFile("announce.properties"), content, "UTF-8");
		} catch (IOException e) {
			LOGGER.error("Error while writing announcement file.");
			return false;
		}
		return true;
	}
}
