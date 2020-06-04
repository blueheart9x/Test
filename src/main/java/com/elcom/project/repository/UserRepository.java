package com.elcom.project.repository;

import com.elcom.project.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Admin
 */
@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
}
