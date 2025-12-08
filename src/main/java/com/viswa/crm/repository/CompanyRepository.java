package com.viswa.crm.repository;

import com.viswa.crm.model.Company;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository {

    Optional<Company> findById(Long id);

    Optional<Company> findByName(String name);

    List<Company> findAll();

    List<Company> searchByKeyword(String keyword);

    Long save(Company company);

    void update(Company company);

    void deleteById(Long id);

    boolean existsById(Long id);
}
