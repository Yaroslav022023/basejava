package com.basejava.webapp.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Company {
    private String name;
    private String webSite;
    private final List<Period> periods = new ArrayList<>();

    public Company(String nameCompany, String webSite, String tittle, String startDate,
                   String endDate, String description) {
        Period period = new Period(tittle, startDate, endDate, description);
        name = nameCompany;
        this.webSite = webSite;
        periods.add(period);

    }

    public Company(String nameCompany, String webSite, String tittle, String startDate,
                   String endDate) {
        Period period = new Period(tittle, startDate, endDate);
        name = nameCompany;
        this.webSite = webSite;
        periods.add(period);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public List<Period> getPeriods() {
        return periods;
    }

    public void addPeriod(String tittle, String startDate, String endDate, String description) {
        Period period = new Period(tittle, startDate, endDate, description);
        periods.add(period);
    }

    public void addPeriod(String tittle, String startDate, String endDate) {
        Period period = new Period(tittle, startDate, endDate);
        periods.add(period);
    }

    public void removePeriod(Period period) {
        periods.remove(period);
    }

    public void clear() {
        periods.clear();
    }

    public class Period {
        private String tittle;
        private String startDate;
        private String endDate;
        private String description;

        public Period(String tittle, String startDate, String dateEnd, String description) {
            this.tittle = tittle;
            this.startDate = startDate;
            this.endDate = dateEnd;
            this.description = description;
        }

        public Period(String tittle, String startDate, String dateEnd) {
            this.tittle = tittle;
            this.startDate = startDate;
            this.endDate = dateEnd;
        }

        public String getTittle() {
            return tittle;
        }

        public void setTittle(String tittle) {
            this.tittle = tittle;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        @Override
        public int hashCode() {
            return Objects.hash(getTittle(), getStartDate(), getEndDate(), getDescription());
        }

        @Override
        public String toString() {
            return "tittle='" + tittle + "', startDate='" + startDate + "', endDate='" + endDate
                    + "', description='" + description + "'";
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getWebSite(), getPeriods());
    }

    @Override
    public String toString() {
        return "name='" + name + "', webSite='" + webSite + "', periods='" + periods + "'";
    }
}