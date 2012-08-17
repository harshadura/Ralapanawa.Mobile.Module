package org.ralapanawa.mobile.android;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.ralapanawa.mobile.android.R;
import org.ralapanawa.mobile.res.ButtonClass;
import org.ralapanawa.mobile.res.ImageAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

public class AndroidclientActivity extends Activity {
	/** Called when the activity is first created. */

	private List<ButtonClass> gridButton = new ArrayList<ButtonClass>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		updateList();
		GridView gridview = (GridView) findViewById(R.id.gridview);

		gridview.setAdapter(new ImageAdapter(this, gridButton));

		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				gridButton.get(position).clickAction();
				/**
				 * Toast.makeText(AndroidclientActivity.this, "" + position,
				 * Toast.LENGTH_SHORT).show();
				 **/
			}
		});

		this.getResources();

	}

	private void updateList() {

		gridButton.add(new ButtonClass(AndroidclientActivity.this,
				EnterWaterLevelManualy.class, R.string.enter_water_level,
				R.drawable.enter_water_level));
		gridButton.add(new ButtonClass(AndroidclientActivity.this,
				RealtimeMonitor.class, R.string.realtimemonitor,
				R.drawable.recode_water_supply));
		gridButton.add(new ButtonClass(AndroidclientActivity.this,
				CheckWeatherLevel.class, R.string.check_weather_level,
				R.drawable.check_weather));
		gridButton.add(new ButtonClass(AndroidclientActivity.this,
				Last5DaysWeather.class, R.string.comming5daysweather,
				R.drawable.island));
		gridButton.add(new ButtonClass(AndroidclientActivity.this,
				LiveCCtv.class, R.string.live_cctv,
				R.drawable.live_cctv));
		gridButton.add(new ButtonClass(AndroidclientActivity.this,
				CompareLast5DaysWater.class, R.string.comparelast5dayslevels,
				R.drawable.day5));
		gridButton.add(new ButtonClass(AndroidclientActivity.this,
				CheckWaterSupply.class, R.string.check_water_supply,
				R.drawable.check_water_supply));
		gridButton.add(new ButtonClass(AndroidclientActivity.this,
				ConfirmWaterSupply.class, R.string.confirm_water_supply_label,
				R.drawable.confirm_water_supply));
		gridButton.add(new ButtonClass(AndroidclientActivity.this,
				CheckWaterLevel.class, R.string.checkwaterlevel,
				R.drawable.explorer2));
		gridButton.add(new ButtonClass(AndroidclientActivity.this,
				FaultReporting.class, R.string.fault_reporting_supply_label,
				R.drawable.fault_reporting));
		gridButton.add(new ButtonClass(AndroidclientActivity.this,
				PredictionWaterLevel.class, R.string.prediction,
				R.drawable.games));
		gridButton.add(new ButtonClass(AndroidclientActivity.this,
				GPSbasedLength.class, R.string.gpsbasedlength,
				R.drawable.compass));
		gridButton.add(new ButtonClass(AndroidclientActivity.this,
				IrrigationCalc.class, R.string.irricalc,
				R.drawable.task));
		gridButton.add(new ButtonClass(AndroidclientActivity.this,
				ViewMessages.class, R.string.view_message,
				R.drawable.view_message));
		gridButton.add(new ButtonClass(AndroidclientActivity.this,
				SyncDataListActivity.class, R.string.data_sync_title,
				R.drawable.options));
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.option_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection

		switch (item.getItemId()) {
		case R.id.menuitem_aboutus: {
			dialogAboutBuilder();
			return true;
		}

		case R.id.menuitem_rateus: {
			rate();
			return true;
		}

		case R.id.menuitem_feedback: {
			feedback();
			return true;
		}
		
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	public void dialogAboutBuilder() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("About.");
		builder.setMessage(getString(R.string.aboutus));
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	public void rate() {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri
				.parse("market://details?id=org.ralapanawa.mobile.android"));
		startActivity(intent);
	}

	public void feedback() {
		
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);
        alert.setTitle("Feedback");
        alert.setMessage("Enter your Suggestions/Bugs: ");

        alert.setView(input);
        alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            String value = input.getText().toString().trim();
                Toast.makeText(getApplicationContext(), value,
                        Toast.LENGTH_SHORT).show();
                
                submitVote(value); 
                
//                Intent Email = new Intent(Intent.ACTION_SEND);
//                Email.setType("text/email");
//                Email.putExtra(Intent.EXTRA_EMAIL, new String[] { "harshadura@gmail.com" });
//                Email.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
//                Email.putExtra(Intent.EXTRA_TEXT, value + "");
//                startActivity(Intent.createChooser(Email, "Send Feedback:"));

                
   }
    });

alert.setNegativeButton("Cancel",
   new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.cancel();
            }
            });
    alert.show();
	}

	
	private void submitVote(String outcome) {
	    HttpClient client = new DefaultHttpClient();
	    HttpPost post = new HttpPost("https://spreadsheets.google.com/spreadsheet/formResponse?hl=en_US&formkey=dDY4VF9ST0U5V1hMUjlrMDN6ekVrUkE6MQ");

	    List<BasicNameValuePair> results = new ArrayList<BasicNameValuePair>();
	    results.add(new BasicNameValuePair("entry.0.single", outcome));
//	    results.add(new BasicNameValuePair("entry.1.single", outcome));
//	    results.add(new BasicNameValuePair("entry.2.single", cardTwoURL));

	    try {
	        post.setEntity(new UrlEncodedFormEntity(results));
	    } catch (UnsupportedEncodingException e) {
	        // Auto-generated catch block
	        Log.e("YOUR_TAG", "An error has occurred", e);
	    }
	    try {
	        client.execute(post);
	        Toast.makeText(AndroidclientActivity.this, "Thanks! Your Feedback Submitted!" , Toast.LENGTH_LONG).show();
					
	    } catch (ClientProtocolException e) {
	        // Auto-generated catch block
	        Log.e("YOUR_TAG", "client protocol exception", e);
	    } catch (IOException e) {
	        // Auto-generated catch block
	        Log.e("YOUR_TAG", "io exception", e);
	    }
	}

	
	@Override
	public void onBackPressed() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				new ContextThemeWrapper(this, R.style.CustomTheme));
		builder.setTitle(R.string.app_name);
		builder.setMessage("Are you sure want to Logout?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								AndroidclientActivity.this.finish();
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();

	}
}