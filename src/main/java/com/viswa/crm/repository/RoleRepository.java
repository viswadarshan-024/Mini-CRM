package com.viswa.crm.repository;

import com.viswa.crm.model.Role;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface RoleRepository {

    Optional<Role> findById(Long id);

    Optional<Role> findByName(String roleName);
}
