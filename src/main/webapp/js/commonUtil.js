function dbg(x){
	var oTxtDbg = document.getElementById("txtDbg");
	var t = new Date();
	oTxtDbg.value=t.getHours()+":"+t.getMinutes()+":"+t.getSeconds()+"."+t.getMilliseconds()+"> "+x+"\n"+oTxtDbg.value;
}

function HashTable(obj) {
    this.length = 0;
    this.items = {};
    for (var p in obj) {
        if (obj.hasOwnProperty(p)) {
            this.items[p] = obj[p];
            this.length++;
        }
    }

    this.setItem = function(key, value) {
        var previous = undefined;
        if (this.hasItem(key)) {
            previous = this.items[key];
        }
        else {
            this.length++;
        }
        this.items[key] = value;
        return previous;
    };

    this.getItem = function(key) {
        return this.hasItem(key) ? this.items[key] : undefined;
    };

    this.hasItem = function(key) {
        return this.items.hasOwnProperty(key);
    };
   
    this.removeItem = function(key) {
        if (this.hasItem(key)) {
            previous = this.items[key];
            this.length--;
            delete this.items[key];
            return previous;
        }
        else {
            return undefined;
        }
    };

    this.keys = function() {
        var keys = [];
        for (var k in this.items) {
            if (this.hasItem(k)) {
                keys.push(k);
            }
        }
        return keys;
    };

    this.values = function() {
        var values = [];
        for (var k in this.items) {
            if (this.hasItem(k)) {
                values.push(this.items[k]);
            }
        }
        return values;
    };

    this.each = function(fn) {
        for (var k in this.items) {
            if (this.hasItem(k)) {
                fn(k, this.items[k]);
            }
        }
    };

    this.clear = function() {
        this.items = {};
        this.length = 0;
    };
}

function arrayList(){
	this.list=[];
	
	this.add = function(item){
		this.list.push(item);
	};
	  
	this.get = function(index){
		return this.list[index];
	};
	
	this.removeAll = function(){
		this.list=[];
	};
	  
	this.size = function(){
		return this.list.length;
	};
	   
	this.remove = function(index){
		var newList=[];
		for(var i=0;i<this.list.length;i++){
			if(i!=index){
				newList.push(this.list[i]);
			};
		};
	   this.list = newList;
	  };
};


function addCommas(nStr){
	nStr += '';
	x = nStr.split('.');
	x1 = x[0];
	x2 = x.length > 1 ? '.' + x[1] : '';
	var rgx = /(\d+)(\d{3})/;
	while (rgx.test(x1)) {
		x1 = x1.replace(rgx, '$1' + ',' + '$2');
	}
	return x1 + x2;;
}

function exFormat(count){
	var ex = 0;
	var unit = "";
	var spacer = "";
	var TERA_NUMBER = 1000000000000;
	var GIGA_NUMBER = 1000000000;
	var MEGA_NUMBER = 1000000;
	var KILO_NUMBER = 1000;
	if(count>=TERA_NUMBER){//...
		ex = count/TERA_NUMBER;
		spacer = " ";
		unit = "T";
	}else if(count<TERA_NUMBER && count>=GIGA_NUMBER){//10억
		ex = count/GIGA_NUMBER;
		spacer = " ";
		unit = "G";
	}else if(count<GIGA_NUMBER && count>=MEGA_NUMBER){//100만
		ex = count/MEGA_NUMBER;
		spacer = " ";
		unit = "M";
	}else if(count<MEGA_NUMBER && count>=KILO_NUMBER){//1000
		ex = count/KILO_NUMBER;
		spacer = " ";
		unit = "K";
	}else{
		ex = changeNaN(count);
	}
	return decimal(ex)+spacer+unit;
}

function sizeFormat(bytes){
	var size = 0;
	var unit = "";
	var TERA_NUMBER = 1024*1024*1024*1024;
	var GIGA_NUMBER = 1024*1024*1024;
	var MEGA_NUMBER = 1024*1024;
	var KILO_NUMBER = 1024;
	if(bytes>=TERA_NUMBER){
		size = bytes/TERA_NUMBER;
		unit = "TB";
	}else if(bytes<TERA_NUMBER && bytes>=GIGA_NUMBER){
		size = bytes/GIGA_NUMBER;
		unit = "GB";
	}else if(bytes<GIGA_NUMBER && bytes>=MEGA_NUMBER){
		size = bytes/MEGA_NUMBER;
		unit = "MB";
	}else if(bytes<MEGA_NUMBER && bytes>=KILO_NUMBER){
		size = bytes/KILO_NUMBER;
		unit = "KB";
	}else{
		size = changeNaN(bytes);
	}
	return addCommas(decimal(size))+" "+unit;
}

