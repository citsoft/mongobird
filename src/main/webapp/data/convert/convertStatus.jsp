<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Distributed Database Management System</title>
<script type="text/javascript">
function init() {
	var status = '${convert_status}';
	if (status != -1 && status != 3) {
		setTimeout(refreshPage, 1000);
	}
}

function refreshPage() {
	document.location.href = '${pageContext.request.contextPath}/convertStatus.do';
}
</script>
</head>
<body onload="init();">
<h2>Tetrad Data Converting Status</h2>

<c:choose>
	<c:when test="${convert_status eq 0}">????</c:when>
	<c:when test="${convert_status eq 1}">Create rrd file..</c:when>
	<c:when test="${convert_status eq 2}">
		Input Data...<p/>
		DB status Data Total Size : <c:out value="${convert_db_tot}" /> / working...<c:out value="${convert_db_cnt}" /> <br/>
		Server status Data Total Size : <c:out value="${convert_server_tot}" /> / working...<c:out value="${convert_server_cnt}" />
	</c:when>
	<c:when test="${convert_status eq 3}">
		converting done!!<p/>
		DB status Data Total Size : <c:out value="${convert_db_tot}" /> / working...<c:out value="${convert_db_cnt}" /> <br/>
		Server status Data Total Size : <c:out value="${convert_server_tot}" /> / working...<c:out value="${convert_server_cnt}" />
	</c:when>
	<c:otherwise>
		Error |<c:out value="${convert_status}" />|
	</c:otherwise>
</c:choose>


</body>
</html>
