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
package com.mtt.myapp.common.model;


import static org.fest.assertions.Assertions.assertThat;

import java.io.File;

import com.mtt.myapp.common.constant.SystemConstants;
import com.mtt.myapp.infra.config.AppConfig;
import com.mtt.myapp.infra.config.AppHome;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

/**
 * {@link Home} Test Class
 * 
 * @author JunHo Yoon
 * 
 */
@ActiveProfiles("unit-test")
@ContextConfiguration("classpath:applicationContext.xml")
public class HomeTest extends AbstractJUnit4SpringContextTests implements SystemConstants {

	@Autowired
	private AppHome home;

	@Test
	public void testHomeFolderGeneration() {
		// Given
		// Home home = config.getHome();
		// When
		File homeDirectory = home.getDirectory();
		// Then
		assertThat(homeDirectory.getName()).isEqualToIgnoringCase("");
	}
}
