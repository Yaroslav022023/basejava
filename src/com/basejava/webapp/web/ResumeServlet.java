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

        CompanySection companySectionExperience = new CompanySection();
        CompanySection companySectionEducation = new CompanySection();
        String[] allForms = {"", "2", "3"};
        for (String form : allForms) {
            addInformationIntoResume(request, form, companySectionExperience, companySectionEducation, resume);
        }

        if (companySectionExperience.getCompanies().isEmpty()) {
            resume.getSections().remove(SectionType.EXPERIENCE);
        }
        if (companySectionEducation.getCompanies().isEmpty()) {
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
            case "view", "edit" -> resume = storage.get(uuid);
            case "create" -> resume = new Resume();
            default -> throw new IllegalArgumentException("Action " + action + " is illegal");
        }
        request.setAttribute("resume", resume);
        request.getRequestDispatcher(
                ("view".equals(action) ? "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp")
        ).forward(request, response);
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
        LOG.log(Level.WARNING, "Invalid date format: " + dateStr);
        throw new IllegalArgumentException("Invalid date format: " + dateStr);
    }

    private void addInformationIntoResume(HttpServletRequest request, String form,
                                          CompanySection experience, CompanySection education, Resume resume) {
        SectionType[] sectionTypes = {SectionType.EXPERIENCE, SectionType.EDUCATION};
        for (SectionType currentType : sectionTypes) {
            String nameCompany = getStringParameter(request, currentType, ".name" + form);
            if (nameCompany != null && !nameCompany.trim().isEmpty()) {
                SectionType sectionTypeCompany = SectionType.valueOf(currentType.name().trim());
                switch (sectionTypeCompany) {
                    case EXPERIENCE -> {
                        experience.addCompany(new Company(
                                nameCompany,
                                getStringParameter(request, currentType, ".link" + form),
                                getStringParameter(request, currentType, ".title" + form),
                                getDateParameter(request, currentType, ".startDate" + form),
                                getDateParameter(request, currentType, ".endDate" + form),
                                getStringParameter(request, currentType, ".description" + form)
                        ));
                        resume.addSection(SectionType.EXPERIENCE, experience);
                    }
                    case EDUCATION -> {
                        education.addCompany(new Company(
                                nameCompany,
                                getStringParameter(request, currentType, ".link" + form),
                                getStringParameter(request, currentType, ".title" + form),
                                getDateParameter(request, currentType, ".startDate" + form),
                                getDateParameter(request, currentType, ".endDate" + form)
                        ));
                        resume.addSection(SectionType.EDUCATION, education);
                    }
                }
            }
        }
    }
}