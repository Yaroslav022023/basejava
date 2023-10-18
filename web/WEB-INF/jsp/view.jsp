<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
                                <c:choose>
                                    <c:when test="${not empty company.webSite}">
                                        <h1 class="company-name"><a href="${company.webSite}">${company.name}</a></h1>
                                    </c:when>
                                    <c:otherwise>
                                        <h1 class="company-name">${company.name}</h1>
                                    </c:otherwise>
                                </c:choose>
                                <c:forEach var="period" items="${company.periods}" varStatus="periodStatus">
                                    <div class="period">
                                        <c:if test="${period.getStartDateFormatted() != '01/1970' &&
                                        period.getEndDateFormatted() != '01/1970'}">
                                            <c:choose>
                                                <c:when test="${period.getEndDateFormatted() == '02/1970'}">
                                                    <div class="date">${period.startDateFormatted} - NOW</div>
                                                </c:when>
                                                <c:otherwise>
                                                    <div class="date">${period.startDateFormatted}
                                                        - ${period.endDateFormatted}</div>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:if>
                                        <h3 class="experience-title">${period.title}</h3><br>
                                        <div class="description">${period.description}</div>
                                        <c:if test="${!periodStatus.last}">
                                            <br><br><br>
                                        </c:if>
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