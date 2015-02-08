var elem = 0;
var tvKey = new Common.API.TVKeyValue();
var widgetAPI = new Common.API.Widget();

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
};

History.Init = function(){
	this.mesgq = new Array();	
};


var Usersetting = {
		
};

var fileSystemObj = new FileSystem();
var path;
var load = function (){
	if(fileSystemObj.isValidCommonPath(curWidget.id) != 1){
		fileSystemObj.createCommonDir(curWidget.id);
	}
	path = curWidget.id + "/userSetting.dat"
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
	//$('#usr_5').text(res[4]);
	$('#usr_5').text(curWidget.id);
	$('#settingPanel').hide();
};

var keyDownMaster = 'outter';
var tmp = 0;

Main.keyDown = function(){
	var keyCode = event.keyCode;
	alert(keyCode + 'key 입력댐');
	var pluginObjectNNAVI = document.getElementById('pluginObjectNNAVI');
	var DUID;
	DUID = pluginObjectNNAVI.GetHWAddr();
	$('#historyT').html(DUID);
	alert("디바이스 UID? : " + DUID);
	$('#key').html(keyCode);
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
	switch(keyCode){
	case tvKey.KEY_RETURN:
		event.preventDefault();
		//widgetAPI.blockNavigation(event);
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
    	if(tmp == 0)
    		tmp = History.mesgq.length;
		$('#perScroll_' + tmp + '1').removeClass('box_shadow');
		$('#perScroll_' + tmp + '2').removeClass('box_shadow');
		$('#perScroll_' + tmp + '3').removeClass('box_shadow');
		$('#perScroll_' + tmp + '4').removeClass('box_shadow');
		$('#perScroll_' + tmp + '5').removeClass('box_shadow');
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
	alert(key);
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
				write(res);
				break;
			}
			break;
		}
}

// 애플리케이션의 종료시점에 호출되는 이벤트 처리 함수
Main.onUnload = function()
{
	if(oIME){
		oIME.onClose();
	}
};
Main.enableKeys = function()	
{
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
    	History.mesgq.push(time + '|' + hi.opcode + '|' + hi.nickname + '|' + hi.title + '|' + hi.mesg.substr(0, 10));
    	if(History.mesgq.length > 5)
    		$('#historyTableDiv').css("height", 77*(5 + 1));
    	else
    		$('#historyTableDiv').css("height", 77*(History.mesgq.length));
    	if(History.mesgq.length > 40)
    		History.mesgq.shift();
    	
		var su = '';
//		if(History.mesgq.length == 6)
//			History.mesgq.shift();
		//su += '<table id="historyTable" border="1"><tr><th colspan="3">알림 기록</th></tr><tr><td>수신 시간</td><td>알림 종류</td><td>사용자명</td><td>제목</td><td>내용</td></tr>';
		for(var i = 0; i < History.mesgq.length; i++){
			var temp = History.mesgq[i].split('|');
			su += '<tr id="perScroll' + i + '">' + '<td id="perScroll_'+i+'1" style="width:15%;">' + temp[0] + '</td><td id="perScroll_'+i+'2" style="width: 15%;">' + temp[1] + '</td><td id="perScroll_'+i+'3" style="width: 15%;">' + temp[2] + '</td><td id="perScroll_'+i+'4" style="width: 25%;">' + temp[3] + '</td><td id="perScroll_'+i+'5" style="width: 30%;">' + temp[4] + '</td></tr>';
		}
		//su += '</table>';
		$('#historyTable').html(su);
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

