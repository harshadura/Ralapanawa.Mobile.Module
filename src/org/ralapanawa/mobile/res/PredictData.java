package org.ralapanawa.mobile.res;

public class PredictData {

	private String RainFail;
	private String RainFailVolume;
	private String PredictWaterLevel;
	private String PredictWaterVolume;
	private String Date;
	

	public PredictData() {
		
	}

	public PredictData(String rainFail, String rainFailVolume,
			String predictWaterLevel, String predictWaterVolume) {
		super();
		RainFail = rainFail;
		RainFailVolume = rainFailVolume;
		PredictWaterLevel = predictWaterLevel;
		PredictWaterVolume = predictWaterVolume;
	}

	public String getRainFail() {
		return RainFail;
	}

	public void setRainFail(String rainFail) {
		RainFail = rainFail;
	}

	public String getRainFailVolume() {
		return RainFailVolume;
	}

	public void setRainFailVolume(String rainFailVolume) {
		RainFailVolume = rainFailVolume;
	}

	public String getPredictWaterLevel() {
		return PredictWaterLevel;
	}

	public void setPredictWaterLevel(String predictWaterLevel) {
		PredictWaterLevel = predictWaterLevel;
	}

	public String getPredictWaterVolume() {
		return PredictWaterVolume;
	}

	public void setPredictWaterVolume(String predictWaterVolume) {
		PredictWaterVolume = predictWaterVolume;
	}

	public String getDate() {
		return Date;
	}

	public void setDate(String date) {
		Date = date;
	}

}
