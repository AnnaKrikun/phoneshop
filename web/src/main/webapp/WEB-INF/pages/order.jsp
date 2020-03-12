<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<tags:master pageTitle="Order">
    <h2>Order</h2>
    <p>
        <br>
        <a href="<c:url value="/cart"/>">
            <button type="button" class="btn btn-info">Back to cart</button>
        </a>
    </p>
    <p class="error-message"><c:out value=" ${errorMessageOutOfStock}"/></p>

    <table class="table">
        <thead>
        <tr>
            <th scope="col">Image</th>
            <th scope="col">Brand</th>
            <th scope="col">Model</th>
            <th scope="col">Color</th>
            <th scope="col">Display size</th>
            <th scope="col">Quantity</th>
            <th scope="col">Price</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="orderItem" items="${order.orderItems}" varStatus="i">
            <tr>
                <td>
                    <a href="<c:url value="/productDetails/${orderItem.phone.id}"/>">
                        <img src="<c:url value="https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/${orderItem.phone.imageUrl}"/>"
                             style="width: 72px; height: 72px;">
                    </a>
                </td>
                <td>
                    <c:out value="${orderItem.phone.brand}"/>
                </td>
                <td>
                    <a href="<c:url value="/productDetails/${orderItem.phone.id}"/>">
                        <c:out value="${orderItem.phone.model}"/>
                    </a>
                </td>
                <td>
                    <c:forEach var="color" items="${orderItem.phone.colors}">
                        <c:out value="${color.code}"/><br>
                    </c:forEach>
                </td>
                <td>
                    <c:out value="${orderItem.phone.displaySizeInches}"/>"
                </td>
                <td>
                        ${orderItem.quantity}
                    </div>
                </td>
                <td>
                    $<c:out value="${orderItem.phone.price}"/>
                </td>
            </tr>
        </c:forEach>
        <tr>
            <td>  </td>
            <td>  </td>
            <td>  </td>
            <td>  </td>
            <td>  </td>
            <td><h4>Subtotal:</h4></td>
            <td><h5>$<c:out value="${order.subtotal}"/></h5></td>
        </tr>
        <tr>
            <td>  </td>
            <td>  </td>
            <td>  </td>
            <td>  </td>
            <td>  </td>
            <td><h4>Delivery price:</h4></td>
            <td><h5>$<c:out value="${order.deliveryPrice}"/></h5></td>
        </tr>
        <tr>
            <td>  </td>
            <td>  </td>
            <td>  </td>
            <td>  </td>
            <td>  </td>
            <td><h4>Total:</h4></td>
            <td><h5>$<c:out value="${order.totalPrice}"/></h5></td>
        </tr>

        <form:form method="POST" modelAttribute="order">
            <table>
                <tr>
                    <td><form:label path="firstName">First Name*</form:label></td>
                    <td><form:input path="firstName" cssClass="form-control"/>
                        <form:errors path="firstName" cssClass="error-message"/>
                    </td>
                </tr>
                <tr>
                    <td><form:label path="lastName">Last Name*</form:label></td>
                    <td><form:input path="lastName" cssClass="form-control"/>
                        <form:errors path="lastName" cssClass="error-message"/></td>
                </tr>
                <tr>
                    <td><form:label path="deliveryAddress">Address*</form:label></td>
                    <td><form:input path="deliveryAddress" cssClass="form-control"/>
                        <form:errors path="deliveryAddress" cssClass="error-message"/></td>
                </tr>
                <tr>
                    <td><form:label path="contactPhoneNo">Phone*</form:label></td>
                    <td><form:input path="contactPhoneNo" cssClass="form-control"/>
                        <form:errors path="contactPhoneNo" cssClass="error-message"/></td>
                </tr>
                <tr>
                    <td colspan="2"><form:textarea path="additionalInfo" cssClass="form-control"/>
                        <form:errors path="additionalInfo"/></td>
                </tr>
                <tr>
                    <td></td>
                    <td class="text-right"><input type="submit" value="Submit" class="btn btn-dark" /></td>
                </tr>
            </table>
        </form:form>
        </tbody>
    </table>
</tags:master>