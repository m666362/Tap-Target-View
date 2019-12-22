package org.richit.taptargetview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;

import java.security.PublicKey;

import dm.audiostreamer.AudioStreamingManager;
import dm.audiostreamer.CurrentSessionCallback;
import dm.audiostreamer.MediaMetaData;

public class MainActivity extends AppCompatActivity implements CurrentSessionCallback, View.OnClickListener{

    public EditText mEditText;
    public Button mEnterButton ;
    public Button mPlayButton;
    public Button mPauseButton ;
    public Button mStopButton;
    public AudioStreamingManager streamingManager;
    public MediaMetaData mMediaMetaData;
    public int flag = 1;
    public boolean firstStart;

    public void initObject(){

        mEditText = findViewById( R.id.editTextxmlid );
        mEnterButton = findViewById( R.id.enterbuttonxml );
        mPlayButton = findViewById( R.id.playbuttonxml );
        mPauseButton = findViewById( R.id.pausebuttonxml );
        mStopButton = findViewById( R.id.stopbuttonxml );
        streamingManager = new AudioStreamingManager();
        mMediaMetaData = new MediaMetaData();
    }

    public void initForSetOnClick(){

        mEnterButton.setOnClickListener( this );
        mPlayButton.setOnClickListener( this );
        mPauseButton.setOnClickListener( this );
        mStopButton.setOnClickListener( this );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        SharedPreferences setting = getSharedPreferences( "PREFS", 0 );
        firstStart = setting.getBoolean( "first_time_start", false );

        if(firstStart){

            SharedPreferences.Editor editor = setting.edit();
            editor.putBoolean( "first_time_start", true );


        }

        TapTargetView.showFor(this,
                TapTarget.forView(findViewById(R.id.dotmainxml),
                        "This is a target", "We have the best targets, believe me")
        .tintTarget( false )
        .targetCircleColor( R.color.md_blue_grey_500_25 ));

        initObject();
        initForSetOnClick();
        streamingManager = AudioStreamingManager.getInstance( this );

    }

    @Override
    public void onStart() {
        super.onStart();
        if (streamingManager != null) {
            streamingManager.subscribesCallBack(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (streamingManager != null) {
            streamingManager.unSubscribeCallBack();
        }
    }

    @Override
    public void updatePlaybackState(int state) {
        switch (state) {
            case PlaybackStateCompat.STATE_PLAYING:
                break;
            case PlaybackStateCompat.STATE_PAUSED:
                break;
            case PlaybackStateCompat.STATE_NONE:
                break;
            case PlaybackStateCompat.STATE_STOPPED:
                break;
            case PlaybackStateCompat.STATE_BUFFERING:
                break;
        }
    }

    @Override
    public void playSongComplete() {
    }

    @Override
    public void currentSeekBarPosition(int progress) {
    }

    @Override
    public void playCurrent(int indexP, MediaMetaData currentAudio) {

    }

    @Override
    public void playNext(int indexP, MediaMetaData CurrentAudio) {
    }

    @Override
    public void playPrevious(int indexP, MediaMetaData currentAudio) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.enterbuttonxml:
                mMediaMetaData.setMediaUrl( mEditText.getText().toString() );
                break;

            case R.id.playbuttonxml:
                streamingManager.onPlay( mMediaMetaData );
                break;

            case R.id.pausebuttonxml:
                streamingManager.onPause();
                break;

            case R.id.stopbuttonxml:
                streamingManager.onStop();
                break;
        }
    }

}
