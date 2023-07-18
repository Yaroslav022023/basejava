package com.basejava.webapp.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CompanySection extends Section{
    private final List<Company> companies = new ArrayList<>();

    public CompanySection(String nameCompany, String webSite, String tittle, String startDate,
                          String endDate, String description) {
        Company company = new Company(nameCompany, webSite, tittle, startDate, endDate, description);
        companies.add(company);
    }

    public CompanySection(String nameCompany, String webSite, String tittle, String startDate,
                          String endDate) {
        Company company = new Company(nameCompany, webSite, tittle, startDate, endDate);
        companies.add(company);
    }

    public List<Company> getCompanies() {
        return companies;
    }

    public Company getCompany(String nameCompany) {
        for (Company company : companies) {
            if (nameCompany.equals(company.getName())) {
                return company;
            }
        }
        return null;
    }

    public void addCompany(Company company) {
        companies.add(company);
    }

    public void removeCompany(String nameCompany) {
        companies.removeIf(company -> nameCompany.equals(company.getName()));
    }

    public void clear() {
        companies.clear();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCompanies());
    }

    @Override
    public String toString() {
        return companies.toString();
    }
}