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

import com.mtt.myapp.AbstractSystemTransactionalTest;
import com.mtt.myapp.common.util.PropertiesWrapper;
import com.mtt.myapp.infra.config.AppConfig;
import com.mtt.myapp.operation.cotroller.SystemConfigController;
import com.mtt.myapp.operation.service.SystemConfigService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.ExtendedModelMap;

public class SystemConfigControllerTest extends AbstractSystemTransactionalTest {

	@Autowired
	private SystemConfigController controller;

	@Autowired
	private SystemConfigService service;

	@Autowired
	private AppConfig config;

	@Autowired
	@Qualifier("systemProperties")
	private PropertiesWrapper systemProperties;

	@Test
	public void testGetSystemConfiguration() {
		ExtendedModelMap model = new ExtendedModelMap();
		controller.getOne(model);
		assertThat(model.containsAttribute("content")).isTrue();
	}

	@Test
	public void testSaveSystemConfiguration() {
		String oriContent = service.getOne();
		ExtendedModelMap model = new ExtendedModelMap();
		String content = "test=My test.";
		try {
			controller.save(content, model);
			sleep(1000); //sleep a while to wait for the file monitor to update the system properties.
			assertThat(service.getOne()).isEqualTo(content);
			assertThat(systemProperties.getProperty("test", "")).isEqualTo("My test.");
		} finally {
			//reset system config
			controller.save(oriContent, model);
		}
	}
}
