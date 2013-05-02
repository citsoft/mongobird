<!--
"mongobird" is released under a dual license model designed to developers 
and commercial deployment.

For using OEMs(Original Equipment Manufacturers), ISVs(Independent Software
Vendor), ISPs(Internet Service Provider), VARs(Value Added Resellers) 
and another distributors, or for using include changed issue
(modify / application), it must have to follow the Commercial License policy.
To check the Commercial License Policy, you need to contact Cardinal Info.Tech.Co., Ltd.
(http://www.citsoft.net)
 *
If not using Commercial License (Academic research or personal research),
it might to be under AGPL policy. To check the contents of the AGPL terms,
please see "http://www.gnu.org/licenses/"
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript">
function goSearch() {
	document.myform.action="/tetrad/dataconvert.do";
	document.myform.submit();
}
</script>
</head>
<body>
<h2>Tetrad Data Converting</h2>

<c:choose>
	<c:when test="${canConvert}">
		<form action="/tetrad/dataconvert.do" method="POST">
			<div>
				<li> Host <input type="text" name="host" value="" /></li>
				<li> Port <input type="text" name="port" value="" /></li>
				<li> Database <input type="text" name="database" value="" /></li>
				<li>Start Date (yyyyMMdd) <input type="text" name="startdt" value=""/></li>
			</div>
			<input type="submit" value="converting" >
		</form>
	</c:when>
	<c:otherwise>
		You can't convert data because it has already been registered a deamon information.		
	</c:otherwise>
</c:choose>
</body>
</html>
