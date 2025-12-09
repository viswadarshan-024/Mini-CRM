package com.viswa.crm.service.impl;

import com.viswa.crm.dto.contact.ContactResponse;
import com.viswa.crm.dto.contact.CreateContactRequest;
import com.viswa.crm.dto.contact.UpdateContactRequest;
import com.viswa.crm.model.Company;
import com.viswa.crm.model.Contact;
import com.viswa.crm.repository.CompanyRepository;
import com.viswa.crm.repository.ContactRepository;
import com.viswa.crm.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;
    private final CompanyRepository companyRepository;

    @Override
    @Transactional
    public ContactResponse createContact(CreateContactRequest request) {

        if (!companyRepository.existsById(request.getCompanyId())) {
            throw new RuntimeException("Company not found");
        }

        Company company = new Company();
        company.setId(request.getCompanyId());

        Contact contact = new Contact();
        contact.setCompany(company);
        contact.setName(request.getName());
        contact.setEmail(request.getEmail());
        contact.setPhone(request.getPhone());
        contact.setJobTitle(request.getJobTitle());
        contact.setCreatedAt(LocalDateTime.now());

        Long id = contactRepository.save(contact);
        contact.setId(id);

        return mapToResponse(contact, null);
    }

    @Override
    @Transactional
    public ContactResponse updateContact(Long contactId, UpdateContactRequest request) {

        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new RuntimeException("Contact not found"));

        if (request.getName() != null) {
            contact.setName(request.getName());
        }

        if (request.getEmail() != null) {
            contact.setEmail(request.getEmail());
        }

        if (request.getPhone() != null) {
            contact.setPhone(request.getPhone());
        }

        if (request.getJobTitle() != null) {
            contact.setJobTitle(request.getJobTitle());
        }

        contactRepository.update(contact);

        return mapToResponse(contact, null);
    }

    @Override
    @Transactional
    public void deleteContact(Long contactId) {

        if (!contactRepository.existsById(contactId)) {
            throw new RuntimeException("Contact not found");
        }

        // Do not allow deletion if contact has active deals

        contactRepository.deleteById(contactId);
    }

    @Override
    @Transactional(readOnly = true)
    public ContactResponse getContactById(Long contactId) {

        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new RuntimeException("Contact not found"));

        String companyName = null;
        if (contact.getCompany() != null) {
            companyName = "UNKNOWN";
        }

        return mapToResponse(contact, companyName);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContactResponse> getContactsByCompany(Long companyId) {

        if (!companyRepository.existsById(companyId)) {
            throw new RuntimeException("Company not found");
        }

        return contactRepository.findByCompanyId(companyId)
                .stream()
                .map(c -> mapToResponse(c, null))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContactResponse> searchContacts(String keyword) {

        return contactRepository.searchByKeyword(keyword)
                .stream()
                .map(c -> mapToResponse(c, null))
                .collect(Collectors.toList());
    }

    private ContactResponse mapToResponse(Contact contact, String companyName) {

        return ContactResponse.builder()
                .id(contact.getId())
                .companyId(
                        contact.getCompany() != null ? contact.getCompany().getId() : null
                )
                .companyName(companyName)
                .name(contact.getName())
                .email(contact.getEmail())
                .phone(contact.getPhone())
                .jobTitle(contact.getJobTitle())
                .createdAt(contact.getCreatedAt())
                .build();
    }

}
