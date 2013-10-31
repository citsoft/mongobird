<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
 
<div id="footer" class="footer_license">mongobird v${releaseVersion } | 
<c:choose>
    <c:when test="${licensekey eq null or licensekey eq ''}">
        non-commercial version 
     </c:when>
     <c:otherwise>
          ${licensetype} version 
     </c:otherwise>
</c:choose>

</div>