package com.viswa.crm.repository;

import com.viswa.crm.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


public interface UserRepository {

    Optional<User> findByUsername(String username);

    Optional<User> findById(Long id);

    List<User> findAll();

    Long save(User user);
}
