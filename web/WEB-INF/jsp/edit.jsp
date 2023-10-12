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
    <script type="text/javascript">
        function validateForm() {
            var nameInput = document.getElementsByName("fullName")[0].value;
            if (!nameInput.trim()) {
                alert("Name cannot be just spaces!");
                return false;
            }
            var types = ["EXPERIENCE", "EDUCATION"];
            var forms = ["", "2", "3"];

            for (var t = 0; t < types.length; t++) {
                for (var i = 0; i < forms.length; i++) {
                    var companyName = document.getElementsByName(types[t] + ".name" + forms[i])[0].value;
                    var companyLink = document.getElementsByName(types[t] + ".link" + forms[i])[0].value;
                    var startDate = document.getElementsByName(types[t] + ".startDate" + forms[i])[0].value;
                    var endDate = document.getElementsByName(types[t] + ".endDate" + forms[i])[0].value;
                    var title = document.getElementsByName(types[t] + ".title" + forms[i])[0].value;
                    var description = document.getElementsByName(types[t] + ".description" + forms[i])[0].value;

                    if (companyName.trim()) {
                        if (!companyLink.trim() || !startDate.trim() || !endDate.trim()
                            || !title.trim() || !description.trim()) {
                            alert("Please fill all the fields for the " + types[t] + " section!");
                            return false;
                        }
                    } else {
                        if (companyLink.trim() || startDate.trim() || endDate.trim()
                            || title.trim() || description.trim()) {
                            alert("Please fill all the fields for the " + types[t] + " section!");
                            return false;
                        }
                    }
                }
            }
            return true;
        }
    </script>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <form method="post" action="resume" id="formEdit" onsubmit="return validateForm();"
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
                        <c:set var="forms" value='${Arrays.asList("", "2", "3")}'/>
                        <c:set var="companies" value="${resume.getSection(type).companies}"/>
                        <c:forEach var="form" items="${forms}" varStatus="formStatus">
                            <c:choose>
                                <c:when test="${formStatus.index < companies.size()}">
                                    <c:set var="company" value="${companies[formStatus.index]}"/>
                                    <dd><input type="text" id="companyName"
                                               name="${type.name()}.name${form}"
                                               size=30
                                               value="${company.name}"
                                               placeholder="Name of company"></dd>
                                    <br>
                                    <dd><input type="text" id="companyLink"
                                               name="${type.name()}.link${form}"
                                               size=30
                                               value="${company.webSite}"
                                               placeholder="website"></dd>

                                    <c:forEach var="period" items="${company.periods}">
                                        <br>
                                        <dd><input type="text" id="periodStartDate"
                                                   name="${type.name()}.startDate${form}"
                                                   size=30
                                                   value="${period.startDate}"
                                                   placeholder="start date"></dd>
                                        <dd><input type="text" id="periodEndDate"
                                                   name="${type.name()}.endDate${form}"
                                                   size=30
                                                   value="${period.endDate}"
                                                   placeholder="end date"></dd>
                                        <br>
                                        <dd><input type="text" id="periodTitle"
                                                   name="${type.name()}.title${form}"
                                                   size=30
                                                   value="${period.title}"
                                                   placeholder="title"></dd>
                                        <br>
                                        <c:if test="${type.name() == 'EXPERIENCE'}">
                                            <dd><input type="text" id="periodDescription"
                                                       name="${type.name()}.description${form}"
                                                       size=30 value="${period.description}"
                                                       placeholder="description"></dd>
                                            <br>
                                        </c:if>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <dd><input type="text"
                                               name="${type.name()}.name${form}"
                                               size=30
                                               placeholder="Name of company"></dd>
                                    <br>
                                    <dd><input type="text"
                                               name="${type.name()}.link${form}"
                                               size=30
                                               placeholder="website"></dd>
                                    <br>
                                    <dd><input type="text"
                                               name="${type.name()}.startDate${form}"
                                               size=30
                                               placeholder="start date"></dd>
                                    <dd><input type="text"
                                               name="${type.name()}.endDate${form}"
                                               size=30
                                               placeholder="end date"></dd>
                                    <br>
                                    <dd><input type="text"
                                               name="${type.name()}.title${form}"
                                               size=30
                                               placeholder="title"></dd>
                                    <br>
                                    <c:if test="${type.name() == 'EXPERIENCE'}">
                                        <dd><input type="text"
                                                   name="${type.name()}.description${form}"
                                                   size=30
                                                   placeholder="description"></dd>
                                        <br>
                                    </c:if>
                                </c:otherwise>
                            </c:choose>
                            <c:if test="${!formStatus.last}">
                                <br>More:<br><br>
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