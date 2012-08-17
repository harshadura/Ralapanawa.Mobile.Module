package org.ralapanawa.mobile.android;

import java.text.DecimalFormat;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class GPSbasedLength extends Activity {

	private Button btStart;
	private EditText etSLan;
	private EditText etSAtt;
	//
	private Button btEnd;
	private EditText etELan;
	private EditText etEAtt;
	//
	private Button btCalculate;
	private TextView tvDisM;
	private TextView tvDisFt;
	//
	private double sLon;
	private double sLat;
	private double eLon;
	private double eLat;

	private boolean startFixed = false;
	private boolean endFixed = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gps_length);

		btStart = (Button) findViewById(R.id.btStart);
		btEnd = (Button) findViewById(R.id.btEnd);

		etSAtt = (EditText) findViewById(R.id.etSAti);
		etEAtt = (EditText) findViewById(R.id.etEAti);

		etSLan = (EditText) findViewById(R.id.etSLati);
		etELan = (EditText) findViewById(R.id.etELati);

		btCalculate = (Button) findViewById(R.id.btCalc);
	
		tvDisM=(TextView) findViewById(R.id.tvDisM);
		tvDisFt=(TextView) findViewById(R.id.tvDisft);
		

		btStart.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				startFixed = true;
			}
		});
		btEnd.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				endFixed = true;
			}
		});

		btCalculate.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				double distance =GPSbasedLength.this.distFrom(sLat, sLon, eLat, eLon);
				
				
				DecimalFormat df = new DecimalFormat("#.00000");
				
				
				tvDisM.setText(df.format(distance)+"");
				tvDisFt.setText(df.format((distance*3.281))+"");
				
				
			}
		});

		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		LocationListener ll = new mylocationlistener();

		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ll);

	}

	public double distFrom(double lat1, double lng1, double lat2, double lng2) {
		double earthRadius = 3958.75;
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2)
				* Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double dist = earthRadius * c;

		int meterConversion = 1609;

		return (dist * meterConversion);
	}

	private class mylocationlistener implements LocationListener {

		public void onLocationChanged(Location location) {
			if (location != null) {

				double latitude = location.getLatitude();
				double longitude = location.getLongitude();
				if (!startFixed) {

					sLat = latitude;
					sLon = longitude;

					etSAtt.setText(latitude + "");
					etSLan.setText(longitude + "");
				}

				if (!endFixed) {

					eLat = latitude;
					eLon = longitude;

					etEAtt.setText(latitude + "");
					etELan.setText(longitude + "");
				}

			}
		}

		public void onProviderDisabled(String provider) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	}

}
