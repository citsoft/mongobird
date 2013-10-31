<%@page import="net.cit.tetrad.common.Config"%>
<%@page import="net.cit.tetrad.utility.StringUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%
    	String currentDbRex = Config.DBNAME_PATTERN;
    String dbRex = request.getParameter("db_rex");
    if (!StringUtils.isNull(dbRex)) {
    	Config.DBNAME_PATTERN = dbRex;
    	out.print("<h1>DONE!!</h1>");
    } else {
    %>    
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>dbname - Reqular Expression</title>
</head>
<body>

<h2>현재 DB Name Regular Expression 은 [<%=currentDbRex %>] 임.</h2>

<form action="./setdbname.jsp" method="post">
    <input type="text" name="db_rex">
    <button type="submit" id="regist">Submit</button>
</form>
</body>
</html>
<%}%>