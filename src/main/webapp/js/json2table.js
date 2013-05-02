/**
 * JSON형태의 데이터를 테이블로 만든다
 * @author azki (azki@azki.org)
 * @version 1.01
 * @param {Element} parent
 * @param {Object} data
 * @return {void}
 */
function jsonDisplay(data){
    
	/**
	 * array 데이타를 풀어서 tableByObject로 보낸다
	 * @param array
	 */
    function tableByArray(array){
        for (var i = 0, len = array.length; i < len; i++) {
            tableByObject(array[i]);
        }
    }
    
	/**
	 * 테이블을 생성 한다.
	 * 첫번째 줄의 bottom style 지정(count를 세서 가장 첫번째 데이터에 스타일 생성)
	 * @param obj
	 */
    function tableByObject(obj){
    	var count = 0;
        for (var name in obj) {
        	count++;
        	var table=document.getElementById("myTable");
            var item = obj[name];
            var row=table.insertRow(0);
            
            item = isObject(item);
            
            var cell1=row.insertCell(0);
            var cell2=row.insertCell(1);
            cell1.width = 300; 
            cell2.width = 500;

        	if(count == 1){
            	cell1.style.borderBottom="2px solid #8256B6";
            	cell2.style.borderBottom="2px solid #8256B6";
        	}
            cell1.innerHTML=name;
            if(item==undefined){
            	item=" ";
            }
            cell2.innerHTML=item;
        }
    }
    
    function isObject(item){
		var subItem = item;
    	if (typeof(item) == 'object') {
        	if (item.constructor == Array) {
        		subItem = objectArr(item);
        	}else{
        		subItem = objectNotArr(item);
        	}
        }
    	return subItem;
    }
    
    function objectArr(array){
    	var arrStr = new Array();
    	var item ;
    	for (var i = 0, len = array.length; i < len; i++) {
    		if (typeof(array[i]) == 'object') {
    			item = objectNotArr(array[i]);
    		}else{
    			item = array[i];
    		}
            arrStr.push(item);
        }
    	return arrStr.join(", ");
    }
    
    function objectNotArr(item){
    	var arrStr = new Array();
        for (var subName in item) {
            var subItem = item[subName];
            subItem = isObject(subItem);
            arrStr.push(subName+":"+subItem);
        }
        var viewItem = "{ "+arrStr.join(", ")+" }";
        return viewItem;
    }
    
    /**
     * 받은 json 데이타에서 aaData만 분리한다.
     * @param obj
     * @returns
     */
    function aadata(obj){
	  	for (var name in obj) {
	    	var item = obj[name];
		  	if(name=="aaData"){
		    	return item;
		  	}
	  	}
    	return obj;
    }
    
    tableByArray(aadata(data));
}