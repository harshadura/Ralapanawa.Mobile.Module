package org.ralapanawa.mobile.android;

import java.util.ArrayList;
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
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class Last5DaysWeather extends Activity {

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.compare_weather);

		Button button = (Button) findViewById(R.id.btApprove);
		listView = (ListView) findViewById(R.id.listView1);

		final List<WetherData> wdDatas = new ArrayList<WetherData>();

		button.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				listView.setAdapter(new CompareWeatherList(
						getWetherDatas("IRT001"), Last5DaysWeather.this));
			}
		});

	}

	public List<WetherData> getWetherDatas(String tankid) {

		List<WetherData> datas = new ArrayList<WetherData>();
		String[] latLang = connectSOAP(tankid);

		for (int i = 0; i < 5; i++) {
			datas.add(connectSOAP2("1", latLang[0], latLang[1]));
		}

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
