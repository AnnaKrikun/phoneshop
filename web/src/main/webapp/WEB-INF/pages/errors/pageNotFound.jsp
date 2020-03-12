<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<tags:master pageTitle="ErrorPage">
    <p>
        Page not found.
    </p>
    <a href="<c:url value="/"/>">
        <p>
            Go to main page.
        </p>
    </a>
</tags:master>
