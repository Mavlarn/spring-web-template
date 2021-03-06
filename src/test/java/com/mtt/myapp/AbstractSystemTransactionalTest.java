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
package com.mtt.myapp;

import javax.sql.DataSource;

import com.mtt.myapp.common.constant.SystemConstants;
import com.mtt.myapp.user.model.User;
import com.mtt.myapp.user.repository.UserRepository;
import com.mtt.myapp.user.service.UserContext;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;


/**
 * This class is used as base class for test case,and it will initialize the DB
 * related config, like datasource, and it will start a transaction for every
 * test function, and rollback after the execution.
 *
 * @author Mavlarn
 */
@ActiveProfiles("unit-test")
@ContextConfiguration({"classpath:applicationContext.xml"})
abstract public class AbstractSystemTransactionalTest extends AbstractTransactionalJUnit4SpringContextTests implements
		SystemConstants {
	protected static final Logger LOG = LoggerFactory.getLogger(AbstractSystemTransactionalTest.class);

	static {
		System.setProperty("unit-test", "true");
	}

	@Autowired
	protected UserRepository userRepository;

	protected User testUser = null;

	@Before
	public void beforeSetSecurity() {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("admin", null);
		SecurityContextImpl context = new SecurityContextImpl();
		context.setAuthentication(token);
		SecurityContextHolder.setContext(context);
	}

	@Autowired
	private UserContext userContext;

	@Autowired
	@Override
	public void setDataSource(@Qualifier("dataSource") DataSource dataSource) {
		super.setDataSource(dataSource);
	}

	public User getUser(String userId) {
		return userRepository.findOneByUserId(userId);
	}

	public User getTestUser() {
		if (testUser == null) {
			testUser = userContext.getCurrentUser();
		}
		return testUser;
	}

	public User getAdminUser() {
		return userRepository.findOneByUserId("admin");
	}

	public void sleep(long milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			LOG.error("error:", e);
		}
	}

}
