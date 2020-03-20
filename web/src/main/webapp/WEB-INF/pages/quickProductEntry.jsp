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
        <table>
            <c:forEach begin="${0}" end="${inputsCount}" varStatus="i">
                <tr>
                    <td>
                        <form:label path="quickProductEntryItems[${i.index}].phoneId">PhoneId:</form:label>
                    </td>
                    <td>
                        <form:input path="quickProductEntryItems[${i.index}].phoneId" class="form-control"/>
                        <div class="error-message" id="error-message">
                            <form:errors path="quickProductEntryItems[${i.index}].phoneId"/>
                        </div>
                    </td>
                    <td>
                        <form:label path="quickProductEntryItems[${i.index}].phoneQuantity">quantity:</form:label>
                    </td>
                    <td>
                        <form:input path="quickProductEntryItems[${i.index}].phoneQuantity" class="form-control"/>
                        <div class="error-message" id="error-message">
                            <form:errors path="quickProductEntryItems[${i.index}].phoneQuantity"/>
                        </div>
                    </td>
                </tr>
            </c:forEach>
            <button class="btn btn-dark add-to-cart">Add 2 cart</button>
        </table>
    </form:form>
</tags:master>