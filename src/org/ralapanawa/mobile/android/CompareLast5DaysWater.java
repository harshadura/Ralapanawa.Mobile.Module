package org.ralapanawa.mobile.android;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.achartengine.GraphicalActivity;
import org.achartengine.chart.BarChart;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.ralapanawa.mobile.helpers.ProjectSettingsHandler;

public class CompareLast5DaysWater extends Activity {

	private static final String SOAP_ACTION = "urn:Ralamobile#graphfunc";
	private static final String METHOD_NAME = "graphfunc";
	private static final String NAMESPACE = "urn:Ralamobile";

	private static final String URL = " http://" + ProjectSettingsHandler.getIp()
			+ "/Ralapanawa_SOAP/Ralapanawalogin.php";

	private boolean errorHappen = false;
	private Map<String, Double> responeObjFromWS = null;
	private ProgressDialog progressBar;
	private int progressBarStatus = 0;
	private Handler progressBarHandler = new Handler();
	private EditText etTankId;

	public static final String CHART = "chart";
	public static final String TITLE = "title";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.compare_water_level);
		etTankId = (EditText) findViewById(R.id.etTankId);
		Button btnStartProgress = (Button) findViewById(R.id.btLoad);
		btnStartProgress.setOnClickListener(buttonSaveOnClickListener);
	}

	public Map<String, Double> connectSOAP(String tankid) {
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
		request.addProperty("date1", tankid);

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

				for (int i = 1; i < 6; i++) {
					
					String value = garphOb.getProperty("Day" + i).toString();
					String key = garphOb.getProperty("Date" + i).toString();

					Log.v("TAG", value + " " + key);

					double parseDouble = Double.parseDouble(value.replace(' ',
							'.'));
					Log.v("TAG", parseDouble + " " + key);
					map.put(key, parseDouble);
				}

				Log.v("TAG", encodedImage);
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorHappen = true;
			progressBarStatus = progressBarStatus + 100;
			return null;
		}
		
		progressBarStatus = progressBarStatus + 100;
		return map;
	}

	public static final Intent getBarChartIntent(Context context,
			XYMultipleSeriesDataset dataset, XYMultipleSeriesRenderer renderer,
			Type type) {
		return getBarChartIntent(context, dataset, renderer, type, "");
	}

	public static final Intent getBarChartIntent(Context context,
			XYMultipleSeriesDataset dataset, XYMultipleSeriesRenderer renderer,
			Type type, String activityTitle) {
		checkParameters(dataset, renderer);
		Intent intent = new Intent(context, GraphicalActivity.class);
		BarChart chart = new BarChart(dataset, renderer, type);
		intent.putExtra(CHART, chart);
		intent.putExtra(TITLE, activityTitle);
		return intent;
	}

	private static void checkParameters(XYMultipleSeriesDataset dataset,
			XYMultipleSeriesRenderer renderer) {

		Log.i("TAG", dataset + "");
		Log.i("TAG", renderer + "");
		Log.i("TAG",
				dataset.getSeriesCount() + " "
						+ renderer.getSeriesRendererCount());
		if (dataset == null
				|| renderer == null
				|| dataset.getSeriesCount() != renderer
						.getSeriesRendererCount()) {
			throw new IllegalArgumentException(
					"Dataset and renderer should be not null and should have the same number of series");
		}
	}

	private XYMultipleSeriesDataset getBarDemoDataset(Map<String, Double> map) {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();

		Set<String> keySet = map.keySet();
		int i = 1;
		for (String key : keySet) {
			Double value = map.get(key);
			Log.v("TAG", key + "  " + value + "FSTS");
			XYSeries series = new XYSeries(key);

			series.add(i, value);
			dataset.addSeries(series);
			i++;
		}

		return dataset;
	}

	public XYMultipleSeriesRenderer getBarDemoRenderer(Set<String> set) {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		renderer.setAxisTitleTextSize(16);
		renderer.setChartTitleTextSize(20);
		renderer.setLabelsTextSize(15);
		renderer.setLegendTextSize(15);
		renderer.setMargins(new int[] { 20, 30, 15, 0 });
		renderer.setBarSpacing(-0.7);

		for (int i = 0; i < set.size(); i++) {
			SimpleSeriesRenderer r = new SimpleSeriesRenderer();

			r.setColor(getRandomColor());
			renderer.addSeriesRenderer(r);
		}
		return renderer;
	}

	private void setChartSettings(XYMultipleSeriesRenderer renderer) {
		renderer.setChartTitle("Last Week Water Levels");
		renderer.setXTitle("Date");
		renderer.setYTitle("Water Level");
		renderer.setXAxisMin(0);
		renderer.setYAxisMin(0);
	}

	private Random numGen = new Random();

	protected int getRandomColor() {
		int red = numGen.nextInt(256);
		red = red < 150 ? red + 100 : red;
		int green = numGen.nextInt(256);
		green = green < 150 ? green + 100 : green;
		int blue = numGen.nextInt(256);
		red = blue < 150 ? blue + 100 : blue;
		return Color.rgb(red, green, blue);
	}

	Button.OnClickListener buttonSaveOnClickListener = new Button.OnClickListener() {
		public void onClick(View v) {
			progressBar = new ProgressDialog(v.getContext());
			progressBar.setCancelable(true);
			progressBar.setMessage("Generating the Graph...");
			progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressBar.setProgress(0);
			progressBar.setMax(100);
			progressBar.show();
			progressBarStatus = 0;
			errorHappen = false;
			
			new Thread(new Runnable() {
				public void run() {
					while (progressBarStatus < 100) {

						String tankid = etTankId.getText().toString();
						responeObjFromWS = connectSOAP(tankid);

						try {
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
							
						runOnUiThread(new Runnable() {
						public void run() {
						
							if (errorHappen == false){
								XYMultipleSeriesDataset dataset = getBarDemoDataset(responeObjFromWS);
								XYMultipleSeriesRenderer renderer = getBarDemoRenderer(responeObjFromWS
										.keySet());
								setChartSettings(renderer);
								Intent intent = getBarChartIntent(
										CompareLast5DaysWater.this, dataset, renderer,
										Type.DEFAULT);
								CompareLast5DaysWater.this.startActivity(intent);
							}
							else{
		    						AlertDialog alertDialog = new AlertDialog.Builder(
		    								CompareLast5DaysWater.this).create();

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
