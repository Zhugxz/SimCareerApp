package com.example.simcareerapp.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

@Entity()
public class CampionatoEntity {
    @PrimaryKey
    @ColumnInfo(name = "id")
    private
    String campionato_id;
    private String nome;
    private ArrayList<Map<String, String>> calendario;
    private ArrayList<Map<String, String>> impostazioni_gioco;
    private String lista_auto;
    private ArrayList<Map<String, String>> piloti_iscritti;

    public String getCampionato_id() {
        return campionato_id;
    }

    public void setCampionato_id(String campionato_id) {
        this.campionato_id = campionato_id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public ArrayList<Map<String, String>> getCalendario() {
        return calendario;
    }

    public void setCalendario(ArrayList<Map<String, String>> calendario) {
        this.calendario = calendario;
    }

    public ArrayList<Map<String, String>> getImpostazioni_gioco() {
        return impostazioni_gioco;
    }

    public void setImpostazioni_gioco(ArrayList<Map<String, String>> impostazioni_gioco) {
        this.impostazioni_gioco = impostazioni_gioco;
    }

    public String getLista_auto() {
        return lista_auto;
    }

    public void setLista_auto(String lista_auto) {
        this.lista_auto = lista_auto;
    }

    public ArrayList<Map<String, String>> getPiloti_iscritti() {
        return piloti_iscritti;
    }

    public void setPiloti_iscritti(ArrayList<Map<String, String>> piloti_iscritti) {
        this.piloti_iscritti = piloti_iscritti;
    }
}
