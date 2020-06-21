package com.example.simcareerapp.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.simcareerapp.database.entity.GalleryImageEntity;

import java.util.List;

@Dao
public interface GalleryImageDao {
    @Insert
    public void insertImage(GalleryImageEntity imageEntity);

    @Insert
    public void insertMultipleImages(List<GalleryImageEntity> imageEntityList);

    @Update
    public void updateImage(GalleryImageEntity imageEntity);

    @Query("select image_resource_id from gallery_images")
    public int[] getAllGalleryImages();

    @Query("select image_resource_id from gallery_images where image_album = :album")
    public int[] getAlbumGalleryImages(String album);

}
