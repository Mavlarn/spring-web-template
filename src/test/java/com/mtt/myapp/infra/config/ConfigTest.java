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
package com.mtt.myapp.infra.config;

import static org.fest.assertions.Assertions.assertThat;

import java.io.File;

import com.mtt.myapp.common.constant.SystemConstants;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ConfigTest implements SystemConstants {

	private MockConfig config;

	@Autowired
	private AppHome home;

	@Before
	public void before() {
		System.setProperty("unit-test", "");
		config = new MockConfig();
	}

	@Test
	public void testDefaultHome() {
		File oracle = new File(System.getProperty("user.home"), ".mtt");
		assertThat(home.getDirectory()).isDirectory();
		assertThat(home.getDirectory().getAbsolutePath()).isEqualTo(oracle.getAbsolutePath());
	}


}
