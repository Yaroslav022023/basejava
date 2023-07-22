package com.basejava.webapp.model;

import com.basejava.webapp.exceptions.NotExistCompanyException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CompanySection extends Section{
    private final List<Company> companies = new ArrayList<>();

    public CompanySection(String nameCompany, String webSite, String tittle, String startDate,
                          String endDate, String description) {
        Objects.requireNonNull(nameCompany, "nameCompany must be not null");
        Objects.requireNonNull(tittle, "tittle must be not null");
        Objects.requireNonNull(startDate, "startDate must be not null");
        Objects.requireNonNull(endDate, "endDate must be not null");
        Objects.requireNonNull(description, "description must be not null");

        Company company = new Company(nameCompany, webSite, tittle, startDate, endDate, description);
        companies.add(company);
    }

    public CompanySection(String nameCompany, String webSite, String tittle, String startDate,
                          String endDate) {
        Objects.requireNonNull(nameCompany, "nameCompany must be not null");
        Objects.requireNonNull(tittle, "tittle must be not null");
        Objects.requireNonNull(startDate, "startDate must be not null");
        Objects.requireNonNull(endDate, "endDate must be not null");

        Company company = new Company(nameCompany, webSite, tittle, startDate, endDate);
        companies.add(company);
    }

    public final List<Company> getCompanies() {
        return companies;
    }

    public final Company getCompany(String nameCompany) {
        for (Company company : companies) {
            if (nameCompany.equals(company.getName())) {
                return company;
            }
        }
        throw new NotExistCompanyException();
    }

    public final void addCompany(Company company) {
        companies.add(company);
    }

    public final boolean removeCompany(String nameCompany) {
        return companies.removeIf(company -> nameCompany.equals(company.getName()));
    }

    public final void clear() {
        companies.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompanySection that = (CompanySection) o;
        return getCompanies().equals(that.getCompanies());
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