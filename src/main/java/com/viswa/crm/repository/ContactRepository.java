package com.viswa.crm.repository;

import com.viswa.crm.model.Contact;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


public interface ContactRepository {

    Optional<Contact> findById(Long id);

    Long save(Contact contact);

    void update(Contact contact);

    void deleteById(Long id);

    List<Contact> findByCompanyId(Long companyId);

    List<Contact> searchByKeyword(String keyword);

    boolean existsById(Long id);
    
//    List<Deal> findByDeal(Contact contact);
}
