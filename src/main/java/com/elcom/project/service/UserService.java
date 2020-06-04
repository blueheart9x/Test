package com.elcom.project.service;

import com.elcom.project.model.User;
import java.util.List;
//import java.util.Optional;

/**
 *
 * @author anhdv
 */
public interface UserService {

    List<User> findAll(Integer currentPage, Integer rowsPerPage, String sort);

    User findById(Long id);
    
    void save(User user);

    void remove(User user);
    
    boolean insertTest();
    
    //User cacheGet(Long id);
}