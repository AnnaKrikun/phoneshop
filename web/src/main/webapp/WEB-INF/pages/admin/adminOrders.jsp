<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<tags:master pageTitle="Admin orders">
    <h2>Orders</h2>

    <table class="table table-hover">
        <thead>
        <tr>
            <th scope="col">Order number</th>
            <th scope="col">Customer</th>
            <th scope="col">Phone</th>
            <th scope="col">Address</th>
            <th scope="col">Date</th>
            <th scope="col">Total price</th>
            <th scope="col">Status</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="order" items="${orderPage.orders}" varStatus="i">
            <tr>
                <td>
                    <a href="<c:url value="/admin/orders/${order.id}"/>">${order.id}</a>
                </td>
                <td>
                    <c:out value="${order.firstName}"/> <c:out value="${order.lastName}"/>
                </td>
                <td>
                    <c:out value="${order.contactPhoneNo}"/>
                </td>
                <td>
                    <c:out value="${order.deliveryAddress}"/>
                </td>
                <td>
                    <c:out value="${order.date}"/>
                </td>
                <td>
                    $<c:out value="${order.totalPrice}"/>
                </td>
                <td>
                    <c:out value="${order.status}"/>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <nav aria-label="Page navigation example">
        <ul class="pagination justify-content-end">
            <c:choose>
                <c:when test="${orderPage.pagination.currentPageNumber eq orderPage.pagination.leftPaginationBorder}">
                    <li class="page-item disabled">
                        <a class="page-link" href="#" tabindex="-1" aria-disabled="true">Previous</a>
                    </li>
                </c:when>
                <c:otherwise>
                    <li class="page-item">
                        <a class="page-link"
                           href="${pageContext.request.contextPath}/admin/orders?page=${orderPage.pagination.currentPageNumber-1}"
                           tabindex="-1">
                            Previous
                        </a>
                    </li>
                </c:otherwise>
            </c:choose>
            <c:forEach begin="${orderPage.pagination.leftPaginationBorder}"
                       end="${orderPage.pagination.rightPaginationBorder}" var="pageNumber">
                <c:choose>
                    <c:when test="${pageNumber eq orderPage.pagination.currentPageNumber}">
                        <li class="page-item active">
                            <a class="page-link"
                               href="${pageContext.request.contextPath}/admin/orders?page=${pageNumber}">
                                    ${pageNumber}
                            </a>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="page-item">
                            <a class="page-link"
                               href="${pageContext.request.contextPath}/admin/orders?page=${pageNumber}">
                                    ${pageNumber}
                            </a>
                        </li>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
            <c:choose>
                <c:when test="${orderPage.pagination.currentPageNumber eq orderPage.pagination.rightPaginationBorder}">
                    <li class="page-item disabled">
                        <a class="page-link" href="#" aria-disabled="true">Next</a>
                    </li>
                </c:when>
                <c:otherwise>
                    <li class="page-item">
                        <a class="page-link"
                           href="${pageContext.request.contextPath}/admin/orders?page=${orderPage.pagination.currentPageNumber+1}">
                            Next
                        </a>
                    </li>
                </c:otherwise>
            </c:choose>
        </ul>
    </nav>
</tags:master>