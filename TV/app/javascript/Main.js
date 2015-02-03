var elem = 0;
var tvKey = new Common.API.TVKeyValue();

var widgetAPI = new Common.API.Widget();

var Main = {
	content : {
		elem : jQuery('#noti'),
	},
};

//IME 오브젝트 선언
var oIME = null;
var keyDownMaster = 'userSetting';
var dropdown = 0;
var subMenu = 0;
var elementNum = Array(0, 4, 3, 3, 4, 2);
Main.onLoad = function()
{
	//this.focus();	// 초기 포커스 설정
	widgetAPI.sendReadyEvent();
	//Main.loadContent();	//현재 카테고리에 해당하는 View 콘텐츠 로드
	Main.enableKeys();
	Main.keyInit();
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
		alert('write gogo...');
	}else{
		alert('write fail...');
	}
	jsFileObj.writeAll(val);
	fileSystemObj.closeFile(jsFileObj);
};

Main.keyInit = function(){
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
}

Main.keyDown = function(){
		var keyCode = event.keyCode;
		alert('catch');
		switch(keyDownMaster){
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
					keyDownMaster = 'inDropdown';
					dropdown = Number(elem);
					subMenu = 0;
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
				keyDownMaster = 'userSetting';
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
    	alert(hi);
    	alert('opcode : ' + hi.opcode);
    	alert('sender : ' + hi.sender);
    	alert('pno : ' + hi.pno);
    	alert('nickname : ' + hi.nickname);
    	alert('color : ' + hi.color);
    	alert('mesg : ' + hi.mesg);
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

