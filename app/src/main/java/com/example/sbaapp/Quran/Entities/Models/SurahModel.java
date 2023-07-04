package com.example.sbaapp.Quran.Entities.Models;

public class SurahModel {

    int idSurah;
    String nameSurah;
    int audioResource;

    public SurahModel(int idSurah, String nameSurah, int audioResource){
        this.idSurah = idSurah;
        this.nameSurah = nameSurah;
        this.audioResource = audioResource;
    }

    public int getIdSurah() {
        return idSurah;
    }

    public void setIdSurah(int idSurah) {
        this.idSurah = idSurah;
    }

    public String getNameSurah() {
        return nameSurah;
    }

    public void setNameSurah(String nameSurah) {
        this.nameSurah = nameSurah;
    }

    public int getAudioResource() {
        return audioResource;
    }

    public void setAudioResource(int audioResource) {
        this.audioResource = audioResource;
    }
}
