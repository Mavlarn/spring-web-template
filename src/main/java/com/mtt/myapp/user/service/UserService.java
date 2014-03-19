package com.mtt.myapp.user.service;

import com.mtt.myapp.exception.ResourceNotFoundException;
import com.mtt.myapp.user.model.User;
import com.mtt.myapp.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected PasswordEncoder passwordEncoder;

	public User save(User user) {
		String encPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encPassword);
		return userRepository.save(user);
	}

	public void saveWithEncPassword(User user) {
		userRepository.save(user);
	}

	public void update(User updatedUser) {
		String encPassword = passwordEncoder.encode(updatedUser.getPassword());
		updatedUser.setPassword(encPassword);

		User userInDB = userRepository.findOneByUserId(updatedUser.getUserId());
		userInDB.merge(updatedUser);
		userRepository.save(userInDB);
	}

    public User findByUserId(String userId) {
        User user = userRepository.findOneByUserId(userId);
        if (user == null) {
            throw new ResourceNotFoundException("User [userId=" + userId + "] is not found.");
        }
        return user;
    }

    @Transactional(readOnly = true)
    public Page<User> findAll(Pageable pageable) {
        Page<User> page = userRepository.findAll(pageable);
        return page;
    }

    @Transactional(readOnly = true)
    public Page<User> findByNameLike(String name, Pageable pageable) {
        String query = name; // TODO escape
        Page<User> page = userRepository.findByNameLike(query, pageable);
        return page;
    }

    public void delete(User user) {
        userRepository.delete(user);
    }

	public void deleteSome(String userIds) {
		String[] ids = userIds.split(",");
		for (String userId: ids) {
			User user = userRepository.findOneByUserId(userId);
			userRepository.delete(user);
		}
	}

}
