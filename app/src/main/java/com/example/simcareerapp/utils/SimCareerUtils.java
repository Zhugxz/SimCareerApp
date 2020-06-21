package com.example.simcareerapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class SimCareerUtils {

    public static void setTable(Context context, TableLayout tableLayout, List<String> stringList){
        for(int i=0; i<stringList.size(); i++){
            addRowToTable(context, tableLayout, stringList.get(i));
        }
    }

    public static void addRowToTable(Context context, TableLayout tableLayout, String text){
        TableRow tableRow =  new TableRow(context);
        TextView textView = new TextView(context);
        textView.setText(text);
        tableRow.addView(textView);
        tableLayout.addView(tableRow);
    }

    public static void jsonArrayToStringList(List<String> list, JSONArray jsonArray, String separator){
        for (int i=0; i<jsonArray.length(); i++){
            try {
                list.add(concatJsonObjectFields(jsonArray.getJSONObject(i), separator));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static void eventiJsonToList(List<String> list, JSONArray jsonArray, String separator){
        for (int i=0; i<jsonArray.length(); i++){
            try {
                list.add(concatJsonObjectFields(jsonArray.getJSONObject(i), separator) + separator +  separator + "Risultati");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static String concatJsonObjectFields(JSONObject jsonObject, String separator) {
        String retval = "";

        Iterator<String> iterator = jsonObject.keys();
        while(iterator.hasNext()){
            try {
                String key = iterator.next();
                retval += jsonObject.getString(key) + separator;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // ritorna la stringa tolta l'ultimo separator in più
        return retval.substring(0, retval.length()-separator.length());
    }

    public static List<String> jsonListaAutoToStringList(String lista_auto){
        return new ArrayList<String>(Arrays.asList(lista_auto.split("\\s*,\\s*")));
    }

    public static JSONObject getJsonFile(Context context, int jsonFileResourceId) {
        JSONObject jsonObject = null;
        InputStream inputStream = context.getResources().openRawResource(jsonFileResourceId);

        try{
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            String jsonString = new String(buffer, "UTF-8");
            jsonObject = new JSONObject(jsonString);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }


        return jsonObject;
    }



    public static JSONObject getCampionatoJsonArrayFromJsonFile(JSONObject jsonFile, String campionato_name) {
        JSONObject jsonObject = null;
        try {

            JSONArray jsonArray = jsonFile.getJSONArray("campionati");

            Boolean findFlag = false;
            int index = 0;

            while (!findFlag && index < jsonArray.length()) {
                JSONObject campionato = jsonArray.getJSONObject(index);
                // if campionato match with campionato_name
                if(campionato.getString("nome").equals(campionato_name)){
                    // return campionato
                    jsonObject = campionato;
                    findFlag = true;
                }

                index++;
            }

        } catch ( JSONException ex) {
            ex.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * convert bitmap in byte array
     * @param bitmap
     * @return byte[]
     * @throws IOException
     */
    public static byte[] bitmapToByteArray(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    /**
     * convert byte array to bitmap
     * @param byteArray
     * @return bitmap
     * @throws IOException
     */
    public static Bitmap byteArrayToBitmap(byte[] byteArray) {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    };
}