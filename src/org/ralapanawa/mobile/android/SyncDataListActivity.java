package org.ralapanawa.mobile.android;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ralapanawa.mobile.entity.RestValue;
import org.ralapanawa.mobile.helpers.DataStack;
import org.ralapanawa.mobile.helpers.ObjectSaver;
import org.ralapanawa.mobile.res.ExpandableSyncListChild;
import org.ralapanawa.mobile.res.ExpandableSyncListGroup;
import org.ralapanawa.mobile.res.SyncDataAdapter;
import org.ralapanawa.mobile.res.SyncExpandAdapter;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class SyncDataListActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sync_data_list);

		DataStack rest_VALUES = null;
		try {
			rest_VALUES = ObjectSaver.readObject(SyncDataListActivity.this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (rest_VALUES != null) {
			List<RestValue> watingitems = new ArrayList<RestValue>();

			List<ExpandableSyncListGroup> androidGroups = new ArrayList<ExpandableSyncListGroup>();

			for (Map<String, String> map : rest_VALUES.getMaps()) {

				RestValue restValue = new RestValue();

				restValue.setMap(map);

				Set<String> keySet = map.keySet();

				ExpandableSyncListGroup group = new ExpandableSyncListGroup();

				List<ExpandableSyncListChild> listChilds = new ArrayList<ExpandableSyncListChild>();

				for (String key : keySet) {
					ExpandableSyncListChild child = new ExpandableSyncListChild();
					child.setKey(key);
					child.setValue(map.get(key));
					listChilds.add(child);
				}

				group.setItems(listChilds);
				group.setName(restValue.getMap().get("para"));

				androidGroups.add(group);

				// watingitems.add(restValue);

			}

			ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.listView);

			SyncExpandAdapter dataAdapter = new SyncExpandAdapter(
					SyncDataListActivity.this, androidGroups);
			expandableListView.setAdapter(dataAdapter);

			/**
			 * ListView listView = (ListView) findViewById(R.id.listView1);
			 * 
			 * SyncDataAdapter adapter = new
			 * SyncDataAdapter(SyncDataListActivity.this,
			 * android.R.layout.simple_expandable_list_item_1, watingitems);
			 * 
			 * listView.setAdapter(adapter);
			 * 
			 * 
			 * 
			 * listView.setOnItemClickListener(new
			 * AdapterView.OnItemClickListener() {
			 * 
			 * public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
			 * long arg3) { Toast.makeText(SyncDataListActivity.this, arg2 +" "
			 * +arg3, Toast.LENGTH_SHORT);
			 * 
			 * } });
			 **/
		}
		else{
			Toast.makeText(getApplicationContext(),	"No Data waiting to be Synced", Toast.LENGTH_LONG).show();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_sync_data_list, menu);
		return true;
	}

}
