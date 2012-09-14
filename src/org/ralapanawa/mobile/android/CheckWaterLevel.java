package org.ralapanawa.mobile.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.ralapanawa.mobile.helpers.ProjectSettingsHandler;
import org.ralapanawa.mobile.res.WeatherData;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class CheckWaterLevel extends Activity {

	private static final String SOAP_ACTION = "urn:Ralamobile#viewwlevel";
	private static final String METHOD_NAME = "viewwlevel";
	private static final String NAMESPACE = "urn:Ralamobile";

	private static final String URL = " http://"
			+ ProjectSettingsHandler.getIp()
			+ "/Ralapanawa_SOAP/Ralapanawalogin.php";

	public static final String CHART = "chart";
	public static final String TITLE = "title";

	private DatePicker dpFrom;
	private DatePicker dpTo;
	private EditText etTankId;
	private Button btLoad;
	
	private String dateFromMonth = "";
	private String dateFromDay = "";
	private String dateToMonth = "";
	private String dateToDay = "";
	
	private List<WeatherData> weatherDatas;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.check_water_level);

		dpFrom = (DatePicker) findViewById(R.id.dpFrom);
		dpTo = (DatePicker) findViewById(R.id.dpTo);
		etTankId = (EditText) findViewById(R.id.tank_id);
		btLoad = (Button) findViewById(R.id.btLoad);

		weatherDatas = new ArrayList<WeatherData>();

		dpFrom.init(2012, 6, 21, null);
		dpTo.init(2012, 6, 25, null);
		
		btLoad.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {

				if (dpFrom.getMonth() < 10 ){
					dateFromMonth = "0" + (dpFrom.getMonth()+1);
				}else{
					dateFromMonth = String.valueOf(dpFrom.getMonth()+1);
				}
				
				if (dpFrom.getDayOfMonth() < 10 ){
					dateFromDay = "0" + dpFrom.getDayOfMonth();
				}else{
					dateFromDay = String.valueOf(dpFrom.getDayOfMonth());
				}
				
				if (dpTo.getMonth() < 10 ){
					dateToMonth = "0" +  (dpTo.getMonth()+1);
				}else{
					dateToMonth = String.valueOf(dpTo.getMonth()+1);
				}
				
				if (dpTo.getDayOfMonth() < 10 ){
					dateToDay = "0" + dpTo.getDayOfMonth();
				}else{
					dateToDay =  String.valueOf(dpTo.getDayOfMonth());
				}
				
				
				String from = dpFrom.getYear() + "-" + dateFromMonth + "-"
						+ dateFromDay;

				String to = dpTo.getYear() + "-" + dateToMonth + "-"
						+ dateToDay;

				Log.i("TAG", from + " " + to);

				connectSOAP(from, to, etTankId.getText().toString());
			}
		});

	}

	public void connectSOAP(String date1, String date2, String tankid) {
		

		
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
		request.addProperty("date1", date1); // "2012-07-21"
		request.addProperty("date2", date2); // "2012-07-25"
		request.addProperty("tankid", tankid); // "IRT001"

		// Log.i("TAG", date1+" "+date2);

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
		

		try {
			if (result != null) {
				// String encodedImage = result.getProperty(0).toString();

				SoapObject garphOb = (SoapObject) result.getProperty(0);
				// SoapObject garphOb2 = (SoapObject) garphOb.getProperty(0);

				Vector objects = (Vector) garphOb.getProperty(0);

				Log.v("TAG_VAL", objects.get(0).toString());

				for (int i = 0; i < objects.size(); i++) {

					Log.v("TAG_VAL", i + "");
					TableRow row = new TableRow(this);

					SoapObject object = (SoapObject) objects.get(i);

					String EmpID = (String) object.getProperty("EmpID");

					

					Log.v("TAG_VAL", EmpID);
					String Datetime = (String) object.getProperty("Datetime");
					Log.v("TAG_VAL", Datetime);
					String Depth = (String) object.getProperty("Depth");
					Log.v("TAG_VAL", Depth);
					String SluiceopLB = (String) object
							.getProperty("SluiceopLB");
					Log.v("TAG_VAL", SluiceopLB);
					String SluiceopRB = (String) object
							.getProperty("SluiceopRB");
					Log.v("TAG_VAL", SluiceopRB);
					String Remarks = (String) object.getProperty("Remarks");
					Log.v("TAG_VAL", Remarks);

					WeatherData data = new WeatherData(EmpID, Datetime, Depth,
							SluiceopLB, SluiceopRB, Remarks);

					weatherDatas.add(data);


				}

			}
		} catch (Exception e) {

		}
		
		
		TableRow.LayoutParams params = new TableRow.LayoutParams(100, 100);
        TableLayout table = (TableLayout) findViewById(R.id.tb);
        String array[] = new String[] { "EidD", "DT", "Dep",
				"SLB", "SRB", "Re" };
        TableRow headerrow = new TableRow(this);
        for (int j = 0; j < array.length; j++) {
            TextView text = new TextView(this);
            text.setLayoutParams(params);
            text.setText(array[j]);
            headerrow.addView(text);
        }
        table.addView(headerrow);
        for (int i = 0; i < weatherDatas.size(); i++) {
            TableRow row = new TableRow(this);
            
            WeatherData weatherData = weatherDatas.get(i);
            
            String vals[]=new String[]{
            	weatherData.getEmpI(),weatherData.getDatetime(),weatherData.getDepth(),weatherData.getSluiceopLB(),weatherData.getSluiceopRB(),
            	weatherData.getRemarks()
            };
            
            for (int j = 0; j < vals.length; j++) {
                TextView text = new TextView(this);
                text.setLayoutParams(params);
                text.setText(vals[j]);
                row.addView(text);
            }
            table.addView(row);
        }

	}

}