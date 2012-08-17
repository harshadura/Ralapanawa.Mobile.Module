package org.ralapanawa.mobile.helpers;

import org.ralapanawa.mobile.android.R;

import android.content.Context;
import android.content.res.Resources;

public class ProjectSettingsHandler {

	private static String ip;
	
	public ProjectSettingsHandler(Context context){
		Resources resources = context.getResources();
		ip=resources.getString(R.string.ip);
	}
	
	public static String getIp() {
		return ip;
	}

}
