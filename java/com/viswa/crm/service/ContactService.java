package com.viswa.crm.service;

import com.viswa.crm.dto.contact.ContactResponse;
import com.viswa.crm.dto.contact.CreateContactRequest;
import com.viswa.crm.dto.contact.UpdateContactRequest;

import java.util.List;

public interface ContactService {

    ContactResponse createContact(CreateContactRequest request);

    ContactResponse updateContact(Long contactId, UpdateContactRequest request);

    void deleteContact(Long contactId);

    ContactResponse getContactById(Long contactId);

    List<ContactResponse> getContactsByCompany(Long companyId);

    List<ContactResponse> searchContacts(String keyword);
}
