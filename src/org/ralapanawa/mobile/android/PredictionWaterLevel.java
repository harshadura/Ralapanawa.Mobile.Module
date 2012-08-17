package org.ralapanawa.mobile.android;

import java.util.ArrayList;
import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.ralapanawa.mobile.helpers.ProjectSettingsHandler;
import org.ralapanawa.mobile.res.PredictData;
import org.ralapanawa.mobile.res.PreditViewList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class PredictionWaterLevel extends Activity {

	private static final String SOAP_ACTION = "urn:Ralamobile2#daysWeathmoni";
	private static final String METHOD_NAME = "daysWeathmoni";
	private static final String NAMESPACE = "urn:Ralamobile2";
	private static final String URL = " http://"
			+ ProjectSettingsHandler.getIp()
			+ "/Ralapanawa_SOAP/weatherwebservice.php";

	private static final String SOAP_ACTION2 = "urn:Ralamobile#volume_towlevel";
	private static final String METHOD_NAME2 = "volume_towlevel";
	private static final String NAMESPACE2 = "urn:Ralamobile";
	private static final String URL2 = " http://"
			+ ProjectSettingsHandler.getIp()
			+ "/Ralapanawa_SOAP/Ralapanawalogin.php";

	private static final String SOAP_ACTION3 = "urn:Ralamobile2#Real_time_water";
	private static final String METHOD_NAME3 = "Real_time_water";
	private static final String NAMESPACE3 = "urn:Ralamobile";
	private static final String URL3 = " http://"
			+ ProjectSettingsHandler.getIp()
			+ "/Ralapanawa_SOAP/Ralapanawalogin.php";

	private static final String SOAP_ACTION4 = "urn:Ralamobile#wlevto_volume";
	private static final String METHOD_NAME4 = "wlevto_volume";
	private static final String NAMESPACE4 = "urn:Ralamobile";
	private static final String URL4 = " http://"
			+ ProjectSettingsHandler.getIp()
			+ "/Ralapanawa_SOAP/Ralapanawalogin.php";

	private ListView listView;
	private Button btLoad;
	private EditText etTankId;
	
	private List<PredictData> prdictData1 = null;
	private ProgressDialog progressBar;
	private int progressBarStatus = 0;
	private Handler progressBarHandler = new Handler();
	boolean finishedProcess = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.predic);

		listView = (ListView) findViewById(R.id.preditcList);
		etTankId=(EditText) findViewById(R.id.etTankId);
		btLoad=(Button) findViewById(R.id.btLoad);
		btLoad.setOnClickListener(buttonSaveOnClickListener);

		List<PredictData> prdictData = new ArrayList<PredictData>();
		listView.setAdapter(new PreditViewList(this, prdictData));
	}

	private List<PredictData> getPredictDatas(String tankid) {
		ArrayList<PredictData> prdictData = new ArrayList<PredictData>();
		
		prdictData.add(connectSOAP(tankid, "1", "7.87", "80.77"));
		progressBarStatus = progressBarStatus + 20;
		
		prdictData.add(connectSOAP(tankid, "2", "7.87", "80.77"));
		progressBarStatus = progressBarStatus + 20;
		
		prdictData.add(connectSOAP(tankid, "3", "7.87", "80.77"));
		progressBarStatus = progressBarStatus + 20;
		
		prdictData.add(connectSOAP(tankid, "4", "7.87", "80.77"));
		progressBarStatus = progressBarStatus + 20;
		
		prdictData.add(connectSOAP(tankid, "5", "7.87", "80.77"));
		progressBarStatus = progressBarStatus + 20;
		
		return prdictData;
	}

	public PredictData connectSOAP(String tankid, String ddid, String latweath,
			String longweath) {
		PredictData data = new PredictData();

		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
		request.addProperty("ddid", ddid);
		request.addProperty("latweath", latweath);
		request.addProperty("longweath", longweath);

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

				if (garphOb!=null) {
					String precipMM = garphOb.getProperty("precipMM")
							.toString();
					double cachment = Double.parseDouble(connectSOAP2(tankid,
							"1460"));
					cachment = cachment * 258.99 * 10;
					// double d2 = Math.Round(cachment, 3);
					double totalrainvolumeday1 = Math
							.round(((Double.parseDouble(precipMM + ""))
									* cachment * 0.000810713194));// convert
																	// to
					String currentwatervolume = connectSOAP4(tankid, "324324");
					double newvolume1 = Double.parseDouble(currentwatervolume)
							+ totalrainvolumeday1;
					String value = connectSOAP2(tankid, ("" + newvolume1));
					data.setDate(garphOb.getProperty("date").toString());
					data.setRainFail(precipMM);
					data.setRainFailVolume(totalrainvolumeday1 + " acfeet");
					data.setPredictWaterVolume(newvolume1 + " Acfeet");
					data.setPredictWaterLevel(value + " ft inch");
				}
				Log.v("TAG", encodedImage);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return data;
	}

	public String connectSOAP2(String ddid, String volume) {
		SoapObject request = new SoapObject(NAMESPACE2, METHOD_NAME2);
		request.addProperty("tankid", ddid);
		request.addProperty("volume", volume);

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

		String res = "1000";
		try {
			if (result != null) {
				String encodedImage = result.getProperty(0).toString();

				SoapObject garphOb = (SoapObject) result.getProperty(0);
				Object property = garphOb.getProperty("cachmentarea");
				if (property != null) {
					res = property.toString();
				}

				Log.v("TAG", encodedImage);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "100";
		}

		return res;
	}

	public String connectSOAP3(String tankid) {
		SoapObject request = new SoapObject(NAMESPACE3, METHOD_NAME3);
		request.addProperty("tankid", tankid);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(request);
		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL3);
		try {
			androidHttpTransport.call(SOAP_ACTION3, envelope);
		} catch (Exception e) {
			e.printStackTrace();
		}

		SoapObject result;
		result = (SoapObject) envelope.bodyIn;

		// if (result == null)

		String res = "";
		try {
			if (result != null) {
				String encodedImage = result.getProperty(0).toString();

				SoapObject garphOb = (SoapObject) result.getProperty(0);

				// res = garphOb.getProperty("return").toString();

				Log.v("TAG", encodedImage);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return res;
	}

	public String connectSOAP4(String tankid, String wlevel) {
		SoapObject request = new SoapObject(NAMESPACE4, METHOD_NAME4);
		request.addProperty("tankid", tankid);
		request.addProperty("wlevel", wlevel);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(request);
		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL4);
		try {
			androidHttpTransport.call(SOAP_ACTION4, envelope);
		} catch (Exception e) {
			e.printStackTrace();
		}

		SoapObject result;
		result = (SoapObject) envelope.bodyIn;

		// if (result == null)

		String res = "1000";
		try {
			if (result != null) {
				String encodedImage = result.getProperty(0).toString();

				SoapObject garphOb = (SoapObject) result.getProperty(0);

				Object property = garphOb.getProperty("volume");
				
				if(property!=null){
					res=property.toString();
				}

				Log.v("TAG", encodedImage);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return res;
	}
	
	Button.OnClickListener buttonSaveOnClickListener = new Button.OnClickListener() {
		public void onClick(View v) {
			progressBar = new ProgressDialog(v.getContext());
			progressBar.setCancelable(true);
			progressBar.setMessage("We are Processing the Data, Please wait a moment...");
			progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressBar.setProgress(0);
			progressBar.setMax(100);
			progressBar.show();
			progressBarStatus = 0;
			
			new Thread(new Runnable() {
				public void run() {
					while (progressBarStatus < 100) {

//						switch (progressBarStatus){
//							case 20 : progressBar.setMessage("Connecting Web Service..."); break;
//							case 40 : progressBar.setMessage("Retrieving Weather Data2..."); break;
//							case 60 : progressBar.setMessage("Retrieving Weather Data3..."); break;
//							case 80 : progressBar.setMessage("Retrieving Weather Data4..."); break;
//							case 100 : progressBar.setMessage("Retrieving Weather Data5..."); break;
//						}
						
						try {
							prdictData1 = getPredictDatas(etTankId.getEditableText().toString());
							finishedProcess = true;
							Thread.sleep(1000);
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
											listView.setAdapter(new PreditViewList(PredictionWaterLevel.this, prdictData1));
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
}
