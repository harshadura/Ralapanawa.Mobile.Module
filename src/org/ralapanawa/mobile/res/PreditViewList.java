package org.ralapanawa.mobile.res;

import java.util.List;

import org.ralapanawa.mobile.android.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PreditViewList extends BaseAdapter {

	private List<PredictData> predictDatas;

	private LayoutInflater mInflater;

	public PreditViewList(Context context, List<PredictData> datas) {
		this.mInflater = LayoutInflater.from(context);
		this.predictDatas=datas;
	}

	public int getCount() {
		return predictDatas.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.predic_date, null);
			holder = new ViewHolder();
			holder.tvTitle=(TextView) convertView.findViewById(R.id.title);
			holder.tvRainFail = (TextView) convertView
					.findViewById(R.id.tvrainfall);
			holder.tvVolRainFail = (TextView) convertView
					.findViewById(R.id.tvvrainfail);
			holder.tvPreWaterVol = (TextView) convertView
					.findViewById(R.id.tvprewavol);
			holder.tvPreWaterLevel = (TextView) convertView
					.findViewById(R.id.tvprewalev);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		PredictData predictData = predictDatas.get(position);

		holder.tvTitle.setText("Day "+(position+1) +"  "+ predictData.getDate());
		holder.tvPreWaterLevel.setText(predictData.getPredictWaterLevel());
		holder.tvPreWaterVol.setText(predictData.getPredictWaterVolume());
		holder.tvRainFail.setText(predictData.getRainFail());
		holder.tvVolRainFail.setText(predictData.getRainFailVolume());
		
		Log.i("TAG", predictData+"");

		return convertView;
	}

	static class ViewHolder {
		TextView tvTitle;
		TextView tvRainFail;
		TextView tvVolRainFail;
		TextView tvPreWaterLevel;
		TextView tvPreWaterVol;

	}
}
