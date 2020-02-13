<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tags:master pageTitle="ProductList">
    <nav class="navbar navbar-light bg-light">
        <a class="navbar-brand"> </a>
        <form class="form-inline" action="${pageContext.servletContext.contextPath}/productList">
            <input class="form-control mr-sm-2" type="search"
                   placeholder="${not empty param.searchQuery? param.searchQuery : "Search"}"
                   aria-label="Search" name="searchQuery">
            <button class="btn btn-outline-success my-2 my-sm-0" type="submit">Search</button>
        </form>
    </nav>
    <p>
    <h1>Phones</h1>
    <table class="table">
        <thead>
        <tr>
            <th scope="col">Image</th>
            <th scope="col">Brand
                <tags:sortAsc sort="brand"/>
                <tags:sortDesc sort="brand"/>
            </th>
            <th scope="col">Model
                <tags:sortAsc sort="model"/>
                <tags:sortDesc sort="model"/>
            </th>
            <th scope="col">Color</th>
            <th scope="col">Display size
                <tags:sortAsc sort="displaySizeInches"/>
                <tags:sortDesc sort="displaySizeInches"/>
            </th>
            <th scope="col">Price
                <tags:sortAsc sort="price"/>
                <tags:sortDesc sort="price"/>
            </th>
            <th scope="col">Quantity</th>
            <th scope="col">Action</th>
        </tr>
        </thead>
        <c:forEach var="phone" items="${phonePage.phoneList}">
            <tr>
                <td>
                    <img src="https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/${phone.imageUrl}">
                </td>
                <td>${phone.brand}</td>
                <td>${phone.model}</td>
                <td>
                    <c:forEach var="color" items="${phone.colors}">
                        <p>${color.code}</p>
                    </c:forEach>
                </td>
                <td>${phone.displaySizeInches}"</td>
                <td>$ ${phone.price}</td>
                <td>
                    <input type="text" class="form-control phone-quantity" name="quantity"/>
                    <input type="hidden" name="phoneId" class="phone-id" value="${phone.id}"/>
                    <div class="success-message"><span></span></div>
                    <div class="error-message"><span></span></div>
                </td>
                <td>
                    <button type="button" class="btn btn-dark add-to-cart">Add</button>
                </td>
            </tr>
        </c:forEach>
    </table>

    <nav aria-label="Page navigation example">
        <ul class="pagination justify-content-end">
            <c:choose>
                <c:when test="${phonePage.pagination.currentPageNumber eq phonePage.pagination.leftPaginationBorder}">
                    <li class="page-item disabled">
                        <a class="page-link" href="#" tabindex="-1" aria-disabled="true">Previous</a>
                    </li>
                </c:when>
                <c:otherwise>
                    <li class="page-item">
                        <a class="page-link" href="${pageContext.request.contextPath}/productList?sort=${not empty param.sort ? param.sort : "brand"}&order=${not empty param.order ? param.order : "asc"}&searchQuery=${param.searchQuery}&page=${phonePage.pagination.currentPageNumber-1}" tabindex="-1">
                            Previous
                        </a>
                    </li>
                </c:otherwise>
            </c:choose>
            <c:forEach begin="${phonePage.pagination.leftPaginationBorder}"
                       end="${phonePage.pagination.rightPaginationBorder}" var="pageNumber">
                <c:choose>
                    <c:when test="${pageNumber eq phonePage.pagination.currentPageNumber}">
                        <li class="page-item active">
                            <a class="page-link" href="${pageContext.request.contextPath}/productList?sort=${not empty param.sort ? param.sort : "brand"}&order=${not empty param.order ? param.order : "asc"}&searchQuery=${param.searchQuery}&page=${pageNumber}">
                                    ${pageNumber}
                            </a>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="page-item">
                            <a class="page-link" href="${pageContext.request.contextPath}/productList?sort=${not empty param.sort ? param.sort : "brand"}&order=${not empty param.order ? param.order : "asc"}&searchQuery=${param.searchQuery}&page=${pageNumber}">
                                    ${pageNumber}
                            </a>
                        </li>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
            <c:choose>
                <c:when test="${phonePage.pagination.currentPageNumber eq phonePage.pagination.rightPaginationBorder}">
                    <li class="page-item disabled">
                        <a class="page-link" href="#" aria-disabled="true">Next</a>
                    </li>
                </c:when>
                <c:otherwise>
                    <li class="page-item">
                        <a class="page-link" href="${pageContext.request.contextPath}/productList?sort=${not empty param.sort ? param.sort : "brand"}&order=${not empty param.order ? param.order : "asc"}&searchQuery=${param.searchQuery}&page=${phonePage.pagination.currentPageNumber+1}">
                            Next
                        </a>
                    </li>
                </c:otherwise>
            </c:choose>
        </ul>
    </nav>
    </nav>
    </p>
</tags:master>