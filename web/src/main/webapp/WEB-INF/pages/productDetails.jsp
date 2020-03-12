<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tags:master pageTitle="ProductDetails">
    <p>
        <br>
    <a href="<c:url value="/productList"/>"><button type="button" class="btn btn-info">Back to product list</button></a>
    </p>
    <div class="row">
        <div class="col left-phone-description">
            <h2><c:out value="${phone.model}"/></h2>
            <div class="image">
                <img src="<c:url value="https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/${phone.imageUrl}"/>"/>
            </div>
            <div class="description">
                <c:out value="${phone.description}"/>
            </div>
                <div class="price">
                    <h3>Price: $ <c:out value="${phone.price}"/></h3>
                    <div>
                        <table class="table table-bordered">
                            <tr>
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
                        </table>
                    </div>
                </div>
        </div>
        <div class="col right-phone-description">
            <c:if test="${not empty phone.announced}">
                <h3>General information</h3>
                <table class="table table-hover">
                    <tr>
                        <td>Announced</td>
                        <td><c:out value="${phone.announced}"/></td>
                    </tr>
                </table>
            </c:if>
            <c:if test="${not empty phone.deviceType or not empty phone.os or
                            not empty phone.colors or not empty phone.ramGb or
                            not empty phone.internalStorageGb or not empty phone.bluetooth or
                            not empty phone.positioning}">
                <h3>Basic</h3>
                <table class="table table-hover">
                    <c:if test="${not empty phone.deviceType}">
                        <tr>
                            <td>Device Type</td>
                            <td><c:out value="${phone.deviceType}"/></td>
                        </tr>
                    </c:if>
                    <c:if test="${not empty phone.os}">
                        <tr>
                            <td>OS</td>
                            <td><c:out value="${phone.os}"/></td>
                        </tr>
                    </c:if>
                    <c:if test="${not empty phone.colors}">
                        <tr>
                            <td>Colors</td>
                            <td>
                                <c:forEach var="color" items="${phone.colors}">
                                    <p><c:out value="${color.code}"/></p>
                                </c:forEach>
                            </td>
                        </tr>
                    </c:if>
                    <c:if test="${not empty phone.ramGb}">
                        <tr>
                            <td>RAM</td>
                            <td><c:out value="${phone.ramGb}"/> GB</td>
                        </tr>
                    </c:if>
                    <c:if test="${not empty phone.internalStorageGb}">
                        <tr>
                            <td>Internal Storage</td>
                            <td><c:out value="${phone.internalStorageGb}"/> GB</td>
                        </tr>
                    </c:if>
                    <c:if test="${not empty phone.bluetooth}">
                        <tr>
                            <td>Bluetooth</td>
                            <td><c:out value="${phone.bluetooth}"/></td>
                        </tr>
                    </c:if>
                    <c:if test="${not empty phone.positioning}">
                        <tr>
                            <td>Positioning</td>
                            <td><c:out value="${phone.positioning}"/> h</td>
                        </tr>
                    </c:if>
                </table>
            </c:if>
            <c:if test="${not empty phone.displaySizeInches or not empty phone.displayResolution or
                            not empty phone.displayTechnology or not empty phone.pixelDensit}">
                <h3>Display</h3>
                <table class="table table-hover">
                    <c:if test="${not empty phone.displaySizeInches}">
                        <tr>
                            <td>Size</td>
                            <td><c:out value="${phone.displaySizeInches}"/></td>
                        </tr>
                    </c:if>
                    <c:if test="${not empty phone.displayResolution}">
                        <tr>
                            <td>Resolution</td>
                            <td><c:out value="${phone.displayResolution}"/></td>
                        </tr>
                    </c:if>
                    <c:if test="${not empty phone.displayTechnology}">
                        <tr>
                            <td>Technology</td>
                            <td><c:out value="${phone.displayTechnology}"/> px</td>
                        </tr>
                    </c:if>
                    <c:if test="${not empty phone.pixelDensity}">
                        <tr>
                            <td>Pixel Density</td>
                            <td><c:out value="${phone.pixelDensity}"/></td>
                        </tr>
                    </c:if>
                </table>
            </c:if>
            <c:if test="${not empty phone.lengthMm or not empty phone.widthMm or
                            not empty phone.heightMm or not empty phone.weightGr}">
                <h3>Dimensions & weight</h3>
                <table class="table table-hover">
                    <c:if test="${not empty phone.lengthMm}">
                        <tr>
                            <td>Length</td>
                            <td><c:out value="${phone.lengthMm}"/> mm</td>
                        </tr>
                    </c:if>
                    <c:if test="${not empty phone.widthMm}">
                        <tr>
                            <td>Width</td>
                            <td><c:out value="${phone.widthMm}"/> mm</td>
                        </tr>
                    </c:if>
                    <c:if test="${not empty phone.heightMm}">
                        <tr>
                            <td>Height</td>
                            <td><c:out value="${phone.heightMm}"/> mm</td>
                        </tr>
                    </c:if>
                    <c:if test="${not empty phone.weightGr}">
                        <tr>
                            <td>Weight</td>
                            <td><c:out value="${phone.weightGr}"/> gr</td>
                        </tr>
                    </c:if>
                </table>
            </c:if>
            <c:if test="${not empty phone.backCameraMegapixels or not empty phone.frontCameraMegapixels}">
                <h3>Camera</h3>
                <table class="table table-hover">
                    <c:if test="${not empty phone.backCameraMegapixels}">
                        <tr>
                            <td>Back</td>
                            <td><c:out value="${phone.backCameraMegapixels}"/> mp</td>
                        </tr>
                    </c:if>
                    <c:if test="${not empty phone.frontCameraMegapixels}">
                        <tr>
                            <td>Front</td>
                            <td><c:out value="${phone.frontCameraMegapixels}"/> mp</td>
                        </tr>
                    </c:if>
                </table>
            </c:if>
            <c:if test="${not empty phone.batteryCapacityMah or not empty phone.talkTimeHours or
                            not empty phone.standByTimeHours}">
                <h3>Battery</h3>
                <table class="table table-hover">
                    <c:if test="${not empty phone.batteryCapacityMah}">
                        <tr>
                            <td>Battery Capacity</td>
                            <td><c:out value="${phone.batteryCapacityMah}"/> mah</td>
                        </tr>
                    </c:if>
                    <c:if test="${not empty phone.talkTimeHours}">
                        <tr>
                            <td>Talk Time</td>
                            <td><c:out value="${phone.talkTimeHours}"/> h</td>
                        </tr>
                    </c:if>
                    <c:if test="${not empty phone.standByTimeHours}">
                        <tr>
                            <td>Stand By Time</td>
                            <td><c:out value="${phone.standByTimeHours}"/> h</td>
                        </tr>
                    </c:if>
                </table>
            </c:if>
        </div>
    </div>
</tags:master>