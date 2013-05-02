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
		strDataSet.append(", \"<a href='#' class='deco_none' title='\"+toolTip_title(parseFloat("+ fetchRow.get("mem_resident") +"),'MB')+\"'>\"+megaSizeFormat('"+fetchRow.get("mem_resident")+"')+\"</a>\"");
		strDataSet.append(", \"<a href='#' class='deco_none' title='\"+toolTip_title(parseFloat("+ fetchRow.get("mem_virtual") +"),'MB')+\"'>\"+megaSizeFormat('"+fetchRow.get("mem_virtual")+"')+\"</a>\"]");
	}
	strDataSet.append("]");
	//out.println(strDataSet.toString());
%>
<script type="text/javascript">
var aDataSet=<%=strDataSet.toString()%>;
</script>

<h5><spring:message code="graph.memoryState"/> <spring:message code="graph.list"/></h5>
<table id="list" border="0" class="tb_list" summary="<spring:message code="graph.daemonDetailedGraph"/>">
	<caption><spring:message code="graph.daemonDetailedGraph"/></caption>
	<colgroup>
		<col width="154">
		<col width="190">
		<col width="169">
	</colgroup>
	<thead>
		<tr>
			<th><spring:message code="graph.time"/></th>
			<th><spring:message code="common.resident"/> (MB)</th>
			<th><spring:message code="common.virtual"/> (MB)</th>
		</tr>
	</thead>
	<tbody>
		<tr>
		</tr>		
	</tbody>
</table>
