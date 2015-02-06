package com.example.pt3;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpParams;
import org.teleal.cling.android.AndroidUpnpService;
import org.teleal.cling.model.meta.Device;
import org.teleal.cling.model.meta.LocalDevice;
import org.teleal.cling.model.meta.RemoteDevice;
import org.teleal.cling.model.meta.RemoteDeviceIdentity;
import org.teleal.cling.registry.DefaultRegistryListener;
import org.teleal.cling.registry.Registry;

import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class BrowserActivity extends ListActivity{

	private ArrayAdapter<DeviceDisplay> deviceListAdapter;
	private BrowseRegistryListener registryListener = new BrowseRegistryListener();
	private AndroidUpnpService upnpService;
	private ServiceConnection serviceConnection = new ServiceConnection() {

		public void onServiceConnected(ComponentName className, IBinder service) {
			upnpService = (AndroidUpnpService) service;

			// Refresh the list with all known devices
			deviceListAdapter.clear();
			for (Device device : upnpService.getRegistry().getDevices()) {
				registryListener.deviceAdded(device);
			}

			// Getting ready for future device advertisements
			upnpService.getRegistry().addListener(registryListener);
		}

		public void onServiceDisconnected(ComponentName className) {
			upnpService = null;
		}
	};
	private ListView listview;

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);		
		deviceListAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
		listview = getListView();
		switchToDeviceList();        

		getApplicationContext().bindService(
				new Intent(this, BrowserUpnpService.class),
				serviceConnection,
				Context.BIND_AUTO_CREATE
				);

	}

	public void switchToDeviceList() {
		setListAdapter(deviceListAdapter);

		/* Executes when the user (long) clicks on a device:
		 */
		listview.setOnItemClickListener(
				new AdapterView.OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						DeviceDisplay clickedDisplay = deviceListAdapter.getItem(position);
						URL targeturl= ((RemoteDeviceIdentity)clickedDisplay.getDevice().getIdentity()).getDescriptorURL();
						String targeturlstr="http://"+targeturl.getHost()+":"+targeturl.getPort()+targeturl.getPath();

						//                    	Toast.makeText(getApplicationContext(), targeturlstr, Toast.LENGTH_SHORT).show();

						httpGetConnection(targeturlstr);
					}
				}
				);

	}
	public void onDestroy(){
		super.onDestroy();
		finish();
		if (upnpService != null) {
			upnpService.getRegistry().removeListener(registryListener);
			getApplicationContext().unbindService(serviceConnection);

		}

	}

	protected void searchNetwork() {
		if (upnpService == null) return;
		Toast.makeText(this, R.string.searching_lan, Toast.LENGTH_SHORT).show();
		upnpService.getRegistry().removeAllRemoteDevices();
		upnpService.getControlPoint().search();
	}


	protected class BrowseRegistryListener extends DefaultRegistryListener {

		/* Discovery performance optimization for very slow Android devices! */
		@Override
		public void remoteDeviceDiscoveryStarted(Registry registry, RemoteDevice device) {
			deviceAdded(device);
		}

		@Override
		public void remoteDeviceDiscoveryFailed(Registry registry, final RemoteDevice device, final Exception ex) {
			runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(
							BrowserActivity.this,
							"Discovery failed of '" + device.getDisplayString() + "': " +
									(ex != null ? ex.toString() : "Couldn't retrieve device/service descriptors"),
									Toast.LENGTH_LONG
							).show();
				}
			});
			deviceRemoved(device);
		}
		/* End of optimization, you can remove the whole block if your Android handset is fast (>= 600 Mhz) */

		@Override
		public void remoteDeviceAdded(Registry registry, RemoteDevice device) {
			deviceAdded(device);
		}

		@Override
		public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {
			deviceRemoved(device);
		}

		@Override
		public void localDeviceAdded(Registry registry, LocalDevice device) {
			deviceAdded(device);
		}

		@Override
		public void localDeviceRemoved(Registry registry, LocalDevice device) {
			deviceRemoved(device);
		}

		public void deviceAdded(final Device device) {

			runOnUiThread(new Runnable() {
				public void run() {
					DeviceDisplay d = new DeviceDisplay(device);

					int position = deviceListAdapter.getPosition(d);
					if (position >= 0) {
						// Device already in the list, re-set new value at same position
						deviceListAdapter.remove(d);
						deviceListAdapter.insert(d, position);
					} else {
						deviceListAdapter.add(d);
					}

					// Sort it?
					// listAdapter.sort(DISPLAY_COMPARATOR);
					// listAdapter.notifyDataSetChanged();
				}
			});
		}

		public void deviceRemoved(final Device device) {
			runOnUiThread(new Runnable() {
				public void run() {
					deviceListAdapter.remove(new DeviceDisplay(device));
				}
			});
		}
	}
	protected class DeviceDisplay {

		Device device;

		public DeviceDisplay(Device device) {
			this.device = device;
		}

		public Device getDevice() {
			return device;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			DeviceDisplay that = (DeviceDisplay) o;
			return device.equals(that.device);
		}

		@Override
		public int hashCode() {
			return device.hashCode();
		}

		@Override
		public String toString() {
			// Display a little star while the device is being loaded (see u optimization earlier)

			URL targeturl= ((RemoteDeviceIdentity)device.getIdentity()).getDescriptorURL();
			return device.isFullyHydrated() ? device.getDisplayString()

					: device.getDisplayString() + " *";
		}
	}
	public static DefaultHttpClient getThreadSafeClient()  {

		DefaultHttpClient client = new DefaultHttpClient();
		ClientConnectionManager mgr = client.getConnectionManager();
		HttpParams params = client.getParams();
		client = new DefaultHttpClient(new ThreadSafeClientConnManager(params,
				mgr.getSchemeRegistry()), params);
		return client;
	}

	static final Comparator<DeviceDisplay> DISPLAY_COMPARATOR =
			new Comparator<DeviceDisplay>() {
		public int compare(DeviceDisplay a, DeviceDisplay b) {
			return a.toString().compareTo(b.toString());
		}
	};


	public void httpGetConnection(final String target){

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				DefaultHttpClient httpclient = getThreadSafeClient();
				HttpGet httpget= new HttpGet(target);

				try {
					final HttpResponse responsebody =httpclient.execute(httpget);
					new Thread(new Runnable(){
						public void run(){
							String appurl=null;
							for(Header h: responsebody.getAllHeaders())
								if(h.getName().equals("Application-URL")){appurl=h.getValue();break;}

							SharedPreferences prefs = getSharedPreferences("PrefName", MODE_PRIVATE);
							SharedPreferences.Editor editor = prefs.edit();
							editor.putString(getResources().getString(R.string.appURL), appurl);
							editor.commit();
							startService(new Intent(getApplicationContext(),NLService.class));
						}
					}).start();
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


			}
		}).start();


	}       

}