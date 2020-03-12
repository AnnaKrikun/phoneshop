<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<tags:master pageTitle="Cart">
    <p>
        <br>
        <a href="<c:url value="/productList"/>">
            <button type="button" class="btn btn-info">Back to product list</button>
        </a>
    </p>

    <table class="table table-hover">
        <thead>
        <tr>
            <th scope="col">Image</th>
            <th scope="col">Brand</th>
            <th scope="col">Model</th>
            <th scope="col">Color</th>
            <th scope="col">Display size</th>
            <th scope="col">Quantity</th>
            <th scope="col">Price</th>
            <th scope="col">Action</th>
        </tr>
        </thead>
        <tbody>
        <form:form method="post" modelAttribute="cartDisplay">
            <input type="hidden" name="_method" value="PUT"/>
            <input type="hidden" name="phoneId"/>
            <c:forEach var="cartDisplayItem" items="${cartDisplay.cartDisplayItems}" varStatus="i">
                <tr>
                    <td>
                        <a href="<c:url value="/productDetails/${cartDisplayItem.phoneId}"/>">
                            <img src="<c:url value="https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/${cartDisplayItem.imageUrl}"/>"
                                 style="width: 72px; height: 72px;">
                        </a>
                    </td>
                    <td>
                        <c:out value="${cartDisplayItem.brand}"/>
                    </td>
                    <td>
                        <a href="<c:url value="/productDetails/${cartDisplayItem.phoneId}"/>">
                            <c:out value="${cartDisplayItem.model}"/>
                        </a>
                    </td>
                    <td>
                        <c:forEach var="color" items="${cartDisplayItem.colors}">
                            <c:out value="${color.code}"/><br>
                        </c:forEach>
                    </td>
                    <td>
                        <c:out value="${cartDisplayItem.displaySizeInches}"/>"
                    </td>
                    <td>
                        <form:hidden path="cartDisplayItems[${i.index}].phoneId"/>
                        <form:input path="cartDisplayItems[${i.index}].quantity" class="form-control"/>
                        <div class="error-message" id="error-message">
                            <form:errors path="cartDisplayItems[${i.index}].quantity"/>
                        </div>
                    </td>
                    <td>
                        $<c:out value="${cartDisplayItem.price}"/>
                    </td>
                    <td>
                        <button type="submit" name="remove" class="btn btn-dark"
                                onclick="return onDeletePhone(<c:out
                                        value="${cartDisplayItem.phoneId}"/>);">
                            Remove
                        </button>
                    </td>
                </tr>
            </c:forEach>
            <tr>
                <td>  </td>
                <td>  </td>
                <td>  </td>
                <td>  </td>
                <td>  </td>
                <td>  </td>
                <td class="text-center"><h4>Total</h4></td>
                <td><h5>$<c:out value="${not empty cart ? cart.totalPrice : 0}"/></h5></td>
            </tr>
            <tr>
                <td>  </td>
                <td>  </td>
                <td>  </td>
                <td>  </td>
                <td>  </td>
                <td>  </td>
                <td class="text-right">
                    <c:if test="${not empty cartDisplay.cartDisplayItems}">
                        <button name="update" type="submit" class="btn btn-dark">
                            Update
                        </button>
                    </c:if>
                </td>
                <td>
                    <c:if test="${not empty cartDisplay.cartDisplayItems}">
                        <a href="<c:url value="/order"/> " class="btn btn-dark">
                            Order
                        </a>
                    </c:if>
                </td>
            </tr>
            <script>
                function onDeletePhone(id) {
                    $('input[name=_method]').remove();
                    $('input[name=phoneId]').val(id);
                    return true;
                }
            </script>
        </form:form>
        </tbody>
    </table>
</tags:master>