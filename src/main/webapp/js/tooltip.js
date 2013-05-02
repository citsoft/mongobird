//function setTip(){	
//	$('table a[title]').qtip({
//	      position: {
//	         corner: {
//	            target: 'topRight',
//	            tooltip: 'bottomLeft'
//	         }
//	      },
//	      style: {
//	         name: 'cream',
//	         padding: '7px 13px',
//	         width: {
//	            max: 210,
//	            min: 0
//	         },
//	         tip: true
//	      }
//	   });
//}
function setTipByTableName(table_id){	
	$("table#"+table_id+" td a[title]").tooltip({
		position:'top center',
		offset:[-5,-20],
		opacity:0.7,
	    delay: 0, 
	    track: true,
	    fade: 250
	});
}

function setTipBySelector(strSelector){	
	$(strSelector).tooltip({
		position:'top left',
		opacity:0.7,
	    delay: 0, 
	    track: true,
	    fade: 250
	});
}

function setTipByObjectForInitDemon(obj){	
	obj.tooltip({
		position:'top center',
		offset:[21,0],
		opacity:0.7,
	    delay: 0, 
	    track: true,
	    fade: 250
	});
}

function setTipByCustom(strSelector,json){	
	$(strSelector).tooltip(json);
}