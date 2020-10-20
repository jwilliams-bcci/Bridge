package com.example.bridge;

import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private static DataManager ourInstance = null;

    private List<Inspection> mInspections = new ArrayList<>();

    public static DataManager getInstance() {
        if(ourInstance == null) {
            ourInstance = new DataManager();
            ourInstance.initializeInspections();
        }
        return ourInstance;
    }

    public List<Inspection> getInspections() {
        return mInspections;
    }
    public Inspection getInspection(int id) {
        for (Inspection inspection : mInspections) {
            if (id == inspection.getInspectionId()) {
                return inspection;
            }
        }
        return null;
    }

    private void initializeInspections() {
        mInspections.add(initializeInspection1());
        mInspections.add(initializeInspection2());
        mInspections.add(initializeInspection3());
        mInspections.add(initializeInspection4());
        mInspections.add(initializeInspection5());
        mInspections.add(initializeInspection6());
        mInspections.add(initializeInspection7());
    }

    //region Inspection Initializations
    private Inspection initializeInspection1() {
        return new Inspection(1,"Test Community 1", "123 One Road", "Final", "Notes go here");
    }
    private Inspection initializeInspection2() {
        return new Inspection(2,"Test Community 1", "234 One Road", "Final", "Notes go here");
    }
    private Inspection initializeInspection3() {
        return new Inspection(3,"Test Community 1", "345 One Road", "Final", "Notes go here");
    }
    private Inspection initializeInspection4() {
        return new Inspection(4,"Test Community 1", "456 One Road", "Final", "Notes go here");
    }
    private Inspection initializeInspection5() {
        return new Inspection(5,"Test Community 2", "123 Two Street", "Final", "Notes go here");
    }
    private Inspection initializeInspection6() {
        return new Inspection(6,"Test Community 2", "234 Two Street", "Final", "Notes go here");
    }
    private Inspection initializeInspection7() {
        return new Inspection(7,"Test Community 2", "345 Two Street", "Final", "Notes go here");
    }
    private Inspection initializeInspection8() {
        return new Inspection(8,"Test Community 2", "456 Two Street", "Final", "Notes go here");
    }
    //endregion
}
