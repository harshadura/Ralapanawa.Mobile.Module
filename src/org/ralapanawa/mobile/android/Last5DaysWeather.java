package org.ralapanawa.mobile.android;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.ralapanawa.mobile.helpers.ProjectSettingsHandler;
import org.ralapanawa.mobile.res.CompareWeatherList;
import org.ralapanawa.mobile.res.WetherData;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class Last5DaysWeather extends Activity {
	private boolean finishedProcess = false;
	private ProgressDialog progressBar;
	private int progressBarStatus = 0;
	private Handler progressBarHandler = new Handler();
	private SoapObject result = null;
	private List<WetherData> compareWeatherList;

	private static final String SOAP_ACTION = "urn:Ralamobile#get_gps";
	private static final String METHOD_NAME = "get_gps";
	private static final String NAMESPACE = "urn:Ralamobile";

	private static final String SOAP_ACTION_WETHER = "urn:Ralamobile#Weathmoni";
	private static final String METHOD_NAME_WETHER = "Weathmoni";

	private static final String URL_WETHER = " http://"
			+ ProjectSettingsHandler.getIp()
			+ "/Ralapanawa_SOAP/weatherwebservice.php";

	private static final String URL = " http://"
			+ ProjectSettingsHandler.getIp()
			+ "/Ralapanawa_SOAP/Ralapanawalogin.php";

	private ListView listView;
	private EditText etTankId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.compare_weather);

		etTankId = (EditText) findViewById(R.id.tankid);
		Button button = (Button) findViewById(R.id.btApprove);
		listView = (ListView) findViewById(R.id.listView1);

		final List<WetherData> wdDatas = new ArrayList<WetherData>();

		button.setOnClickListener(buttonSaveOnClickListener);
		
	}

	Button.OnClickListener buttonSaveOnClickListener = new Button.OnClickListener() {
		public void onClick(View v) {
			progressBar = new ProgressDialog(v.getContext());
			progressBar.setCancelable(true);
			progressBar.setMessage("Retrieving data from Rala Web service...");
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
							compareWeatherList = getWetherDatas(etTankId.getText().toString());
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
							    			try {
							    				listView.setAdapter(new CompareWeatherList(compareWeatherList, Last5DaysWeather.this));
							    			} catch (Exception e) {
							    				e.printStackTrace();
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
	
	public List<WetherData> getWetherDatas(String tankid) {

		List<WetherData> datas = new ArrayList<WetherData>();
		String[] latLang = connectSOAP(tankid);
		Calendar calendar = Calendar.getInstance();
		for (int i = 0; i < 5; i++) {
			WetherData connectSOAP2 = connectSOAP2(""+(i+1), latLang[0], latLang[1]);
			calendar.add(Calendar.DATE, i);
			connectSOAP2.setObTime(calendar.getTime().toString());
			datas.add(connectSOAP2);
		}
		progressBarStatus = progressBarStatus + 100;
		return datas;
		
	}

	public WetherData connectSOAP2(String ddi, String latti, String longt) {
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_WETHER);
		request.addProperty("ddid", ddi);
		request.addProperty("latweath", latti);
		request.addProperty("longweath", longt);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(request);
		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL_WETHER);
		try {
			androidHttpTransport.call(SOAP_ACTION_WETHER, envelope);
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

				WetherData data = new WetherData();
				
				data.setImageLoc(garphOb.getProperty("weatherIconUrl")+"");
				data.setTemp(garphOb.getProperty("temp_C")+"");
				data.setWetherDesc(garphOb.getProperty("weatherDesc")+"");
				data.setWindSpeed(garphOb.getProperty("windspeedKmph")+"");
				
				

				
				return data;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return null;
	}

	public String[] connectSOAP(String tankid) {
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
		request.addProperty("tankid", tankid);

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
