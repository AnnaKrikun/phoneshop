<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<tags:master pageTitle="ErrorPage">
    <p>
        Error in updating status
    </p>
    <a href="<c:url value="/admin/orders"/>">
        <p>
            Go to orders page.
        </p>
    </a>
</tags:master>
