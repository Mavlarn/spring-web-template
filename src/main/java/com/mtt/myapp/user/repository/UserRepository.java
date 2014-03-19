package com.mtt.myapp.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mtt.myapp.user.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(value = "SELECT x FROM User x WHERE x.userName LIKE :name% ORDER BY x.id", countQuery = "SELECT COUNT(x) FROM User x WHERE x.userName LIKE :name%")
	public Page<User> findByNameLike(@Param("name") String name, Pageable page);

	public User findOneByUserId(String userId);

	/**
	 * Find users who are matching to given spec with paging.
	 *
	 * @param spec     spec
	 * @param pageable pageable
	 * @return user list
	 */
	public Page<User> findAll(Specification<User> spec, Pageable pageable);
}
