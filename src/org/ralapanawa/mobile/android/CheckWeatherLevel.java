package org.ralapanawa.mobile.android;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.ralapanawa.mobile.android.R;
import org.ralapanawa.mobile.helpers.ProjectSettingsHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CheckWeatherLevel extends Activity {

	private ImageView image;
	private Thread t;
	private Splash splash;
	
	private String lat;
	private String longat;
	
	private Button btLoad;
	private TextView observation_time;
	private TextView temp_C;
	private TextView temp_F;
	private TextView weatherDesc;
	private TextView windspeedMiles;
	private TextView windspeedKmph;
	private TextView winddirDegree;
	private TextView winddir16Point;
	private TextView precipMM;
	private TextView humidity;
	private TextView visibility;
	private TextView pressure;
	private TextView cloudcover;
	private SoapObject result = null;

	private boolean finishedProcess = false;
	private ProgressDialog progressBar;
	private int progressBarStatus = 0;
	private Handler progressBarHandler = new Handler();

	private static final String SOAP_ACTION2 = "urn:Ralamobile#get_gps";
	private static final String METHOD_NAME2 = "get_gps";
	private static final String NAMESPACE2 = "urn:Ralamobile";

	private static final String URL2 = " http://"
			+ ProjectSettingsHandler.getIp()
			+ "/Ralapanawa_SOAP/Ralapanawalogin.php";
	
	private static final String SOAP_ACTION = "urn:Ralamobile2#Weathmoni";
	private static final String METHOD_NAME = "Weathmoni";
	private static final String NAMESPACE = "urn:Ralamobile";
	
	private static final String URL = " http://" + ProjectSettingsHandler.getIp()
			+ "/Ralapanawa_SOAP/weatherwebservice.php";

	private EditText etTankId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.check_weather);
		
		etTankId = (EditText) findViewById(R.id.etTankId);
		
		observation_time = (TextView) findViewById(R.id.observation_time);
		temp_C = (TextView) findViewById(R.id.temp_C);
		temp_F = (TextView) findViewById(R.id.temp_F);
		weatherDesc = (TextView) findViewById(R.id.weatherDesc);
		windspeedMiles = (TextView) findViewById(R.id.windspeedMiles);
		windspeedKmph = (TextView) findViewById(R.id.windspeedKmph);
		winddirDegree = (TextView) findViewById(R.id.winddirDegree);
		winddir16Point = (TextView) findViewById(R.id.winddir16Point);
		precipMM = (TextView) findViewById(R.id.precipMM);
		humidity = (TextView) findViewById(R.id.humidity);
		visibility = (TextView) findViewById(R.id.visibility);
		pressure = (TextView) findViewById(R.id.pressure);
		cloudcover = (TextView) findViewById(R.id.cloudcover);

		btLoad = (Button) findViewById(R.id.btLoad);

		btLoad.setOnClickListener(buttonSaveOnClickListener);

		splash = new Splash(this);
		t = new Thread(splash);
		t.start();
	}

	private Drawable LoadImageFromWeb(String url) {
		try {
			InputStream is = (InputStream) new URL(url).getContent();
			Drawable d = Drawable.createFromStream(is, "src name");
			return d;
		} catch (Exception e) {
			System.out.println("Exc=" + e);
			return null;
		}
	}

	public void getWeatherFromSOAPService(String lati, String longat) {
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
		request.addProperty("latweath", lati);
		request.addProperty("longweath", longat);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(request);
		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
		try {
			androidHttpTransport.call(SOAP_ACTION, envelope);
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		result = (SoapObject) envelope.bodyIn;
		progressBarStatus = progressBarStatus + 100;
	
	}

	public class Splash implements Runnable {

		Activity activity;
		final Handler myHandler = new Handler();

		public Splash(Activity activity) {
			this.activity = activity;
		}

		public void run() {
			// TODO Auto-generated method stub

		}

		public synchronized void setTextow(final Drawable drawable) {
			// Wrap DownloadTask into another Runnable to track the statistics
			myHandler.post(new Runnable() {

				public void run() {
					image = (ImageView) activity
							.findViewById(R.id.weatherIconUrl);
					image.setImageDrawable(drawable);
				}
			});
		}
	}

	private void init_gps() {

		LocationManager locationManager;
		String context = Context.LOCATION_SERVICE;
		locationManager = (LocationManager) getSystemService(context);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		String provider = locationManager.getBestProvider(criteria, true);
		Location location = locationManager.getLastKnownLocation(provider);
		updateWithNewLocation(location);
		locationManager.requestLocationUpdates(provider, 2000, 10,
				locationListener);
	}

	private void updateWithNewLocation(Location location) {
		if (location != null) {
			lat = String.valueOf(location.getLatitude());
			longat = String.valueOf(location.getLongitude());
		} else {
			lat = String.valueOf(0);
			longat = String.valueOf(0);
		}
	}

	private final LocationListener locationListener = new LocationListener() {
		public void onLocationChanged(Location location) {
			updateWithNewLocation(location);
		}

		public void onProviderDisabled(String provider) {
			updateWithNewLocation(null);
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};
	
	Button.OnClickListener buttonSaveOnClickListener = new Button.OnClickListener() {
		public void onClick(View v) {
			progressBar = new ProgressDialog(v.getContext());
			progressBar.setCancelable(true);
			progressBar.setMessage("Retrieving data from Weather Web service...");
			progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressBar.setProgress(0);
			progressBar.setMax(100);
			progressBar.show();
			progressBarStatus = 0;
				
			new Thread(new Runnable() {
				public void run() {
					while (progressBarStatus < 100) {

						try {
							Thread.sleep(1000);
							
							String[] latLang = getGPS(etTankId.getText().toString());
							lat = latLang[0];
							longat = latLang[1];
							
							Log.v("TAG", lat + " " + longat);
							getWeatherFromSOAPService(lat, longat);
							finishedProcess = true;
							
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						progressBarHandler.post(new Runnable() {
							public void run() {
								progressBar.setProgress(progressBarStatus);
							}
						});
					}
					if (progressBarStatus >= 100) {
						
						try {
							Thread.sleep(2000);
							
							// UI running Thread !!
							runOnUiThread(new Runnable() {
							     public void run() {
							    		if(finishedProcess == true){
											
							    			if (result != null) {

							    				SoapObject weatherResponse = (SoapObject) result.getProperty(0);
							    				Log.v("TAG", weatherResponse.toString());

							    				observation_time.setText(weatherResponse.getProperty(
							    						"observation_time").toString());
							    				temp_C.setText(weatherResponse.getProperty("temp_C").toString());
							    				temp_F.setText(weatherResponse.getProperty("temp_F").toString());
							    				weatherDesc.setText(weatherResponse.getProperty("weatherDesc")
							    						.toString());
							    				windspeedMiles.setText(weatherResponse
							    						.getProperty("windspeedMiles").toString());
							    				windspeedKmph.setText(weatherResponse.getProperty("windspeedKmph")
							    						.toString());
							    				winddirDegree.setText(weatherResponse.getProperty("winddirDegree")
							    						.toString());
							    				winddir16Point.setText(weatherResponse
							    						.getProperty("winddir16Point").toString());
							    				precipMM.setText(weatherResponse.getProperty("precipMM").toString());
							    				humidity.setText(weatherResponse.getProperty("humidity").toString());
							    				visibility.setText(weatherResponse.getProperty("visibility")
							    						.toString());
							    				pressure.setText(weatherResponse.getProperty("pressure").toString());
							    				cloudcover.setText(weatherResponse.getProperty("cloudcover")
							    						.toString());

							    				try {
							    					String imageS = weatherResponse.getProperty("weatherIconUrl")
							    							.toString();
							    					Drawable drawable = LoadImageFromWeb(imageS);
							    					splash.setTextow(drawable);
							    				} catch (Exception e) {
							    					e.printStackTrace();
							    				}
							    			}
							    			
											finishedProcess = false;
										}
							    }
							});	
							
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						progressBar.dismiss();
					}
				}
			}).start();
		}
	};

	public String[] getGPS(String tankid) {
		SoapObject request = new SoapObject(NAMESPACE2, METHOD_NAME2);
		request.addProperty("tankid", tankid);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(request);
		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL2);
		try {
			androidHttpTransport.call(SOAP_ACTION2, envelope);
		} catch (Exception e) {
			e.printStackTrace();
		}

		SoapObject result;
		result = (SoapObject) envelope.bodyIn;

		// if (result == null)

		try {
			if (result != null) {
				String encodedImage = result.getProperty(0).toString();
				SoapObject garphOb = (SoapObject) result.getProperty(0);
				Log.v("TAG", encodedImage);

				return new String[] { "" + garphOb.getProperty("lat"),
						"" + garphOb.getProperty("long") };
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return null;
	}
}
