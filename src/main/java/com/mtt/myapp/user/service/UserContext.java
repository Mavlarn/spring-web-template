package com.mtt.myapp.user.service;


import com.mtt.myapp.user.model.User;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * User Context which return current user.
 *
 * @author Mavlarn
 * @since 1.0
 */
@Component
public class UserContext {

//	@Autowired
//	private UserService userService;

	/**
	 * Get current user object from context.
	 * 
	 * @return current user;
	 */
	public User getCurrentUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null) {
			throw new AuthenticationCredentialsNotFoundException("No authentication");
		}
		Object principal = auth.getPrincipal();
		if (principal instanceof User) {
			User currUser = (User)principal;
			return currUser;
//		} else if (principal instanceof UserDetails) {
//			String userId = ((UserDetails)principal).getUsername();
//			return userService.findByUserId(userId);
		} else {
			throw new AuthenticationCredentialsNotFoundException("Invalid authentication with " +  principal);
		}
	}
}
