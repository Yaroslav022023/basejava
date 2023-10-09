<%@ page import="com.basejava.webapp.model.ContactType" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/main.css">
    <title>List resumes</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section class="content-section">
    <div class="button-and-table-container">
        <div class="button-container">
            <a class="create-resume" href="resume?action=create">
                <p class="create-resume-title">Create Resume</p>
            </a>
        </div>
        <table class="resume-table" border="1" cellpadding="8" cellspacing="0">
            <tr>
                <th>Name</th>
                <th>Email</th>
                <th></th>
                <th></th>
            </tr>
            <c:forEach var="resume" items="${resumes}">
                <jsp:useBean id="resume" type="com.basejava.webapp.model.Resume"/>
                <tr>
                    <td><a href="resume?uuid=${resume.uuid}&action=view">${resume.fullName}</a></td>
                    <td><%=ContactType.EMAIL.toHtml(resume.getContact(ContactType.EMAIL))%>
                    </td>
                    <td><a href="resume?uuid=${resume.uuid}&action=edit"><img src="img/pencil.png"></a></td>
                    <td><a href="resume?uuid=${resume.uuid}&action=delete"><img src="img/delete.png"></a></td>
                </tr>
            </c:forEach>
        </table>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>