package com.example.simcareerapp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.simcareerapp.R;
import com.example.simcareerapp.database.SimCareerDatabase;
import com.example.simcareerapp.database.dao.GalleryImageDao;
import com.example.simcareerapp.database.entity.GalleryImageEntity;
import com.example.simcareerapp.utils.SimCareerUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class AlbumActivity extends AppCompatActivity {

    private static final String TAG = "AlbumActivity";

    private SimCareerDatabase database;
    private GalleryImageDao galleryImageDao;

    private GridView gridView;

    private BottomNavigationView bottom_navbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        initViews();
        initDb();

        String album = getIntent().getStringExtra("album");
        Log.d(TAG, "album to display: " + album);

        // get album images from database
        int[] galleryImagesResourceIdArray = galleryImageDao.getAlbumGalleryImages(album);
        Log.d(TAG, "get images resource id array success");

        List<Map<String, Integer>> list = new ArrayList<>();

        // populate the adapter list
        populateListMap(list, galleryImagesResourceIdArray);

        SimpleAdapter adapter = new SimpleAdapter(this,list,R.layout.album_item,new String[]{"image"},new int[]{R.id.album_item_img} );

        this.gridView.setAdapter(adapter);
        this.gridView.setOnItemClickListener(this::albumItemOnClick);

    }

    // share image onclick
    private void albumItemOnClick(AdapterView<?> adapterView, View view, int i, long l) {
        Uri uriToImage = null;

        ImageView imageView = view.findViewById(R.id.album_item_img);
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap imageBitmap = drawable.getBitmap();

        if(imageBitmap == null){
            Log.d(TAG, "album item bitmap null");
        } else {
            try {
                uriToImage = Uri.parse(MediaStore.Images.Media.insertImage(this.getContentResolver(),
                        imageBitmap, null, null));
            } catch (NullPointerException e) {
                Log.d(TAG, "uri parse exception");
            }
        }

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uriToImage);
        shareIntent.setType("image/png");
        this.startActivity(Intent.createChooser(shareIntent, "Condividi l'immagine con"));
    }

    private void populateListMap (List<Map<String, Integer>> list, int[] galleryImagesResourceIdArray){
        int size = galleryImagesResourceIdArray.length;
        Log.d(TAG, "populateListMap size: " + size);
        for(int i=0; i<size; i++){
            Map<String, Integer> map = new HashMap<>();
            map.put("image", galleryImagesResourceIdArray[i]);
            list.add(map);
        }
    }

    private void initViews(){
        this.gridView = this.findViewById(R.id.album_view_grid);

        /********** bottom navbar **************/
        this.bottom_navbar = this.findViewById(R.id.album_bottombar);
        this.bottom_navbar.setOnNavigationItemSelectedListener(this::bottomNavbarItemSelected);
        // set menuitem checked to current activity
        MenuItem menuItem = bottom_navbar.getMenu().getItem(0);
        menuItem.setChecked(true);
    }

    private boolean bottomNavbarItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_bottom_item_profilo:
                Log.d(TAG, "bottom navbar profile selected");
                Intent profiloIntent = new Intent(AlbumActivity.this, ProfileActivity.class);
                this.startActivity(profiloIntent);
                break;
            case R.id.menu_bottom_item_campionati:
                Log.d(TAG, "bottom navbar campionati selected");
                Intent campionatiIntent = new Intent(AlbumActivity.this, HomeActivity.class);
                this.startActivity(campionatiIntent);
                break;
            case R.id.menu_bottom_item_galleria:
                Log.d(TAG, "bottom navbar galleria selected");
                Intent galleriaIntent = new Intent(AlbumActivity.this, GalleriaActivity.class);
                this.startActivity(galleriaIntent);
                break;
        }
        return false;
    }

    private void initDb(){
        Log.d(TAG, "initDb");
        database = SimCareerDatabase.getDatabase(this);
        Log.d(TAG, "getDatabase ok");
        galleryImageDao = database.galleryImageDao();
    }


}
