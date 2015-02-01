function getAbsPath(linkString) {

	var sPath = null;
	var sLocation = window.location.href;
	var sRoot = sLocation.substring(0, sLocation.lastIndexOf('/') + 1);

	sLocation = unescape(sLocation);
	sRoot = unescape(sRoot);

	if (sRoot.indexOf('file://localhost/') != -1 && (sLocation.indexOf('C:') != -1 || sLocation.indexOf('D:') != -1)) {
		sPath = sRoot.split('file://localhost/')[1].replace(/\\/g, '/');
	} else if (sRoot.indexOf('file://localhost/') != -1) {
		sPath = '/' + sRoot.split('file://localhost/')[1];
	} else if (sRoot.indexOf('file://C/') != -1 || sRoot.indexOf('file://c/') != -1) {
		sPath = 'C://' + (sRoot.indexOf('file://C/') != -1 ? sRoot.split('file://C/')[1] : sRoot.split('file://c/')[1]);
	} else if (sRoot.indexOf('file://D/') != -1 || sRoot.indexOf('file://d/') != -1) {
		sPath = 'D://' + (sRoot.indexOf('file://D/') != -1 ? sRoot.split('file://D/')[1] : sRoot.split('file://d/')[1]);
	} else {
		sPath = sRoot;
	}
	
	return sPath+linkString;
}