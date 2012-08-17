package org.ralapanawa.mobile.android;

import java.util.HashMap;
import java.util.Map;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.ralapanawa.mobile.helpers.ProjectSettingsHandler;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

public class CheckWaterLevel extends Activity {
	
	private static final String SOAP_ACTION = "urn:Ralamobile#viewwlevel";
	private static final String METHOD_NAME = "viewwlevel";
	private static final String NAMESPACE = "urn:Ralamobile";

	private static final String URL = " http://" + ProjectSettingsHandler.getIp()
			+ "/Ralapanawa_SOAP/Ralapanawalogin.php";
	
	public static final String CHART = "chart";
	public static final String TITLE = "title";
	

	
	private DatePicker dpFrom;
	private DatePicker dpTo;
	private EditText etTankId;
	private Button btLoad;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.check_water_level);
		
		dpFrom=(DatePicker) findViewById(R.id.dpFrom);
		dpTo=(DatePicker) findViewById(R.id.dpTo);
		etTankId=(EditText) findViewById(R.id.tank_id);
		btLoad=(Button) findViewById(R.id.btLoad);
		
		
		
		
		
		btLoad.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				String from =dpFrom.getYear()+"-"+dpFrom.getMonth()+"-"+dpFrom.getDayOfMonth();
				
				String to =dpTo.getYear()+"-"+dpTo.getMonth()+"-"+dpTo.getDayOfMonth();
				
				
				Log.i("TAG", from+" "+to);
				
				
				connectSOAP(from, to, etTankId.getText().toString());
			}
		});
		
		
	}

	public Map<String, Double> connectSOAP(String date1,String date2,String tankid) {
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
		request.addProperty("tankid", "IRT001");
		request.addProperty("date1", "2012-07-01");
		request.addProperty("date2", "2012-07-25");
		
		Log.i("TAG", date1+" "+date2);

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

		Map<String, Double> map = new HashMap<String, Double>();
		try {
			if (result != null) {
				String encodedImage = result.getProperty(0).toString();

				SoapObject garphOb = (SoapObject) result.getProperty(0);

				for (int i = 0; i < garphOb.getPropertyCount(); i++) {
					Log.v("TAG", garphOb.getProperty(i)+"");
				}

				Log.v("TAG", encodedImage);
			}
		} catch (Exception e) {

			return null;
		}
		return map;
	}
	
}