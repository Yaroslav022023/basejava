<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/main.css">
    <jsp:useBean id="resume" type="com.basejava.webapp.model.Resume" scope="request"/>
    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <h2>${resume.fullName}&nbsp;<a href="resume?uuid=${resume.uuid}&action=edit"><img src="img/pencil.png"></a></h2>
    <p>
        <c:forEach var="contactEntry" items="${resume.contacts}">
            <jsp:useBean id="contactEntry"
                         type="java.util.Map.Entry<com.basejava.webapp.model.ContactType, java.lang.String>"/>
            <c:out value="${contactEntry.key.toHtml(contactEntry.value)}" escapeXml="false"/><br/>
        </c:forEach>
    <p>
    <hr>
    <div>
        <c:forEach var="sectionEntry" items="${resume.sections}">
            <jsp:useBean id="sectionEntry"
                         type="java.util.Map.Entry<com.basejava.webapp.model.SectionType,
                   com.basejava.webapp.model.Section >"/>
            <c:if test="${sectionEntry.value != null}">
                <%
                    String sectionName = sectionEntry.getKey().getSection();
                %>
                <h2><%=sectionName%>
                </h2>
                <c:choose>
                    <c:when test="${sectionEntry.key == 'OBJECTIVE' || sectionEntry.key == 'PERSONAL'}">
                        ${sectionEntry.value}
                    </c:when>
                    <c:when test="${sectionEntry.key == 'ACHIEVEMENT' || sectionEntry.key == 'QUALIFICATION'}">
                        <ul>
                            <c:forEach var="item" items="${sectionEntry.value.texts}">
                                <li>${item}</li>
                            </c:forEach>
                        </ul>
                    </c:when>
                    <c:otherwise>
                    </c:otherwise>
                </c:choose>
            </c:if>
        </c:forEach>
    </div>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>