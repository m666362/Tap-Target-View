package org.richit.taptargetview;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.getkeepsafe.taptargetview.TapTargetView;

import dm.audiostreamer.AudioStreamingManager;
import dm.audiostreamer.CurrentSessionCallback;
import dm.audiostreamer.MediaMetaData;

public class MainActivity extends AppCompatActivity implements CurrentSessionCallback, View.OnClickListener {

    public EditText mEditText;
    public Button mEnterButton;
    public Button mPlayButton;
    public Button mPauseButton;
    public Button mStopButton;
    public AudioStreamingManager streamingManager;
    public MediaMetaData mMediaMetaData;
    public boolean firstStart;
    public Toolbar toolbar;
    private String TAG = this.getClass().getSimpleName();
    private int tapTargetCurrentStep;
    public SharedPreferences sharedPreferences;

    public void initObject() {

        mEditText = findViewById( R.id.editTextxmlid );
        mEnterButton = findViewById( R.id.enterbuttonxml );
        mPlayButton = findViewById( R.id.playbuttonxml );
        mPauseButton = findViewById( R.id.pausebuttonxml );
        mStopButton = findViewById( R.id.stopbuttonxml );

        toolbar = findViewById( R.id.toolbarxml );
        setSupportActionBar( toolbar );
        getSupportActionBar().setHomeButtonEnabled( true );
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        getSupportActionBar().setDisplayShowHomeEnabled( true );
        getSupportActionBar().setHomeAsUpIndicator( R.drawable.ic_magnify_white_24dp );
        getSupportActionBar().setDisplayShowTitleEnabled( false );

        streamingManager = new AudioStreamingManager();
        mMediaMetaData = new MediaMetaData();
    }

    public void initForSetOnClick() {

        mEnterButton.setOnClickListener( this );
        mPlayButton.setOnClickListener( this );
        mPauseButton.setOnClickListener( this );
        mStopButton.setOnClickListener( this );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        initObject();
        initForSetOnClick();
        streamingManager = AudioStreamingManager.getInstance( this );
        sharedPreferences = getSharedPreferences( "PREFS", 0 );
        firstStart = sharedPreferences.getBoolean( "first_time_start", true );

        if (firstStart) {

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean( "first_time_start", false ).apply();

        }
        tapTargetViews();

    }

    @Override
    public void onStart() {
        super.onStart();
        if (streamingManager != null) {
            streamingManager.subscribesCallBack( this );
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
        switch (v.getId()) {

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

    public void tapTargetViews() {

        toolbar.inflateMenu( R.menu.main_menu );

        new TapTargetSequence( this )
                .targets(
                        TapTarget
                                .forToolbarNavigationIcon( toolbar, "Search here" )
                                .tintTarget( true ),

                        TapTarget
                                .forToolbarMenuItem( toolbar, R.id.dotmenuxml, "haskfds" ),

                        TapTarget
                                .forView( mPlayButton, "Click here to play" )
                                .tintTarget( true ),

                        TapTarget.forView
                                ( mStopButton, "Click here to stop" )

                ).continueOnCancel( true ).considerOuterCircleCanceled( true ).listener( new TapTargetSequence.Listener() {
            @Override
            public void onSequenceFinish() {
                Log.d( TAG, "onSequenceFinish: " );
            }

            @Override
            public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {
                tapTargetCurrentStep++;
                Log.d( TAG, "onSequenceStep: " + tapTargetCurrentStep );
            }

            @Override
            public void onSequenceCanceled(TapTarget lastTarget) {
                Log.d( TAG, "onSequenceCanceled: " + lastTarget );
            }
        } ).start();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.main_menu, menu );
        return super.onCreateOptionsMenu( menu );
    }
}
