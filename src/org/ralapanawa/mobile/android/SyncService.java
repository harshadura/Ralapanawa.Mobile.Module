package org.ralapanawa.mobile.android;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.ralapanawa.mobile.android.R;
import org.ralapanawa.mobile.entity.RestValue;
import org.ralapanawa.mobile.helpers.ObjectSaver;
import org.ralapanawa.mobile.helpers.RestHelper;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class SyncService extends Service {
	private String ns = Context.NOTIFICATION_SERVICE;
	private NotificationManager mNotificationManager ;
	public SyncService() {
		
		
	}

	private boolean checkdataAVB() {
		ConnectivityManager connec = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		android.net.NetworkInfo wifi = connec
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		android.net.NetworkInfo mobile = connec
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		return mobile.isConnected();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		 mNotificationManager=(NotificationManager) this.getSystemService(ns);
			
	}

	@Override
	public void onDestroy() {
		// code to execute when the service is shutting down
	}

	@Override
	public void onStart(Intent intent, int startid) {

		if (intent!=null) {
			Bundle extras = intent.getExtras();
			RestValue values = extras.getParcelable("values");
			final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			Map<String, String> pairs = values.getMap();
			for (String key : pairs.keySet()) {

				String object = pairs.get(key) + "";

				nameValuePairs.add(new BasicNameValuePair(key, object));

			}
			Thread thread = new Thread(new Runnable() {

				public void run() {
					try {
						RestHelper.rest(nameValuePairs);
					} catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					Log.i("DATA SYNC", "DATA SYNC");

				}
			});
			Log.i("DATAAVB", checkdataAVB() + "");
			if (checkdataAVB()) {
				thread.start();
			} else {
				ObjectSaver objectSaver = null;
				try {
					objectSaver = new ObjectSaver(this);

					RestValue restValue = new RestValue();

					restValue.setMap(values.getMap());

					objectSaver.serializeRestValue(restValue);
					
					
					
					
					int icon = R.drawable.load;
					CharSequence tickerText = "Data Base Updated";
					long when = System.currentTimeMillis();

					Notification notification = new Notification(icon, tickerText, when);
					
					
					Context context = SyncService.this.getApplicationContext();
					CharSequence contentTitle = "Data Not Available";
					CharSequence contentText = restValue.get("para")+ "Added To Sync";
					Intent notificationIntent = new Intent(context, SyncDataListActivity.class);
					PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

					notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
					
					mNotificationManager.notify(1, notification);
					
					
					
					
					
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			Toast.makeText(this, "Data Added", Toast.LENGTH_LONG);
			Log.i("DATA ADDEd", "DATA ADDEd");
		}

	}
}
