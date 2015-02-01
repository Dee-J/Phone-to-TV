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

import android.util.Log;

public class ConvergenceUtil {
	
	private final String TAG = "ConvergenceUtil";
	private DefaultHttpClient httpClient;

	private String ipAddress 	= "192.168.0.59";                       	// [�ݵ�� �Է����ּ���] TV�� IP �ּ�
	private String portNumber 	= "8080" ;			                   		// [�ݵ�� �Է����ּ���] ����TV�� 80, ���ķ����ʹ� 8008
	private String appId 		= "js";         							// [�ݵ�� �Է����ּ���] TV ���ø����̼��� ID ��
	private String macAddress 	=""; 
//			"BC:72:B1:D9:27:C5";			         	// [���û���] ����� �ܸ��� MAC �ּ�
	
	public  final String RECEIVE = "com.example.pt3";
	
	/**
	 * ������
	 * @param context
	 */
	public ConvergenceUtil() {
		Log.i(TAG, "generator()");
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
	 * TV�� ���� �Լ� 
	 * @return
	 */
	public int connect() {
		Log.i(TAG, "connect()");
		// URL ����
		String urlStr = "http://" + ipAddress + ":" + portNumber + "/ws/app/" + appId + "/connect";
		URL url = null;
		int statusCode = -1;
        try {
			url = new URL(urlStr);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		// HttpClient ��ü ���� �� �������� ����
		httpClient =  getThreadSafeClient();
		ProtocolVersion protocol = new ProtocolVersion("HTTP", 1, 1);
		httpClient.getParams().setParameter(
				CoreProtocolPNames.PROTOCOL_VERSION, protocol);

		// �����û�� �ʿ��� ��� ����
		AbstractHttpMessage message = new HttpPost(urlStr);
		message.setParams(new BasicHttpParams().setParameter(urlStr, url));
		message.addHeader("User-Agent", "Android-Phone");
		message.addHeader("SLDeviceID", macAddress);
		message.addHeader("VendorID", 	"VendorTV");
		message.addHeader("ProductID", 	"SMARTdev");
		message.addHeader("DeviceName", "SamsungGalaxyS4");
		Log.i(TAG, "aaa");
		
		try {
			// TV�� �����û (HttpResponse ��ü ��ȯ)
			HttpResponse response = httpClient.execute((HttpUriRequest) message);
			Log.i(TAG, "xx1x");

            Log.i(TAG, "bbb");
                // �����ڵ� ��ȯ
                statusCode = response.getStatusLine().getStatusCode();
                String result = null;

                if (statusCode == 200) {
                    result = "* * * ���Ἲ�� * * * ";
                }else {
                    result = "[�������] �ٽ� �õ��� �ּ���.";
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
	 * TV���� ���� ���� �Լ� 
	 */
	public void disconnect() {
		Log.i(TAG, "disconnect()");
		// URL ����
		String urlStr = "http://" + ipAddress + ":" + portNumber + "/ws/app/" +appId + "/disconnect";
		URL url = null;
		try {
			url = new URL(urlStr);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		// HttpClient ��ü ���� �� �������� ����
		httpClient = new DefaultHttpClient();
		ProtocolVersion protocol = new ProtocolVersion("HTTP", 1, 1);
		httpClient.getParams().setParameter(
				CoreProtocolPNames.PROTOCOL_VERSION, protocol);

		// ���������� �ʿ��� ��� ����
		AbstractHttpMessage message = new HttpPost(urlStr);
		message.setParams(new BasicHttpParams().setParameter(urlStr, url));
		message.addHeader("SLDeviceID", macAddress);

		try {
			// TV �������� (Response ��ü ��ȯ)
			HttpResponse response = httpClient
					.execute((HttpUriRequest) message);

			// �����ڵ� ��ȯ
			int statusCode = response.getStatusLine().getStatusCode();
			String result = null;
			
			if (statusCode == 200) {
				result = "* * * �������� * * * ";
			}else {
				result = "[��������] �ٽ� �õ��� �ּ���.";
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
	 * TV�� �޽��� �۽� 
	 * @param type	TV�� �Ծ��� Ÿ�� �� 
	 * @param msg	TV�� �Ծ��� �޽��� �� 
	 */
	public void sendMessage(JSONObject msg) {
		Log.i(TAG, "sendMessage()"+msg);
		// URL ����
            String urlStr = "http://" + ipAddress + ":" + portNumber + "/ws/app/"
                    + appId + "/queue";
            URL url = null;
            try {
                url = new URL(urlStr);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		
		
		// JSON��ü�� String ��ȯ
		String body = msg.toString();
		
		// HttpClient ��ü ���� �� �������� ����
		httpClient = new DefaultHttpClient();
		ProtocolVersion protocol = new ProtocolVersion("HTTP", 1, 1);
		httpClient.getParams().setParameter(
				CoreProtocolPNames.PROTOCOL_VERSION, protocol);

		// �޽��� ���ۿ� �ʿ��� ��� ����
		AbstractHttpMessage message = new HttpPost(urlStr);
		message.setParams(new BasicHttpParams().setParameter(urlStr, url));
		message.addHeader("User-Agent", "Android-Phone");
		message.addHeader("SLDeviceID", macAddress);

		// JSON��ü�� ����� type�� message�� byte ��ȯ
		AbstractHttpEntity entity=null;
		try {
			entity = new ByteArrayEntity(body.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// Content-Type ��� ����
		entity.setContentType("application/json");

		// byteȭ�� type�� message ������ entity ��ü�� ����
		HttpEntityEnclosingRequest entityMessage = (HttpEntityEnclosingRequest) message;
		entityMessage.setEntity(entity);
		try {
			// TV�� �޽��� ���� (Response ��ü ��ȯ)
			HttpResponse response = httpClient.execute((HttpUriRequest) entityMessage);

			// �����ڵ� ��ȯ
			int statusCode = response.getStatusLine().getStatusCode();
			String result = null;
			
			if (statusCode != 200) {
				result = "[���ۿ���] �ٽ� �õ��� �ּ���.";
			}
			
			if(result != null){
            	Log.d("recv",result+"errCode: "+statusCode);	
            	}			
			else Log.d("recv","***���� �Ϸ�***");
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * TV�� ���� �޽��� ���� 
	 */
	public void receiveMessage() {
		HttpResponse response = null;
		String type = "";
		String msg = "";


		// URL ����
		String urlStr = "http://" + ipAddress + ":" + portNumber + "/ws/app/"
				+ appId + "/queue/device/" + macAddress;
		URL url = null;
		try {
			url = new URL(urlStr);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		// HttpClient ��ü ���� �� �������� ����
		httpClient = new DefaultHttpClient();
		ProtocolVersion protocol = new ProtocolVersion("HTTP", 1, 1);
		httpClient.getParams().setParameter(
				CoreProtocolPNames.PROTOCOL_VERSION, protocol);

		// �޽��� ������ ���� ��û ��� ����
		AbstractHttpMessage message = new HttpGet(urlStr); // GET���
		message.setParams(new BasicHttpParams().setParameter(urlStr, url));
		message.addHeader("SLDeviceID", macAddress);
		try {
			// �޽��� ������ ���� ��û (Response ��ü ��ȯ)
			response = httpClient.execute((HttpUriRequest) message);
			int statusCode = response.getStatusLine().getStatusCode();

			// �����ڵ尡 200 �� ��� (�������� ���)
			if (statusCode == 200) {
				// Response ��ü�� ���Ե� �޽����� ��ȯ�ϱ� ���� ����
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					// InputStream ������ �޽����� String ������ ��ȯ
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
					
					//String ���� ��ȯ�� �޽����� type, msg���� JSON��ü�� �̿��� ���� ��ȯ
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
