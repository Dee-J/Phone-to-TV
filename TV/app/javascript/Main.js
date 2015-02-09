var elem = 0;
var tvKey = new Common.API.TVKeyValue();
var widgetAPI = new Common.API.Widget();


var PL_NNAVI_STATE_BANNER_VOL = 1;

window.onShow = function(e) {
    var NNaviPlugin = caph.platform.dtv.Device.plugin('NNAVI');
    NNaviPlugin.SetBannerState(PL_NNAVI_STATE_BANNER_VOL);
    caph.platform.dtv.Device.unRegisterKey(caph.platform.Key.VOL_UP);
    caph.platform.dtv.Device.unRegisterKey(caph.platform.Key.VOL_DOWN);
};



var Main = {
	content : {
		elem : jQuery('#noti'),
	},
};

var History;

//IME 오브젝트 선언
var oIME = null;
var userSettingKeyMaster = 'userSetting';
var dropdown = 0;
var subMenu = 0;
var elementNum = Array(0, 4, 3, 3, 4, 2);
Main.onLoad = function()
{
	$('#key').html(curWidget.id);
	//this.focus();	// 초기 포커스 설정
	widgetAPI.sendReadyEvent();
	//Main.loadContent();	//현재 카테고리에 해당하는 View 콘텐츠 로드
	Main.enableKeys();
	Usersetting.keyInit();
	History.Init();
	$("#icon1").css('width', 50).css("height", 50).css("left", 970+100).css("top", 20);
	$("#icon2").css('width', 50).css("height", 50).css("left", 1030+100).css("top", 20);
	$("#icon3").css('width', 50).css("height", 50).css("left", 1090+100).css("top", 20);
	$("#icon1Circle").css('font-size', '18px').css('width', 25).css("height", 25).css("left", 970+100).css("top", 20).hide();
	$("#icon2Circle").css('font-size', '18px').css('width', 25).css("height", 25).css("left", 1030+100).css("top", 20).hide();
	$("#icon3Circle").css('font-size', '18px').css('width', 25).css("height", 25).css("left", 1090+100).css("top", 20).hide();

	$("#mail1").css('width', 25).css("height", 70).css("left", 1260).css("top", 20).hide();
	$("#mail2").css('width', 25).css("height", 70).css("left", 1260).css("top", 60 + 45*2).hide();
	$("#mail3").css('width', 25).css("height", 70).css("left", 1260).css("top", 100 + 45*3).hide();
	$("#mail4").css('width', 25).css("height", 70).css("left", 1260).css("top", 140 + 45*4).hide();
	$("#mail1Circle").css('font-size', '18px').css('width', 25).css("height", 25).css("left", 1260).css("top", 20).hide();
	$("#mail2Circle").css('font-size', '18px').css('width', 25).css("height", 25).css("left", 1260).css("top", 60 + 45*2).hide();
	$("#mail3Circle").css('font-size', '18px').css('width', 25).css("height", 25).css("left", 1260).css("top", 100 + 45*3).hide();
	$("#mail4Circle").css('font-size', '18px').css('width', 25).css("height", 25).css("left", 1260).css("top", 140 + 45*4).hide();
	iconInit();
};

function iconInit(){
	for(var i = 1; i < 4; i++)
		$("#iconInner" + i).hide();
}

History.Init = function(){
	this.mesgq = new Array();	
};

function successCB() {
    console.log("tuning is successful");  
}   
function errorCB(error) {
    console.log(error.name);  
}    
var Usersetting = {
		
};

var fileSystemObj = new FileSystem();
var path;
var load = function (){
	if(fileSystemObj.isValidCommonPath(curWidget.id) != 1){
		fileSystemObj.createCommonDir(curWidget.id);
	}
	path = curWidget.id + "/userSetting.dat";
};

var read = function(){
	var result, jsFileObj;
	alert(path);
	jsFileObj = fileSystemObj.openCommonFile(path, "r");
	if(jsFileObj){
		result = jsFileObj.readAll();
		fileSystemObj.closeFile(jsFileObj);
	}else{
		result = false;
	}
	return result;
};