function dhtmlTimeFormat(time){
	var seconds = 0;
	var unit = "";
	var micro = 1000000;
	var mili = 1000;
	if(time>=micro){
		seconds = parseFloat(time)/micro;
		unit = "sec";
	} else if (time>=mili) {
		seconds = parseFloat(time)/mili;
		unit = "msec";
	} else{
		seconds = parseFloat(time)
		unit = "micro";
	}
	return addCommas(decimal(seconds))+" "+unit;
}

function megaSizeFormat(mega){
	var size = 0;
	var unit = "";
	var TERA_NUMBER = 1024*1024;
	var GIGA_NUMBER = 1024;
	if(mega>=TERA_NUMBER){
		size = mega/TERA_NUMBER;
		unit = "TB";
	}else if(mega<TERA_NUMBER && mega>=GIGA_NUMBER){
		size = mega/GIGA_NUMBER;
		unit = "GB";
	}else{
		size = changeNaN(mega);
		if(size!=0)unit = "MB";
	}
	return addCommas(decimal(size))+" "+unit;
}

function microSecondsFormat(get){
	var seconds = get;
	if(get > 0){
		seconds = parseFloat(get)/1000000;
	}
	return seconds;
}

function convertMicroStoMilliS(get){
	var micro = changeNaN(get);
	if(get > 0){
		micro = parseFloat(micro)/1000;
	}
	return addCommas(decimal(micro))+" ms";
}

function toolTip_title(value,unit){
	var u = "";
	if(unit!="")u = " " + unit;
	var title = addCommas(value) + u;
	return title;
}

function changeNaN(bytes){
	if (isNaN(bytes)) return 0;
	return bytes;
}

function decimal(size){
	var deci = parseFloat(size).toFixed(2);
	return deci;
}

function PeriodMinuteFormat(get){
	var seconds = get;
	if(get > 0){
		seconds = parseFloat(get/1000);
	}
	return deciFormat(seconds);
}

function num_only( Ev ){ 
    var evCode = ( window.netscape ) ? Ev.which : event.keyCode ; 
    /* FF일 경우 Ev.which 값을, 
        IE을 경우 event.keyCode 값을 evCode에 대입 */ 
    if ( ! ( evCode == 0 || evCode == 8 || ( evCode > 47 && evCode < 58 ) ) ) { 
    /* 눌러진 키 코드가 숫자가 아닌 경우 
        ( '0'은 FF에서 Tab 키, 
          '8'은 FF에서 BackSpace가 먹히지 않아 삽입)    */ 
        if ( window.netscape ) {        // FF일 경우 
            Ev.preventDefault() ;        // 이벤트 무효화 
        } else {                                // IE일 경우 
            event.returnValue=false;    // 이벤트 무효화 
        } 
    } 
}

function num_eng( Ev ){ 
    var evCode = ( window.netscape ) ? Ev.which : event.keyCode ; 
    /* FF일 경우 Ev.which 값을, 
        IE을 경우 event.keyCode 값을 evCode에 대입 */ 
    if ( !((65 <= evCode && evCode <= 90) || (evCode >= 97 && evCode <= 122) || (48 <= evCode && evCode <=57) || ( evCode == 0 || evCode == 8 || evCode == 64 || evCode == 46 )) ) { 
    /* 눌러진 키 코드가 숫자가 아닌 경우 
        ( '0'은 FF에서 Tab 키, 
          '8'은 FF에서 BackSpace가 먹히지 않아 삽입,
          '64' @
          '96' .)*/ 
        if ( window.netscape ) {        // FF일 경우 
            Ev.preventDefault() ;        // 이벤트 무효화 
        } else {                                // IE일 경우 
            event.returnValue=false;    // 이벤트 무효화 
        } 
    } 
}

function deciFormat(num){
	if(num==0.0||num==0.00)return 0 ;
	return num;
}

function defaultImg_graph(url, obj){
    var altSrc = url;
    obj.src = altSrc;
}

function leadingZeros(n, digits) {
	var zero = '';
	n = n.toString();

	if (n.length < digits) {
		for (i = 0; i < digits - n.length; i++)
			zero += '0';
	}
	return zero + n;
}

function convertFigures2HTML(figures){
	var spaceIdx = figures.indexOf(" ");
	var strNum = "";
	var strUnit = "";
	if(spaceIdx < 0){
		strNum = figures;
		strUnit = "<font color=\"white\">_</font>";
	}else{
		strNum = figures.substr(0,spaceIdx);
		strUnit = figures.substring(spaceIdx);
	}

	return "<label class=\"figuresUnit\">"+strUnit+"</label><label class=\"figuresNum\">"+strNum+"</label>"
}
