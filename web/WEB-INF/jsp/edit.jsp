<%@ page import="com.basejava.webapp.model.ContactType" %>
<%@ page import="com.basejava.webapp.model.SectionType" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/main.css">
    <jsp:useBean id="resume" type="com.basejava.webapp.model.Resume" scope="request"/>
    <title>Resume ${resume.fullName}</title>
    <script type="text/javascript">
        function validateForm() {
            var nameInput = document.getElementsByName("fullName")[0].value;
            if (!nameInput.trim()) {
                alert("Name cannot be just spaces!");
                return false;
            }
            return true;
        }
    </script>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <form method="post" action="resume" onsubmit="return validateForm();"
          enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="uuid" value="${resume.uuid}">
        <dl>
            <dt>Name:</dt>
            <dd><input type="text" name="fullName" size=50 value="${resume.fullName}" required></dd>
        </dl>
        <h3>Contacts:</h3>
        <c:forEach var="type" items="<%=ContactType.values()%>">
            <dl>
                <dt>${type.contact}</dt>
                <dd><input type="text" name="${type.name()}" size=30 value="${resume.getContact(type)}"></dd>
            </dl>
        </c:forEach>
        <hr>
        <h3>Sections:</h3>
        <c:forEach var="type" items="<%=SectionType.values()%>">
            <dl>
                <dt>${type.section}</dt>
                <c:choose>
                    <c:when test="${type.name() == 'OBJECTIVE' || type.name() == 'PERSONAL'}">
                        <dd><input type="text" name="${type.name()}" size=30 value="${resume.getSection(type)}"></dd>
                    </c:when>
                    <c:when test="${type.name() == 'ACHIEVEMENT' || type.name() == 'QUALIFICATION'}">
                        <dd><textarea name="${type.name()}" rows="4" cols="30">${resume.getSection(type)}</textarea>
                        </dd>
                    </c:when>
                    <c:otherwise>
                    </c:otherwise>
                </c:choose>
            </dl>
        </c:forEach>
        <hr>
        <button type="submit">Save</button>
        <button type="button" onclick="window.history.back()">Cancel</button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>