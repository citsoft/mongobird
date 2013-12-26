function regist(){
    var licenseKey = $('#licensekey').val();
    
    if(validate(licenseKey) ){
    	printMessage("muted", "requesting...");
    	 $.ajax({
             type:'POST',
             url : 'registLicensekey.do',
             data : {"licensekey" : licenseKey, "email":''},
             dataType:'html',
             success:function(val, textStatus){
         	    clearMessage();
            	 var resultCode = eval(val);
            	 if (resultCode == 10000) {
	                 window.parent.$.smartPop.close();
	                 window.parent.location.href="./loginView.do";
            	 } else {
            		 var message = "License key is Invalid";
            		 if (resultCode == 50000) {
            			 message = "Can't connect server";
            		 }
            		printMessage('text-error', message);              
            	 }
             },
             error:function(xhr, textStatus, errorThrown){
            	 printMessage("text-error", "mongobird returned error information for request.");
             }
         });
    }
}
    
function validate(value) {
	clearMessage();
    if (value == "") {
    	printMessage("text-error", "License key is required.");
        $('#licensekey').focus();
        return false;
    }  else {
    	var re = new RegExp(/^([A-Z0-9]{4})-([A-Z0-9]{4})-([A-Z0-9]{4})-([A-Z0-9]{4})-([A-Z0-9]{4})$/);
    	var returnVal = re.test(value);
    	
    	if (!returnVal) {
    		printMessage("text-error", "License key is Invalid.");
    		$('#licensekey').focus();
    		return false; 
    	}
    }
    return true;
}

function clearMessage() {
    $("#resultBox").attr('class','');
    $("#resultBox").html('');
}

function printMessage(cssattr, message) {
	$("#errorMessage").attr('class', cssattr);
	$("#errorMessage").html(message);
}