/*******************************************************************************
 * "mongobird" is released under a dual license model designed to developers 
 * and commercial deployment.
 * 
 * For using OEMs(Original Equipment Manufacturers), ISVs(Independent Software
 * Vendor), ISPs(Internet Service Provider), VARs(Value Added Resellers) 
 * and another distributors, or for using include changed issue
 * (modify / application), it must have to follow the Commercial License policy.
 * To check the Commercial License Policy, you need to contact Cardinal Info.Tech.Co., Ltd.
 * (http://www.citsoft.net)
 *  *
 * If not using Commercial License (Academic research or personal research),
 * it might to be under AGPL policy. To check the contents of the AGPL terms,
 * please see "http://www.gnu.org/licenses/"
 ******************************************************************************/
var reg_date = "";
var refreshIntervalId;
function setTrafficLight(){
	$.ajax({
		type:'GET',
		url : 'mainList.do',
		dataType:'html',
		success:function(json,textStatus){
			var jsonData = eval("(" + json + ')');
			reg_date = "";

			var NOREG_DEMON="-1";
			var RUN_DEMON="1";
			var STOP_DEMON="0";
			
			var statusMongos = NOREG_DEMON;
			var statusMongod = NOREG_DEMON;
			var statusConfig = NOREG_DEMON;
			
			for(var i=0;i<jsonData.aaData.length;i++){
				var aData = jsonData.aaData[i];
				//dbg(aData.type+","+aData.ok);
				if(aData.type=="mongos" && statusMongos != STOP_DEMON){
					statusMongos = (aData.ok == 1 )?RUN_DEMON:STOP_DEMON;
				}
				if(aData.type=="mongod" && statusMongod != STOP_DEMON){
					statusMongod = (aData.ok == 1 )?RUN_DEMON:STOP_DEMON;
				}
				if(aData.type=="config" && statusConfig != STOP_DEMON){
					statusConfig = (aData.ok == 1 )?RUN_DEMON:STOP_DEMON;
				}					
			}
			
			if ( statusMongos == RUN_DEMON ){
				$('li#mongos').html('<a href="subPageView.do?type=mongos"><img id="mongos_img" src="./img/btn_sign_01_green.gif" width="52" height="52"></a>');
			}else if ( statusMongos == STOP_DEMON ){
				$('li#mongos').html('<a href="subAlarmView.do?type=mongos"><img id="mongos_img" src="./img/btn_sign_01_red.gif" width="52" height="52"></a>');					
			}else{
				$('li#mongos').html('<img id="mongos_img" src="./img/btn_sign_01_gray.gif" width="52" height="52">');
			}
			
			if ( statusMongod == RUN_DEMON ){
				$('li#mongod').html('<a href="subPageView.do?type=mongod"><img id="mongod_img" src="./img/btn_sign_02_green.gif" width="52" height="52"></a>');
			}else if ( statusMongod == STOP_DEMON ){
				$('li#mongod').html('<a href="subAlarmView.do?type=mongod"><img id="mongod_img" src="./img/btn_sign_02_red.gif" width="52" height="52"></a>');
			}else{
				$('li#mongod').html('<img id="mongod_img" src="./img/btn_sign_02_gray.gif" width="52" height="52">');
			}
			
			if ( statusConfig == RUN_DEMON ){
				$('li#config').html('<a href="subPageView.do?type=config"><img id="config_img" src="./img/btn_sign_03_green.gif" width="52" height="52"></a>');
			}else if ( statusConfig == STOP_DEMON ){
				$('li#config').html('<a href="subAlarmView.do?type=config"><img id="config_img" src="./img/btn_sign_03_red.gif" width="52" height="52"></a>');
			}else{
				$('li#config').html('<img id="config_img" src="./img/btn_sign_03_gray.gif" width="52" height="52">');
			}
		},
		error:function(xhr,textStatus, errorThrown){
			if (xhr.readyState == 4) {
				if(reg_date == "")reg_date = nowDateFormat();
				show_stack_bar_bottom("error",jsnoResponseTitle, jsnoResponseText+"\n" +
						jscheckStartTime+" : "+reg_date+"\n"+jscheckEndTime+" : "+nowDateFormat()+"\n");
			} else {
				if(reg_date == "")reg_date = nowDateFormat();
				show_stack_bar_bottom("info",jsabnormalResponseTitle, jsabnormalResponseText+"\n" +
						jscheckStartTime+" : "+reg_date+"\n"+jscheckEndTime+" : "+nowDateFormat()+"\n");
//				clearInterval(refreshIntervalId);
			}
		}
	});	
}

var stack_bar_bottom = {"dir1": "up", "dir2": "right", "spacing1": 0, "spacing2": 0};
function show_stack_bar_bottom(type, paramTitle, paramText) {
	var opts = {
		title: "Over Here",
		text: "Check me out. I'm in a different stack.",
		addclass: "stack-bar-bottom",
		cornerclass: "",
		width: "70%",
		stack: stack_bar_bottom,
		delay: mainRefreshPeriodMinute-1600
	};
	opts.title = paramTitle;
	opts.text = paramText+"<a href='#' class='fr' onclick='$.pnotify_remove_all();'>Remove all notices</a>\n";
	switch (type) {
		case 'error':
			opts.type = "error";
			break;
		case 'info':
			opts.type = "info";
			break;
		case 'success':
			opts.type = "success";
			break;
	}
	$.pnotify(opts);
};

function nowDateFormat(){
	var now = new Date();
	var year = now.getFullYear();
	var mon = numFormat(now.getMonth()+1);
	var day = now.getDate()>9 ? ''+now.getDate() : '0'+now.getDate();
	var hour = numFormat(now.getHours());
	var min = numFormat(now.getMinutes());
	var sec = numFormat(now.getSeconds());
	var formatDate = year +"-"+ mon +"-"+ day +" "+ hour +":"+ min +":"+ sec;
	return formatDate;
}

function numFormat(num){
	var numformat = num>9 ? ''+num : '0'+num;
	return numformat;
}

function setShardingLight(){
	$.ajax({
		type:'POST',
		url : 'shadingCheck.do',
		dataType:'html',
		success:function(json,textStatus){
			if(json == "true"){
				$('#shardingCheck').show();
			}else{
				$('#shardingCheck').hide();
			}
		},
		error:function(xhr,textStatus, errorThrown){
			
		}
	});	
}

$(document).ready(function() {
	 refreshIntervalId = setInterval(function() { setTrafficLight();setShardingLight(); }, mainRefreshPeriodMinute);
		setTrafficLight();
		setShardingLight();
		
		var tooltipJson = {
				position:'bottom center',
				opacity:1,
			    delay: 0, 
			    track: true,
			    fade: 250
		};
		setTipByCustom('li#mongos',tooltipJson);
		setTipByCustom('li#mongod',tooltipJson);
		setTipByCustom('li#config',tooltipJson);
		setTipByCustom('li#balancing',tooltipJson);
});
