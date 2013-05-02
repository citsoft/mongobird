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
//2007-01-01 문자열을날짜객체로변환
function getDateObjectFromDashStr(dateStr, hours, minutes){
	var dateinfo = dateStr.split("-");
	return new Date(dateinfo[0] , dateinfo[1] -1 , dateinfo[2], hours, minutes);
}

//Date 객체를 2007-01-01 형식문자열로변환
function getDateStrFromDateObject(dateObject){
	var str = null;
	var month = dateObject.getMonth() + 1;
	var day = dateObject.getDate();

	if(month <  10)
		month = '0' + month;

	if(day < 10)
		day = '0' + day;

	str = dateObject.getFullYear() + '-' + month + '-' + day;
	return str;

}


function getHourStrFromDateObject(dateObject){
	var str = null;
	str = leadingZeros(dateObject.getHours(), 2);
	return str;

}

function getMinuteStrFromDateObject(dateObject){
	var str = null;
	str = leadingZeros(dateObject.getMinutes(), 2);
	return str;
	
}

//두날짜사이의기간리턴edate - sdate  (sdate 보다edate가크면1 , 작으면-1 , 같으면0)
function compareIsPastDay(sdate , edate){
	if(edate - sdate < 0)
		return -1;
	else if(edate - sdate == 0)
		return 0 ;
	else
		return 1;
}                

//날짜조건계산후date 객체리턴
function getDateObjectOfPlusDay(targetDate , plusDayInt){
	var newDate = new Date();
	var processTime = targetDate.getTime() - (parseInt(plusDayInt) * 24 * 60 * 60 * 1000);
	newDate.setTime(processTime);                                    
	return newDate;
}

//날짜검색용invalidator
function invalidateSearchDay(sdateStr, shours, sminute, edateStr, ehours, eminute, limitDay){
	
	var sdate = getDateObjectFromDashStr(sdateStr, shours, sminute);
	var edate = getDateObjectFromDashStr(edateStr, ehours, eminute);
	
	var checkDayResult = compareIsPastDay(sdate , edate);
	
	if(checkDayResult <= 0){
		alert('시작일 ~ 종료일이어야합니다.');
		return false;
	}

	if (limitDay >= 0) {
		var before7Day = getDateObjectOfPlusDay(edate , limitDay);
		var checkResult = compareIsPastDay(before7Day , sdate);
	
		if(checkResult < 0){
			alert(limitDay + ' 일이내로검사하세요.');
			return false;
		}
	}
	return true;
}

function plusSecond(sec) {
	return new Date(Date.parse(new Date()) + sec*1000);
}

function plusMinute(min) {
	return new Date(Date.parse(new Date()) + (min*1000) * 60);
}

function plusHour(hour) {
	return new Date(Date.parse(new Date()) + (hour*1000) * 60 * 60);
}

function plusSecondOnDateObj(dateObj, sec) {
	if (sec >= 0) {
		return new Date(Date.parse(dateObj) + sec*1000);
	} else {
		return new Date(Date.parse(dateObj) - (sec*-1)*1000);
	}	
}

function plusMinuteOnDateObj(dateObj, min) {
	if (min >= 0) {
		return new Date(Date.parse(dateObj) + (min*1000) * 60);		
	} else {
		return new Date(Date.parse(dateObj) - ((min*-1)*1000) * 60);
	}
}

function plusHourOnDateObj(dateObj, hour) {
	if (hour >= 0) {
		return new Date(Date.parse(dateObj) + (hour*1000) * 60 * 60);		
	} else {
		return new Date(Date.parse(dateObj) - ((hour*-1)*1000) * 60 * 60);
	}
}

function getDateFormatyyyyMMddHHmmss(d) {
	var s =
	    leadingZeros(d.getFullYear(), 4) + '-' +
	    leadingZeros(d.getMonth() + 1, 2) + '-' +
	    leadingZeros(d.getDate(), 2) + ' ' +

	    leadingZeros(d.getHours(), 2) + ':' +
	    leadingZeros(d.getMinutes(), 2) + ':' +
	    leadingZeros(d.getSeconds(), 2);
	return s;
}

function nowDateNoFormat(){
	var nowDate = new Date();
	return nowDate.getTime();
}

function dateFormatSplit(str){
	var setDate = new Date();
	
	var arrFullDate = str.split(" ");
	var dateStr = arrFullDate[0];
	var timeStr = arrFullDate[1];
	
	var arrDate = dateStr.split("-");
	var arrTime = timeStr.split(":");
	
	setDate = new Date(arrDate[0], arrDate[1]-1, arrDate[2], arrTime[0], arrTime[1]);
	return setDate;
}

function formatDate(value){
	return $.format.date(value, "yyyy-MM-dd HH:mm");
}
