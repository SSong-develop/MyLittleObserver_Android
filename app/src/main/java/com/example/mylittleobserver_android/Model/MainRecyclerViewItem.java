package com.example.mylittleobserver_android.Model;

public class MainRecyclerViewItem {
    String Title;
    String Date;
    String Heartrate;
    String Decibel;
    String Warningword;

    public MainRecyclerViewItem() {

    }

    public MainRecyclerViewItem(String title, String date, String heartRate, String decibel, String warningWord) {
        this.Title = title;
        this.Date = date;
        this.Heartrate = heartRate;
        this.Decibel = decibel;
        this.Warningword = warningWord;
    }


    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        this.Title = title;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        this.Date = date;
    }

    public String getHeartRate() {
        return Heartrate;
    }

    public void setHeartRate(String heartRate) {
        this.Heartrate = heartRate;
    }

    public String getDecibel() {
        return Decibel;
    }

    public void setDecibel(String decibel) {
        this.Decibel = decibel;
    }

    public String getWarningWord() {
        return Warningword;
    }

    public void setWarningWord(String warningWord) {
        this.Warningword = warningWord;
    }
}
