package org.ralapanawa.mobile.res;

import java.util.List;

import org.ralapanawa.mobile.entity.RestValue;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SyncDataAdapter extends ArrayAdapter<RestValue> {
	private List<RestValue> objects;

	public SyncDataAdapter(Context context, int textViewResourceId,
			List<RestValue> objects) {
		super(context, textViewResourceId, objects);
		this.objects=objects;
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView view = (TextView) super.getView(position, convertView, parent);
				
		view.setText(objects.get(position).get("para"));
		return view;
	}
	
	
	

}
