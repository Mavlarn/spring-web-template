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
package com.mtt.myapp.user.repository;


import static org.fest.assertions.Assertions.assertThat;

import java.util.Date;
import java.util.List;

import com.mtt.myapp.AbstractSystemTransactionalTest;
import com.mtt.myapp.user.model.Role;
import com.mtt.myapp.user.model.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;

public class UserTest extends AbstractSystemTransactionalTest {

	@Autowired
	public UserRepository userRepository;

	@Test
	public void testUser() {
		User user = new User();
		user.setUserName("MyName1");
		user.setEmail("junoyoon@gmail.com");
		user.setCreatedUser(getUser("user"));
		user.setCreatedDate(new Date());
		user.setUserId("hello");
		user.setRole(Role.USER);
		userRepository.save(user);
		User user2 = new User();
		user2.setUserId("hello2");
		user2.setUserName("MyName2");
		user2.setEmail("junoyoon@paran.com");
		user2.setCreatedUser(getUser("user"));
		user2.setCreatedDate(new Date());
		user2.setRole(Role.USER);
		userRepository.save(user2);

		assertThat(userRepository.count()).isSameAs(2L);
		Pageable page = new PageRequest(1, 10);
		List<User> list = userRepository.findAll(UserSpecification.emailLike("gmail"), page).getContent();
		assertThat(list.size()).isSameAs(1);

		list = userRepository.findAll(Specifications.where(UserSpecification.emailLike("@paran")).and(
				UserSpecification.nameLike("MyName2")), page).getContent();
		assertThat(list.size()).isSameAs(1);

	}
}
