package com.example.simcareerapp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.simcareerapp.R;
import com.example.simcareerapp.database.SimCareerDatabase;
import com.example.simcareerapp.database.dao.UserDao;
import com.example.simcareerapp.database.entity.UserEntity;

public class LoginActivity extends AppCompatActivity {

    private TextView signupLink;
    private EditText email, password;
    private Button loginButton;

    private UserDao userDao;
    private SimCareerDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        prepareDb();
        initViews();

    }

    private void prepareDb(){
        database = SimCareerDatabase.getDatabase(this);
        userDao = database.userDao();
    }

    private void initViews(){
        this.email = this.findViewById(R.id.login_input_email);
        this.password = this.findViewById(R.id.login_input_password);
        this.signupLink = this.findViewById(R.id.login_text_registrati);
        this.loginButton = this.findViewById(R.id.login_btn_accedi);

        this.signupLink.setOnClickListener(this::signupLinkOnClick);
        this.loginButton.setOnClickListener(this::loginButtonOnClick);
    }

    private void loginButtonOnClick( View v ){

        String email = this.email.getText().toString();
        String psw = this.password.getText().toString();

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(psw)){
            Toast.makeText(LoginActivity.this, "Si prega di inserire tutti i campi obbligatori!", Toast.LENGTH_SHORT).show();
        } else {

            // cerca l'utente tramite email e password
            UserEntity user = userDao.findUserByEmailPass(email, psw);

            if (user != null) {

                SharedPreferences sharedPref = getSharedPreferences("user", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("email", user.getUser_email());
                editor.putString("nome", user.getUser_nome());
                editor.putString("cognome", user.getUser_cognome());
                editor.commit();

                Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                this.startActivity(i);
                this.finish();
            }else{
                Toast.makeText(LoginActivity.this, "Credenziali non corrette!", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void signupLinkOnClick( View v ) {
        Intent i = new Intent(LoginActivity.this, SignupActivity.class);
        this.startActivity(i);
    }
}
