package com.example.pt3;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.AbstractHttpMessage;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.util.Log;

public class ConvergenceUtil {
	
	private final String TAG = "ConvergenceUtil";
	private DefaultHttpClient httpClient;

		                   		// [반드시 입력해주세요] 실제TV는 80, 에뮬레이터는 8008
	private String appId 		= "qq";         							// [반드시 입력해주세요] TV 애플리케이션의 ID 값
	private String macAddress 	=			"BC:72:B1:D9:27:C5";			         	// [선택사항] 모바일 단말의 MAC 주소
	private String appURL="";
	public  final String RECEIVE = "com.example.pt3";
	
	/**
	 * 생성자
	 * @param context
	 */
	public ConvergenceUtil() {
		Log.i(TAG, "generator()");
	}
	
	public void setIpAddress(String appURL){
		this.appURL=appURL;
		Log.d("appURL",appURL);
	}
	
    public static DefaultHttpClient getThreadSafeClient()  {

        DefaultHttpClient client = new DefaultHttpClient();
        ClientConnectionManager mgr = client.getConnectionManager();
        HttpParams params = client.getParams();
        client = new DefaultHttpClient(new ThreadSafeClientConnManager(params,
                mgr.getSchemeRegistry()), params);
        return client;
    }
	
	/**
	 * TV와 연결 함수 
	 * @return
	 */
	public int connect() {
		Log.i(TAG, "connect()");
		// URL 설정
		String urlStr = appURL + appId + "/connect";
		Log.d("appURL",appURL);

		URL url = null;
		int statusCode = -1;
        try {
			url = new URL(urlStr);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		// HttpClient 객체 생성 및 프로토콜 설정
		httpClient =  getThreadSafeClient();
		ProtocolVersion protocol = new ProtocolVersion("HTTP", 1, 1);
		httpClient.getParams().setParameter(
				CoreProtocolPNames.PROTOCOL_VERSION, protocol);

		// 연결요청에 필요한 헤더 설정
		AbstractHttpMessage message = new HttpPost(urlStr);
		message.setParams(new BasicHttpParams().setParameter(urlStr, url));
		message.addHeader("User-Agent", "Android-Phone");
		message.addHeader("SLDeviceID", macAddress);
		message.addHeader("VendorID", 	"VendorTV");
		message.addHeader("ProductID", 	"SMARTdev");
		message.addHeader("DeviceName", "SamsungGalaxyS4");
		Log.i(TAG, "aaa");
		
		try {
			// TV로 연결요청 (HttpResponse 객체 반환)
			HttpResponse response = httpClient.execute((HttpUriRequest) message);
	
            Log.i(TAG, "bbb");
                // 응답코드 반환
                statusCode = response.getStatusLine().getStatusCode();
                String result = null;

                if (statusCode == 200) {
                    result = "* * * 연결성공 * * * ";
                }else {
                    result = "[연결오류] 다시 시도해 주세요.";
                }

                Log.i(TAG, "connect() : statusCode = "+statusCode+"/result = "+result);

                if(result != null){
                	Log.d("recv",result);
			}
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
		return statusCode;
	}

	/**
	 * TV와의 연결 끊는 함수 
	 */
	public void disconnect() {
		Log.i(TAG, "disconnect()");
		// URL 설정
		String urlStr = appURL +appId + "/disconnect";
		URL url = null;
		try {
			url = new URL(urlStr);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		// HttpClient 객체 생성 및 프로토콜 설정
		httpClient = new DefaultHttpClient();
		ProtocolVersion protocol = new ProtocolVersion("HTTP", 1, 1);
		httpClient.getParams().setParameter(
				CoreProtocolPNames.PROTOCOL_VERSION, protocol);

		// 연결해제에 필요한 헤더 설정
		AbstractHttpMessage message = new HttpPost(urlStr);
		message.setParams(new BasicHttpParams().setParameter(urlStr, url));
		message.addHeader("SLDeviceID", macAddress);

		try {
			// TV 연결해제 (Response 객체 반환)
			HttpResponse response = httpClient
					.execute((HttpUriRequest) message);

			// 응답코드 반환
			int statusCode = response.getStatusLine().getStatusCode();
			String result = null;
			
			if (statusCode == 200) {
				result = "* * * 연결해제 * * * ";
			}else {
				result = "[해제오류] 다시 시도해 주세요.";
			}
			
			if(result != null){
            	Log.d("recv",result);
            	}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * TV로 메시지 송신 
	 * @param type	TV와 규약한 타입 값 
	 * @param msg	TV와 규약한 메시지 값 
	 */
	public void sendMessage(JSONObject msg) {
		Log.i(TAG, "sendMessage()"+msg);
		// URL 설정
            String urlStr = appURL
                    + appId + "/queue";
            URL url = null;
            try {
                url = new URL(urlStr);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		
		
		// JSON객체를 String 변환
		String body = msg.toString();
		
		// HttpClient 객체 생성 및 프로토콜 설정
		httpClient = new DefaultHttpClient();
		ProtocolVersion protocol = new ProtocolVersion("HTTP", 1, 1);
		httpClient.getParams().setParameter(
				CoreProtocolPNames.PROTOCOL_VERSION, protocol);

		// 메시지 전송에 필요한 헤더 설정
		AbstractHttpMessage message = new HttpPost(urlStr);
		message.setParams(new BasicHttpParams().setParameter(urlStr, url));
		message.addHeader("User-Agent", "Android-Phone");
		message.addHeader("SLDeviceID", macAddress);

		// JSON객체에 저장된 type과 message를 byte 변환
		AbstractHttpEntity entity=null;
		try {
			entity = new ByteArrayEntity(body.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// Content-Type 헤더 설정
		entity.setContentType("application/json");

		// byte화된 type과 message 정보를 entity 객체에 저장
		HttpEntityEnclosingRequest entityMessage = (HttpEntityEnclosingRequest) message;
		entityMessage.setEntity(entity);
		try {
			// TV로 메시지 전송 (Response 객체 반환)
			HttpResponse response = httpClient.execute((HttpUriRequest) entityMessage);

			// 응답코드 반환
			int statusCode = response.getStatusLine().getStatusCode();
			String result = null;
			
			if (statusCode != 200) {
				result = "[전송오류] 다시 시도해 주세요.";
			}
			
			if(result != null){
            	Log.d("recv",result+"errCode: "+statusCode);	
            	}			
			else Log.d("recv","***수신 완료***");
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * TV로 부터 메시지 수신 
	 */
	public void receiveMessage() {
		HttpResponse response = null;
	

		// URL 설정
		String urlStr = appURL
				+ appId + "/queue/device/" + macAddress;
		URL url = null;
		try {
			url = new URL(urlStr);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		// HttpClient 객체 생성 및 프로토콜 설정
		httpClient = new DefaultHttpClient();
		ProtocolVersion protocol = new ProtocolVersion("HTTP", 1, 1);
		httpClient.getParams().setParameter(
				CoreProtocolPNames.PROTOCOL_VERSION, protocol);

		// 메시지 수신을 위한 요청 헤더 설정
		AbstractHttpMessage message = new HttpGet(urlStr); // GET방식
		message.setParams(new BasicHttpParams().setParameter(urlStr, url));
		message.addHeader("SLDeviceID", macAddress);
		try {
			// 메시지 수신을 위한 요청 (Response 객체 반환)
			response = httpClient.execute((HttpUriRequest) message);
			int statusCode = response.getStatusLine().getStatusCode();

			// 응답코드가 200 일 경우 (성공했을 경우)
			if (statusCode == 200) {
				// Response 객체에 포함된 메시지를 반환하기 위한 과정
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					// InputStream 형태의 메시지를 String 값으로 변환
					InputStream is = entity.getContent();
					StringBuffer out = new StringBuffer();
					byte[] b = new byte[4096];
					for (int n; (n = is.read(b)) != -1;) {
						out.append(new String(b, 0, n));
					}
					
					String responseBody = out.toString();

					if (responseBody != null) {

	                	Log.d("recvtest",responseBody.toString());
					}
					
					//String 으로 변환된 메시지의 type, msg값을 JSON객체를 이용해 각각 반환
//					JSONObject jsonObj;
//					try {
//						jsonObj = new JSONObject(responseBody);
//						if (jsonObj != null) {
//							type = jsonObj.getString("type");
//							msg = jsonObj.getString("msg");
//						}
//					} catch (JSONException e) {
//						e.printStackTrace();
//					}
				}

				
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
