<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="pageTitle" required="true" %>
<jsp:useBean id="cart" class="com.es.core.model.cart.Cart" scope="session"/>
<html>
<head>
    <title>${pageTitle}</title>
    <link href='http://fonts.googleapis.com/css?family=Lobster+Two' rel='stylesheet' type='text/css'>
    <!-- Bootstrap CSS -->
    <link
            rel="stylesheet"
            href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
            integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
            crossorigin="anonymous"
    />
    <link href="<c:url value="/resources/styles/main.css"/>" rel="stylesheet">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="<c:url value="/resources/js/ajaxCart.js"/>"></script>
</head>
<body class="product-list">
<header>
    <nav class="navbar navbar-light" style="background-color: #e3f2fd;;">
        <a href="${pageContext.request.contextPath}/productList" class="navbar-brand">Phonify</a>
        <div class="cart-container" style="margin-right: 10px">
            <a href="<c:url value="/cart"/>" class="btn btn-outline-dark">
                My Cart: <span class="cart-total-quantity">${not empty cart ? cart.totalQuantity : 0}</span>
                Items ($<span class="cart-total-price">${not empty cart ? cart.totalPrice : 0}</span>)
            </a>
        </div>
    </nav>
</header>
<main>
    <jsp:doBody/>
</main>
</body>
</html>