package com.basejava.webapp.web;

import com.basejava.webapp.exceptions.NotExistStorageException;
import com.basejava.webapp.model.*;
import com.basejava.webapp.sql.SqlHelper;
import com.basejava.webapp.storage.Storage;
import com.basejava.webapp.util.Config;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ResumeServlet extends HttpServlet {
    private static final Logger LOG = Logger.getLogger(SqlHelper.class.getName());
    private Storage storage;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("PostgreSQL JDBC Driver not found!", e);
        }
        storage = Config.getInstance().getStorage();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String uuid = request.getParameter("uuid");
        String fullName = request.getParameter("fullName");
        boolean newResume = false;
        Resume resume;
        try {
            resume = storage.get(uuid);
            resume.setFullName(fullName);
        } catch (NotExistStorageException e) {
            resume = new Resume(uuid, fullName);
            newResume = true;
        }
        for (ContactType type : ContactType.values()) {
            String value = request.getParameter(type.name());
            if (value != null && !value.trim().isEmpty()) {
                resume.addContact(type, value);
            } else {
                resume.getContacts().remove(type);
            }
        }

        SectionType[] sectionTypes = {SectionType.OBJECTIVE, SectionType.PERSONAL,
                SectionType.ACHIEVEMENT, SectionType.QUALIFICATION};
        for (SectionType type : sectionTypes) {
            String value = request.getParameter(type.name());
            if (value != null && !value.trim().isEmpty()) {
                SectionType sectionType = SectionType.valueOf(type.name().trim());
                switch (sectionType) {
                    case OBJECTIVE, PERSONAL -> resume.addSection(sectionType, new TextSection(value));
                    case ACHIEVEMENT, QUALIFICATION ->
                            resume.addSection(sectionType, new ListSection(value.trim().split("\n")));
                }
            } else {
                resume.getSections().remove(type);
            }
        }

        CompanySection experienceSection = (CompanySection) resume.getSection(SectionType.EXPERIENCE);
        if (experienceSection != null) {
            experienceSection.getCompanies().clear();
        }
        int i = 0;
        while (request.getParameter(SectionType.EXPERIENCE.name() + ".name" + i) != null) {
            addInformationIntoResume(request, String.valueOf(i), experienceSection, SectionType.EXPERIENCE, resume);
            i++;
        }

        CompanySection educationSection = (CompanySection) resume.getSection(SectionType.EDUCATION);
        if (educationSection != null) {
            educationSection.getCompanies().clear();
        }
        i = 0;
        while (request.getParameter(SectionType.EDUCATION.name() + ".name" + i) != null) {
            addInformationIntoResume(request, String.valueOf(i), educationSection, SectionType.EDUCATION, resume);
            i++;
        }

        if (experienceSection != null && experienceSection.getCompanies().isEmpty()) {
            resume.getSections().remove(SectionType.EXPERIENCE);
        }
        if (educationSection != null && educationSection.getCompanies().isEmpty()) {
            resume.getSections().remove(SectionType.EDUCATION);
        }

        if (newResume) {
            storage.save(resume);
        } else {
            storage.update(resume);
        }
        response.sendRedirect("resume?uuid=" + uuid + "&action=view");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws javax.servlet.ServletException, IOException {
        String uuid = request.getParameter("uuid");
        String action = request.getParameter("action");
        if (action == null) {
            request.setAttribute("resumes", storage.getAllSorted());
            request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
            return;
        }
        Resume resume;
        switch (action) {
            case "delete" -> {
                storage.delete(uuid);
                response.sendRedirect("resume");
                return;
            }
            case "view", "edit" -> {
                resume = storage.get(uuid);
                int extraForm = 1;
                request.setAttribute("extraForm", extraForm);
            }
            case "create" -> {
                resume = new Resume();
                int extraForm = 1;
                request.setAttribute("extraForm", extraForm);
            }
            default -> throw new IllegalArgumentException("Action " + action + " is illegal");
        }
        request.setAttribute("resume", resume);
        request.getRequestDispatcher(
                ("view".equals(action) ? "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp")
        ).forward(request, response);
    }

    private void addInformationIntoResume(HttpServletRequest request, String formNumber,
                                          CompanySection section, SectionType sectionType, Resume resume) {
        String nameCompany = getStringParameter(request, sectionType, ".name" + formNumber);
        if (nameCompany != null && !nameCompany.trim().isEmpty()) {
            if (section == null) {
                section = new CompanySection();
            }
            Company company = new Company();
            company.setName(nameCompany);
            company.setWebSite(getStringParameter(request, sectionType, ".link" + formNumber));

            int i = 0;
            while (true) {
                Company.Period period = new Company.Period();
                String parameterValue = request.getParameter(sectionType.name() + ".title" + formNumber + "_" + i);
                if (parameterValue == null || parameterValue.trim().isEmpty()) {
                    break;
                }
                String periodNumber = formNumber + "_" + i;
                period.setTitle(getStringParameter(request, sectionType, ".title" + periodNumber));
                period.setStartDate(getDateParameter(request, sectionType, ".startDate" + periodNumber));
                period.setEndDate(getDateParameter(request, sectionType, ".endDate" + periodNumber));
                if (sectionType.name().equals("EXPERIENCE")) {
                    period.setDescription(getStringParameter(request, sectionType,
                            ".description" + periodNumber));
                }
                if (parameterValue != null && !parameterValue.trim().isEmpty()) {
                    company.addPeriod(period);
                }
                i++;
            }
            section.addCompany(company);
            resume.addSection(sectionType, section);
        }
    }

    private String getStringParameter(HttpServletRequest request, SectionType type, String parameter) {
        return request.getParameter(type.name() + parameter);
    }

    private LocalDate getDateParameter(HttpServletRequest request, SectionType type, String parameter) {
        String dateStr = request.getParameter(type.name() + parameter);
        List<DateTimeFormatter> formatters = Arrays.asList(
                DateTimeFormatter.ofPattern("dd.MM.yyyy"),
                DateTimeFormatter.ofPattern("dd-MM-yyyy"),
                DateTimeFormatter.ofPattern("dd/MM/yyyy"),
                DateTimeFormatter.ofPattern("yyyy.MM.dd"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                DateTimeFormatter.ofPattern("yyyy/MM/dd"),
                DateTimeFormatter.ofPattern("MM.yyyy"),
                DateTimeFormatter.ofPattern("MM-yyyy"),
                DateTimeFormatter.ofPattern("MM/yyyy"),
                DateTimeFormatter.ofPattern("yyyy.MM"),
                DateTimeFormatter.ofPattern("yyyy-MM"),
                DateTimeFormatter.ofPattern("yyyy/MM")
        );
        for (DateTimeFormatter formatter : formatters) {
            try {
                if (formatter.toString().contains("dd")) {
                    return LocalDate.parse(dateStr, formatter);
                } else {
                    YearMonth yearMonth = YearMonth.parse(dateStr, formatter);
                    return yearMonth.atDay(1);
                }
            } catch (DateTimeParseException e) {
            }
        }
        if (parameter.matches("^\\.endDate\\d+_\\d+$") && dateStr.equalsIgnoreCase("Now".trim())) {
            return LocalDate.of(1970, 2, 2);
        }
        LOG.log(Level.WARNING, "Invalid date format: " + dateStr);
        return LocalDate.of(1970, 1, 1);
    }
}