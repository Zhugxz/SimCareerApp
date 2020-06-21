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

import java.io.IOException;

public class SignupActivity extends AppCompatActivity {

    private TextView loginLink;
    private ImageView img_profilo;
    private EditText username, email, password, confermaPassword, nome, cognome,
            nascita, residenza, ngarapref, autopref, circuitoOdiato, circuitoPref;
    private Button signupButton;

    private UserDao userDao;
    private SimCareerDatabase database;

    public static final int REQUEST_CODE_GALLERY = 0;

    private Bitmap profile_bitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        prepareDb();
        initViews();
    }

    private void prepareDb(){
        database = SimCareerDatabase.getDatabase(this);
        userDao = database.userDao();
    }

    private void initViews(){

        this.loginLink = this.findViewById(R.id.signup_text_accedi);
        this.img_profilo = this.findViewById(R.id.signup_img_profilo);
        this.signupButton = this.findViewById(R.id.signup_btn_registrati);

        this.username = this.findViewById(R.id.signup_input_username);
        this.email = this.findViewById(R.id.signup_input_email);
        this.password = this.findViewById(R.id.signup_input_password);
        this.confermaPassword = this.findViewById(R.id.signup_input_conferma);
        this.nome = this.findViewById(R.id.signup_input_nome);
        this.cognome = this.findViewById(R.id.signup_input_cognome);
        this.nascita = this.findViewById(R.id.signup_input_dataNascita);
        this.residenza = this.findViewById(R.id.signup_input_residenza);
        this.ngarapref = this.findViewById(R.id.signup_input_numeroGara);
        this.autopref = this.findViewById(R.id.signup_input_auto);
        this.circuitoOdiato = this.findViewById(R.id.signup_input_circuitoOdiato);
        this.circuitoPref = this.findViewById(R.id.signup_input_circuitoPreferito);

        this.loginLink.setOnClickListener(this::loginLinkOnClick);
        this.img_profilo.setOnClickListener(this::setProfileImage);
        this.signupButton.setOnClickListener(this::signupButtonOnClick);

        this.resetViews();
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

    private void signupButtonOnClick(View view){
        String mUsername = this.username.getText().toString();
        String mEmail = this.email.getText().toString();
        String mPassword = this.password.getText().toString();
        String mConferma = this.confermaPassword.getText().toString();
        String mNome = getInputText(nome);
        String mCognome = getInputText(cognome);
        String mDataNascita = getInputText(nascita);
        String mResidenza = getInputText(residenza);
        Integer mNGaraPref = getInputInteger(ngarapref);
        String mAutoPref = getInputText(autopref);
        String mCircuitoOdiato = getInputText(circuitoOdiato);
        String mCircuitoPref = getInputText(circuitoPref);
        byte[] mProfilo = getInputImage();


        if(TextUtils.isEmpty(mUsername) || TextUtils.isEmpty(mEmail) || TextUtils.isEmpty(mPassword)
                || TextUtils.isEmpty(mConferma) || TextUtils.isEmpty(mCognome) || TextUtils.isEmpty(mNome)){
            Toast.makeText(SignupActivity.this, "Si prega di inserire tutti i campi obbligatori (campi con *)!", Toast.LENGTH_SHORT).show();
        } else if(userDao.findUserByEmail(mEmail) != null){
            Toast.makeText(SignupActivity.this, "Email utente già esistente!", Toast.LENGTH_SHORT).show();
        } else if(!mPassword.equals(mConferma)){
            Toast.makeText(SignupActivity.this, "La password e la conferma password non sono uguali!", Toast.LENGTH_SHORT).show();
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

            userDao.insertUser(user);

            SharedPreferences sharedPref = getSharedPreferences("user", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("email", user.getUser_email());
            editor.putString("nome", user.getUser_nome());
            editor.putString("cognome", user.getUser_cognome());
            editor.putString("Leon Supercopa", "");
            editor.putString("International TCT", "");
            editor.commit();

            Intent i = new Intent(SignupActivity.this, HomeActivity.class);
            this.startActivity(i);
            this.finish();
        }

    }

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

    private void resetViews() {
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.logo);
        this.img_profilo.setImageDrawable(drawable);

        this.username.setText("");
        this.email.setText("");
        this.password.setText("");
        this.confermaPassword.setText("");
        this.nome.setText("");
        this.cognome.setText("");
        this.nascita.setText("");
        this.residenza.setText("");
        this.ngarapref.setText("");
        this.autopref.setText("");
        this.circuitoOdiato.setText("");
        this.circuitoPref.setText("");

    }

    private void loginLinkOnClick( View v ) {
        Intent i = new Intent(SignupActivity.this, LoginActivity.class);
        this.startActivity(i);
    }
}
