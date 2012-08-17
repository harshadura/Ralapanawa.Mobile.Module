package org.ralapanawa.mobile.android;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.ralapanawa.mobile.android.R;
import org.ralapanawa.mobile.helpers.ProjectSettingsHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class RealtimeMonitor extends Activity {

	private Button btAdd;
	private TextView tvCapacity;
	private TextView tvdate;
	private EditText etTankId;
	private ImageView meter;
	
	private boolean finishedProcess = false;
	private ProgressDialog progressBar;
	private int progressBarStatus = 0;
	private Handler progressBarHandler = new Handler();
	private SoapObject result = null;

	private static final String SOAP_ACTION = "urn:Ralamobile#Real_time_water";
	private static final String METHOD_NAME = "Real_time_water";
	private static final String NAMESPACE = "urn:Ralamobile";
	private static final String URL = "http://"+ProjectSettingsHandler.getIp()+"/Ralapanawa_SOAP/Ralapanawalogin.php";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.realtime_monitor);
		tvCapacity = (TextView) findViewById(R.id.tvCapacity);
		tvdate = (TextView) findViewById(R.id.etDate);
		etTankId = (EditText) findViewById(R.id.etTankId);
		meter = (ImageView) findViewById(R.id.meter);
		
		btAdd = (Button) findViewById(R.id.btLoad);
		btAdd.setOnClickListener(buttonSaveOnClickListener);
	}
	
	public void connectSOAP(String tankid) {
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

		result = (SoapObject) envelope.bodyIn;
		progressBarStatus = progressBarStatus + 100;
		
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
							
							String tankid = etTankId.getText().toString();
							connectSOAP(tankid);			
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
							    				if (result != null) {
							    					SoapObject garphOb = (SoapObject) result.getProperty(0);

							    						String date = garphOb.getProperty("wleveldate").toString();
							    						String depth = garphOb.getProperty("depth").toString();

							    						Log.v("TAG", date + " " + depth);

							    					if (date == null || depth == null || date.equals("0") || depth.equals("0")) {
							    						AlertDialog alertDialog = new AlertDialog.Builder(
							    								RealtimeMonitor.this).create();

							    						alertDialog.setTitle("Ralapanawa");
							    						alertDialog.setMessage("Invalid Tank ID, No data found!");

							    						alertDialog.setButton("OK",
							    								new DialogInterface.OnClickListener() {
							    									public void onClick(DialogInterface dialog,
							    											int which) {
							    									}
							    								});
							    						alertDialog.show();
							    						return;
							    					}
							    		
							    						tvdate.setText(date);
							    						tvCapacity.setText(depth);
							    						int capa = Integer.parseInt(depth.trim());
							    						
							    						Drawable imgDrawable=getResources().getDrawable(R.drawable.meter0);
							    						
							    						switch (capa){
							    							case 1 :  imgDrawable=getResources().getDrawable(R.drawable.meter1); break;
							    							case 2 :  imgDrawable=getResources().getDrawable(R.drawable.meter2); break;
							    							case 3 :  imgDrawable=getResources().getDrawable(R.drawable.meter3); break;
							    							case 4 :  imgDrawable=getResources().getDrawable(R.drawable.meter4); break;
							    							case 5 :  imgDrawable=getResources().getDrawable(R.drawable.meter5); break;
							    							case 6 :  imgDrawable=getResources().getDrawable(R.drawable.meter6); break;
							    							case 7 :  imgDrawable=getResources().getDrawable(R.drawable.meter7); break;
							    							default:  imgDrawable=getResources().getDrawable(R.drawable.meter0);
							    						}
							    						
							    						meter.setImageDrawable(imgDrawable);
							    					}
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
}
