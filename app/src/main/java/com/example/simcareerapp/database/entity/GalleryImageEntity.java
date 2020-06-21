package com.example.simcareerapp.database.entity;

import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "gallery_images")
public class GalleryImageEntity {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int image_id;

    private int image_resource_id;

    private String image_album;

    public GalleryImageEntity(int image_resource_id, String image_album){
        this.image_resource_id = image_resource_id;
        this.image_album = image_album;
    }


    public int getImage_id() {
        return image_id;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }

    public int getImage_resource_id() {
        return image_resource_id;
    }

    public void setImage_resource_id(int image_resource_id) {
        this.image_resource_id = image_resource_id;
    }

    public String getImage_album() {
        return image_album;
    }

    public void setImage_album(String image_album) {
        this.image_album = image_album;
    }
}
