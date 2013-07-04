<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>  
<%@ page import = "java.util.ArrayList,
							java.util.HashMap"
%>
<%
	ArrayList statusList = (ArrayList)request.getAttribute("status");
	HashMap<String, Object> fetchRow = new HashMap<String, Object>();
	StringBuffer strDataSet = new StringBuffer();
	strDataSet.append("[");
	for(int i=statusList.size()-1;i>=0;i--){
		fetchRow = (HashMap<String, Object>)statusList.get(i);
		if(statusList.size()>i+1){
			strDataSet.append(",");
		}
		strDataSet.append("['"+fetchRow.get("uptime")+"'");
		strDataSet.append(", \"<a href='#' class='deco_none' title='\"+toolTip_title(parseFloat("+ fetchRow.get("dbDataSize") +"),'Byte')+\"'>\"+sizeFormat('"+fetchRow.get("dbDataSize")+"')+\"</a>\"]");
	}
	strDataSet.append("]");
	//out.println(strDataSet.toString());
%>
<script type="text/javascript">
var aDataSet=<%=strDataSet.toString()%>;
</script>

<h5><spring:message code="graph.total"/> <spring:message code="common.dbSize"/></h5>
<table id="list" border="0" class="tb_list" summary="<spring:message code="graph.daemonDetailedGraph"/>">
	<caption><spring:message code="graph.daemonDetailedGraph"/></caption>
		<colgroup>
		<col width="154">
		<col width="">
	</colgroup>
	<thead>
		<tr>
			<th><spring:message code="graph.time"/></th>
			<th><spring:message code="common.dbSize"/></th>
		</tr>
	</thead>
	<tbody>
		<tr>
		</tr>
	</tbody>
</table>