var write = function(val){
	var jsFileObj;
	jsFileObj = fileSystemObj.openCommonFile(path, 'w');
	if(jsFileObj){
		alert('write go...');
	}else{
		alert('write fail...');
	}
	jsFileObj.writeAll(val);
	fileSystemObj.closeFile(jsFileObj);
};

Usersetting.keyInit = function(){
	load();
	if(read() == false){
		alert('file not found');
		write("카드형 알림|중간 크기|상단|3초|알림 활성화");
	}
	var str = read();
	alert(str);
	var res = str.split("|");
	$('#usr_1').text(res[0]);
	$('#usr_2').text(res[1]);
	$('#usr_3').text(res[2]);
	$('#usr_4').text(res[3]);
	$('#usr_5').text(res[4]);
	//$('#usr_5').text(curWidget.id);
	$('#settingPanel').hide();
	initAlertType(res[0]);
};

var keyDownMaster = 'outter';
var tmp = 0;

Main.keyDown = function(){
	var keyCode = event.keyCode;
	alert(keyCode + 'key 입력댐');
	
	switch(keyDownMaster){
	case 'outter':
		outterKeyDown(keyCode);
		break;
	case 'userSetting': 
	case 'inDropdown':
		userSettingKeyDown(keyCode);
		break;
	case 'history':
		historyKeyDown(keyCode);
		break;
	}
	
	
	//unregister 처리
	switch(keyCode){
	case tvKey.KEY_RETURN:
		event.preventDefault();
		break;
	case 68:
		webapis.tv.channel.tuneUp(successCB, errorCB, webapis.tv.channel.NAVIGATOR_MODE_ALL, 0);
		break;
	case 65:
		webapis.tv.channel.tuneDown(successCB, errorCB, webapis.tv.channel.NAVIGATOR_MODE_ALL, 0);
		break;
	}
};

function historyKeyDown(key){
	alert(key);
	switch(key){
    case tvKey.KEY_RETURN:
		$("#history").slideUp();
		keyDownMaster = 'outter';
		break;
    case tvKey.KEY_DOWN:
		$('#perScroll_' + tmp + '1').removeClass('box_shadow');
		$('#perScroll_' + tmp + '2').removeClass('box_shadow');
		$('#perScroll_' + tmp + '3').removeClass('box_shadow');
		$('#perScroll_' + tmp + '4').removeClass('box_shadow');
		$('#perScroll_' + tmp + '5').removeClass('box_shadow');
		tmp += 1;
		if(tmp == History.mesgq.length)
			tmp = 0;
    	$('#historyTableDiv').scrollTop(tmp * 77);
    	$('#perScroll_' + tmp + '1').addClass('box_shadow');
    	$('#perScroll_' + tmp + '2').addClass('box_shadow');
    	$('#perScroll_' + tmp + '3').addClass('box_shadow');
    	$('#perScroll_' + tmp + '4').addClass('box_shadow');
    	$('#perScroll_' + tmp + '5').addClass('box_shadow');
		break;
    case tvKey.KEY_UP:
		$('#perScroll_' + tmp + '1').removeClass('box_shadow');
		$('#perScroll_' + tmp + '2').removeClass('box_shadow');
		$('#perScroll_' + tmp + '3').removeClass('box_shadow');
		$('#perScroll_' + tmp + '4').removeClass('box_shadow');
		$('#perScroll_' + tmp + '5').removeClass('box_shadow');
    	if(tmp == 0)
    		tmp = History.mesgq.length;
		tmp -= 1;
    	$('#historyTableDiv').scrollTop(tmp * 77);
    	$('#perScroll_' + tmp + '1').addClass('box_shadow');
    	$('#perScroll_' + tmp + '2').addClass('box_shadow');
    	$('#perScroll_' + tmp + '3').addClass('box_shadow');
    	$('#perScroll_' + tmp + '4').addClass('box_shadow');
    	$('#perScroll_' + tmp + '5').addClass('box_shadow');
		if(tmp == -1)
			tmp = History.mesgq.length;
		break;	
	}
	
	switch(key){
	case tvKey.KEY_RETURN:
		event.preventDefault();
		break;
	}
};

