package com.elcom.project.service.impl;

import com.elcom.project.model.User;
import com.elcom.project.repository.UserCustomizeRepository;
import com.elcom.project.repository.UserRepository;
import com.elcom.project.service.UserService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 *
 * @author Admin
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserCustomizeRepository userCustomizeRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserCustomizeRepository userCustomizeRepository) {
        this.userRepository = userRepository;
        this.userCustomizeRepository = userCustomizeRepository;
    }

    @Override
    public List<User> findAll(Integer currentPage, Integer rowsPerPage, String sort) {
        
        if( currentPage > 0 ) currentPage--;
        
        Pageable paging = PageRequest.of(currentPage, rowsPerPage, Sort.by(sort).descending());
        
        Page<User> pagedResult = userRepository.findAll(paging);
        
        if(pagedResult.hasContent())
            return pagedResult.getContent();
        else
            return new ArrayList<>();
        //return (List<User>) userRepository.findAll(paging);
    }

    @Override
    public User findById(Long id) {
        return userCustomizeRepository.findById(id);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public void remove(User user) {
        userRepository.delete(user);
    }
    
    @Override
    public boolean insertTest() {
        return userCustomizeRepository.insertTest();
    }
}
