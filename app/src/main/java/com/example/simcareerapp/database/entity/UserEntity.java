package com.example.simcareerapp.database.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Arrays;

@Entity(tableName = "users")
public class UserEntity implements Serializable {

    @PrimaryKey
    @NonNull
    private String user_email;

    @NonNull
    private String user_username;

    @NonNull
    private String user_password;

    @NonNull
    private String user_nome;
    @NonNull
    private String user_cognome;
    @Nullable
    private String user_residenza;
    @Nullable
    private String user_data_nascita;

    @Nullable
    private Integer user_numero_gara_pref;
    @Nullable
    private String user_circuito_pref;
    @Nullable
    private String user_circuito_odiato;
    @Nullable
    private String user_auto_pref;

    @Nullable
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] user_profile_image;

    public UserEntity(String user_username, String user_email, String user_password){
        this.user_username = user_username;
        this.user_email = user_email;
        this.user_password = user_password;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "user_email='" + user_email + '\'' +
                ", user_username='" + user_username + '\'' +
                ", user_password='" + user_password + '\'' +
                ", user_nome='" + user_nome + '\'' +
                ", user_cognome='" + user_cognome + '\'' +
                ", user_residenza='" + user_residenza + '\'' +
                ", user_data_nascita='" + user_data_nascita + '\'' +
                ", user_numero_gara_pref=" + user_numero_gara_pref +
                ", user_circuito_pref='" + user_circuito_pref + '\'' +
                ", user_circuito_odiato='" + user_circuito_odiato + '\'' +
                ", user_auto_pref='" + user_auto_pref + '\'' +
                ", user_profile_image=" + Arrays.toString(user_profile_image) +
                '}';
    }

    @NonNull
    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(@NonNull String user_email) {
        this.user_email = user_email;
    }

    @NonNull
    public String getUser_username() {
        return user_username;
    }

    public void setUser_username(@NonNull String user_username) {
        this.user_username = user_username;
    }

    @NonNull
    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(@NonNull String user_password) {
        this.user_password = user_password;
    }

    @NonNull
    public String getUser_nome() {
        return user_nome;
    }

    public void setUser_nome(@NonNull String user_nome) {
        this.user_nome = user_nome;
    }

    @NonNull
    public String getUser_cognome() {
        return user_cognome;
    }

    public void setUser_cognome(@NonNull String user_cognome) {
        this.user_cognome = user_cognome;
    }

    public String getUser_residenza() {
        return user_residenza;
    }

    public void setUser_residenza(String user_residenza) {
        this.user_residenza = user_residenza;
    }

    public String getUser_data_nascita() {
        return user_data_nascita;
    }

    public void setUser_data_nascita(String user_data_nascita) {
        this.user_data_nascita = user_data_nascita;
    }

    public Integer getUser_numero_gara_pref() {
        return user_numero_gara_pref;
    }

    public void setUser_numero_gara_pref(Integer user_numero_gara_pref) {
        this.user_numero_gara_pref = user_numero_gara_pref;
    }

    public String getUser_circuito_pref() {
        return user_circuito_pref;
    }

    public void setUser_circuito_pref(String user_circuito_pref) {
        this.user_circuito_pref = user_circuito_pref;
    }

    public String getUser_circuito_odiato() {
        return user_circuito_odiato;
    }

    public void setUser_circuito_odiato(String user_circuito_odiato) {
        this.user_circuito_odiato = user_circuito_odiato;
    }

    public String getUser_auto_pref() {
        return user_auto_pref;
    }

    public void setUser_auto_pref(String user_auto_pref) {
        this.user_auto_pref = user_auto_pref;
    }

    public byte[] getUser_profile_image() {
        return user_profile_image;
    }

    public void setUser_profile_image(byte[] user_profile_image) {
        this.user_profile_image = user_profile_image;
    }
}
