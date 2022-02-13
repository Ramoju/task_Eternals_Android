
package com.example.task_eternals_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.task_eternals_android.Adapter.ImageAdapter;

import java.io.File;
import java.io.IOException;

import pl.droidsonroids.gif.GifImageView;

public class TaskDetailsActivity extends AppCompatActivity {

    private Button playbtn, recordbtn, pausebtn, stopbtn;
    SeekBar seekbar;
    private static int MICROPHONE_PERMISSION_CODE = 1;
    private boolean isRecording;

    private static final int[] images = {R.drawable.chinesenoodles, R.drawable.chinesenoodles2, R.drawable.chinesenoodles3, R.drawable.chinesenoodles4, R.drawable.chineserice, R.drawable.chinesenoodles3, R.drawable.chineserice2};


    MediaPlayer mediaPlayer;
    MediaRecorder mediaRecorder;
    File file;
    GifImageView recordingAnim;

    Handler handler = new Handler();
    Runnable runnable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        System.out.println("third screen");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        System.out.println("third screen after view on create");

        seekbar = findViewById(R.id.seekbar);
        recordbtn = findViewById(R.id.recordbtn);
        playbtn = findViewById(R.id.playbtn);
        recordingAnim = findViewById(R.id.recordinggif);
        //seekbar.setProgress(0);
        //seekbar.setMax(mediaPlayer.getDuration());
        seekbar.setVisibility(View.GONE);
        recordingAnim.setVisibility(View.GONE);
        mediaPlayer = new MediaPlayer();
        mediaRecorder = new MediaRecorder();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        RecyclerView recyclerView = findViewById(R.id.imagesRV);
        recyclerView.setLayoutManager(layoutManager);
        ImageAdapter adapter = new ImageAdapter(this, images);
        recyclerView.setAdapter(adapter);

        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File fileDirectory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);

        //if microphone is on then get the permission
        if (isMicrophoneOn()) {
            getMicrophonePermission();
        }

        runnable = new Runnable() {
            @Override
            public void run() {
                seekbar.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed(runnable, 1000);
            }
        };
        handler.postDelayed(runnable,1000);

        recordbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isRecording) {
                    try {
                        isRecording = true;
                        recordingAnim.setVisibility(View.VISIBLE);
                        //mediaRecorder = new MediaRecorder();
                        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                        //Here we are getting the filepath
                        file = new File(fileDirectory, "RecordedFile" + ".mp3");
                        mediaRecorder.setOutputFile(file.getPath());
                        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                        //Toast.makeText(this, "Recording started", Toast.LENGTH_LONG).show();
                        recordbtn.setBackgroundResource(R.drawable.ic_baseline_mic_off_24);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    isRecording = false;
                    mediaRecorder.stop();
                    mediaRecorder.release();
                    mediaRecorder = null;
                    recordingAnim.setVisibility(View.GONE);
                    recordbtn.setBackgroundResource(R.drawable.ic_baseline_mic_24);
                }
            }
        });

        playbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seekbar.setVisibility(View.VISIBLE);
                if (!mediaPlayer.isPlaying()) {
                    try {
                        mediaPlayer.setDataSource(file.getPath());
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                        seekbar.setMax(mediaPlayer.getDuration());
                        handler.postDelayed(runnable,0);
                        playbtn.setBackgroundResource(R.drawable.ic_baseline_pause_24);
                        //Toast.makeText(this, "playing the recording", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else {
                    mediaPlayer.pause();
                    handler.removeCallbacks(runnable);
                    playbtn.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
                }
            }
        });

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                playbtn.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
                seekbar.setVisibility(View.GONE);
            }
        });
    }

    //check whether the microphone is on or not
    private boolean isMicrophoneOn(){
        if(this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE)){
            return true;
        }
        else{
            return false;
        }
    }

    //get the permission
    private void getMicrophonePermission(){
        //if the user permission got denied then we ask to give the permission
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},MICROPHONE_PERMISSION_CODE);
        }
    }
}