<%@page contentType="text/html; charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="ftm" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html">
    <link rel="stylesheet" type="text/css" href="resources/extJs/resources/css/ext-all.css" />

    <script type="text/javascript" src="resources/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="resources/extJs/ext-all.js"></script>
    <script type="text/javascript" src="resources/extJs/ext-all-debug.js"></script>
    <script type="text/javascript" src="resources/js/app/extApp.js"></script>
</head>
<body>
<iframe id="responseIframe"  src="http://localhost:8181/results.jsp" width="100%" height="300px"/>


</body>
</html>



