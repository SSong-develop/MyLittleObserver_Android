package com.example.mylittleobserver_android.Model;

public class InsideItem {
    String alarmId; // alarmId
    String alarmTitle; // hear , decibel , tumble 합
    String date; // date


    public InsideItem() {

    }

    public InsideItem(String alarmId, String alarmTitle, String date) {
        this.alarmId = alarmId;
        this.alarmTitle = alarmTitle;
        this.date = date;
    }

    public InsideItem(String alarmTitle, String date) {
        this.alarmTitle = alarmTitle;
        this.date = date;
    }

    public String getAlarmTitle() {
        return alarmTitle;
    }

    public void setAlarmTitle(String alarmTitle) {
        this.alarmTitle = alarmTitle;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(String alarmId) {
        this.alarmId = alarmId;
    }
}
