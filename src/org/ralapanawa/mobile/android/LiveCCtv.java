package org.ralapanawa.mobile.android;

import org.ralapanawa.mobile.android.R;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class LiveCCtv extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.live_cctv);
		
		VideoView videoView = (VideoView) findViewById(R.id.videoView1);
	    Uri video = Uri.parse("http://175.157.81.117:5544/");
	    videoView.setVideoURI(video);
	    videoView.start();

//		mVideoView = (VideoView) findViewById(R.id.live_cctv);
//		mVideoView.setVideoPath("rtsp://175.157.81.117:5544/");
//		mVideoView.setMediaController(new MediaController(this));
//		mVideoView.start();
	}	
}
