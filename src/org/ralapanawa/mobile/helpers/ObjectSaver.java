package org.ralapanawa.mobile.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.ralapanawa.mobile.android.R;
import org.ralapanawa.mobile.android.AndroidclientActivity;
import org.ralapanawa.mobile.android.SyncDataListActivity;
import org.ralapanawa.mobile.entity.RestValue;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

public class ObjectSaver implements Serializable {
	/**
	 * 
	 */

	private static Thread THREAD;

	private static final long serialVersionUID = 1L;
	private static String extStorageDirectory;
	private static final String FILENAME = "RALADATA.DATA";

	private static DataStack REST_VALUES;
	private static ObjectSaver OBJECT_SAVER;

	

	private Context context;

	public ObjectSaver(final Context context) throws IOException,
			ClassNotFoundException {

		this.context = context;

		REST_VALUES=getREST_VALUES(context);
		
		String ns = Context.NOTIFICATION_SERVICE;
		final NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(ns);
		
		extStorageDirectory = Environment.getExternalStorageDirectory()
				.toString();
		

		if (THREAD == null) {
			THREAD = new Thread(new Runnable() {

				public void run() {

					while (true) {
						if (!REST_VALUES.getMaps().isEmpty() && RestHelper.checkdataAVB(context)) {
							Map<String, String> pop = REST_VALUES.getMaps()
									.pop();
							List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
							for (String key : pop.keySet()) {
								BasicNameValuePair nameValuePair = new BasicNameValuePair(
										key, pop.get(key));
								nameValuePairs.add(nameValuePair);
								
								
								
								

							}
							try {
								
								
								RestHelper.rest(nameValuePairs);
								
								
								serializeRestValue(REST_VALUES);
								

								int icon = R.drawable.load;
								CharSequence tickerText = "Data Base Updated";
								long when = System.currentTimeMillis();

								Notification notification = new Notification(icon, tickerText, when);
								
								
								Context context = ObjectSaver.this.context.getApplicationContext();
								CharSequence contentTitle = "Data Synced";
								CharSequence contentText = pop.get("para")+ " Finish Data Sync";
								Intent notificationIntent = new Intent(context, SyncDataListActivity.class);
								PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

								notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
								
								mNotificationManager.notify(REST_VALUES.getMaps().size(), notification);
								
								serializeRestValue(REST_VALUES);
								
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
						}else{
							try {
								Thread.sleep(2000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}

				}
			});
			THREAD.start();
		}
		

	}

	public void serializeRestValue(DataStack restValues) throws IOException {

		
		FileOutputStream fos = null;

		ObjectOutputStream outputStream = null;
		try {
			File file = new File(extStorageDirectory, FILENAME);
			fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
			outputStream = new ObjectOutputStream(fos);
			outputStream.writeObject(restValues);

		} catch (IOException exception) {
			Log.e("WRTIE ERROT", exception.getMessage(), exception);
		} finally {
			outputStream.flush();
			outputStream.close();
		}
	}
	
	public void serializeRestValue(RestValue restValue) throws IOException {

		REST_VALUES.getMaps().push(restValue.getMap());

		FileOutputStream fos = null;

		ObjectOutputStream outputStream = null;
		try {
			File file = new File(extStorageDirectory, FILENAME);
			fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
			outputStream = new ObjectOutputStream(fos);
			outputStream.writeObject(REST_VALUES);

		} catch (IOException exception) {
			Log.e("WRTIE ERROT", exception.getMessage(), exception);
		} finally {
			outputStream.flush();
			outputStream.close();
		}
	}

	public static DataStack readObject(Context context) throws IOException, ClassNotFoundException {
		FileInputStream fis = context.openFileInput(FILENAME);
		ObjectInputStream ois = new ObjectInputStream(fis);
		DataStack obj = (DataStack) ois.readObject();

		return obj;

	}
	
	public static DataStack getREST_VALUES(Context context) {
		
		try {
			REST_VALUES =readObject(context);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			REST_VALUES = new DataStack();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return REST_VALUES;
	}

	public static void setREST_VALUES(DataStack rEST_VALUES) {
		REST_VALUES = rEST_VALUES;
	}

}
