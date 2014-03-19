package com.mtt.myapp.infra.init;

import java.util.Date;

import javax.annotation.PostConstruct;

import com.mtt.myapp.user.model.Role;
import com.mtt.myapp.user.model.User;
import com.mtt.myapp.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Database Initialization. 
 * When the first boot-up, some data(ex: user account) should be inserted into DB.
 * 
 * And... It's the perfect place to upgrade DB.
 * 
 * @author Mavlarn
 * @since 1.0
 */
@Service
public class DBInit {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	protected PasswordEncoder passwordEncoder;

	/**
	 * Initialize DB.
	 */
	@PostConstruct
	@Transactional
	public void init() {
		createDefaultUserIfNecessary();
	}
	
	/**
	 * Create users.
	 * 
	 * @param userId
	 *            userId
	 * @param password
	 *            raw user password
	 * @param role
	 *            role
	 * @param userName
	 *            user name
	 * @param email
	 *            email
	 */
	private void createUser(String userId, String password, Role role, String userName, String email) {
		if (userRepository.findOneByUserId(userId) == null) {
			User user = new User();
			user.setUserId(userId);
			user.setPassword(passwordEncoder.encode(password));
			user.setRole(role);
			user.setUserName(userName);
			user.setEmail(email);
			user.setCreatedDate(new Date());
			userRepository.save(user);
		}

	}

	/**
	 * Create initial users.
	 */
	private void createDefaultUserIfNecessary() {
		// If there is no users.. make admin and user and U, S, A roles.
		if (userRepository.count() < 2) {
			createUser("admin", "admin", Role.ADMIN, "admin", "admin@mtt.com");
			createUser("user", "user", Role.USER, "user", "user@mtt.com");
			createUser("superuser", "superuser", Role.SUPER_USER, "superuser", "superuser@mtt.com");
			createUser("system", "system", Role.SYSTEM_USER, "system", "system@mtt.com");
		}
	}
}
