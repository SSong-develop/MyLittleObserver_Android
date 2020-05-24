package com.example.mylittleobserver_android.Model;

import java.util.ArrayList;
import java.util.List;

public class Section {

    private String sectionName;
    private ArrayList<InsideItem> sectionItems;

    public Section(){

    }

    public Section(String sectionName, ArrayList<InsideItem> sectionItems) {
        this.sectionName = sectionName;
        this.sectionItems = sectionItems;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public ArrayList<InsideItem> getSectionItems() {
        return sectionItems;
    }

    public void setSectionItems(ArrayList<InsideItem> sectionItems) {
        this.sectionItems = sectionItems;
    }
}
