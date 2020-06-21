package com.example.simcareerapp.ui.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.simcareerapp.R;
import com.example.simcareerapp.ui.activity.CampionatoActivity;
import com.example.simcareerapp.utils.SimCareerUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InformazioniFragment extends Fragment {

    private static final String TAG = "InformazioniFragment";

    private static final String ISCRITTI_SEPARATOR = " - ";
    private static final String IMPOSTAZIONI_SEPARATOR = " : ";
    private static final String EVENTI_SEPARATOR = "   ";


    private ImageView imageView;
    private TextView title;
    private TableLayout iscrittiTable, impostazioniTable, eventiTable;
    private ToggleButton iscrivittiButton;
    private LinearLayout dialogLinearLayout;
    private Spinner spinner;

    private List<String> iscrittiList, impostazioniList, eventiList, autoList;

    private String user_name, user_surname, campionato_name;

    private JSONObject jsonFile;

    private SharedPreferences sharedPreferences;

    private Context context;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView =  inflater.inflate(R.layout.fragment_informazioni, container, false);

        context = requireContext();

        // init jsonFile
        this.jsonFile = SimCareerUtils.getJsonFile(context, R.raw.campionati);

        // init sharedPreferences
        this.sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);

        // ottengo il nome del campionato, nome e cognome dell'utente
        Log.d(TAG, "get bundle");
        campionato_name = getArguments().getString("campionato_name");
        user_name = sharedPreferences.getString("nome", "error");
        user_surname = sharedPreferences.getString("cognome", "error");

        Log.d(TAG, "init views");
        initViews(fragmentView);

        Log.d(TAG, "populate lists");
        populateLists();

        // set table
        Log.d(TAG, "set tables");
        SimCareerUtils.setTable(context, eventiTable, eventiList);
        SimCareerUtils.setTable(context, iscrittiTable, iscrittiList);
        SimCareerUtils.setTable(context, impostazioniTable, impostazioniList);

        // init button
        Log.d(TAG, "init iscriviti button");
        String iscrizioneAlCampionato = sharedPreferences.getString(campionato_name, "");
        if("".equals(iscrizioneAlCampionato)){ // nessuna iscrizione
            iscrivittiButton.setChecked(false);
        } else { // sì iscrizione
            iscrivittiButton.setChecked(true);
            SimCareerUtils.addRowToTable(context, iscrittiTable, iscrizioneAlCampionato);
        }

        return fragmentView;
    }

    private void initViews(View fragmentView){
        this.imageView = fragmentView.findViewById(R.id.info_img);
        this.eventiTable = fragmentView.findViewById(R.id.info_table_eventi);
        this.iscrittiTable = fragmentView.findViewById(R.id.info_table_iscritti);
        this.impostazioniTable = fragmentView.findViewById(R.id.info_table_impostazioni);
        this.iscrivittiButton = fragmentView.findViewById(R.id.info_btn_iscriviti);
        this.title = fragmentView.findViewById(R.id.info_txt_title);

        // set title
        title.setText(campionato_name);

        // set the right image
        Log.d(TAG, "set campionato logo");
        if("Leon Supercopa".equals(campionato_name)){
            imageView.setImageResource(R.drawable.logo_champ_0);
        } else {
            imageView.setImageResource(R.drawable.logo_champ_1);
        }

        // init dialog linear layout
        this.dialogLinearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.iscriviti_dialog, null);

        iscrivittiButton.setOnClickListener(this::iscrivittiButtonOnClick);

    }

    private void updateIscrizioneInSharedPref(String value){
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putString(this.campionato_name, value);
        editor.commit();
    }

    private void iscrivittiButtonOnClick(View view) {
        Log.d(TAG, "iscriviti button onclick");

        ToggleButton button = (ToggleButton) view;
        boolean iscritto = button.isChecked();

        // se la scritta è iscrizione apre un dialog per iscrizione
        // altrimenti annulla iscrizione e cancella l'utente dalla lista
        if(iscritto){ // iscriviti onclick
            Log.d(TAG, "avvia iscrizione");
            setIscrizioneAlertDialog();

        } else { // cancella iscrizione onclick
            Log.d(TAG, "cancella iscrizione");

            iscrittiTable.removeViewAt(iscrittiTable.getChildCount()-1);

            // update iscrizione al campionato to false
            updateIscrizioneInSharedPref("");

            Toast.makeText(context, "Iscrizione cancellata con successo", Toast.LENGTH_SHORT).show();
        }
    }

    private void setIscrizioneAlertDialog(){
        Log.d(TAG, "set dialog");
        // init spinner
        initSpinner();

        // define iscriviti dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogLinearLayout);
        builder.setNegativeButton("Annulla", null);
        builder.setPositiveButton("Conferma iscrizione", (dialog, which) -> {
            // get auto choice and team input
            String iscrizione_auto = spinner.getSelectedItem().toString();
            TextView teamTextView = dialogLinearLayout.findViewById(R.id.iscriviti_team);
            String iscrizione_team = teamTextView.getText().toString();

            if(!TextUtils.isEmpty(iscrizione_team)){
                String iscrizione_nome = user_name + " " + user_surname;
                String rowString = iscrizione_nome + ISCRITTI_SEPARATOR + iscrizione_team + ISCRITTI_SEPARATOR + iscrizione_auto;

                // add user info to iscritti table
                SimCareerUtils.addRowToTable(context, iscrittiTable, rowString);

                // update iscrizione al campionato
                updateIscrizioneInSharedPref(rowString);

                Toast.makeText(context, "Iscrizione con successo", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(context, "Il campo team non può essere vuoto!", Toast.LENGTH_SHORT);
            }

        });

        builder.create().show();
    }

    private void initSpinner(){
        Log.d(TAG, "init spinner");
        // add elenco auto to spinner
        this.spinner = this.dialogLinearLayout.findViewById(R.id.iscriviti_spinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item, autoList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinner.setAdapter(spinnerAdapter);
    }

    private void redirectIntent(){
        Intent intent = new Intent(getActivity(), CampionatoActivity.class);
        startActivity(intent);
    }

    private void populateLists(){
        iscrittiList = new ArrayList<>();
        impostazioniList = new ArrayList<>();
        eventiList = new ArrayList<>();

        try {
            JSONObject campionato = SimCareerUtils.getCampionatoJsonArrayFromJsonFile(this.jsonFile, campionato_name);

            // get string from arrays and populate the lists
            SimCareerUtils.jsonArrayToStringList(iscrittiList,
                    campionato.getJSONArray("piloti-iscritti"), ISCRITTI_SEPARATOR);
            SimCareerUtils.jsonArrayToStringList(impostazioniList,
                    campionato.getJSONArray("impostazioni-gioco"), IMPOSTAZIONI_SEPARATOR);
            SimCareerUtils.eventiJsonToList(eventiList,
                    campionato.getJSONArray("calendario"), EVENTI_SEPARATOR);

            // get auto list
            autoList = SimCareerUtils.jsonListaAutoToStringList(campionato.getString("lista-auto"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }





}
