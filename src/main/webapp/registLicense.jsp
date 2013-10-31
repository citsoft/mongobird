<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>check license key</title>
<link href="assets/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
<link href="assets/plugins/bootstrap/css/bootstrap-responsive.min.css" rel="stylesheet" type="text/css"/>
<link href="assets/plugins/font-awesome/css/font-awesome.css" rel="stylesheet" type="text/css"/>
<link href="assets/css/style-metro.css" rel="stylesheet" type="text/css"/>
<link href="assets/css/style.css" rel="stylesheet" type="text/css"/>

</head>
<body>
<div class="headerArea" >
    <p class="muted">Enter the license key.</p>
</div>

<div class="formArea">
    <input type="text" id="licensekey" name="licensekey" placeholder="license key" class="m-wrap biglarge" />	    
    <button type="button" class="btn blue" id="regist">Submit</button>
</div>

<div class="errorMessageArea">
    <p id="errorMessage">&nbsp; </p>
</div>

<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-latest.js"></script>
<script src="js/login_js/registLicense.js" type="text/javascript"></script> 
<script type="text/javascript">
$(document).ready( function() {
	$('#licensekey').focus();
    $('#regist').click(function (event) {
        regist();
    }); 
    
    $('#licensekey').keypress(function (e) {
        if (e.which == 13) {
        	regist();
            return false;
        }
    });
});
</script>
</body>
</html>