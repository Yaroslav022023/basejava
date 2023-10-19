<%@ page import="com.basejava.webapp.model.ContactType" %>
<%@ page import="com.basejava.webapp.model.SectionType" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Arrays" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/main.css">
    <jsp:useBean id="resume" type="com.basejava.webapp.model.Resume" scope="request"/>
    <title>Resume ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <form method="post" action="resume" id="formEdit"
          enctype="application/x-www-form-urlencoded">
        <input type="hidden"
               name="uuid"
               value="${resume.uuid}">
        <dl>
            <dt>Name:</dt>
            <dd><input type="text"
                       name="fullName"
                       size=50
                       value="${resume.fullName}"
                       placeholder="name"
                       required></dd>
        </dl>
        <h3>Contacts:</h3>
        <c:forEach var="type" items="<%=ContactType.values()%>">
            <dl>
                <dt>${type.contact}</dt>
                <dd><input type="text"
                           name="${type.name()}"
                           size=30
                           value="${resume.getContact(type)}"
                           placeholder="${type.name()}"></dd>
            </dl>
        </c:forEach>
        <hr>
        <h2>Sections:</h2>
        <c:forEach var="type" items="<%=SectionType.values()%>">
            <dl>
                <h3>
                    <dt>${type.section}</dt>
                </h3>
                <c:choose>
                    <c:when test="${type.name() == 'OBJECTIVE' || type.name() == 'PERSONAL'}">
                        <dd><input type="text"
                                   name="${type.name()}"
                                   size=30
                                   value="${resume.getSection(type)}"
                                   placeholder="text"></dd>
                    </c:when>
                    <c:when test="${type.name() == 'ACHIEVEMENT' || type.name() == 'QUALIFICATION'}">
                        <dd><textarea name="${type.name()}"
                                      rows="4"
                                      cols="30"
                                      placeholder="text">${resume.getSection(type)}</textarea>
                        </dd>
                    </c:when>
                    <c:when test="${type.name() == 'EXPERIENCE' || type.name() == 'EDUCATION'}">
                        <c:set var="amountForms" value="${resume.getSection(type).companies.size() + extraForm}"/>
                        <c:set var="companies" value="${resume.getSection(type).companies}"/>
                        <c:forEach begin="0" end="${amountForms - 1}" varStatus="formStatus">
                            <c:choose>
                                <c:when test="${formStatus.index < resume.getSection(type).companies.size()}">
                                    <c:set var="company" value="${companies[formStatus.index]}"/>
                                    <dd><input type="text"
                                               id="companyName"
                                               name="${type.name()}.name${formStatus.index}"
                                               size=30
                                               value="${company.name}"
                                               placeholder="Name of company"></dd>
                                    <br>
                                    <dd><input type="text"
                                               id="companyLink"
                                               name="${type.name()}.link${formStatus.index}"
                                               size=30
                                               value="${company.webSite}"
                                               placeholder="website"></dd>
                                    <c:choose>
                                        <c:when test="${company.periods.size() > 0}">
                                            <c:forEach var="period" items="${company.periods}" varStatus="periodStatus">
                                                <c:set var="periodNumber"
                                                       value="${formStatus.index}_${periodStatus.index}"/>
                                                <br>
                                                <dd><input type="text"
                                                           id="periodStartDate"
                                                           name="${type.name()}.startDate${periodNumber}"
                                                           size=30
                                                <c:choose>
                                                <c:when test="${period.getStartDateFormatted() != '01/1970'}">
                                                           value="${period.getStartDateFormatted()}"
                                                </c:when>
                                                <c:otherwise>
                                                           value=""
                                                </c:otherwise>
                                                </c:choose>
                                                           placeholder="Start date: MM/YYYY"></dd>
                                                <dd><input type="text"
                                                           id="periodEndDate"
                                                           name="${type.name()}.endDate${periodNumber}"
                                                           size=30
                                                <c:choose>
                                                <c:when test="${period.getEndDateFormatted() != '01/1970' &&
                                                period.getEndDateFormatted() != '02/1970'}">
                                                           value="${period.getEndDateFormatted()}"
                                                </c:when>
                                                <c:when test="${period.getEndDateFormatted() == '01/1970'}">
                                                           value=""
                                                </c:when>
                                                <c:when test="${period.getEndDateFormatted() == '02/1970'}">
                                                           value="NOW"
                                                </c:when>
                                                </c:choose>
                                                           placeholder="End date: MM/YYYY or write 'Now'"></dd>
                                                <br>
                                                <dd><input type="text"
                                                           id="periodTitle"
                                                           name="${type.name()}.title${periodNumber}"
                                                           size=30
                                                           value="${period.title}"
                                                           placeholder="title"></dd>
                                                <br>
                                                <c:if test="${type.name() == 'EXPERIENCE'}">
                                                    <dd><input type="text"
                                                               id="periodDescription"
                                                               name="${type.name()}.description${periodNumber}"
                                                               size=30 value="${period.description}"
                                                               placeholder="description"></dd>
                                                    <br>
                                                </c:if>
                                                <c:if test="${periodStatus.last}">
                                                    <br>
                                                    <dd><input type="text"
                                                               id="periodStartDate3"
                                                               name="${type.name()}.startDate${formStatus.index}_${periodStatus.index + 1}"
                                                               size=30
                                                               placeholder="Start date: MM/YYYY"></dd>
                                                    <dd><input type="text"
                                                               id="periodEndDate3"
                                                               name="${type.name()}.endDate${formStatus.index}_${periodStatus.index + 1}"
                                                               size=30
                                                               placeholder="End date: MM/YYYY or write 'Now'"></dd>
                                                    <br>
                                                    <dd><input type="text"
                                                               id="periodTitle3"
                                                               name="${type.name()}.title${formStatus.index}_${periodStatus.index + 1}"
                                                               size=30
                                                               placeholder="title"></dd>
                                                    <br>
                                                    <c:if test="${type.name() == 'EXPERIENCE'}">
                                                        <dd><input type="text"
                                                                   id="periodDescription3"
                                                                   name="${type.name()}.description${formStatus.index}_${periodStatus.index + 1}"
                                                                   size=30
                                                                   placeholder="description"></dd>
                                                        <br>
                                                    </c:if>
                                                </c:if>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            <br>
                                            <dd><input type="text"
                                                       id="periodStartDate4"
                                                       name="${type.name()}.startDate${formStatus.index}_0"
                                                       size=30
                                                       placeholder="Start date: MM/YYYY"></dd>
                                            <dd><input type="text"
                                                       id="periodEndDate4"
                                                       name="${type.name()}.endDate${formStatus.index}_0"
                                                       size=30
                                                       placeholder="End date: MM/YYYY or write 'Now'"></dd>
                                            <br>
                                            <dd><input type="text"
                                                       id="periodTitle4"
                                                       name="${type.name()}.title${formStatus.index}_0"
                                                       size=30
                                                       placeholder="title"></dd>
                                            <br>
                                            <c:if test="${type.name() == 'EXPERIENCE'}">
                                                <dd><input type="text"
                                                           id="periodDescription4"
                                                           name="${type.name()}.description${formStatus.index}_0"
                                                           size=30
                                                           placeholder="description"></dd>
                                                <br>
                                            </c:if>
                                        </c:otherwise>
                                    </c:choose>
                                </c:when>
                                <c:otherwise>
                                    <dd><input type="text"
                                               id="companyName2"
                                               name="${type.name()}.name${formStatus.index}"
                                               size=30
                                               placeholder="Name of company"></dd>
                                    <br>
                                    <dd><input type="text"
                                               id="companyLink2"
                                               name="${type.name()}.link${formStatus.index}"
                                               size=30
                                               placeholder="website"></dd>
                                    <br>
                                    <dd><input type="text"
                                               id="periodStartDate2"
                                               name="${type.name()}.startDate${formStatus.index}_0"
                                               size=30
                                               placeholder="Start date: MM/YYYY"></dd>
                                    <dd><input type="text"
                                               id="periodEndDate2"
                                               name="${type.name()}.endDate${formStatus.index}_0"
                                               size=30
                                               placeholder="End date: MM/YYYY or write 'Now'"></dd>
                                    <br>
                                    <dd><input type="text"
                                               id="periodTitle2"
                                               name="${type.name()}.title${formStatus.index}_0"
                                               size=30
                                               placeholder="title"></dd>
                                    <br>
                                    <c:if test="${type.name() == 'EXPERIENCE'}">
                                        <dd><input type="text"
                                                   id="periodDescription2"
                                                   name="${type.name()}.description${formStatus.index}_0"
                                                   size=30
                                                   placeholder="description"></dd>
                                        <br>
                                    </c:if>
                                </c:otherwise>
                            </c:choose>
                            <c:if test="${!formStatus.last}">
                                <c:if test="${type.name() == 'EXPERIENCE'}">
                                    <br>Next Company:<br><br>
                                </c:if>
                                <c:if test="${type.name() == 'EDUCATION'}">
                                    <br>Next School:<br><br>
                                </c:if>
                            </c:if>
                        </c:forEach>
                    </c:when>
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
