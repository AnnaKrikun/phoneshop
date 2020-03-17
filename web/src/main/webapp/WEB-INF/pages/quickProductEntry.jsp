<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<tags:master pageTitle="Quick product entry">
    <c:if test="${not empty errors}">
        <span class="error-message"> <c:out value="${errors}"/></span>
        <br>
        <a href="${pageContext.servletContext.contextPath}/quickProductEntry">Try once again!</a>
    </c:if>
    <form:form method="post" action="${pageContext.servletContext.contextPath}/quickProductEntry/add"
               modelAttribute="quickProductEntry">
        <c:forEach begin="${0}" end="${inputsCount}" varStatus="i">
            <form:label path="phoneIds">PhoneId:</form:label>
            <form:input path="phoneIds" name="phonesId${i.index}"/>
            <form:label path="phoneQuantities">quantity:</form:label>
            <form:input path="phoneQuantities" name="quantities${i.index}"/><br>
        </c:forEach>
        <button class="btn btn-dark add-to-cart">Add 2 cart</button>
    </form:form>
</tags:master>