function outterKeyDown(key){
	switch(key){
	case 22:
		keyDownMaster = 'userSetting';
		$("#settingPanel").slideDown();
		alert('blue key setted');
		break;
	case 21:
		keyDownMaster = 'history';
		$("#history").slideDown();
		alert('yello key setted');
		break;
	}
	switch($('#usr_1').text()){
	case '메일함형 알림':
		break;
	case '아이콘형 알림':
		iconKeyDown(key);
		break;
	}
}

var iconIsMenuIn = 0;
var iconIndex = 0;

function iconKeyDown(key){
	if(iconIsMenuIn == 0){
		switch(key){
		case tvKey.KEY_RIGHT:
			$('#icon' + iconIndex).removeClass('box_shadow2');
			$('#icon' + iconIndex).addClass('non_box_shadow2');
			iconIndex += 1;
			if(iconIndex == 4)
				iconIndex = 1;
			$('#icon' + iconIndex).removeClass('non_box_shadow2');
			$('#icon' + iconIndex).addClass('box_shadow2');
			break;
		case tvKey.KEY_LEFT:
			$('#icon' + iconIndex).removeClass('box_shadow2');
			$('#icon' + iconIndex).addClass('non_box_shadow2');
			iconIndex -= 1;
			if(iconIndex == 0)
				iconIndex = 3;
			$('#icon' + iconIndex).removeClass('non_box_shadow2');
			$('#icon' + iconIndex).addClass('box_shadow2');
			break;
		case tvKey.KEY_ENTER:
			iconIsMenuIn = 1;
			showIconMenu();
			break;
		}
	}else{
		switch(key){
        case tvKey.KEY_RETURN:
        	iconIsMenuIn = 0;
        	hideIconMenu();
        	break;
		}
	}
}

function showIconMenu(){
	$("#iconInner" + iconIndex).slideDown();
}
function hideIconMenu(){
	$("#iconInner" + iconIndex).slideUp();
	icon.flushQ(iconIndex);
}

function userSettingKeyDown(key){
		var keyCode = key;
		switch(userSettingKeyMaster){
		case 'userSetting':
			switch(keyCode){
				case tvKey.KEY_DOWN:
					$('#usr_' + elem).removeClass('box_shadow');
					elem += 1;
					if(elem == 6)
						elem = 1;
					$('#usr_' + elem).addClass('box_shadow');
					break;
				case tvKey.KEY_UP:
					$('#usr_' + elem).removeClass('box_shadow');
					elem -= 1;
					if(elem == 0)
						elem = 5;
					$('#usr_' + elem).addClass('box_shadow');
					break;
				case tvKey.KEY_ENTER:
					$("#dropdown_" + elem).slideDown("slow");
					userSettingKeyMaster = 'inDropdown';
					dropdown = Number(elem);
					subMenu = 0;
					break;
		        case tvKey.KEY_RETURN:
					$("#settingPanel").slideUp();
					keyDownMaster = 'outter';
					break;
			}
			break;
		case 'inDropdown':
			switch(keyCode){
			case tvKey.KEY_DOWN:
				$('#dropdown_' + dropdown + '_' + subMenu).removeClass('box_shadow');
				subMenu += 1;
				if(subMenu == elementNum[elem] + 1)
					subMenu= 1;
				$('#dropdown_' + dropdown + '_' + subMenu).addClass('box_shadow');
				break;
			case tvKey.KEY_UP:
				$('#dropdown_' + dropdown + '_' + subMenu).removeClass('box_shadow');
				subMenu -= 1;
				if(subMenu == 0)
					subMenu= elementNum[elem];
				$('#dropdown_' + dropdown + '_' + subMenu).addClass('box_shadow');
				break;
			case tvKey.KEY_ENTER:
				//아무것도 선택 안함? break;
				if(subMenu == 0)
					break;
				$('#dropdown_' + dropdown + '_' + subMenu).removeClass('box_shadow');
				$('#usr_' + elem).text($('#dropdown_' + dropdown + '_' + subMenu).text());
				$("#dropdown_" + elem).slideUp();
				subMenu = 0;
				//state를 userSetting으로
				userSettingKeyMaster = 'userSetting';
				var res = '';
				//파일입출력...
				for(var i = 1; i <= 5; i++)
					res += $('#usr_' + i).text() + '|';
				initAlertType($('#usr_1').text());
				write(res);
				break;
			}
			break;
		}
}
function disableAll(){
	$("#icon1").hide();
	$("#icon2").hide();
	$("#icon3").hide();
	$("#mail1").hide();
	$("#mail2").hide();
	$("#mail3").hide();
	$("#mail4").hide();
}

