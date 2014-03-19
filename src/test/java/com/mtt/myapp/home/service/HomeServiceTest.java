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
package com.mtt.myapp.home.service;


import static org.fest.assertions.Assertions.assertThat;

import java.io.IOException;
import java.util.List;

import com.mtt.myapp.AbstractSystemTransactionalTest;
import com.mtt.myapp.home.model.PanelEntry;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class HomeServiceTest extends AbstractSystemTransactionalTest {

	@Autowired
	private HomeService homeService;

	@Test
	public void testHome() throws IOException {
		List<PanelEntry> leftPanelEntries = homeService.getLeftPanelEntries("http://ngrinder.642.n7.nabble.com/ngrinder-user-en-f50.xml");
		assertThat(leftPanelEntries.size()).isGreaterThan(2);
		assertThat(leftPanelEntries.size()).isLessThanOrEqualTo(8);

		List<PanelEntry> rightPanel = homeService.getRightPanelEntries("http://ngrinder.642.n7.nabble.com/ngrinder-user-en-f50.xml");
		assertThat(rightPanel.size()).isGreaterThan(2);
		assertThat(rightPanel.size()).isLessThanOrEqualTo(8);
	}
}
