package org.ralapanawa.mobile.helpers;

import java.io.Serializable;

import org.ralapanawa.mobile.android.AndroidclientActivity;
import org.ralapanawa.mobile.android.Login;
import org.ralapanawa.mobile.android.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.provider.Settings;


public class ServiceHelper implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Context context;
	
	
	public  void checkin(Context context) {
		
		this.context=context;
		String message = null;

		ConnectivityManager connec = (ConnectivityManager) this.context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		android.net.NetworkInfo wifi = connec
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		android.net.NetworkInfo mobile = connec
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

		// Here if condition check for wifi and mobile network is available or
		// not.
		// If anyone of them is available or connected then it will return true,
		// otherwise false;

		/*
		 * if (!wifi.isConnected()) { Log.i(tag, "WIFI Not AVB"); return ""; }
		 * else
		 */

		LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

		if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			buildAlertMessageNoGps();
		}

		if (!mobile.isConnected()) {
			buildAlertMessageNoData();
		}
	}

	private  void buildAlertMessageNoGps() {
		final AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
		builder.setMessage(
				"Your GPS seems disabled, Sometimes Ralapanawa-Mobile needs GPS to process data, Do you want to enable it now?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(
									@SuppressWarnings("unused") final DialogInterface dialog,
									@SuppressWarnings("unused") final int id) {
								context.startActivity(new Intent(
										Settings.ACTION_LOCATION_SOURCE_SETTINGS));
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(final DialogInterface dialog,
							@SuppressWarnings("unused") final int id) {
						dialog.cancel();
					}
				});
		final AlertDialog alert = builder.create();
		alert.show();
	}

	private  void buildAlertMessageNoData() {
		final AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
		builder.setMessage(
				"Your Data seems disabled, Sometimes Ralapanawa-Mobile needs Data to process info, Do you want to enable it now?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(
									@SuppressWarnings("unused") final DialogInterface dialog,
									@SuppressWarnings("unused") final int id) {
								context.startActivity(new Intent(
										Settings.ACTION_DATA_ROAMING_SETTINGS));
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(final DialogInterface dialog,
							@SuppressWarnings("unused") final int id) {
						dialog.cancel();
					}
				});
		final AlertDialog alert = builder.create();
		alert.show();
	}
	
	public static final void showSavingDataMessageDialog(final Activity activity){
		final AlertDialog alertDialog = new AlertDialog.Builder(
				activity).create();

		alertDialog.setTitle(R.string.app_name);
		alertDialog
				.setMessage("Your Data Saved Successfully!");

		alertDialog.setButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int which) {
						Intent intent2 = new Intent(activity,
								AndroidclientActivity.class);
						activity.startActivity(intent2);

						activity.finish();
						alertDialog.dismiss();
						
					}
				});
		alertDialog.show();
	}
}