function initAlertType(t){
	disableAll();
	switch(t){
	case '자막형 알림':
		break;
	case '메일함형 알림':
		initMails();
		break;
	case '카드형 알림':
		break;
	case '아이콘형 알림':
		initIcons();
		break;
	}
}

function initIcons(){
	$("#icon1").show();
	$("#icon2").show();
	$("#icon3").show();
}

function initMails(){
	for(var i = 1; i <= mail.connectedUserCount; i++){
		$("#mail" + i).show();
	}
}

// 애플리케이션의 종료시점에 호출되는 이벤트 처리 함수
Main.onUnload = function()
{
	if(oIME){
		oIME.onClose();
	}
};
Main.enableKeys = function() {
	document.getElementById("anchor").focus();
};

Main.focus = function(){ 

};

var form_submit = function(){
	Main.login.elem.empty();
	Main.login.elem.text('WelCome ! '+Main.login.form.val() + '.');
	
	login_flag = true;	//로그인이 됐는지 안됐는지를 나타내는 논리값
	
	Main.category.anchor.focus();
	Main.login.elem.removeClass('focus');
	Main.category.elem.addClass('focus');
};

var Convergence = {
    api: window.webapis.customdevice || {},
    aDevice: [],
    init: function() {
        this.api.registerManagerCallback(Convergence.registerManager);
        this.api.getCustomDevices(Convergence.getCustomDevices);
    },
    registerManager: function(oManagerEvent) {
        var _this = Convergence;	        
        switch(oManagerEvent.eventType) {
            case _this.api.MGR_EVENT_DEV_CONNECT:
                _this.api.getCustomDevices(Convergence.getCustomDevices);
                break;
            case _this.api.MGR_EVENT_DEV_DISCONNECT: 
                _this.api.getCustomDevices(Convergence.getCustomDevices);
                break;
            default: 
                break;
        }
    },
    getCustomDevices: function(aDevice) {
        var _this = Convergence;
        _this.aDevice = aDevice;
        //접속시 이 부분이 실행된다.
        alert("device length : " + aDevice.length);
        for(var i = 0; i < aDevice.length; i++) {
            var sID = aDevice[i].getUniqueID();	 
            alert("sid : " + sID);
            aDevice[i].registerDeviceCallback(function(oDeviceInfo) {
                _this.registerDevice(sID, oDeviceInfo);
            });
        }
    },
    registerDevice: function(sID, oDeviceInfo) {
    	//Device to TV, mesg 교환시 실행되는 부분...
    	for(var key in oDeviceInfo.data){
    		alert(key + ' : ' + oDeviceInfo.data[key]);
    	}
    	var hi = jQuery.parseJSON(oDeviceInfo.data.message1);
		var dt = new Date();
		var time = dt.getHours() + ":" + dt.getMinutes() + ":" + dt.getSeconds();
    	History.mesgq.push(time + '|' + hi.opcode + '|' + hi.nickname + '|' + hi.title.substr(0, 10) + '|' + hi.mesg.substr(0, 10));
    	if(History.mesgq.length > 5)
    		$('#historyTableDiv').css("height", 77*(5 + 1));
    	else
    		$('#historyTableDiv').css("height", 77*(History.mesgq.length));
    	if(History.mesgq.length > 40)
    		History.mesgq.shift();
    	
		var su = '';
		for(var i = 0; i < History.mesgq.length; i++){
			var temp = History.mesgq[i].split('|');
			su += '<tr id="perScroll' + i + '">' + '<td id="perScroll_'+i+'1" style="width:15%;">' + temp[0] + '</td><td id="perScroll_'+i+'2" style="width: 15%;">'
				+ temp[1] + '</td><td id="perScroll_'+i+'3" style="width: 15%;">' + temp[2] + '</td><td id="perScroll_'+i+'4" style="width: 25%;">' + temp[3] + '</td><td id="perScroll_' + i
				+ '5" style="width: 30%;">' + temp[4] + '</td></tr>';
		}
		$('#historyTable').html(su);
		switch($('#usr_1').text()){
		case '자막형 알림':
			makeCaption(hi);
			break;
		case '메일함형 알림':
			mail.push(hi);
			break;
		case '카드형 알림':
			card.push_back(hi);
			break;
		case '아이콘형 알림':
			icon.push(hi);
			break;
		}
    },
    sendMessage: function(oDevice, sMessage) {
        return oDevice.sendMessage(sMessage);
    },
    broadcastMessage: function(sMessage) {
        return this.aDevice[0] && this.aDevice[0].broadcastMessage(sMessage);
    },
    uploadFile: function(sName) {
        //sName: 이미지 파일 이름
        var sUrl = 'http://127.0.0.1/ws/app/' + curWidget.id  + '/file/' + sName;
        return '<img src="' + sUrl + '"/>';
    }
};
Convergence.init();

