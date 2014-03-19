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
package com.mtt.myapp.operation;


import static org.fest.assertions.Assertions.assertThat;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mtt.myapp.AbstractSystemTransactionalTest;
import com.mtt.myapp.infra.config.AppConfig;
import com.mtt.myapp.operation.cotroller.LogMonitorController;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;

public class LogMonitorControllerTest extends AbstractSystemTransactionalTest {

	public static final Logger LOGGER = LoggerFactory.getLogger(LogMonitorControllerTest.class);

	@Autowired
	private LogMonitorController logMonitorController;

	@Autowired
	private AppConfig config;

	@Test
	public void testLogMonitorController() {
		sleep(3000);
		logMonitorController.enableVerbose(false);
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < 100; i++) {
			buffer.append("====================================");
		}
		LOGGER.info(buffer.toString());
		LOGGER.info("Core Logger");
		LOGGER.debug("TEST TEST");
		sleep(1000);
		// if logMonitorController.enableVerbose(false), it will check system setting.
		boolean isDebug = config.isVerbose();
		if (!isDebug) {
			assertThat(getLastMessage()).doesNotContain("TEST TEST");
		}
		LOGGER.info("Core Logger");
		sleep(4000);
		// assertThat(getLastMessage(), containsString("Core Logger"));

		logMonitorController.enableVerbose(true);
		LOGGER.debug("TEST TEST");
		sleep(1000);
		assertThat(getLastMessage()).contains("TEST TEST");

	}

	private String getLastMessage() {
		HttpEntity<String> lastLog = logMonitorController.getLast();
		JsonParser parser = new JsonParser();
		JsonElement parse = parser.parse(lastLog.getBody());
		String message = parse.getAsJsonObject().get("log").getAsString();
		return message;
	}
}
