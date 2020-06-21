package com.example.simcareerapp.ui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import com.example.simcareerapp.R;
import com.example.simcareerapp.database.SimCareerDatabase;
import com.example.simcareerapp.database.dao.UserDao;
import com.example.simcareerapp.database.entity.UserEntity;
import com.example.simcareerapp.utils.SimCareerUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";

    private ImageView img_profilo;
    private EditText username, email, password, nome, cognome,
            nascita, residenza, ngarapref, autopref, circuitoOdiato, circuitoPref;
    private Button saveButton;

    private UserDao userDao;
    private SimCareerDatabase database;
    private SharedPreferences sharedPref;
    private UserEntity user = null;

    private static final int REQUEST_CODE_GALLERY = 0;
    private Bitmap profile_bitmap = null;

    private BottomNavigationView bottom_navbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        prepareDb();
        initViews();

        sharedPref = getSharedPreferences("user", Context.MODE_PRIVATE);
        String email_string = sharedPref.getString("email", "null");
        // set email field to user email
        email.setText(email_string);
        // init user
        user = userDao.findUserByEmail(email_string);

        // inizializzo i campi con i dati del db
        setProfileFields();

    }

    private void saveButtonOnClick(View view){

        String mEmail = this.email.getText().toString();
        String mUsername = this.username.getText().toString();
        String mPassword = this.password.getText().toString();
        String mNome = getInputText(nome);
        String mCognome = getInputText(cognome);
        String mDataNascita = getInputText(nascita);
        String mResidenza = getInputText(residenza);
        Integer mNGaraPref = getInputInteger(ngarapref);
        String mAutoPref = getInputText(autopref);
        String mCircuitoOdiato = getInputText(circuitoOdiato);
        String mCircuitoPref = getInputText(circuitoPref);
        byte[] mProfilo = getInputImage();


        if(TextUtils.isEmpty(mUsername) || TextUtils.isEmpty(mPassword)
                || TextUtils.isEmpty(mNome) || TextUtils.isEmpty(mCognome)){
            Toast.makeText(ProfileActivity.this, "Si prega di inserire tutti i campi obbligatori (campi con *)!", Toast.LENGTH_SHORT).show();
        } else {
            UserEntity user = new UserEntity(mUsername, mEmail, mPassword);
            user.setUser_auto_pref(mAutoPref);
            user.setUser_circuito_odiato(mCircuitoOdiato);
            user.setUser_circuito_pref(mCircuitoPref);
            user.setUser_nome(mNome);
            user.setUser_cognome(mCognome);
            user.setUser_data_nascita(mDataNascita);
            user.setUser_residenza(mResidenza);
            user.setUser_numero_gara_pref(mNGaraPref);
            user.setUser_profile_image(mProfilo);

            userDao.updateUser(user);

            // update shared preferences
            SharedPreferences.Editor editor = this.sharedPref.edit();
            editor.putString("nome", user.getUser_nome());
            editor.putString("cognome", user.getUser_cognome());
            editor.commit();

            Toast.makeText(ProfileActivity.this, "Modifiche salvate con successo!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
            this.startActivity(intent);
        }

    }

    /**
     * inizializza l'immagine di profilo se il campo nel db non è vuoto,
     * altrimenti imposta il logo
     */
    private void setProfileImageField(){
        byte[] imageByte = user.getUser_profile_image();

        if(imageByte != null){
            this.img_profilo.setImageBitmap(SimCareerUtils.byteArrayToBitmap(imageByte));
        } else {
            this.img_profilo.setImageResource(R.drawable.logo);
        }
    }

    /**
     * inizializza i campi con i dati nel db
     */
    private void setProfileFields(){
        this.username.setText(user.getUser_username());
        this.password.setText(user.getUser_password());
        this.nome.setText(user.getUser_nome());
        this.cognome.setText(user.getUser_cognome());
        this.nascita.setText(user.getUser_data_nascita());
        this.residenza.setText(user.getUser_residenza());

        Integer ngarapref_int = user.getUser_numero_gara_pref();
        if(ngarapref_int == null){
            this.ngarapref.setText("");
        } else {
            this.ngarapref.setText(""+ngarapref_int);
        }

        this.autopref.setText(user.getUser_auto_pref());
        this.circuitoOdiato.setText(user.getUser_circuito_odiato());
        this.circuitoPref.setText(user.getUser_circuito_pref());

        setProfileImageField();
    }

    /**
     *
     * @param editText
     * @return the string value of the EditText or null if empty
     */
    public String getInputText(EditText editText){
        String text = editText.getText().toString();
        return TextUtils.isEmpty(text) ? null : text;
    }

    /**
     *
     * @param editText EditText of type number
     * @return the integer value of the EditText or null if empty
     */
    public Integer getInputInteger(EditText editText) {
        String text = editText.getText().toString();
        return TextUtils.isEmpty(text) ? null : Integer.parseInt(text);
    }

    /**
     *
     * @return bitmap of image profile or null
     */
    public byte[] getInputImage(){
        return profile_bitmap == null ? null : SimCareerUtils.bitmapToByteArray(profile_bitmap);
    }

    /**
     * permette di scegliere un'immagine dalla galleria e impostare come immagine di profilo
     * @param view
     */
    private void setProfileImage(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Upload immagine di profilo");
        builder.setNegativeButton("Annulla", null);
        builder.setPositiveButton("Scegli da galleria", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Build.VERSION.SDK_INT <= 19) {
                    Intent i = new Intent();
                    i.setType("image/*");
                    i.setAction(Intent.ACTION_GET_CONTENT);
                    i.addCategory(Intent.CATEGORY_OPENABLE);
                    startActivityForResult(i, REQUEST_CODE_GALLERY);
                } else if (Build.VERSION.SDK_INT > 19) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, REQUEST_CODE_GALLERY);
                }
            }
        });

        builder.create().show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_GALLERY && data != null) {
            Uri selectedImageUri = data.getData();

            if(Build.VERSION.SDK_INT < 28){
                try {
                    profile_bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), selectedImageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                ImageDecoder.Source source = ImageDecoder.createSource(getApplicationContext().getContentResolver(), selectedImageUri);
                try {
                    profile_bitmap = ImageDecoder.decodeBitmap(source);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            this.img_profilo.setImageBitmap(profile_bitmap);

        }

    }

    private void initViews(){
        this.img_profilo = this.findViewById(R.id.modifica_profilo_img_profilo);
        this.saveButton = this.findViewById(R.id.modifica_profilo_salva);

        this.username = this.findViewById(R.id.modifica_profilo_input_username);
        this.email = this.findViewById(R.id.modifica_profilo_input_email);
        this.password = this.findViewById(R.id.modifica_profilo_input_password);
        this.nome = this.findViewById(R.id.modifica_profilo_input_nome);
        this.cognome = this.findViewById(R.id.modifica_profilo_input_cognome);
        this.nascita = this.findViewById(R.id.modifica_profilo_input_dataNascita);
        this.residenza = this.findViewById(R.id.modifica_profilo_input_residenza);
        this.ngarapref = this.findViewById(R.id.modifica_profilo_input_numeroGara);
        this.autopref = this.findViewById(R.id.modifica_profilo_input_auto);
        this.circuitoOdiato = this.findViewById(R.id.modifica_profilo_input_circuitoOdiato);
        this.circuitoPref = this.findViewById(R.id.modifica_profilo_input_circuitoPreferito);


        this.img_profilo.setOnClickListener(this::setProfileImage);
        this.saveButton.setOnClickListener(this::saveButtonOnClick);

        /********** bottom navbar **************/
        this.bottom_navbar = this.findViewById(R.id.profile_bottombar);
        this.bottom_navbar.setOnNavigationItemSelectedListener(this::bottomNavbarItemSelected);
        // set menuitem checked to current activity
        MenuItem menuItem = bottom_navbar.getMenu().getItem(2);
        menuItem.setChecked(true);

    }

    private boolean bottomNavbarItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_bottom_item_profilo:
                Log.d(TAG, "bottom navbar profile selected");
                break;
            case R.id.menu_bottom_item_campionati:
                Log.d(TAG, "bottom navbar campionati selected");
                Intent campionatiIntent = new Intent(ProfileActivity.this, HomeActivity.class);
                this.startActivity(campionatiIntent);
                break;
            case R.id.menu_bottom_item_galleria:
                Log.d(TAG, "bottom navbar galleria selected");
                Intent galleriaIntent = new Intent(ProfileActivity.this, GalleriaActivity.class);
                this.startActivity(galleriaIntent);
                break;
        }
        return false;
    }

    private void prepareDb(){
        database = SimCareerDatabase.getDatabase(this);
        userDao = database.userDao();
    }
}
