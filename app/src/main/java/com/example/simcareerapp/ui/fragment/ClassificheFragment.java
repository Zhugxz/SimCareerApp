package com.example.simcareerapp.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.simcareerapp.R;
import com.example.simcareerapp.utils.SimCareerUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ClassificheFragment extends Fragment {

    private static final String SEPARATOR = " - ";

    private TableLayout pilotiTable, teamTable;
    private TextView title;

    private List<String> pilotiList, teamList;

    private JSONObject jsonFile;

    private String campionato_name;

    private  Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_classifiche, container, false);

        context = requireContext();

        // init jsonFile
        this.jsonFile = SimCareerUtils.getJsonFile(context, R.raw.classifiche);

        campionato_name = getArguments().getString("campionato_name");


        initViews(fragmentView);

        populateLists();

        SimCareerUtils.setTable(context, pilotiTable, pilotiList);
        SimCareerUtils.setTable(context, teamTable, teamList);

        return fragmentView;
    }

    private void initViews(View fragmentView){
        this.pilotiTable = fragmentView.findViewById(R.id.rank_table_piloti);
        this.teamTable = fragmentView.findViewById(R.id.rank_table_team);
        this.title = fragmentView.findViewById(R.id.rank_txt_title);

        title.setText(campionato_name);
    }

    private void populateLists(){
        pilotiList = new ArrayList<>();
        teamList = new ArrayList<>();

        try {
            JSONObject campionato = SimCareerUtils.getCampionatoJsonArrayFromJsonFile(this.jsonFile, campionato_name);

            // get string from arrays and populate the lists
            SimCareerUtils.jsonArrayToStringList(pilotiList,
                    campionato.getJSONArray("classifica-piloti"), SEPARATOR);
            SimCareerUtils.jsonArrayToStringList(teamList,
                    campionato.getJSONArray("classifica-team"), SEPARATOR);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
