<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="sort" required="true" %>
<%@ attribute name="order" required="true" %>

<c:set var="currentSort" value="${param.sort eq sort and param.order eq order}"/>
<a href="${pageContext.request.contextPath}/productList?sort=${sort}&order=${order}&searchQuery=${param.searchQuery}"
   style ="${currentSort? 'font-weight:bold': ''}">${order eq 'asc' ? '&#8595' : '&#8593'}</a>