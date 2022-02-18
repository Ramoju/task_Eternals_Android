package com.example.task_eternals_android.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task_eternals_android.Model.ImageModel;
import com.example.task_eternals_android.R;
import com.example.task_eternals_android.TaskDetailsActivity;
import com.example.task_eternals_android.Utilities.DBHelper;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;


public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder>{

    private List<ImageModel> images;
    private TaskDetailsActivity activity;
    private DBHelper myDB;

    public ImageAdapter(DBHelper myDB, TaskDetailsActivity activity){
        this.activity = activity;
        this.myDB = myDB;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //final int takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
        //activity.getContentResolver().takePersistableUriPermission(Uri.parse(images.get(position).getImageFilePath()), takeFlags);
//        try {
//            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), Uri.parse(images.get(position).getImageFilePath()));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        //holder.image.setImageURI(Uri.parse(images.get(position).getImageFilePath()));
//        Picasso.get()
//                .load(images.get(position).getImageFilePath())
//                .resize(120,120)
//                .into(holder.image);

//        Glide.with(activity)
//                .load(images.get(position).getImageFilePath())
//                .into(holder.image);
//        try {
//            InputStream inputStream = getContext().getContentResolver().openInputStream(Uri.parse(images.get(position).getImageFilePath()));
//            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//            holder.image.setImageBitmap(bitmap);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
      try {
            holder.image.setImageURI(Uri.parse((images.get(position).getImageFilePath())));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setImages(List<ImageModel> mList){
        this.images = mList;
        notifyDataSetChanged();
    }

        public Context getContext(){
            return activity;
        }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imagecard);
        }
    }
}
