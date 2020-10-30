package com.example.bridge.data;

import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private static DataManager ourInstance = null;

    private List<Inspection> mInspections = new ArrayList<>();
    private List<Builder> mBuilders = new ArrayList<>();

    public static DataManager getInstance() {
        if(ourInstance == null) {
            ourInstance = new DataManager();
            ourInstance.initializeInspections();
            ourInstance.initializeBuilders();
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
    public List<Builder> getBuilders() {
        return mBuilders;
    }
    public Builder getBuilder(int id) {
        for (Builder builder : mBuilders) {
            if (id == builder.getBuilderId()) {
                return builder;
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
        mInspections.add(initializeInspection8());
    }

    private void initializeBuilders() {
        mBuilders.add(initializeBuilder1());
        mBuilders.add(initializeBuilder2());
    }

    //region Inspection Initializations
    private Inspection initializeInspection1() {
        return new Inspection(1, 1, "Test Community 1", "123 One Road", "Final", "Notes go here");
    }
    private Inspection initializeInspection2() {
        return new Inspection(2, 1, "Test Community 1", "234 One Road", "Final", "Notes go here");
    }
    private Inspection initializeInspection3() {
        return new Inspection(3, 2, "Test Community 1", "345 One Road", "Final", "Notes go here");
    }
    private Inspection initializeInspection4() {
        return new Inspection(4, 2, "Test Community 1", "456 One Road", "Final", "Notes go here");
    }
    private Inspection initializeInspection5() {
        return new Inspection(5, 1, "Test Community 2", "123 Two Street", "Final", "Notes go here");
    }
    private Inspection initializeInspection6() {
        return new Inspection(6, 1, "Test Community 2", "234 Two Street", "Final", "Notes go here");
    }
    private Inspection initializeInspection7() {
        return new Inspection(7, 2, "Test Community 2", "345 Two Street", "Final", "Notes go here");
    }
    private Inspection initializeInspection8() {
        return new Inspection(8, 2, "Test Community 2", "456 Two Street", "Final", "Notes go here");
    }
    //endregion

    //region Builder Initializations
    private Builder initializeBuilder1() {
        return new Builder(1, "Redhawk Builders");
    }
    private Builder initializeBuilder2() {
        return new Builder(2, "MiamiU Builders");
    }
    //endregion
}
