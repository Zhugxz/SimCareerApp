package com.example.simcareerapp.ui.activity;

import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import com.example.simcareerapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class GalleriaActivity extends AppCompatActivity {
    private static final String TAG = "GalleriaActivity";

    private CardView gte1, gte2;

    private BottomNavigationView bottom_navbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galleria);

        initViews();
    }

    private boolean bottomNavbarItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_bottom_item_profilo:
                Log.d(TAG, "bottom navbar profile selected");
                Intent profiloIntent = new Intent(GalleriaActivity.this, ProfileActivity.class);
                this.startActivity(profiloIntent);
                break;
            case R.id.menu_bottom_item_campionati:
                Log.d(TAG, "bottom navbar campionati selected");
                Intent campionatiIntent = new Intent(GalleriaActivity.this, HomeActivity.class);
                this.startActivity(campionatiIntent);
                break;
            case R.id.menu_bottom_item_galleria:
                Log.d(TAG, "bottom navbar galleria selected");
                break;
        }
        return false;
    }

    private void initViews(){
        this.gte1 = this.findViewById(R.id.galleria_view_gte1);
        this.gte2 = this.findViewById(R.id.galleria_view_gte2);

        this.gte1.setOnClickListener(this::cardOnClick);
        this.gte2.setOnClickListener(this::cardOnClick);

        /********** bottom navbar **************/
        this.bottom_navbar = this.findViewById(R.id.galleria_bottomnavbar);
        this.bottom_navbar.setOnNavigationItemSelectedListener(this::bottomNavbarItemSelected);
        // set menuitem checked to current activity
        MenuItem menuItem = bottom_navbar.getMenu().getItem(0);
        menuItem.setChecked(true);
    }

    private void cardOnClick(View view) {
        switch (view.getId()){
            case R.id.galleria_view_gte1: startIntent("GTE1");
                break;
            case R.id.galleria_view_gte2: startIntent("GTE2");
                break;
        }
    }

    private void startIntent(String album){
        Intent i = new Intent(GalleriaActivity.this, AlbumActivity.class);
        i.putExtra("album", album);
        this.startActivity(i);
    }
}
