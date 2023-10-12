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
                            <c:forEach var="listSection" items="${sectionEntry.value.texts}">
                                <li>${listSection}</li>
                            </c:forEach>
                        </ul>
                    </c:when>
                    <c:when test="${sectionEntry.key == 'EXPERIENCE' || sectionEntry.key == 'EDUCATION'}">
                        <ul>
                            <c:forEach var="company" items="${sectionEntry.value.companies}" varStatus="loop">
                                <h1 class="company-name"><a href="${company.webSite}">${company.name}</a></h1>
                                <c:forEach var="period" items="${company.periods}">
                                    <div class="period">
                                        <div class="date">${period.startDate} - ${period.endDate}</div>
                                        <h3 class="experience-title">${period.title}</h3><br>
                                        <div class="description">${period.description}</div>
                                    </div>
                                </c:forEach>
                                <c:if test="${!loop.last}">
                                    <hr>
                                </c:if>
                            </c:forEach>
                        </ul>
                    </c:when>
                </c:choose>
            </c:if>
        </c:forEach>
    </div>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>