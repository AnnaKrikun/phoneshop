<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<tags:master pageTitle="Order overview">
    <h2>Thanks for your order!</h2>
    <h4>Order number:<c:out value="${order.id}"/></h4>

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
                             class="picture">
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


        <table>
            <tr>
                <td>First Name:</td>
                <td><c:out value="${order.firstName}"/></td>
            </tr>
            <tr>
                <td>Last Name:</td>
                <td><c:out value="${order.lastName}"/></td>
            </tr>
            <tr>
                <td>Address:</td>
                <td><c:out value="${order.deliveryAddress}"/></td>
            </tr>
            <tr>
                <td>Phone:</td>
                <td><c:out value="${order.contactPhoneNo}"/></td>
            </tr>
            <tr>
                <td colspan="3"><c:out value="${order.additionalInfo}"/></td>
            </tr>
        </table>
        </tbody>
    </table>
    <p>
        <br>
        <a href="<c:url value="/productList"/>">
            <button type="button" class="btn btn-info">Back to shopping</button>
        </a>
    </p>
</tags:master>