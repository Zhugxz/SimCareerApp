package com.example.simcareerapp.database;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.example.simcareerapp.R;
import com.example.simcareerapp.database.dao.GalleryImageDao;
import com.example.simcareerapp.database.dao.UserDao;
import com.example.simcareerapp.database.entity.GalleryImageEntity;
import com.example.simcareerapp.database.entity.UserEntity;
import com.example.simcareerapp.utils.SimCareerUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {UserEntity.class, GalleryImageEntity.class}, version = 1)
public abstract class SimCareerDatabase extends RoomDatabase {

    private static final String TAG = "SimCareerDatabase";

    public abstract UserDao userDao();
    public abstract GalleryImageDao galleryImageDao();

    private static SimCareerDatabase INSTANCE;

    public synchronized static SimCareerDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = buildDatabase(context);
        }
        return INSTANCE;
    }

    private static SimCareerDatabase buildDatabase(final Context context) {
        return Room.databaseBuilder(context.getApplicationContext(),
                SimCareerDatabase.class, "simcareerDB.db").allowMainThreadQueries()
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);

                        Executors.newSingleThreadScheduledExecutor().execute(()->{
                            INSTANCE.galleryImageDao().insertMultipleImages(populateData(context));
                        });
                    }
                })
                .build();
    }

    public static List<GalleryImageEntity> populateData(Context context) {
        Log.d(TAG, "onCreate start populate db with images");
        List<GalleryImageEntity> imageEntityList = new ArrayList<GalleryImageEntity>();

        addImage(R.drawable.gte1, "GTE1", imageEntityList);
        addImage(R.drawable.gte2, "GTE1", imageEntityList);
        addImage(R.drawable.gte3, "GTE1", imageEntityList);
        addImage(R.drawable.gte4, "GTE1", imageEntityList);
        addImage(R.drawable.gte5, "GTE1", imageEntityList);
        addImage(R.drawable.gte6, "GTE1", imageEntityList);
        addImage(R.drawable.gte7, "GTE1", imageEntityList);
        addImage(R.drawable.gte8, "GTE1", imageEntityList);
        addImage(R.drawable.gte9, "GTE1", imageEntityList);
        addImage(R.drawable.gte10, "GTE1", imageEntityList);
        addImage(R.drawable.gte11, "GTE1", imageEntityList);
        addImage(R.drawable.gte12, "GTE1", imageEntityList);
        addImage(R.drawable.gte14, "GTE1", imageEntityList);
        addImage(R.drawable.gte15, "GTE1", imageEntityList);
        addImage(R.drawable.gte16, "GTE1", imageEntityList);
        addImage(R.drawable.gte18, "GTE1", imageEntityList);
        addImage(R.drawable.gte19, "GTE1", imageEntityList);
        addImage(R.drawable.gte20, "GTE1", imageEntityList);

        addImage(R.drawable.gte_1, "GTE2", imageEntityList);
        addImage(R.drawable.gte_2, "GTE2", imageEntityList);
        addImage(R.drawable.gte_3, "GTE2", imageEntityList);
        addImage(R.drawable.gte_4, "GTE2", imageEntityList);
        addImage(R.drawable.gte_5, "GTE2", imageEntityList);
        addImage(R.drawable.gte_6, "GTE2", imageEntityList);
        addImage(R.drawable.gte_7, "GTE2", imageEntityList);
        addImage(R.drawable.gte_8, "GTE2", imageEntityList);
        addImage(R.drawable.gte_9, "GTE2", imageEntityList);
        addImage(R.drawable.gte_10, "GTE2", imageEntityList);
        addImage(R.drawable.gte_11, "GTE2", imageEntityList);
        addImage(R.drawable.gte_12, "GTE2", imageEntityList);
        addImage(R.drawable.gte_13, "GTE2", imageEntityList);
        addImage(R.drawable.gte_14, "GTE2", imageEntityList);
        addImage(R.drawable.gte_15, "GTE2", imageEntityList);
        addImage(R.drawable.gte_16, "GTE2", imageEntityList);

        Log.d(TAG, "onCreate end populate db with images");
        Log.d(TAG, "imageEntityList size: " + imageEntityList.size());

        return imageEntityList;
    }

    private static void addImage(int id, String album, List<GalleryImageEntity> imageEntityList){
        GalleryImageEntity galleryImageEntity = new GalleryImageEntity(id, album);
        imageEntityList.add(galleryImageEntity);
    }




}

