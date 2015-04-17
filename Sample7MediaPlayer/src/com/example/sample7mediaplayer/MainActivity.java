package com.example.sample7mediaplayer;

import java.io.IOException;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;


public class MainActivity extends ActionBarActivity {

	MediaPlayer mPlayer;
	PlayState mState = PlayState.IDLE;
	enum PlayState {
		IDLE,
		INITIIALIZED,
		PREPARED,
		STARTED,
		PAUSED,
		STOPPED,
		END
	}
	
	Handler mHandler = new Handler(Looper.getMainLooper());
	SeekBar progressView;
	boolean isTouchProgress = false;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPlayer = MediaPlayer.create(this, R.raw.winter_blues);
        mState = PlayState.PREPARED;
        progressView = (SeekBar)findViewById(R.id.seek_progress);
        int duration = mPlayer.getDuration();
        progressView.setMax(duration);
        progressView.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			int progress;
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				if (progress != -1) {
					if (mState == PlayState.STARTED) {
						mPlayer.seekTo(progress);
					} 
				}
				isTouchProgress = false;
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				progress = -1;
				isTouchProgress = true;
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if (fromUser) {
					this.progress = progress;
				}
			}
		});
        Button btn = (Button)findViewById(R.id.btn_play);
        btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				start();
			}
		});
        
        btn = (Button)findViewById(R.id.btn_pause);
        btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pause();
			}
		});
        
        btn = (Button)findViewById(R.id.btn_stop);
        btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				stop();
			}
		});
    }

    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	if (mPlayer != null) {
    		mPlayer.release();
    		mPlayer = null;
    		mState = PlayState.END;
    	}
    }

    Runnable updateRunnable = new Runnable() {
		
		@Override
		public void run() {
			if (mState == PlayState.STARTED) {
				if (!isTouchProgress) {
					int position = mPlayer.getCurrentPosition();
					progressView.setProgress(position);
				}
				mHandler.postDelayed(this, 100);
			}
		}
	};
    private void start() {
    	if (mState == PlayState.INITIIALIZED || mState == PlayState.STOPPED) {
    		try {
				mPlayer.prepare();
				mState = PlayState.PREPARED;
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    	
    	if (mState == PlayState.PREPARED || mState == PlayState.PAUSED) {
    		mPlayer.seekTo(progressView.getProgress());
    		mPlayer.start();
    		mState = PlayState.STARTED;
    		mHandler.post(updateRunnable);
    	}
    }
    
    private void pause() {
    	if (mState == PlayState.STARTED) {
    		mPlayer.pause();
    		mState = PlayState.PAUSED;
    	}
    }
    
    private void stop() {
    	if (mState == PlayState.STARTED || mState == PlayState.PAUSED) {
    		mPlayer.stop();
    		mState = PlayState.STOPPED;
    		progressView.setProgress(0);
    	}
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}