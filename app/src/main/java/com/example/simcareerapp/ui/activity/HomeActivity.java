package com.example.simcareerapp.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.simcareerapp.R;
import com.example.simcareerapp.utils.SimCareerUtils;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    
    private static final String TAG = "HomeActivity";
    
    private BottomNavigationView bottom_navbar;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int[] campionatiImageList = new int[]{R.drawable.logo_champ_0, R.drawable.logo_champ_1};;
        List<String> campionatiNameList = new ArrayList<>();;

        initViews();


        try {
            JSONArray campionati = SimCareerUtils.getJsonFile(this, R.raw.campionati).getJSONArray("campionati");
            // add campionati name to campionatiNameList
            for(int i=0; i < campionati.length(); i++){
                campionatiNameList.add(campionati.getJSONObject(i).getString("nome"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // create adpater list
        List<Map<String, Object>> list = new ArrayList<>();

        // populate the adapter list
        populateListMap(list, campionatiImageList, campionatiNameList);

        // create adapter and set it to listview
        SimpleAdapter adapter = new SimpleAdapter(this,list,R.layout.main_card,
                new String[]{"image", "name"},new int[]{R.id.main_card_img, R.id.main_card_txt} );

        // ovveride adapter getview


        this.listView.setAdapter(adapter);

        this.listView.setOnItemClickListener(this::cardItemOnClick);
    }

    private void cardItemOnClick(AdapterView<?> adapterView, View view, int i, long l) {
        Log.d(TAG, "Listview clicked");
        Intent campionatoActivity = new Intent(HomeActivity.this, CampionatoActivity.class);
        TextView campionatoName = view.findViewById(R.id.main_card_txt);
        campionatoActivity.putExtra("campionato_name", campionatoName.getText().toString());
        this.startActivity(campionatoActivity);
    }

    private void populateListMap (List<Map<String, Object>> list, int[] campionatiImageList, List<String> campionatiNameList){
        int size = campionatiImageList.length;
        Log.d(TAG, "populateListMap size: " + size);
        for(int i=0; i<size; i++){
            Map<String, Object> map = new HashMap<>();
            map.put("image", campionatiImageList[i]);
            map.put("name", campionatiNameList.get(i));
            list.add(map);
        }
    }

    private void initViews() {
        /************findviewbyid***************/
        this.listView = this.findViewById(R.id.main_view_listview);

        /*************set listener**************/


        /********** bottom navbar **************/
        this.bottom_navbar = this.findViewById(R.id.main_bottomnavbar);
        this.bottom_navbar.setOnNavigationItemSelectedListener(this::bottomNavbarItemSelected);
        // set menuitem checked to current activity
        MenuItem menuItem = bottom_navbar.getMenu().getItem(1);
        menuItem.setChecked(true);
    }

    private boolean bottomNavbarItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_bottom_item_profilo:
                Log.d(TAG, "bottom navbar profile selected");
                Intent profiloIntent = new Intent(HomeActivity.this, ProfileActivity.class);
                this.startActivity(profiloIntent);
                break;
            case R.id.menu_bottom_item_campionati:
                Log.d(TAG, "bottom navbar campionati selected");
                break;
            case R.id.menu_bottom_item_galleria:
                Log.d(TAG, "bottom navbar galleria selected");
                Intent galleriaIntent = new Intent(HomeActivity.this, GalleriaActivity.class);
                this.startActivity(galleriaIntent);
                break;
        }
        return false;
    }


}



