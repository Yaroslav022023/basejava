package com.basejava.webapp.model;

import com.basejava.webapp.exceptions.ExistPeriodException;
import com.basejava.webapp.util.LocalDateAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
public class Company implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String name;
    private String webSite;
    private final List<Period> periods = new ArrayList<>();

    public Company() {
    }

    public Company(String name, String webSite, String tittle, LocalDate startDate,
                   LocalDate endDate, String description) {
        Period period = new Period(tittle, startDate, endDate, description);
        this.name = name;
        this.webSite = webSite;
        periods.add(period);

    }

    public Company(String name, String webSite, String tittle, LocalDate startDate,
                   LocalDate endDate) {
        Period period = new Period(tittle, startDate, endDate);
        this.name = name;
        this.webSite = webSite;
        periods.add(period);
    }

    public final String getName() {
        return name;
    }

    public final void setName(String name) {
        this.name = name;
    }

    public final String getWebSite() {
        return webSite;
    }

    public final void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public final List<Period> getPeriods() {
        return periods;
    }

    public final void addPeriod(Period period) {
        periods.add(period);
    }

    public final void addPeriod(String tittle, LocalDate startDate, LocalDate endDate) {
        Objects.requireNonNull(tittle, "tittle must be not null");

        for (Period period : periods) {
            if (period.getStartDate().equals(startDate) || period.getEndDate().equals(endDate)) {
                throw new ExistPeriodException();
            }
        }
        Period period = new Period(tittle, startDate, endDate);
        periods.add(period);
    }

    public final void addPeriod(String tittle, LocalDate startDate, LocalDate endDate, String description) {
        Objects.requireNonNull(tittle, "tittle must be not null");
        Objects.requireNonNull(description, "description must be not null");

        for (Period period : periods) {
            if (period.getStartDate().equals(startDate) || period.getEndDate().equals(endDate)) {
                throw new ExistPeriodException();
            }
        }
        Period period = new Period(tittle, startDate, endDate, description);
        periods.add(period);
    }

    public final void removePeriod(Period period) {
        periods.remove(period);
    }

    public final void clear() {
        periods.clear();
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Period implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;
        private String title;
        @XmlJavaTypeAdapter(LocalDateAdapter.class)
        private LocalDate startDate;
        @XmlJavaTypeAdapter(LocalDateAdapter.class)
        private LocalDate endDate;
        private String description;

        public Period() {
        }

        public Period(String title, LocalDate startDate, LocalDate dateEnd, String description) {
            this.title = title;
            this.startDate = startDate;
            this.endDate = dateEnd;
            this.description = description;
        }

        public Period(String title, LocalDate startDate, LocalDate dateEnd) {
            this.title = title;
            this.startDate = startDate;
            this.endDate = dateEnd;
        }

        public final String getTitle() {
            return title;
        }

        public final void setTitle(String title) {
            this.title = title;
        }

        public final LocalDate getStartDate() {
            return startDate;
        }

        public final void setStartDate(LocalDate startDate) {
            this.startDate = startDate;
        }

        public final LocalDate getEndDate() {
            return endDate;
        }

        public final void setEndDate(LocalDate endDate) {
            this.endDate = endDate;
        }

        public final String getDescription() {
            return description;
        }

        public final void setDescription(String description) {
            this.description = description;
        }

        public String getStartDateFormatted() {
            YearMonth ymStart = YearMonth.from(startDate);
            return ymStart.format(DateTimeFormatter.ofPattern("MM/yyyy"));
        }

        public String getEndDateFormatted() {
            YearMonth ymEnd = YearMonth.from(endDate);
            return ymEnd.format(DateTimeFormatter.ofPattern("MM/yyyy"));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Period period = (Period) o;
            return Objects.equals(getTitle(), period.getTitle()) &&
                    Objects.equals(getStartDate(), period.getStartDate()) &&
                    Objects.equals(getEndDate(), period.getEndDate()) &&
                    Objects.equals(getDescription(), period.getDescription());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getTitle(), getStartDate(), getEndDate(), getDescription());
        }

        @Override
        public String toString() {
            return "tittle='" + title + "', startDate='" + startDate + "', endDate='" + endDate
                    + "', description='" + description + "'";
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return Objects.equals(getName(), company.getName()) &&
                Objects.equals(getWebSite(), company.getWebSite()) &&
                Objects.equals(getPeriods(), company.getPeriods());
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