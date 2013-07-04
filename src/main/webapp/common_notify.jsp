<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<link href="${pageContext.request.contextPath}/css/common_notify/bootstrap.css" id="bootstrap-css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/common_notify/jquery.pnotify.js"></script>
<link href="${pageContext.request.contextPath}/css/common_notify/jquery.pnotify.default.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/common_notify/jquery.pnotify.default.icons.css" rel="stylesheet" type="text/css" />
<style type="text/css">
		.ui-pnotify.stack-bar-bottom {
			margin-left: 15%;
			right: auto;
			bottom: 0;
			top: auto;
			left: auto;
		}
</style>
<script type="text/javascript">
var jsnoResponseTitle = "<spring:message code='noResponse.title'/>";
var jsnoResponseText = "<spring:message code='noResponse.text'/>";
var jscheckStartTime = "<spring:message code='noResponse.checkStartTime'/>";
var jscheckEndTime = "<spring:message code='noResponse.checkEndTime'/>";
var jsabnormalResponseTitle = "<spring:message code='abnormalResponse.title'/>";
var jsabnormalResponseText = "<spring:message code='abnormalResponse.text'/>";
// 	var stack_bar_bottom = {"dir1": "up", "dir2": "right", "spacing1": 0, "spacing2": 0};
// 	function show_stack_bar_bottom(type) {
// 		var opts = {
// 			title: "Over Here",
// 			text: "Check me out. I'm in a different stack.",
// 			addclass: "stack-bar-bottom",
// 			cornerclass: "",
// 			width: "70%",
// 			stack: stack_bar_bottom
// 		};
// 		switch (type) {
// 			case 'error':
// 				opts.title = "Oh No";
// 				opts.text = "Watch out for that water tower!<a href='#' onclick='$.pnotify_remove_all();'>remove all notices</a>";
// 				opts.type = "error";
// 				break;
// 			case 'info':
// 				opts.title = "Breaking News";
// 				opts.text = "Have you met Ted?";
// 				opts.type = "info";
// 				break;
// 			case 'success':
// 				opts.title = "Good News Everyone";
// 				opts.text = "I've invented a device that bites shiny metal asses.";
// 				opts.type = "success";
// 				break;
// 		}
// 		$.pnotify(opts);
// 	};
</script>
