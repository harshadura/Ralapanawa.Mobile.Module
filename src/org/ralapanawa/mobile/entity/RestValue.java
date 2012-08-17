package org.ralapanawa.mobile.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.NameValuePair;

import android.os.Parcel;
import android.os.Parcelable;

public class RestValue implements Parcelable ,Serializable{

	/**
	 * 
	 */
	private Map<String, String> map;

	public RestValue() {
		setMap(new HashMap<String, String>());
	}

	public RestValue(Parcel in) {
		setMap(new HashMap<String, String>());
		readFromParcel(in);
	}

	@SuppressWarnings("rawtypes")
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public RestValue createFromParcel(Parcel in) {
			return new RestValue(in);
		}

		public RestValue[] newArray(int size) {
			return new RestValue[size];
		}
	};

	
	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(getMap().size());
		for (String s : getMap().keySet()) {
			dest.writeString(s);
			dest.writeString(getMap().get(s));
		}
	}

	public void readFromParcel(Parcel in) {
		int count = in.readInt();
		for (int i = 0; i < count; i++) {
			getMap().put(in.readString(), in.readString());
		}
	}

	public String get(String key) {
		return getMap().get(key);
	}

	public void put(String key, String value) {
		getMap().put(key, value);
	}

	public Map<String, String> getMap() {
		return map;
	}

	public void setMap(Map<String, String> map) {
		this.map = map;
	}

}
