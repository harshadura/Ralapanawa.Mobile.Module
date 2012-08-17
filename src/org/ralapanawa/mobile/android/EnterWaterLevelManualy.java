package org.ralapanawa.mobile.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.ralapanawa.mobile.android.R;
import org.ralapanawa.mobile.entity.RestValue;
import org.ralapanawa.mobile.helpers.ServiceHelper;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class EnterWaterLevelManualy extends Activity {
	private EditText TankID;
	private EditText EmpID;
	private EditText Depth;
	private Spinner spinnerLB;
	private Spinner spinnerRB;
	private EditText Remarks;
	private Button btSave;
	private Button butClose;
	private double lati = 0;
	private double longat = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.enter_water_level_manual);

		init_gps();

		TankID = (EditText) findViewById(R.id.tank_id);
		EmpID = (EditText) findViewById(R.id.emp_id);
		Depth = (EditText) findViewById(R.id.depth);
		Remarks = (EditText) findViewById(R.id.remarks);
		spinnerLB = (Spinner) findViewById(R.id.sluiceListLB);
		spinnerRB = (Spinner) findViewById(R.id.sluiceListRB);

		List<String> waterLevel = new ArrayList<String>();
		waterLevel.add("Open");
		waterLevel.add("Close");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				R.layout.simple_spinner, waterLevel);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinnerLB.setAdapter(dataAdapter);
		spinnerRB.setAdapter(dataAdapter);

		butClose = (Button) findViewById(R.id.btClose);
		butClose.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				EnterWaterLevelManualy.this.finish();
			}
		});

		btSave = (Button) findViewById(R.id.btSave);
		btSave.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				if (TankID.getText().equals("") || EmpID.getText().equals("")
						|| Depth.getText().equals("")
						|| Remarks.getText().equals("")) {
					AlertDialog alertDialog = new AlertDialog.Builder(
							EnterWaterLevelManualy.this).create();

					alertDialog.setTitle(R.string.app_name);
					alertDialog
							.setMessage("Please fill in data to nessasary fields!");

					alertDialog.setButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
								}
							});
					alertDialog.show();
					return;
				}

				if (lati == 0 || longat == 0) {
					init_gps();		
				}

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

				nameValuePairs.add(new BasicNameValuePair("para",
						"insertWaterLevel"));
				nameValuePairs.add(new BasicNameValuePair("tank_id", TankID
						.getEditableText().toString()));
				nameValuePairs.add(new BasicNameValuePair("empid", EmpID
						.getEditableText().toString()));
				nameValuePairs.add(new BasicNameValuePair("depth", Depth
						.getEditableText().toString()));
				nameValuePairs.add(new BasicNameValuePair("slopen", spinnerLB
						.getSelectedItem().toString()));
				nameValuePairs.add(new BasicNameValuePair("slclose", spinnerRB
						.getSelectedItem().toString()));
				nameValuePairs.add(new BasicNameValuePair("remarks", Remarks
						.getEditableText().toString()));
				nameValuePairs.add(new BasicNameValuePair("gpslong", String
						.valueOf(lati)));
				nameValuePairs.add(new BasicNameValuePair("gpslat", String
						.valueOf(longat)));

				RestValue restValue=new RestValue();
				Map<String, Object> map = new HashMap<String, Object>();

				for (NameValuePair nameValuePair : nameValuePairs) {
					restValue.put(nameValuePair.getName(), nameValuePair.getValue());

				}
				try {

					Intent intent = new Intent(EnterWaterLevelManualy.this,
							SyncService.class);

					intent.putExtra("values", (Parcelable) restValue);

					startService(intent);

					ServiceHelper.showSavingDataMessageDialog(EnterWaterLevelManualy.this);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void init_gps() {
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Location location = lm
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		Log.i("LO", location + "");
		if (location != null) {
			try {
				lati = location.getLatitude();
				longat = location.getLongitude();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			lati = 79.45;
			longat = 45.67;
		}

		final LocationListener locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				try {
					lati = location.getLatitude();
					longat = location.getLongitude();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			public void onProviderDisabled(String arg0) {
				AlertDialog alertDialog = new AlertDialog.Builder(
						EnterWaterLevelManualy.this).create();

				alertDialog.setTitle("Ralapanawa");
				alertDialog.setMessage("Please ON GPS in your Device! We Need GPS to get Latitude, Longitude");

				alertDialog.setButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {}
						});
				alertDialog.show();
			}

			public void onProviderEnabled(String arg0) {
				// TODO Auto-generated method stub

			}

			public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
				// TODO Auto-generated method stub

			}
		};

		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10,
				locationListener);
	}

	public void clearAll() {
		TankID.setText("");
		Depth.setText("");
		EmpID.setText("");
		Remarks.setText("");
	}
}
