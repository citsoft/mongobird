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
//하단 좌측 심각 리스트
	$(document).ready( function() {
	    var oTableCriticalEvent;
	    oTableCriticalEvent = $('#alarm_critical').dataTable( {
			//테이블의 페이징 검색 등 부가적인 기능 생략
			"bPaginate": false,
			"bLengthChange": false,
			"bFilter": false,
			"bSort": false,
			"bInfo": false,
			"bAutoWidth": false,
			
			"bProcessing":false,
	        "bServerSide":true,
	        "bRetrieve":true,
            "cache": false,
	    	"sAjaxSource":'alarmList.do?alarm=1',
			"aoColumns": [
	            { "sClass": "lb", "mDataProp": "reg_date" },
	            {
	            	"fnRender": function ( oObj ) {
	            		return oObj.aData.ip+":"+oObj.aData.port;
	            	}
	            },
	            {
	            	"sClass": "tableFiguresLeft", "fnRender": function ( oObj ) {
	            		return groupCodeTable.getItem(parseFloat( oObj.aData.groupCode ) );
	            	}
	            },
	            {
	            	"sClass": "tableFiguresLeft", "fnRender": function ( oObj ) {
	            		var str = deviceCodeTable.getItem(parseFloat( oObj.aData.deviceCode ) );
	            		return !str?"&nbsp;":str;
	            	}
	            },
	            {
	            	"sClass": "tableFiguresLeft", "fnRender": function ( oObj ) {
	            		var str = eventCodeTable.getItem( oObj.aData.cri_type );
	            		return !str?"&nbsp;":str;
	            	}
	            },
	            {
	            	"sClass": "rb tableFigures", "fnRender": function ( oObj ) {
                        var value = parseFloat( oObj.aData.cri_value );
                        var figure = parseFloat( oObj.aData.figure );
                        var realValue = parseFloat( oObj.aData.real_cri_value );
                        var realFigure = parseFloat( oObj.aData.real_figure );
                        var criType = oObj.aData.cri_type;
                        var returnValue = "";
                        if(criType == "Connection_refused"){
                                returnValue = "-";
                        }else if(criType == "connections_current"){
                                returnValue = "<a href=\"#\" class=\"deco_none\" title=\""+toolTip_title(realFigure,"")+" / "+toolTip_title(realValue,"")+"\">"
                                					+decimal(figure)+"/"+decimal(value)
                                					+"</a>";
                        }else if(criType == "mem_resident"){
                                returnValue = "<a href=\"#\" class=\"deco_none\" title=\""+toolTip_title(realFigure, "MB")+" / "+toolTip_title(realValue, "MB")+"\">"
                                					+decimal(figure)+"/"+decimal(value)
                                					+"</a>";
                        }else if(criType == "dbDataSize" || criType == "dbFileSize"){
                                returnValue = "<a href=\"#\" class=\"deco_none\" title=\""+sizeFormat(parseFloat(realFigure))+" / "+sizeFormat(parseFloat(realValue))+"\">"
                                					+decimal(figure)+"/"+ decimal(value)
                                					+"</a>";
                        }else if(criType == "diff_extra_info_page_faults"){
                                returnValue = "<a href=\"#\" class=\"deco_none\" title=\""+toolTip_title(realFigure,"")+" / "+toolTip_title(realValue,"")+"\">"
                                					+exFormat(figure)+"/"+exFormat(value)
                                					+"</a>" ;
                        }else if(criType == "diff_globalLock_lockTime" || criType == "diff_locks_timeLockedMicros_R" || criType == "diff_locks_timeLockedMicros_W" || criType == "diff_db_sum_locks_timeLockedMicros_r" || criType == "diff_db_sum_locks_timeLockedMicros_w"){
                                returnValue = "<a href=\"#\" class=\"deco_none\" title=\""+toolTip_title(realFigure,"μs")+" / "+toolTip_title(realValue,"μs")+"\">"
                                					+exFormat(microSecondsFormat(figure))+"/"+ exFormat(microSecondsFormat(value))
                                					+"</a>" ;
                        }else{
                                returnValue = figure +"/"+ value;
                        }
                        return returnValue;
	            	}
	            }
	        ],
	    	//마우스 오버 시 라인 색이 바뀌도록
			"fnDrawCallback": function(){
				//setTipByTableName("alarm_critical");
				setTipBySelector("table#alarm_critical td a[title]");
				var oSettings = oTableCriticalEvent.fnSettings();
				var iTotalRecords = oSettings.fnRecordsTotal();
				if(iTotalRecords>0){
			      $('table#alarm_critical td').bind('mouseenter', function () { $(this).parent().children().each(function(){$(this).addClass('row_selected');}); });
			      $('table#alarm_critical td').bind('mouseleave', function () { $(this).parent().children().each(function(){$(this).removeClass('row_selected');}); });
				}else{
					$('table#alarm_critical td').eq(0).attr('class', 'lb rb');
				}
			},
			"fnServerData": function ( sSource, aoData, fnCallback ) { 
				//alert("start");
//				jQuery('table thead#alarm_critical_header').showLoading();
//				jQuery('table thead#alarm_warning_header').showLoading();
			    $.getJSON( sSource, aoData, function (json) {  
			       fnCallback(json) ;
			    }).complete(function(){
			    	//alert("end");
//			    	jQuery('table thead#alarm_critical_header').hideLoading();
//			    	jQuery('table thead#alarm_warning_header').hideLoading();
			    }); 
			}
	    });
	    setInterval(function() { oTableCriticalEvent.fnDraw(); }, mainRefreshPeriodMinute); 
	    /* Add a click handler to the rows */
		$("#alarm_critical tbody").delegate("tr", "click", function() {
			var position = oTableCriticalEvent.fnGetPosition(this); // getting the clicked row position
			if(position!=null){
				var contactId = oTableCriticalEvent.fnGetData(position).idx;
				$.smartPop.open({ background: "gray", width: 800, height: 520, url: "mainConfirmView.do?idx="+contactId });
			}
		});
	});
	
	// 하단 우측 경고 영역 리스트
	$(document).ready( function() {
	    var oTableWarningEvent;
	    oTableWarningEvent = $('#alarm_warning').dataTable( {
			//테이블의 페이징 검색 등 부가적인 기능 생략
			"bPaginate": false,
			"bLengthChange": false,
			"bFilter": false,
			"bSort": false,
			"bInfo": false,
			"bAutoWidth": false,
			
			"bProcessing":false,
	        "bServerSide":true,
	        "bRetrieve":true,
            "cache": false,
	    	"sAjaxSource":'alarmList.do?alarm=2',
			"aoColumns": [
	            { "sClass": "lb", "mDataProp": "reg_date" },
	            {
	            	"fnRender": function ( oObj ) {
	            		return oObj.aData.ip+":"+oObj.aData.port;
	            	}
	            },
	            {
	            	"sClass": "tableFiguresLeft", "fnRender": function ( oObj ) {
	            		return groupCodeTable.getItem(oObj.oSettings.fnFormatNumber( parseFloat( oObj.aData.groupCode ) ));
					}
	            },
	            {
	            	"sClass": "tableFiguresLeft", "fnRender": function ( oObj ) {
	            		return deviceCodeTable.getItem(oObj.oSettings.fnFormatNumber( parseFloat( oObj.aData.deviceCode ) ));
					}
	            },
	            {
	            	"sClass": "tableFiguresLeft", "fnRender": function ( oObj ) {
	            		var str = eventCodeTable.getItem( oObj.aData.cri_type );
	            		return !str?"&nbsp;":str;
	            	}
	            },
	            {
	            	"sClass": "rb tableFigures", "fnRender": function ( oObj ) {
                        var value = parseFloat( oObj.aData.cri_value );
                        var figure = parseFloat( oObj.aData.figure );
                        var realValue = parseFloat( oObj.aData.real_cri_value );
                        var realFigure = parseFloat( oObj.aData.real_figure );
                        var criType = oObj.aData.cri_type;
                        var returnValue = "";
                        if(criType == "Connection_refused"){
                                returnValue = "-";
                        }else if(criType == "connections_current"){
                                returnValue = "<a href=\"#\" class=\"deco_none\" title=\""+toolTip_title(realFigure,"")+" / "+toolTip_title(realValue,"")+"\">"
                                					+decimal(figure)+"/"+decimal(value)
                                					+"</a>";
                        }else if(criType == "mem_resident"){
                                returnValue = "<a href=\"#\" class=\"deco_none\" title=\""+toolTip_title(realFigure, "MB")+" / "+toolTip_title(realValue, "MB")+"\">"
                                					+decimal(figure)+"/"+decimal(value)
                                					+"</a>";
                        }else if(criType == "dbDataSize" || criType == "dbFileSize"){
                                returnValue = "<a href=\"#\" class=\"deco_none\" title=\""+sizeFormat(parseFloat(realFigure))+" / "+sizeFormat(parseFloat(realValue))+"\">"
                                					+decimal(figure)+"/"+ decimal(value)
                                					+"</a>";
                        }else if(criType == "diff_extra_info_page_faults"){
                                returnValue = "<a href=\"#\" class=\"deco_none\" title=\""+toolTip_title(realFigure,"")+" / "+toolTip_title(realValue,"")+"\">"
                                					+exFormat(figure)+"/"+exFormat(value)
                                					+"</a>" ;
                        }else if(criType == "diff_globalLock_lockTime" || criType == "diff_locks_timeLockedMicros_R" || criType == "diff_locks_timeLockedMicros_W" || criType == "diff_db_sum_locks_timeLockedMicros_r" || criType == "diff_db_sum_locks_timeLockedMicros_w"){
                                returnValue = "<a href=\"#\" class=\"deco_none\" title=\""+toolTip_title(realFigure,"μs")+" / "+toolTip_title(realValue,"μs")+"\">"
                                					+exFormat(microSecondsFormat(figure))+"/"+ exFormat(microSecondsFormat(value))
                                					+"</a>" ;
                        }else{
                                returnValue = figure +"/"+ value;
                        }
                        return returnValue;
	            	}
	            }
	        ],
	    	//마우스 오버 시 라인 색이 바뀌도록
			"fnDrawCallback": function(){
				//setTipByTableName("alarm_warning");
				setTipBySelector("table#alarm_warning td a[title]");
				var oSettings = oTableWarningEvent.fnSettings();
				var iTotalRecords = oSettings.fnRecordsTotal();
				if(iTotalRecords>0){
					$('table#alarm_warning td').bind('mouseenter', function () { $(this).parent().children().each(function(){$(this).addClass('row_selected');}); });
					$('table#alarm_warning td').bind('mouseleave', function () { $(this).parent().children().each(function(){$(this).removeClass('row_selected');}); });
				}else{
					$('table#alarm_warning td').eq(0).attr('class', 'lb rb');
				}
			}
	    });
	    setInterval(function() { oTableWarningEvent.fnDraw(); }, mainRefreshPeriodMinute); 
	    $("#alarm_warning tbody").delegate("tr", "click", function() {
			var position = oTableWarningEvent.fnGetPosition(this); // getting the clicked row position
			if(position!=null){
				var contactId = oTableWarningEvent.fnGetData(position).idx;
				$.smartPop.open({ background: "gray", width: 800, height: 520, url: "mainConfirmView.do?idx="+contactId });
			}
		});
	});
