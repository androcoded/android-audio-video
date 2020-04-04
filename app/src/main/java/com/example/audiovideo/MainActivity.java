package com.example.audiovideo;
import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.VideoView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,MediaPlayer.OnCompletionListener{

    // UI component variable declaration
    private VideoView mVideoView;
    private Button btnPlayVideo, btnPlayMusic, btnPauseMusic;
    private SeekBar seekBarVolume,seekBarAudio;

    private Uri mUri;
    private MediaController mMediaController;
    private MediaPlayer mMusicPlayer;
    private AudioManager mAudioManager;
    private Timer mTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mVideoView = findViewById(R.id.videoView);
        btnPlayVideo = findViewById(R.id.btnPlayVideo);
        btnPlayMusic = findViewById(R.id.btnPlayMusic);
        btnPauseMusic = findViewById(R.id.btnPauseMusic);
        seekBarVolume = findViewById(R.id.seekBarVolume);
        seekBarAudio = findViewById(R.id.seekBarAudio);
        btnPlayVideo.setOnClickListener(MainActivity.this);
        btnPlayMusic.setOnClickListener(MainActivity.this);
        btnPauseMusic.setOnClickListener(MainActivity.this);
        mMediaController = new MediaController(this);
        mMusicPlayer = MediaPlayer.create(this,R.raw.sound);
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        mTimer = new Timer();

        seekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,progress,0);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarAudio.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mMusicPlayer.seekTo(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mMusicPlayer.pause();

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mMusicPlayer.start();

            }
        });
        int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        seekBarVolume.setMax(maxVolume);
        seekBarVolume.setProgress(currentVolume);
        int maxLength = mMusicPlayer.getDuration();
        seekBarAudio.setMax(maxLength);


    }

    @Override
    public void onClick(View btnView) {

        switch (btnView.getId()) {

            case R.id.btnPlayVideo:
                mUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video);
                mVideoView.setVideoURI(mUri);
                mVideoView.setMediaController(mMediaController);
                mMediaController.setAnchorView(mVideoView);
                mVideoView.start();
                break;
            case R.id.btnPlayMusic:
                mMusicPlayer.start();
                mTimer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {

                        seekBarAudio.setProgress(mMusicPlayer.getCurrentPosition());

                    }
                },0,1000);

                break;
            case R.id.btnPauseMusic:
                mMusicPlayer.pause();
                mTimer.cancel();
                break;
        }
    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        mTimer.cancel();
    }
}
