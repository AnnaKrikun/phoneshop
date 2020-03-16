<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<head>
    <!-- Bootstrap CSS -->
    <link
            rel="stylesheet"
            href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
            integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
            crossorigin="anonymous"
    />
    <link href="<c:url value="/resources/styles/main.css"/>" rel="stylesheet">
</head>
<body>
<div class="pt-3">
    <table>
        <tr>
            <td width="65%"></td>
            <td>
                <form method="post">
                    <c:if test="${param.error != null}">
                        <p class="error-message">
                            Invalid username or password.
                        </p>
                    </c:if>
                    <p>
                        <label for="username">Username</label>
                        <input type="text" id="username" name="username" class="form-control"/>
                    </p>
                    <p>
                        <label for="password">Password</label>
                        <input type="password" id="password" name="password" class="form-control"/>
                    </p>
                    <sec:csrfInput/>
                    <button type="submit" class="btn btn-primary">Log in</button>
                </form>
            </td>
        </tr>
    </table>
</div>
</body>
</html>
