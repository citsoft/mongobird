<%@page import="net.cit.tetrad.utility.code.Code"%>
<%@page import="java.util.Locale"%>
<%@page import="org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver"%>
<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%
	AcceptHeaderLocaleResolver re = new AcceptHeaderLocaleResolver();
	Locale locale = re.resolveLocale(request);
	Code.event.getCode().updateCode(locale);
%>
