<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
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
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="graph.daemonDetails"/></title>
<LINK rel=stylesheet type=text/css href="${pageContext.request.contextPath}/css/reset-fonts.css">
<LINK rel=stylesheet type=text/css href="${pageContext.request.contextPath}/css/base.css">
<LINK rel=stylesheet type=text/css href="${pageContext.request.contextPath}/css/default.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/daemonGraph.css">
<%@ include file="./management/CodeUtil.jsp" %>
<%if(session.getAttribute("loginAuth").equals(1)||session.getAttribute("loginAuth").equals(2)){}else{%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/common_analytics.js"></script>
<%}%>
<SCRIPT type=text/javascript src="${pageContext.request.contextPath}/js/yuiloader-dom-event.js"></SCRIPT>
<SCRIPT type=text/javascript src="${pageContext.request.contextPath}/js/json-min.js"></SCRIPT>
<SCRIPT type=text/javascript src="${pageContext.request.contextPath}/js/default-min.js"></SCRIPT>
<script type="text/javascript">
function init(){
	parent.runCommand('${comm.deviceCode}');
}

function getElementsByClassName(classname, node)  {
	if(!node) node = document.getElementsByTagName("body")[0];
	var a = [];
	var re = new RegExp('\\b' + classname + '\\b');
	var els = node.getElementsByTagName("*");
	for(var i=0,j=els.length; i<j; i++)
	    if(re.test(els[i].className))a.push(els[i]);
	return a;
}

function clickAttach(){
    var arrObj=getElementsByClassName("NUMBER");
    for (var i=0;i<arrObj.length ;i++ )
    {
        arrObj[i].onclick=function(){
            var SEP_CHARS="_@#$_";
            var obj=this;
            var objTmp;
            var strTreeName="";
            var strTmpTreeName="";
            var strArrElementName="";
            var strTdId="";
            var isArray=false;
            do{
                strTdId=obj.parentElement.id;
                obj=obj.parentElement.parentElement;
                strTmpTreeName=obj.children[0].innerHTML;

                //Array 형태인 경우 판별
                var j=1;
                isArray=false;
                while (j<obj.children.length)
                {
                    objTmp=obj.children[j];
                    if(objTmp.id!=""){
                        if(objTmp.id==strTdId){
                            isArray=true;
                            break;
                        }
                    }else{
                        break;
                    }
                    j++;
                }

                obj=obj.parentElement.parentElement;

                if (!isArray){
                    strTreeName=strTmpTreeName+(strTreeName!=""?SEP_CHARS:"")+strTreeName;
                }
                if(isArray){
                    //Array 형태인 경우라면 Array 의 라벨을 더해 준 후 트리네임을 더해 준다.
                    objTmp=obj.children[1];
                    objTmp=objTmp.children[0];
                    objTmp=objTmp.children[j];
                    strArrElementName=objTmp.innerHTML;
                    strTreeName=strArrElementName+(strTreeName!=""?SEP_CHARS:"")+strTreeName;
                    strTreeName=strTmpTreeName+(strTreeName!=""?SEP_CHARS:"")+strTreeName;
                }

            }while(obj.id.indexOf("p0")<0);
            	var arrTreeName = strTreeName.split(SEP_CHARS);
            	var dbname  = arrTreeName[1];
    			var dsname = strTreeName.split(SEP_CHARS).join("_");
    			if (dsname.indexOf("totalRecordStats") == 0) {
    				dsname = dsname.replace("totalRecordStats", "recordStats");
    			} 
    			parent.isShowGraph(dsname,  '${comm.deviceCode}', dbname);
//             parent.daemonLayerdPop(dsname, '${comm.deviceCode}');
        }

    }
}

</script>
</head>
<body onLoad="init()">
		<div class="demon_detail">
			<h2>
				<img src="./img/daemon_subtit03<spring:message code="common.img"/>.png">
			</h2>
</div>	

<DIV>
	<OUTPUT style="DISPLAY: block" id=jsonValidateOutput for="jsonInput cmdValidate"></OUTPUT>
	<OUTPUT style="DISPLAY: block" id=jsonOutput ></OUTPUT>
</DIV>

	<div id="menudiv">
	<H2 style="MARGIN-TOP: 0px; FONT-SIZE: 108%"><LABEL for=jsonInput>Input:</LABEL> 
	<SMALL id=jsonSize></SMALL></H2> 
	
	<H2 style="FONT-SIZE: 108%">Output:</H2>
	<MENU 
	style="PADDING-BOTTOM: 0px; MARGIN: 0px 0px 1em; PADDING-LEFT: 0px; WIDTH: 12em; PADDING-RIGHT: 0px; FLOAT: left; PADDING-TOP: 0px">
	<TEXTAREA id=jsonInput rows=10 cols=50 autofocus="autofocus" spellcheck="false" placeholder="Paste your JSON here"></TEXTAREA>
	  <H3 style="MARGIN-TOP: 0px">Input</H3>
	  <P><LABEL><INPUT id=jsonStrict value=1 CHECKED type=radio name=jsonParser> 
	  Strict JSON </LABEL><BR><LABEL><INPUT id=jsonEval value=0 type=radio 
	  name=jsonParser> Eval </LABEL></P>
	  <H3>Output</H3>
	  <P id=jsonOutputSet><LABEL><INPUT id=json2HTML value=1 CHECKED type=radio 
	  name=jsonOutStyle> HTML </LABEL><BR><LABEL><INPUT id=json2JSON value=0 
	  type=radio name=jsonOutStyle> JSON </LABEL></P>
	  <H3>Options:</H3>
	  <P id=jsonOptionSet class=HTML><LABEL id=jsonTrunc_label><INPUT id=jsonTrunc 
	  value=1 CHECKED type=checkbox> Truncate long strings </LABEL><BR><LABEL 
	  id=jsonDate_label><INPUT id=jsonDate value=1 CHECKED type=checkbox> Detect 
	  encoded dates </LABEL><BR><LABEL id=jsonData_label><INPUT id=jsonData value=1 
	  CHECKED type=checkbox> Detect data structures </LABEL><BR><LABEL 
	  id=jsonSpace_label><INPUT id=jsonSpace value=1 type=checkbox> Preserve 
	  whitespace </LABEL></P>
	  <H3>Actions:</H3>
	  <P>
	  <BUTTON id=cmdRender type=submit>Render</BUTTON>
	  <BR><SPAN 
	  id=loadCommands><A id=cmdLoad>Load</A> <SPAN id=reloadCommand>/ <A 
	  id=cmdReload>Reload</A> </SPAN></SPAN><SPAN style="DISPLAY: none" 
	  id=loadMessage>Loading... </SPAN><BR><A id=cmdValidate>Validate</A><BR><A 
	  id=cmdClear>Clear</A><BR><A id=cmdEncode>Re-encode</A><BR><A 
	  id=cmdRemoveCRLF>Remove Line Breaks</A><BR><A id=cmdDecodeURI>Decode 
	  URI</A><BR><A id=cmdTrim2JSON>Trim non-JSON</A><BR><SPAN id=htmlCommands><A 
	  id=cmdCollapse>Collapse</A> / <A id=cmdExpand>Expand</A> All<BR></SPAN><A 
	  id=cmdHelp href="http://chris.photobooks.com/json/help.htm" 
	  target=help>Help</A><BR><A id=cmdBeer 
	  href="http://chris.photobooks.com/json/beer.htm" target=beer>Beer Fund</A> 
	  </P>
	  <CODE id=jsonLocation></CODE></MENU>
	</div>
	
</body>
</html>
