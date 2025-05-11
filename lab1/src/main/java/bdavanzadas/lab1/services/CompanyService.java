package bdavanzadas.lab1.services;

import bdavanzadas.lab1.entities.CompanyEntity;
import bdavanzadas.lab1.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CompanyService {
    @Autowired
    private CompanyRepository companyRepository;

    @Transactional(readOnly = true)
    public List<CompanyEntity> getAllCompanies() {
        List<CompanyEntity> companies = companyRepository.findAll();
        return companies;
    }

    @Transactional(readOnly = true)
    public CompanyEntity findbyid(int id) {
        return companyRepository.findbyid(id);
    }

    @Transactional
    public void saveCompany(CompanyEntity company) {
        companyRepository.save(company);
    }

    @Transactional
    public void updateCompany(CompanyEntity company) {
        companyRepository.update(company);
    }

    @Transactional
    public void deleteCompany(int id) {
        companyRepository.delete(id);
    }

    @Transactional(readOnly = true)
    public List<CompanyEntity> getCompaniesWithMostFailedDeliveries() {
        return companyRepository.getCompaniesWithMostFailedDeliveries();
    }

    @Transactional
    public void updateCompanyMetrics() {
        companyRepository.updateCompanyMetrics();
    }
}