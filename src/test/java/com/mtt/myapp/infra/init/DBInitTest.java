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
package com.mtt.myapp.infra.init;


import static org.fest.assertions.Assertions.assertThat;

import java.util.List;

import com.mtt.myapp.AbstractSystemTransactionalTest;
import com.mtt.myapp.user.model.User;
import com.mtt.myapp.user.repository.UserRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DBInitTest extends AbstractSystemTransactionalTest {

	@Autowired
	private DBInit dbInit;

	@Autowired
	private UserRepository userRepository;


	@Test
	public void initUserDB() {
		dbInit.init();
		List<User> users = userRepository.findAll();

		// Two users should be exist
		assertThat(users.size()).isSameAs(4);
		assertThat(users.get(0).getUserId()).isEqualTo("admin");
		assertThat(users.get(0).getUserId()).isEqualTo("user");
	}
}
