
package com.example.task_eternals_android;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ClipData;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.task_eternals_android.Adapter.ImageAdapter;
import com.example.task_eternals_android.Model.ImageModel;
import com.example.task_eternals_android.Model.TaskModel;
import com.example.task_eternals_android.Utilities.DBHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class TaskDetailsActivity extends AppCompatActivity {

    private Button playbtn, recordbtn, pausebtn, stopbtn;
    private TextView categoryTv,titleTv,descriptionTv,dueDateTv;
    SeekBar seekbar;
    private static int MICROPHONE_PERMISSION_CODE = 1;
    private boolean isRecording;
    ImageView capturePhoto;
    ActivityResultLauncher<String> mGetContent;
    ActivityResultLauncher<Intent> mGetPermission;
    ImageModel newImage;
    List<ImageModel> images;
    ArrayList<Bitmap> imgLists;
    DBHelper db;
    int img_total = 0;
    ImageAdapter adapter;
    int PICK_IMAGE_MULTIPLE = 1;
    Uri imageURI;
    private Bitmap imagetoStore;


    private static final int[] sampleimages = {R.drawable.chinesenoodles, R.drawable.chinesenoodles2, R.drawable.chinesenoodles3, R.drawable.chinesenoodles4, R.drawable.chineserice, R.drawable.chinesenoodles3, R.drawable.chineserice2};


    TaskModel task;
    MediaPlayer mediaPlayer;
    MediaRecorder mediaRecorder;
    File file;
    GifImageView recordingAnim;

    Handler handler = new Handler();
    Runnable runnable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);


        categoryTv = findViewById(R.id.categoryvalue);
        titleTv = findViewById(R.id.tasktitlevalue);
        dueDateTv = findViewById(R.id.duedatevalue);
        descriptionTv = findViewById(R.id.taskdescriptiontxt);
        seekbar = findViewById(R.id.seekbar);
        recordbtn = findViewById(R.id.recordbtn);
        playbtn = findViewById(R.id.playbtn);
        recordingAnim = findViewById(R.id.recordinggif);
        capturePhoto = findViewById(R.id.attachimages);
        //seekbar.setProgress(0);
        //seekbar.setMax(mediaPlayer.getDuration());
        seekbar.setVisibility(View.GONE);
        recordingAnim.setVisibility(View.GONE);
        mediaPlayer = new MediaPlayer();
        mediaRecorder = new MediaRecorder();

        task = getIntent().getParcelableExtra("task");
        System.out.println("Category in 3rd screen: " + task.getTitle());
        categoryTv.setText(getIntent().getStringExtra("category-name"));
        titleTv.setText(task.getTitle());
        descriptionTv.setText(task.getDescription());
        dueDateTv.setText(task.getDate());
        imgLists = new ArrayList<>();

        db = new DBHelper(TaskDetailsActivity.this);
        images = new ArrayList<>();
        newImage = new ImageModel();
        adapter = new ImageAdapter(db,TaskDetailsActivity.this);
        images = db.getAllImages();
        Collections.reverse(images);
        adapter.setImages(images);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        RecyclerView recyclerView = findViewById(R.id.imagesRV);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File fileDirectory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);

        //Uri fileUri = FileProvider.getUriForFile(this, "com.example.task_eternals_android.fileprovider", fileDirectory);
//        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
//            @Override
//            public void onActivityResult(Uri result) {
//                if (result != null) {
//                    imageURI = result;
//                    //TaskDetailsActivity.this.grantUriPermission(getPackageName(), result, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION );
//                    //TaskDetailsActivity.this.getContentResolver().takePersistableUriPermission(result, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//                    System.out.println("Result after image pick: " + result.getPath() + result.toString());
//                    newImage.setImageFilePath(result.toString());
//                    newImage.setCategoryTaskID(task.getTitle());
//                    images.add(newImage);
//                    db.insertImage(newImage);
//                }
//            }
//        });
//        adapter.notifyDataSetChanged();
//
//        mGetPermission = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
//            @Override
//            public void onActivityResult(ActivityResult result) {
//                if (result.getResultCode() == TaskDetailsActivity.RESULT_OK) {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                        TaskDetailsActivity.this.getContentResolver().takePersistableUriPermission(imageURI, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//                    }
//                }
//            }
//        });

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
        capturePhoto.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                takePermission();
//                ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
//                File fileDirectory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//
//                Uri fileUri = FileProvider.getUriForFile(TaskDetailsActivity.this, "com.example.task_eternals_android.fileprovider", fileDirectory);
//                //mGetContent.launch("image/*");
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                intent.putExtra(Intent.EXTRA_STREAM, fileUri);
//                intent.setType("image/png");
//                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                startActivity(intent);
                Intent intent = new Intent();
                // setting type to select to be image
                intent.setType("image/*");
                // allowing multiple image to be selected
                //intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                if (Build.VERSION.SDK_INT <19) {
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                } else {
                    intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                }

                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_MULTIPLE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && null != data) {
            if (Build.VERSION.SDK_INT < 19) {
                imageURI = data.getData();
            } else {
                imageURI = data.getData();
                int takeFlags = data.getFlags();
                takeFlags &= (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                try {
                    this.getContentResolver().takePersistableUriPermission(imageURI, takeFlags);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }
                newImage.setImageFilePath(String.valueOf(imageURI));
                newImage.setCategoryTaskID(String.valueOf(categoryTv.getText()));
                images.add(newImage);
                db.insertImage(newImage);
                adapter.setImages(images);

            //et_gallery.setText(img_total+" photos selected");
            adapter.notifyDataSetChanged();
        } else {
            // show this if no image is selected
            Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
    }

    private void takePermissions() {
        if(Build.VERSION.SDK_INT==Build.VERSION_CODES.R) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s",getApplicationContext().getPackageName())));
                mGetPermission.launch(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
        }
    }

    private boolean isPermissionGranted() {
        if(Build.VERSION.SDK_INT==Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        }
        else {
            int readExternalStoragePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            return readExternalStoragePermission==PackageManager.PERMISSION_GRANTED;
        }
    }

    public void takePermission() {
        if(isPermissionGranted()) {
            Toast.makeText(this, "Permission there", Toast.LENGTH_SHORT).show();
        } else {
            takePermissions();
        }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 101: if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }
            else {
                Toast.makeText(this, "No permissions granted", Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }

    public static String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos = new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        if(temp==null)
        {
            return null;
        }
        else
            return temp;
    }

    public static Bitmap StringToBitMap(String encodedString){
        try {
            byte[] encodeByte = Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            if(bitmap==null)
            {
                return null;
            }
            else
            {
                return bitmap;
            }

        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }


}