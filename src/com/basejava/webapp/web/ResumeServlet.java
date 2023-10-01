package com.basejava.webapp.web;

import com.basejava.webapp.ResumeTestData;
import com.basejava.webapp.model.Resume;
import com.basejava.webapp.storage.SqlStorage;
import com.basejava.webapp.storage.Storage;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

public class ResumeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html>");

        out.println("<head>");
        out.println("<title>Resume</title>");
        out.println("</head>");

        out.println("<body>");
        out.println("<table border='1'>");
        out.println("<caption>Resumes</caption>");
        out.println("<tr>");
        out.println("<th>Uuid</th>");
        out.println("<th>Full Name</th>");
        out.println("</tr>");
        for (Resume resume : createResume()) {
            out.println("<tr>");
            out.println("<td>" + resume.getUuid() + "</td>");
            out.println("<td>" + resume.getFullName() + "</td>");
            out.println("</tr>");
        }
        out.println("</table>");
        out.println("</body>");

        out.println("</html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {

    }

    private static Storage getStorage() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("PostgreSQL JDBC Driver not found!", e);
        }

        Storage sqlStorage;
        File PROPS = new File("/Users/jaroslavyar/javaops/basejava/config/resumes.properties");
        try (InputStream is = new FileInputStream(PROPS)) {
            Properties props = new Properties();
            props.load(is);
            sqlStorage = new SqlStorage(props.getProperty("db.url"), props.getProperty("db.user"),
                    props.getProperty("db.password"));
        } catch (IOException e) {
            throw new IllegalStateException("Error reading from " +
                    PROPS.getAbsolutePath() + ": " + e.getMessage(), e);
        }
        return sqlStorage;
    }

    private static List<Resume> createResume() {
        Storage sqlStorage = getStorage();
        sqlStorage.clear();
        final Resume r1 = ResumeTestData.createResume(UUID.randomUUID().toString(), "FULL_NAME_1");
        final Resume r2 = ResumeTestData.createResume(UUID.randomUUID().toString(), "FULL_NAME_2");
        final Resume r3 = ResumeTestData.createResume(UUID.randomUUID().toString(), "FULL_NAME_3");
        sqlStorage.save(r1);
        sqlStorage.save(r2);
        sqlStorage.save(r3);
        return sqlStorage.getAllSorted();
    }
}