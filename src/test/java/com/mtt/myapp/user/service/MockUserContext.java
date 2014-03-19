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

import javax.annotation.PostConstruct;

import com.mtt.myapp.user.model.Role;
import com.mtt.myapp.user.model.User;
import com.mtt.myapp.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * user util
 *
 * @author Mavlarn
 */
@Profile("unit-test")
@Component
public class MockUserContext extends UserContext {
	public static final String TEST_USER_ID = "TEST_USER";
	public static final String TEST_USER_TIMEZONE_US = "America/New_York";
	public static final String TEST_USER_TIMEZONE_ZH = "Asia/Shanghai";

	@Autowired
	protected UserRepository userRepository;

	@Autowired
	@Qualifier("passwordEncoder")
	protected PasswordEncoder passwordEncoder;

	@PostConstruct
	public void init() {
		User user = userRepository.findOneByUserId(TEST_USER_ID);
		if (user == null) {
			user = new User();
			user.setUserId(TEST_USER_ID);
			user.setUserName("TEST_USER");
			user.setEmail("TEST_USER@nhn.com");
			user.setPassword("123");
			user.setRole(Role.USER);

			String encodePassword = passwordEncoder.encode(user.getPassword());
			user.setPassword(encodePassword);

			userRepository.save(user);
		}
	}

	public User getCurrentUser() {
		User user = userRepository.findOneByUserId(TEST_USER_ID);
		user.setUserLanguage("zh");
		user.setTimezone(TEST_USER_TIMEZONE_ZH);
		return user;
	}
}
