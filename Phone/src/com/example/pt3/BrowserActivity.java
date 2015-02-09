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

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class BrowserActivity extends Fragment  {

	private ListView listview;

	private ArrayAdapter<DeviceDisplay> deviceListAdapter;
	private BrowseRegistryListener registryListener = new BrowseRegistryListener();
	private AndroidUpnpService upnpService;
	private ConvergenceUtil mConvergenceUtil ;
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

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		deviceListAdapter = new ArrayAdapter<DeviceDisplay>(getActivity(),R.layout.textstyle);

		getActivity().bindService(
				new Intent(getActivity(), BrowserUpnpService.class), serviceConnection,
				Context.BIND_AUTO_CREATE);
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if(listview!=null){
			ViewGroup parent = (ViewGroup)listview.getParent();
			if(parent != null)
				parent.removeView(listview);
		}
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	
		View view=inflater.inflate(R.layout.activity_browser, container, false);
	
		listview =(ListView)view.findViewById(R.id.list);
			
	
		switchToDeviceList();

	
		
		return view;

	}

	  @Override
	    public void onAttach(Activity activity) {
	        super.onAttach(activity);
	 	    }
	  
	  
	public void switchToDeviceList() {
		listview.setAdapter(deviceListAdapter);

		/*
		 * Executes when the user (long) clicks on a device:
		 */
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				DeviceDisplay clickedDisplay = deviceListAdapter
						.getItem(position);
				URL targeturl = ((RemoteDeviceIdentity) clickedDisplay
						.getDevice().getIdentity()).getDescriptorURL();
				String targeturlstr = "http://" + targeturl.getHost() + ":"
						+ targeturl.getPort() + targeturl.getPath();

				Toast.makeText(getActivity(), targeturlstr,
						Toast.LENGTH_SHORT).show();

				httpGetConnection(targeturlstr);			
			}
		});

	}

	public void onDestroy() {
		super.onDestroy();



	}

	protected class BrowseRegistryListener extends DefaultRegistryListener {

		/* Discovery performance optimization for very slow Android devices! */
		@Override
		public void remoteDeviceDiscoveryStarted(Registry registry,
				RemoteDevice device) {
			deviceAdded(device);
		}

		@Override
		public void remoteDeviceDiscoveryFailed(Registry registry,
				final RemoteDevice device, final Exception ex) {
			getActivity().runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(
							getActivity(),
							"Discovery failed of '"
									+ device.getDisplayString()
									+ "': "
									+ (ex != null ? ex.toString()
											: "Couldn't retrieve device/service descriptors"),
											Toast.LENGTH_LONG).show();
				}
			});
			deviceRemoved(device);
		}

		/*
		 * End of optimization, you can remove the whole block if your Android
		 * handset is fast (>= 600 Mhz)
		 */

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

			getActivity().runOnUiThread(new Runnable() {
				public void run() {
					DeviceDisplay d = new DeviceDisplay(device);

					int position = deviceListAdapter.getPosition(d);
					if (position >= 0) {
						// Device already in the list, re-set new value at same
						// position
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
			getActivity().runOnUiThread(new Runnable() {
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
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;
			DeviceDisplay that = (DeviceDisplay) o;
			return device.equals(that.device);
		}

		@Override
		public int hashCode() {
			return device.hashCode();
		}

		@Override
		public String toString() {
			// Display a little star while the device is being loaded (see u
			// optimization earlier)

			URL targeturl = ((RemoteDeviceIdentity) device.getIdentity())
					.getDescriptorURL();
			return device.isFullyHydrated() ? device.getDisplayString()

					: device.getDisplayString() + " *";
		}
	}

	public static DefaultHttpClient getThreadSafeClient() {

		DefaultHttpClient client = new DefaultHttpClient();
		ClientConnectionManager mgr = client.getConnectionManager();
		HttpParams params = client.getParams();
		client = new DefaultHttpClient(new ThreadSafeClientConnManager(params,
				mgr.getSchemeRegistry()), params);
		return client;
	}

	static final Comparator<DeviceDisplay> DISPLAY_COMPARATOR = new Comparator<DeviceDisplay>() {
		public int compare(DeviceDisplay a, DeviceDisplay b) {
			return a.toString().compareTo(b.toString());
		}
	};

	public void httpGetConnection(final String target) {
		mConvergenceUtil = ConvergenceUtil.getInstance();
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				DefaultHttpClient httpclient = new DefaultHttpClient();
				HttpGet httpget = new HttpGet(target);

				try {
					final HttpResponse responsebody = httpclient
							.execute(httpget);
					new Thread(new Runnable() {
						public void run() {
							String appurl = null;
							for (Header h : responsebody.getAllHeaders())
								if (h.getName().equals("Application-URL")) {
									appurl = h.getValue();
									break;
								}
							Log.d("setURL", appurl != null ? appurl : "null");

							if (appurl == null)
								return;

							mConvergenceUtil.setIpAddress(appurl);
//							getActivity().unbindService(serviceConnection);
//							upnpService.getRegistry().removeListener(registryListener);
//							SharedPreferences pref = getSharedPreferences("MySettings", MODE_PRIVATE);
//							Editor editor =pref.edit();
//							editor.putString("appURL", appurl);
//							editor.commit();
						}	
					}
							).start();
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		t.start();

	}

}