var mail = new MailManager();

/* 클래스 선언 */
function MailManager() {
	/* public 변수 */
	this.connectedUserList = new Array();
	this.connectedUserCount = 0;
	this.mailBox = new Array(4);
	
	/* private 변수 */
	
	/* public 메서드 */
	this.push = function (obj) {
		if(this.connectedUserList[obj.nickname] == null){
			if(this.connectedUserCount == 4){
				return;
			}
			this.connectedUserCount += 1;
			this.connectedUserList[this.connectedUserCount] = obj.nickname;
			this.connectedUserList[obj.nickname] = this.connectedUserCount;
			initMails();
			//등록맨
		}
		alert('gogo' + this.mailBox[this.connectedUserList[obj.nickname]].length);
		if(this.mailBox[this.connectedUserList[obj.nickname]].length < 20){
			this.mailBox[this.connectedUserList[obj.nickname]].push(obj);
			$("#mail"+this.connectedUserList[obj.nickname]+"Circle").show().text(this.mailBox[this.connectedUserList[obj.nickname]].length);
			alert("#mail"+this.connectedUserList[obj.nickname]+"Circle");
		}
	};
	
	this.showMessages = function(idx){
		
	};
	/* private 메서드 */
}


var icon = new IconManager();

/* 클래스 선언 */
function IconManager() {
	/* public 변수 */

	this.notiNum = 0;
	/* private 변수 */
	var callNum = 0;
	var smsNum = 0;
	
	/* public 메서드 */
	this.push = function (obj) {
		switch(obj.opcode){
		case '전화':
			break;
		case '문자':
			break;
		default:
			this.pushNoti(obj);
			break;
		}
	};

	this.flushQ = function(n){
		$('#iconInner' + n).text('');
		this.notiNum = 0;
		$("#icon" + n + "Circle").hide();
	};
	this.pushNoti = function(obj) {
		//그려줘야함...
		this.notiNum += 1;
		$('#iconInner3').append("<div>" + obj.nickname + "</div>");
		$("#icon3Circle").show().text(this.notiNum);
	};
}




var card = new CardManager();

