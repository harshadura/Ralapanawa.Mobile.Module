package org.ralapanawa.mobile.res;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.ralapanawa.mobile.android.R;
import org.ralapanawa.mobile.res.PreditViewList.ViewHolder;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CompareWeatherList extends BaseAdapter {

	private List<WetherData> wdDatas = new ArrayList<WetherData>();
	private Context context;
	
	private LayoutInflater mInflater;

	public CompareWeatherList(List<WetherData> wdDatas, Context context) {
		super();
		this.wdDatas = wdDatas;
		this.context = context;
		this.mInflater=LayoutInflater.from(context);
	}

	public int getCount() {
		return wdDatas.size();
	}

	public Object getItem(int arg0) {
		return arg0;
	}

	public long getItemId(int arg0) {
		return arg0;
	}
	
	private Drawable LoadImageFromWeb(String url) {
		try {
			InputStream is = (InputStream) new URL(url).getContent();
			Drawable d = Drawable.createFromStream(is, "src name");
			return d;
		} catch (Exception e) {
			System.out.println("Exc=" + e);
			return null;
		}
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.wether_data, null);
			holder = new ViewHolder();
			
			holder.temp_C=(TextView) convertView.findViewById(R.id.tvtemp);
			holder.weatherDesc=(TextView) convertView.findViewById(R.id.tvtemp);
			holder.title=(TextView) convertView.findViewById(R.id.title);
			holder.imIcon=(ImageView) convertView.findViewById(R.id.imW);
			

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
			
		}

		WetherData wetherData = wdDatas.get(position);

		holder.temp_C.setText(wetherData.getTemp());
		holder.title.setText(wetherData.getObTime());
		holder.imIcon.setImageDrawable(LoadImageFromWeb(wetherData.getImageLoc()));

		Log.i("TAG", wetherData + "");

		return convertView;
	}

	static class ViewHolder {

		public TextView title;
		private TextView observation_time;
		private TextView temp_C;
		private TextView temp_F;
		private TextView weatherDesc;
		private TextView windspeedMiles;
		private TextView windspeedKmph;
		private TextView winddirDegree;
		private TextView winddir16Point;
		private TextView precipMM;
		private TextView humidity;
		private TextView visibility;
		private TextView pressure;
		private TextView cloudcover;
		
		private ImageView imIcon;
	}

}
