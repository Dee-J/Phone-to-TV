var widgetAPI = new Common.API.Widget();
var tvKey = new Common.API.TVKeyValue();

var Main = {
	content : {
		elem : jQuery('#noti'),
	},
};

//IME 오브젝트 선언
var oIME = null;

Main.onLoad = function()
{
	//this.focus();	// 초기 포커스 설정
	widgetAPI.sendReadyEvent();
	//Main.loadContent();	//현재 카테고리에 해당하는 View 콘텐츠 로드
};

//애플리케이션의 종료시점에 호출되는 이벤트 처리 함수
Main.onUnload = function()
{
	if(oIME){
		oIME.onClose();
	}
};
Main.enableKeys = function()	
{
	
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
        for(var i = 0; i < aDevice.length; i++) {
            var sID = aDevice[i].getUniqueID();	            
            aDevice[i].registerDeviceCallback(function(oDeviceInfo) {
                _this.registerDevice(sID, oDeviceInfo);
            });
        }
    },
    registerDevice: function(sID, oDeviceInfo) {
    	//Device to TV, mesg 교환시 실행되는 부분...
    	var hi = jQuery.parseJSON(oDeviceInfo.data.message1);

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

