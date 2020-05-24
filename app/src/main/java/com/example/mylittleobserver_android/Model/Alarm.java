package com.example.mylittleobserver_android.Model;

public class Alarm {
    Long alarmId;
    Long heart;
    Long decibel;
    String tumble;
    String date;

    public Alarm() {

    }

    public Alarm(Long alarmId, Long heart, Long decibel, String tumble, String date) {
        this.alarmId = alarmId;
        this.heart = heart;
        this.decibel = decibel;
        this.tumble = tumble;
        this.date = date;
    }

    public Long getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(Long alarmId) {
        this.alarmId = alarmId;
    }

    public Long getHeart() {
        return heart;
    }

    public void setHeart(Long heart) {
        this.heart = heart;
    }

    public Long getDecibel() {
        return decibel;
    }

    public void setDecibel(Long decibel) {
        this.decibel = decibel;
    }

    public String getTumble() {
        return tumble;
    }

    public void setTumble(String tumble) {
        this.tumble = tumble;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
