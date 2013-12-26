<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!--
/**
*    Copyright (C) 2012 Cardinal Info.Tech.Co.,Ltd.
*
*    This program is free software: you can redistribute it and/or modify
*    it under the terms of the GNU Affero General Public License, version 3,
*    as published by the Free Software Foundation.
*
*    This program is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU Affero General Public License for more details.
*
*    You should have received a copy of the GNU Affero General Public License
*    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
-->
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
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
