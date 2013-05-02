(function calendarSelector() {
  var self = this;

  /* ******************************************************************
  
   * Example formats:
   *   - "Y-m-d"     => 2010-12-31 (script default)
   *   - "F jS, Y"   => December 31st, 2010
   *   - 'm/j/y"     => 12/31/10
   *   - 'Y-M-j"     => 2010-Dec-31
   *   - "D M j, Y"  => Fri Dec 31, 2010
   *
   */
  this.format = "Y-m-d";

  this.weekStart = 0;  // 0 = Sunday, 1 = Monday


  this.date = function(format, ts) {
    var jsd = (typeof ts === 'undefined') ? new Date() : (ts instanceof Date) ? new Date(ts) : new Date(ts * 1000);
    var fChr = /\\?([a-z])/gi, fChrCb = function (t, s) { return f[t] ? f[t]() : s; };
    var _pad = function (n, c) { return ((n = n + "").length < c) ? new Array((++c) - n.length).join("0") + n : n; };
    var txt_words = [
     "Sun", "Mon", "Tues", "Wednes", "Thurs", "Fri", "Satur",
     "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"
    ], txt_ordin = {1: "st", 2: "nd", 3: "rd", 21: "st", 22: "nd", 23: "rd", 31: "st"};
    var f = { // Day
      d: function() { return _pad(f.j(), 2); },
      D: function() { return f.l().slice(0, 3); },
      j: function() { return jsd.getDate(); },
      l: function() { return txt_words[f.w()] + 'day'; },
      S: function() { return txt_ordin[f.j()] || 'th'; },
      w: function() { return jsd.getDay(); },
      // Month
      F: function() { return txt_words[6 + f.n()]; },
      m: function() { return _pad(f.n(), 2); },
      M: function() { return f.F().slice(0, 3); },
      n: function() { return jsd.getMonth() + 1; },
      // Year
      Y: function() { return jsd.getFullYear(); },
      y: function() { return (f.Y() + "").slice(-2); }
    };
    return format.replace(fChr, fChrCb);
  };

  // ***** Parse a date from a string *********************************
  this.parse = function(data, rtrn, format) {
    if (!data) return rtrn;
    if (!format) format = this.format; 
    var fChr = /\\?([a-z])/gi, fChrCb = function (t, s) { return f[t] ? f[t]() : s; }, ordr = [];
    var months = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
    var f = { // Day
      d: function() { ordr.push("d"); return "(\\d\\d)"; },
      D: function() { return "(?:Mon|Tue|Wed|Thu|Fri|Sat|Sun)"; },
      j: function() { ordr.push("d"); return "(\\d\\d?)"; },
      l: function() { return "(?:Mon|Tues|Wednes|Thurs|Fri|Satur|Sun)day"; },
      S: function() { return "(?:st|nd|rd|th)"; },
      w: function() { return "\\d"; },
      // Month
      F: function() { ordr.push("M"); return "(January|February|March|April|May|June|July|August|September|October|November|December)"; },
      m: function() { ordr.push("m"); return "(\\d\\d)"; },
      M: function() { ordr.push("M"); return "(" + months.join("|") + ")"; },
      n: function() { ordr.push("m"); return "(\\d\\d?)"; },
      // Year
      Y: function() { ordr.push("y"); return "(\\d{4})"; },
      y: function() { ordr.push("y"); return "(\\d\\d)"; }
    };
    format = format.replace(/[-[\]{}()*+?.,\\^$|#\s]/g, "\\$&");
    var outp = data.match(new RegExp("^\\s*" + format.replace(fChr, fChrCb) + "\\s*$", ""));
    if (outp && ordr.length == 3) {
      for (var x = 0, val = []; x < ordr.length; x++) {
        switch (ordr[x]) {
          case "d": val[2] = parseInt(outp[x + 1], 10); break;
          case "m": val[1] = parseInt(outp[x + 1], 10) - 1; break;
          case "y": val[0] = parseInt(outp[x + 1], 10); break;
          case "M":
            for (var y = 0, o = outp[x + 1].slice(0, 3); y < months.length; y++)
              if (o == months[y]) val[1] = y;
        }
      } return new Date(val[0], val[1], val[2]);
    } else return rtrn;
  };

  // ***** Show the calendar ******************************************
  this.show = function(elem) {
    if (this.cal.parentNode != document.body || elem != this.elem) {
      this.elem = elem;
      this.point = this.parse(elem.value, new Date());
      this.build();
      var cleft = ctop = 0, obj = elem;
      do { cleft += obj.offsetLeft; ctop += obj.offsetTop; } while (obj = obj.offsetParent);
      this.cal.style.left = cleft + "px";
      this.cal.style.top = (ctop + elem.offsetHeight) + "px";
      document.body.appendChild(this.cal);
      setTimeout(function() {
        _addListener(document.documentElement, 'click', self.listener = function() {
          self.hide(false);
        }, false);
      }, 0);
    } else this.hide(false);
  };

  // ***** Hide the calendar ******************************************
  this.hide = function(value) {
    if (typeof value == "string") this.elem.value = value;
    this.cal = document.body.removeChild(this.cal);
    _removeListener(document.documentElement, 'click', this.listener, false);
  };

  // ***** Build the body of the calendar *****************************
  this.build = function() {
    var mindt = (mindt = this.parse(this.elem.getAttribute('min'), false, "Y-m-d")) ? this.date("Ymd", mindt) : "";
    var maxdt = (maxdt = this.parse(this.elem.getAttribute('max'), false, "Y-m-d")) ? this.date("Ymd", maxdt) : "";
    var slctd = (slctd = this.parse(this.elem.value, false)) ? this.date("Ymd", slctd) : "";

    this.point.setDate(15);
    if (mindt) while (this.date("Ym", this.point) < mindt.substr(0, 6)) this.point.setMonth(this.point.getMonth() + 1);
    if (maxdt) while (this.date("Ym", this.point) > maxdt.substr(0, 6)) this.point.setMonth(this.point.getMonth() - 1);

    var day = new Date(this.point.getTime()), today = this.date("Ymd");
    day.setDate(1);
    day.setDate(1 - day.getDay() + this.weekStart);

    while (this.cal.tBody.firstChild) this.cal.tBody.removeChild(this.cal.tBody.firstChild);
    do {
      var tr = document.createElement('tr');
      for (var x = 0, ymd; x < 7; x++) {
        ymd = this.date("Ymd", day);
        var td = document.createElement('td');
            td.className = "";
            td.title = this.date("l", day);
            td.date = new Date(day.getTime());
          if ((!mindt || ymd >= mindt) && (!maxdt || ymd <= maxdt)) {
              td.className = "valid";
              td.onclick = function() { self.hide(self.date(self.format, this.date)); };
          } else td.className = "";
          if (ymd == slctd) td.className += " selected";
          if (day.getMonth() == this.point.getMonth()) {
            td.className += " month";
            if (ymd == today) td.className += " today";
            if (!day.getDay() || day.getDay() == 6) td.className += " weekend";
          } td.appendChild(document.createTextNode(day.getDate()));
          tr.appendChild(td);
        day.setDate(day.getDate() + 1);
      } this.cal.tBody.appendChild(tr);
    } while (day.getMonth() == this.point.getMonth());

    if (mindt) {
      mindt = (this.point.getFullYear() - mindt.substr(0, 4)) * 12 - parseInt(mindt.substr(4, 2), 10) + this.point.getMonth() + 1;
      this.cal.tHeads[0].className = (mindt < 1) ? "disabled" : "";
      this.cal.tHeads[1].getElementsByTagName('strong')[0].className = (mindt < 1) ? "disabled" : "";
    } this.cal.tHeads[1].getElementsByTagName('span')[0].firstChild.nodeValue = this.date("F, Y", point);
    if (maxdt) {
      maxdt = (this.point.getFullYear() - maxdt.substr(0, 4)) * 12 - parseInt(maxdt.substr(4, 2), 10) + this.point.getMonth() + 1;
      this.cal.tHeads[2].className = (maxdt > -1) ? "disabled" : "";
      this.cal.tHeads[1].getElementsByTagName('em')[0].className = (maxdt > -1) ? "disabled" : "";
    }
  };

  var _addListener = function(elem, type, listener, useCapture) {
    if (elem.addEventListener) {
      elem.addEventListener(type, listener, useCapture);
    } else if (elem.attachEvent) elem.attachEvent('on' + type, listener);
  };
  var _removeListener = function(elem, type, listener, useCapture) {
    if (elem.removeEventListener) {
      elem.removeEventListener(type, listener, useCapture);
    } else if (elem.detachEvent) elem.detachEvent('on' + type, listener);
  };

  var _isIE6 = /*@cc_on@if(@_jscript_version == 5.6)!@end@*/false;

  // Build the generic calendar container
  this.elem = false;
  this.cal = document.createElement('div');
  this.cal.id = "calendarSelector";
  var table = document.createElement('table');
      table.cellSpacing = 1;
    var thead = document.createElement('thead');
      var tr = document.createElement('tr');
        var td = document.createElement('td');
            td.appendChild(document.createTextNode((_isIE6) ? "<<" : "\u25C2\u25C2"));
            td.onclick = function() {
              if (this.className != "disabled") {
                self.point.setFullYear(self.point.getFullYear() - 1);
                self.build(); } };
          tr.appendChild(td);
        var th = document.createElement('th');
            th.colSpan = 5;
          var strong = document.createElement('strong');
              strong.appendChild(document.createTextNode((_isIE6) ? "<" : "\u25C2"));
              strong.onclick = function() {
                if (this.className != "disabled") {
                  self.point.setMonth(self.point.getMonth() - 1);
                  self.build(); } };
            th.appendChild(strong);
          var em = document.createElement('em');
              em.appendChild(document.createTextNode((_isIE6) ? ">" : "\u25B8"));
              em.onclick = function() {
                if (this.className != "disabled") {
                  self.point.setMonth(self.point.getMonth() + 1);
                  self.build(); } };
            th.appendChild(em);
          var span = document.createElement('span');
              span.appendChild(document.createTextNode("\u2014"));
            th.appendChild(span);
          tr.appendChild(th);
        var td = document.createElement('td');
            td.appendChild(document.createTextNode((_isIE6) ? ">>" : "\u25B8\u25B8"));
            td.onclick = function() {
              if (this.className != "disabled") {
                self.point.setFullYear(self.point.getFullYear() + 1);
                self.build(); } };
          tr.appendChild(td);
        thead.appendChild(tr);
      table.appendChild(thead);
    var tfoot = document.createElement("tfoot");
      var tr = document.createElement('tr');
        var td = document.createElement('td');
            td.colSpan = 2;
            td.appendChild(document.createTextNode('Today'));
            td.onclick = function() { self.hide(self.date(self.format)); };
          tr.appendChild(td);
        var td = document.createElement('td');
            td.colSpan = 3;
            td.appendChild(document.createTextNode('Cancel'));
            td.onclick = function() { self.hide(false); };
          tr.appendChild(td);
        var td = document.createElement('td');
            td.colSpan = 2;
            td.appendChild(document.createTextNode('Clear'));
            td.onclick = function() { self.hide(""); };
          tr.appendChild(td);
        tfoot.appendChild(tr);
      table.appendChild(tfoot);
      table.appendChild(this.cal.tBody = document.createElement('tbody'));
  this.cal.appendChild(table);
  this.cal.tHeads = thead.rows[0].cells;
  this.cal.onselectstart = function() { return false; };
  this.cal.unselectable = "on";
  this.cal.onclick = function(e) {
    e = e || event;
    if (e.stopPropagation) { e.stopPropagation(); } else e.cancelBubble = true;
  };
  if (window.opera) this.cal.onmousedown = function() { return false; };

  this.eventReturn = function inputBoxControl(){
	  var key = window.event.keyCode;
	  	
	  	if( (key == 8) || (key == 9) || (key == 13) || (key == 16) || 
	  			(key == 37)  || (key == 39) || (key == 46) || (key == 116) ){
	  		return true;
	  	} else if( (key<58 && key>47) || (key == 189)){
	  		return false;
	  	} else {
	  		return false;
	  	}
  };
  
  // ***** Apply listeners to applicable inputs
  _addListener(window, 'load', function() {
    for (var x = 0, inputs = document.getElementsByTagName('input'), val; x < inputs.length; x++) {
      if (inputs[x].getAttribute('type') == "sdate") {
        inputs[x].type = "text";
        inputs[x].onclick = function() { self.show(this); };
        inputs[x].onkeydown = function() { window.event.returnValue = self.eventReturn(); };
        
        if (val = this.parse(inputs[x].value, false, "Y-m-d")) {
          var mindt = this.parse(inputs[x].getAttribute('min'), false, "Y-m-d");
          var maxdt = this.parse(inputs[x].getAttribute('max'), false, "Y-m-d");
          if (mindt && val < mindt) val = mindt;
          if (maxdt && val > maxdt) val = maxdt;
          inputs[x].value = this.date(this.format, val);
        } else inputs[x].value = "";
      }

	  if (inputs[x].getAttribute('type') == "edate") {
        inputs[x].type = "text";
        inputs[x].onclick = function() { self.show(this); };
        inputs[x].onkeydown = function() { window.event.returnValue = self.eventReturn(); };
    
        if (val = this.parse(inputs[x].value, false, "Y-m-d")) {
          var mindt = this.parse(inputs[x].getAttribute('min'), false, "Y-m-d");
          var maxdt = this.parse(inputs[x].getAttribute('max'), false, "Y-m-d");
          if (mindt && val < mindt) val = mindt;
          if (maxdt && val > maxdt) val = maxdt;
          inputs[x].value = this.date(this.format, val);
        } else inputs[x].value = "";
      }
	  
	  if (inputs[x].getAttribute('type') == "searchdate") {
        inputs[x].type = "text";
        inputs[x].onclick = function() { self.show(this); };
        inputs[x].onkeydown = function() { window.event.returnValue = self.eventReturn(); };
    
        if (val = this.parse(inputs[x].value, false, "Y-m-d")) {
          var mindt = this.parse(inputs[x].getAttribute('min'), false, "Y-m-d");
          var maxdt = this.parse(inputs[x].getAttribute('max'), false, "Y-m-d");
          if (mindt && val < mindt) val = mindt;
          if (maxdt && val > maxdt) val = maxdt;
          inputs[x].value = this.date(this.format, val);
        } else inputs[x].value = "";
      }
    }
  }, false);
})();