/* 클래스 선언 */
function CardManager() {
	/* public 변수 */

	/* private 변수 */
	var readyQueue = new Array();
	var screenShowCardNum = 0;
	var cardIndex = 0;
	var cardVarDelete = 0;
	var screenShowCardArray = new Array();
	
	/* public 메서드 */
	this.push_back = function (obj) {
		if(screenShowCardNum == 5)
			readyQueue.push(obj);
		else
			drawAppend(obj);
	};
	
	/* private 메서드 */
	var drawAppend = function(obj) {
		//그려줘야함...
		$('#card').append("<div id=\"instantCardAlert" + cardIndex + "\" style=\"position: absolute;\"></div>");
		$("#instantCardAlert" + cardIndex).text(obj.nickname);
		$("#instantCardAlert" + cardIndex).css('background-color', 'red');
		$("#instantCardAlert" + cardIndex).css('width', 200);
		$("#instantCardAlert" + cardIndex).css("left", 900);
		$("#instantCardAlert" + cardIndex).css("top", 70 * screenShowCardNum);
		$("#instantCardAlert" + cardIndex).css("height", 50);
		setTimeout(function(){
			dequeue();
		}, 3000);
		screenShowCardArray.push($('#instantCardAlert' + cardIndex));
		screenShowCardNum += 1;
		cardIndex += 1;
	};
	
	var dequeue = function(){
		$("#instantCardAlert" + cardVarDelete).animate({
			opacity: 0
		}, 200, function(){
			$("#instantCardAlert" + cardVarDelete).remove();
			cardVarDelete += 1;
		});
		screenShowCardArray.shift();
		for(var i = 0; i < screenShowCardArray.length; i++){
			screenShowCardArray[i].animate({ "top": "-=70px"}, 200, "linear");
		}
		screenShowCardNum -= 1;
		if(readyQueue.length != 0){
			drawAppend(readyQueue[0]);
			readyQueue.shift();
		}
	};
}

var captionVar = 0;
var captionVarDelete = 1;

function makeCaption(obj){
	if($('#instantCaptionAlert' + captionVar).width() == null || $('#instantCaptionAlert' + captionVar).offset().left + $("#instantCaptionAlert" + captionVar).width() < 1280){
			//새로만들기
		captionVar += 1;
		$("#caption").append("<div id=\"instantCaptionAlert" + captionVar + "\" style=\"position: absolute;\"></div>");
		$("#instantCaptionAlert" + captionVar).text(obj.nickname).css('background-color', 'red').css('width', 200).css("left", 1280)
			.css("top", 640).css("height", 50).animate({ "left": "-=1500px"}, 12000, "linear", function(){
				$("#instantCaptionAlert" + captionVarDelete).remove();
				captionVarDelete++;
			} );
	}else{
		//$("#instantCaptionAlert" + captionVar).text($("#instantCaptionAlert" + captionVar).text() + obj.nickname);
		captionVar += 1;
		$("#caption").append("<div id=\"instantCaptionAlert" + captionVar + "\" style=\"position: absolute;\"></div>");
		var lft = $("#instantCaptionAlert" + (captionVar-1)).offset().left + $("#instantCaptionAlert" + (captionVar-1)).width();
		$("#instantCaptionAlert" + captionVar).text(obj.nickname).css('background-color', 'red').css('width', 200).css("left", lft - 10)
			.css("top", 640).css("height", 50).animate({ "left": "-=" + (lft + 220) + "px"}, (lft + 220)*8, "linear", function(){
				$("#instantCaptionAlert" + captionVarDelete).remove();
				captionVarDelete++;
			});
	}
}

function requestToServer(authKey, regID, opcode, dummy){
	var formData = "auth=" + authKey + '&opcode=' + opcode + '&smsNo=' + dummy + '&regID=' + regID;  //Name value Pair
	$.ajax({
	    url : "http://210.118.74.55:18080/sendMessage",
	    type: "POST",
	    data : formData,
	    success: function(data, textStatus, jqXHR) {
	        //
	    },
	    error: function (jqXHR, textStatus, errorThrown) {
	 
	    }
	});
}

//요청한 이벤트를 핸들링하는 함수
var handleMobileEvent = function(event){
	switch(event) {
		case 'msg_show' :
			$('#convergence_help').show();
			break;
		case 'msg_hide' :
			$('#convergence_help').hide();
			break;
	}
};