package org.ralapanawa.mobile.android;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.ralapanawa.mobile.android.R;
import org.ralapanawa.mobile.helpers.RestHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ConfirmWaterSupply extends Activity {

	private EditText etWaterIssuId;
	private EditText etRemark;
	private EditText etEmpID;

	private Button btReport;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.confirm_water_level);

		etWaterIssuId = (EditText) findViewById(R.id.etWaterIssuId);
		etRemark = (EditText) findViewById(R.id.etRemark);
		etEmpID = (EditText) findViewById(R.id.etEmpId);
		btReport = (Button) findViewById(R.id.btApprove);

		btReport.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

				nameValuePairs.add(new BasicNameValuePair("para",
						"confirmSupply"));
				nameValuePairs.add(new BasicNameValuePair("water_issue_id",
						etWaterIssuId.getEditableText().toString()));
				nameValuePairs.add(new BasicNameValuePair("remarks",
						etRemark.getEditableText().toString()));
				nameValuePairs.add(new BasicNameValuePair("emp_id", etEmpID
						.getEditableText().toString()));

				try {
					HttpEntity entity=RestHelper.rest(nameValuePairs);
					String responseString=EntityUtils.toString(entity);
					JSONObject responseJSON = new JSONObject(responseString);
				
					if (responseJSON.getString("status").equalsIgnoreCase("success")){
						AlertDialog alertDialog = new AlertDialog.Builder(
								ConfirmWaterSupply.this).create();

						alertDialog.setTitle(R.string.app_name);
						alertDialog.setMessage("Water Supply Confirmed Successfully!");

						alertDialog.setButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
									}
								});
						alertDialog.show();
						clearAll();
						return;
					}
					else{
						AlertDialog alertDialog2 = new AlertDialog.Builder(
								ConfirmWaterSupply.this).create();

						alertDialog2.setTitle("Ralapanawa");
						alertDialog2.setMessage(responseJSON.getString("status"));

						alertDialog2.setButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
									}
								});
						alertDialog2.show();
						clearAll();
						return;
					}
					
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

	}
	
	public void clearAll(){
		etEmpID.setText("");
		etRemark.setText("");
		etWaterIssuId.setText("");
	}

}
