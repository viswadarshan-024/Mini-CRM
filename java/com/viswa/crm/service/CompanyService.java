package com.viswa.crm.service;

import com.viswa.crm.dto.company.CompanyResponse;
import com.viswa.crm.dto.company.CreateCompanyRequest;
import com.viswa.crm.dto.company.UpdateCompanyRequest;

import java.util.List;

public interface CompanyService {

    CompanyResponse createCompany(CreateCompanyRequest request);

    CompanyResponse updateCompany(Long companyId, UpdateCompanyRequest request);

    void deleteCompany(Long companyId);

    CompanyResponse getCompanyById(Long companyId);

    List<CompanyResponse> searchCompanies(String keyword);

    List<CompanyResponse> getAllCompanies();
}
