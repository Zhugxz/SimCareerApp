package com.example.simcareerapp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.example.simcareerapp.R;
import com.example.simcareerapp.ui.adapter.CampionatoViewPagerAdapter;
import com.example.simcareerapp.ui.fragment.ClassificheFragment;
import com.example.simcareerapp.ui.fragment.InformazioniFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

public class CampionatoActivity extends AppCompatActivity {

    private static final String TAG = "CampionatoActivity";

    private ViewPager viewPager;
    private TabLayout tabLayout;

    private InformazioniFragment informazioniFragment = new InformazioniFragment();
    private ClassificheFragment classificheFragment = new ClassificheFragment();

    private BottomNavigationView bottom_navbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campionato);

        Log.d(TAG, "campionato init");

        initViews();
        // pass campionato name to fragments
        Bundle bundle = new Bundle();
        bundle.putString("campionato_name", getIntent().getStringExtra("campionato_name"));

        informazioniFragment.setArguments(bundle);
        classificheFragment.setArguments(bundle);

        Log.d(TAG, "set fragments");

        // init and set custom viewpager adapter
        CampionatoViewPagerAdapter campionatoViewPagerAdapter = new CampionatoViewPagerAdapter(getSupportFragmentManager());
        campionatoViewPagerAdapter.addToList(informazioniFragment, "informazioni");
        campionatoViewPagerAdapter.addToList(classificheFragment, "classifiche");
        this.viewPager.setAdapter(campionatoViewPagerAdapter);
        tabLayout.setupWithViewPager(this.viewPager);

    }

    private boolean bottomNavbarItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_bottom_item_profilo:
                Log.d(TAG, "bottom navbar profile selected");
                Intent profiloIntent = new Intent(CampionatoActivity.this, ProfileActivity.class);
                this.startActivity(profiloIntent);
                break;
            case R.id.menu_bottom_item_campionati:
                Log.d(TAG, "bottom navbar campionati selected");
                Intent campionatiIntent = new Intent(CampionatoActivity.this, HomeActivity.class);
                this.startActivity(campionatiIntent);
                break;
            case R.id.menu_bottom_item_galleria:
                Log.d(TAG, "bottom navbar galleria selected");
                Intent galleriaIntent = new Intent(CampionatoActivity.this, GalleriaActivity.class);
                this.startActivity(galleriaIntent);
                break;
        }
        return false;
    }

    private  void initViews(){
        this.viewPager = this.findViewById(R.id.campionato_viewpager);
        this.tabLayout = this.findViewById(R.id.campionato_tablayout);

        /********** bottom navbar **************/
        this.bottom_navbar = this.findViewById(R.id.campionato_bottomnavbar);
        this.bottom_navbar.setOnNavigationItemSelectedListener(this::bottomNavbarItemSelected);
        // set menuitem checked to current activity
        MenuItem menuItem = bottom_navbar.getMenu().getItem(1);
        menuItem.setChecked(true);
    }
}
