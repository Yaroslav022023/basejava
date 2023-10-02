package com.basejava.webapp.web;

import com.basejava.webapp.ResumeTestData;
import com.basejava.webapp.model.Resume;
import com.basejava.webapp.storage.Storage;
import com.basejava.webapp.util.Config;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;

public class ResumeServlet extends HttpServlet {
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

    private List<Resume> createResume() {
        storage.clear();
        final Resume r1 = ResumeTestData.createResume(UUID.randomUUID().toString(), "FULL_NAME_1");
        final Resume r2 = ResumeTestData.createResume(UUID.randomUUID().toString(), "FULL_NAME_2");
        final Resume r3 = ResumeTestData.createResume(UUID.randomUUID().toString(), "FULL_NAME_3");
        storage.save(r1);
        storage.save(r2);
        storage.save(r3);
        return storage.getAllSorted();
    }
}