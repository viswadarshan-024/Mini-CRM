package com.viswa.crm.service.impl;

import com.viswa.crm.dto.company.CompanyResponse;
import com.viswa.crm.dto.company.CreateCompanyRequest;
import com.viswa.crm.dto.company.UpdateCompanyRequest;
import com.viswa.crm.model.Company;
import com.viswa.crm.repository.CompanyRepository;
import com.viswa.crm.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    @Override
    @Transactional
    public CompanyResponse createCompany(CreateCompanyRequest request) {

        // Uniqueness check
        companyRepository.findByName(request.getName())
                .ifPresent(c -> {
                    throw new RuntimeException("Company with this name already exists");
                });

        Company company = new Company();
        company.setName(request.getName());
        company.setEmail(request.getEmail());
        company.setPhone(request.getPhone());
        company.setAddress(request.getAddress());
        company.setCreatedAt(LocalDateTime.now());

        Long id = companyRepository.save(company);
        company.setId(id);

        return mapToResponse(company);
    }

    @Override
    @Transactional
    public CompanyResponse updateCompany(Long companyId, UpdateCompanyRequest request) {

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        if (request.getName() != null && !request.getName().equals(company.getName())) {
            companyRepository.findByName(request.getName())
                    .ifPresent(existing -> {
                        throw new RuntimeException("Another company with this name already exists");
                    });
            company.setName(request.getName());
        }

        if (request.getEmail() != null) {
            company.setEmail(request.getEmail());
        }
        if (request.getPhone() != null) {
            company.setPhone(request.getPhone());
        }
        if (request.getAddress() != null) {
            company.setAddress(request.getAddress());
        }

        companyRepository.update(company);
        return mapToResponse(company);
    }

    @Override
    @Transactional
    public void deleteCompany(Long companyId) {

        if (!companyRepository.existsById(companyId)) {
            throw new RuntimeException("Company not found");
        }

        // Need to check if company has contacts or deals before deletion

        companyRepository.deleteById(companyId);
    }

    @Override
    @Transactional(readOnly = true)
    public CompanyResponse getCompanyById(Long companyId) {

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        return mapToResponse(company);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompanyResponse> searchCompanies(String keyword) {

        return companyRepository.searchByKeyword(keyword)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompanyResponse> getAllCompanies() {

        return companyRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private CompanyResponse mapToResponse(Company company) {

        CompanyResponse response = new CompanyResponse();
        response.setId(company.getId());
        response.setName(company.getName());
        response.setEmail(company.getEmail());
        response.setPhone(company.getPhone());
        response.setAddress(company.getAddress());
        response.setCreatedAt(company.getCreatedAt());

        return response;
    }
}
