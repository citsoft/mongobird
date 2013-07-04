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
		strDataSet.append(", \"<a href='#' class='deco_none' title='\"+toolTip_title(parseFloat("+ fetchRow.get("opcounters_insert") +"),'')+\"'>\"+exFormat('"+fetchRow.get("opcounters_insert")+"')+\"</a>\"");
		strDataSet.append(", \"<a href='#' class='deco_none' title='\"+toolTip_title(parseFloat("+ fetchRow.get("opcounters_query") +"),'')+\"'>\"+exFormat('"+fetchRow.get("opcounters_query")+"')+\"</a>\"");
		strDataSet.append(", \"<a href='#' class='deco_none' title='\"+toolTip_title(parseFloat("+ fetchRow.get("opcounters_update") +"),'')+\"'>\"+exFormat('"+fetchRow.get("opcounters_update")+"')+\"</a>\"");
		strDataSet.append(", \"<a href='#' class='deco_none' title='\"+toolTip_title(parseFloat("+ fetchRow.get("opcounters_delete") +"),'')+\"'>\"+exFormat('"+fetchRow.get("opcounters_delete")+"')+\"</a>\"]");
	}
	strDataSet.append("]");
	//out.println(strDataSet.toString());
%>
<script type="text/javascript">
var aDataSet=<%=strDataSet.toString()%>;
</script>

<h5><spring:message code="graph.queryState"/> <spring:message code="graph.list"/></h5>
<table id="list" border="0" class="tb_list" summary="<spring:message code="graph.daemonDetailedGraph"/>">
	<caption><spring:message code="graph.daemonDetailedGraph"/></caption>
	<colgroup>
		<col width="154">
		<col width="89">
		<col width="89">
		<col width="89">
		<col width="89">
	</colgroup>
	<thead>
		<tr>
			<th><spring:message code="graph.time"/></th>
			<th>Insert</th>
			<th>Query</th>
			<th>Update</th>
			<th>Delete</th>
		</tr>
	</thead>
	<tbody>
		<tr>
		</tr>
	</tbody>
</table>
