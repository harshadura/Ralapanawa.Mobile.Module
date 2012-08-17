package org.ralapanawa.mobile.android;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.ralapanawa.mobile.android.R;
import org.ralapanawa.mobile.entity.RestValue;
import org.ralapanawa.mobile.helpers.RestHelper;
import org.ralapanawa.mobile.helpers.ServiceHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class FaultReporting extends Activity implements Serializable {

	private static final long serialVersionUID = 1L;
	private EditText etTitle;
	private EditText etTankName;
	private EditText etEmpID;
	private EditText etDesc;
	private Button btReport;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fault_reporting);

		etTitle = (EditText) findViewById(R.id.etFaultTitle);
		etTankName = (EditText) findViewById(R.id.etTankName);
		etEmpID = (EditText) findViewById(R.id.eEmpId);
		etDesc = (EditText) findViewById(R.id.etDesc);
		btReport = (Button) findViewById(R.id.btReport);
		btReport.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

				nameValuePairs.add(new BasicNameValuePair("para",
						"faultReporting"));
				nameValuePairs.add(new BasicNameValuePair("fault_title",
						etTitle.getEditableText().toString()));
				nameValuePairs.add(new BasicNameValuePair("tank_name",
						etTankName.getEditableText().toString()));
				nameValuePairs.add(new BasicNameValuePair("emp_id", etEmpID
						.getEditableText().toString()));
				nameValuePairs.add(new BasicNameValuePair("des", etDesc
						.getEditableText().toString()));
				nameValuePairs.add(new BasicNameValuePair("reviewed", "NO"));


				RestValue restValue=new RestValue();

				Map<String, Object> map = new HashMap<String, Object>();

				for (NameValuePair nameValuePair : nameValuePairs) {
					restValue.put(nameValuePair.getName(), nameValuePair.getValue());

				}

				

				try {

					Intent intent = new Intent(FaultReporting.this,
							SyncService.class);

					
					intent.putExtra("values", (Parcelable)restValue);

					startService(intent);
					
					
					ServiceHelper.showSavingDataMessageDialog(FaultReporting.this);

					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	public void clearAll() {
		etTitle.setText("");
		etTankName.setText("");
		etEmpID.setText("");
		etDesc.setText("");
	}

}
