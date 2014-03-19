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
package com.mtt.myapp.user.service;


import static org.fest.assertions.Assertions.assertThat;

import com.mtt.myapp.AbstractSystemTransactionalTest;
import com.mtt.myapp.infra.config.AppConfig;
import com.mtt.myapp.user.model.Permission;
import com.mtt.myapp.user.model.Role;
import com.mtt.myapp.user.model.User;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public class UserServiceTest extends AbstractSystemTransactionalTest {

	@Autowired
	private UserService userService;

	@Autowired
	AppConfig config;

	@Before
	public void before() {

	}

	private User createTestUser(String userId) {
		User user = new User();
		user.setUserId(userId);
		user.setUserName("hello");
		user.setPassword("www");
		user.setEmail("www@test.com");
		user.setRole(Role.SUPER_USER);
		user = userService.save(user);
		assertThat(user.getRole().hasPermission(Permission.GET_ALL_TESTS)).isTrue();
		assertThat(user.getUserId()).isEqualTo(userId);
		return user;
	}

	@Test
	public void testUpdateUser() {
		User user = createTestUser("testId1");

		User user2 = new User();
		user2.setId(user.getId());
		user2.setUserId("hello");
		user2.setPassword("www222");
		user2.setEmail("www@test.com");
		user2.setRole(Role.ADMIN);
		user2 = userService.save(user2);
		User userById = userService.findByUserId("hello");
		assertThat(userById.getId()).isEqualTo(user.getId());
		userService.save(user2);
		userById = userService.findByUserId("hello");
		assertThat(userById.getRole().hasPermission(Permission.GET_ALL_TESTS)).isTrue();
		assertThat(userById.getRole()).isEqualTo(Role.ADMIN);
	}

	@Test
	public void testDeleteUser() {
		final User user = createTestUser("testId3");
		assertThat(user).isNotNull();
		userService.delete(user);
		assertThat(userService.findByUserId(user.getUserId())).isNull();
	}

	@Test
	public void testGetUserListByKeyWord() {
		User user = new User();
		user.setUserId("testIdForNameSearch");
		user.setUserName("testIdForNameSearch");
		user.setPassword("111111");
		user.setEmail("testIdForNameSearch@test.com");
		user.setRole(Role.USER);
		user = userService.save(user);
		assertThat(user.getUserId()).isEqualTo("testIdForNameSearch");

		Page<User> userList = userService.findByNameLike("ForNameSearch", new PageRequest(1, 10));
		assertThat(userList.getContent().size()).isSameAs(1);

		userList = userService.findByNameLike("ForName", new PageRequest(1, 10));
		assertThat(userList.getContent().size()).isSameAs(1);
	}
}
