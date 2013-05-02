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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ page import="net.cit.tetrad.common.Utility"%>
<div id="contents_graph">
	<div class="left_graph">
		<b><img src="./img/ico_square.gif">&nbsp;Function :&nbsp;</b>			
		<input type="radio" id="last_consolFun" name="consolFun" value="LAST" <c:if test="${comm.consolFun=='LAST'}" >checked</c:if> ><label>Last</label>&nbsp;&nbsp; 
		<input type="radio" id="avg_consolFun" name="consolFun" value="AVERAGE" <c:if test="${comm.consolFun=='AVERAGE'}" >checked</c:if> ><label>Avg</label>&nbsp;&nbsp; 
		<input type="radio" id="total_consolFun" name="consolFun" value="TOTAL" <c:if test="${comm.consolFun=='TOTAL'}" >checked</c:if> ><label>Total</label>&nbsp;&nbsp;
		<b>&nbsp;&nbsp; by :&nbsp;</b>		
		<input type="radio" id="secStep" name="graph_step" value="sec" <c:if test="${comm.graph_step=='sec'}" >checked</c:if> ><label>10sec</label>&nbsp;&nbsp; 
		<input type="radio" id="minStep" name="graph_step" value="min" <c:if test="${comm.graph_step=='min'}" >checked</c:if> ><label>1min</label>&nbsp;&nbsp; 
		<input type="radio" id="5minStep" name="graph_step" value="5min" <c:if test="${comm.graph_step=='5min'}" >checked</c:if> ><label>5min</label>&nbsp;&nbsp; 
		<input type="radio" id="30minStep" name="graph_step" value="30min" <c:if test="${comm.graph_step=='30min'}" >checked</c:if> ><label>30min</label>&nbsp;&nbsp; 
		<input type="radio" id="hourStep" name="graph_step" value="hour" <c:if test="${comm.graph_step=='hour'}" >checked</c:if> ><label>1hour</label>&nbsp;&nbsp;
	</div>
	<div class="center_graph">
	</div> 
	<div class="right_graph txtRight">
		<label id="enlarge" onClick="checkNull(sortClick)" class="text_top"></label>
	</div> 
	<fieldset class="all_graph border">
		<div class="all_graph">
			<fieldset class="slider_bar">
				<div id="element"></div>
			</fieldset>
				<div class="text_left">
					<input type="image" src="./img/btn_apply<spring:message code="common.img"/>.gif" width="45" height="20" alt="apply" class="btn_apply" id="btn_apply" onclick='checkNull(setGraph); return false;'>
				</div>
		</div>
		<div class="select_left_graph">
			<a href="#" onClick="setGraph_period('1h'); return false;" id="slider_1h">1h</a>&nbsp;&nbsp;
			<a href="#" onClick="setGraph_period('2h'); return false;" id="slider_2h">2h</a>&nbsp;&nbsp;
			<a href="#" onClick="setGraph_period('3h'); return false;" id="slider_3h">3h</a>&nbsp;&nbsp;
			<a href="#" onClick="setGraph_period('6h'); return false;" id="slider_6h">6h</a>&nbsp;&nbsp;
			<a href="#" onClick="setGraph_period('12h'); return false;" id="slider_12h">12h</a>&nbsp;&nbsp;
			<a href="#" onClick="setGraph_period('1d'); return false;" id="slider_1d">1d</a>&nbsp;&nbsp;
			<a href="#" onClick="setGraph_period('1w'); return false;" id="slider_1w">1w</a>&nbsp;&nbsp;
			<a href="#" onClick="setGraph_period('2w'); return false;" id="slider_2w">2w</a>&nbsp;&nbsp;
			<a href="#" onClick="setGraph_period('1m'); return false;" id="slider_1m">1m</a>&nbsp;&nbsp;
			<a href="#" onClick="setGraph_period('all'); return false;" id="slider_all">All</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<label onClick="setNowGraphDate()" class="text_top"><a href="#" class="deco_none">Now</a></label>
		</div>
		<div class="select_center_graph txtRight">
			<label id="alert_area"><spring:message code="graph.pressBtn"/></label>
		</div>
		<div class="select_right_graph txtLeft">
			<input type="text" id="selectDate" name="selectDate" value="<c:out value="${comm.selectDate}" />"/>
			<select id="selectHour" name="selectHour">
				<%for(int h=0;h<24;h++){%>
					<c:set var ="h" value="<%=Utility.intFormat(h)%>"></c:set>
					<option value="${h}" <c:if test="${comm.selectHour eq h}" >selected</c:if>>${h}</option>
				<%}%>
			</select> 
			<select id="selectMin" name="selectMin">
				<%for(int m=0;m<60;m++){%>
					<c:set var ="m" value="<%=Utility.intFormat(m)%>"></c:set>
					<option value="${m}" <c:if test="${comm.selectMin == m}" >selected</c:if>>
						${m}
					</option>
				<%}%>
			</select> 
		</div>
	</fieldset>
	<fieldset class="all_graph">
			<div class="graph_area_graph" id="all_graph_area"></div>
			<div class="graph_area_graph" id="graph_area_graph"></div>
			<div class="graph_area_graph" id="solo_graph_area"></div>
	</fieldset>
</div>
