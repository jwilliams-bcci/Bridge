package data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class DataManager {
    private static DataManager ourInstance = null;

    private List<Inspection> mInspections = new ArrayList<>();
    private List<Builder> mBuilders = new ArrayList<>();
    private List<Location> mLocations = new ArrayList<>();
    private List<Inspector> mInspectors = new ArrayList<>();
    private List<InspectionHistory> mInspectionHistories = new ArrayList<>();

    public static DataManager getInstance() {
        if(ourInstance == null) {
            ourInstance = new DataManager();
            ourInstance.initializeInspections();
            ourInstance.initializeBuilders();
            ourInstance.initializeLocations();
            ourInstance.initializeInspectors();
            ourInstance.initializeInspectionHistories();
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

    public List<Location> getLocations() {
        return mLocations;
    }
    public Location getLocation(int id) {
        for (Location location : mLocations) {
            if (id == location.getLocationId()) {
                return location;
            }
        }
        return null;
    }

    public List<Inspector> getInspectors() {
        return mInspectors;
    }
    public Inspector getInspector(int id) {
        for(Inspector inspector : mInspectors) {
            if (id == inspector.getInspectorId()) {
                return inspector;
            }
        }
        return null;
    }

    public List<InspectionHistory> getInspectionHistories() {
        return mInspectionHistories;
    }
    public List<InspectionHistory> getInspectionHistories(final int locationId) {
        return mInspectionHistories.stream().filter(p -> p.getLocationId() == locationId).collect(Collectors.toList());
    }
    public InspectionHistory getInspectionHistory(int id) {
        for(InspectionHistory inspectionHistory : mInspectionHistories) {
            if (id == inspectionHistory.getInspectionHistoryId()) {
                return inspectionHistory;
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

    private void initializeLocations() {
        mLocations.add(initializeLocation1());
        mLocations.add(initializeLocation2());
        mLocations.add(initializeLocation3());
        mLocations.add(initializeLocation4());
        mLocations.add(initializeLocation5());
        mLocations.add(initializeLocation6());
        mLocations.add(initializeLocation7());
        mLocations.add(initializeLocation8());
    }

    private void initializeInspectors() {
        mInspectors.add(initializeInspector1());
        mInspectors.add(initializeInspector2());
        mInspectors.add(initializeInspector3());
        mInspectors.add(initializeInspector4());
    }

    private void initializeInspectionHistories() {
        mInspectionHistories.add(initializeInspectionHistory1());
        mInspectionHistories.add(initializeInspectionHistory2());
        mInspectionHistories.add(initializeInspectionHistory3());
        mInspectionHistories.add(initializeInspectionHistory4());
        mInspectionHistories.add(initializeInspectionHistory5());
        mInspectionHistories.add(initializeInspectionHistory6());
        mInspectionHistories.add(initializeInspectionHistory7());
        mInspectionHistories.add(initializeInspectionHistory8());
        mInspectionHistories.add(initializeInspectionHistory9());
        mInspectionHistories.add(initializeInspectionHistory10());
        mInspectionHistories.add(initializeInspectionHistory11());
        mInspectionHistories.add(initializeInspectionHistory12());
        mInspectionHistories.add(initializeInspectionHistory13());
        mInspectionHistories.add(initializeInspectionHistory14());
        mInspectionHistories.add(initializeInspectionHistory15());
        mInspectionHistories.add(initializeInspectionHistory16());
        mInspectionHistories.add(initializeInspectionHistory17());
        mInspectionHistories.add(initializeInspectionHistory18());
        mInspectionHistories.add(initializeInspectionHistory19());
        mInspectionHistories.add(initializeInspectionHistory20());
        mInspectionHistories.add(initializeInspectionHistory21());
        mInspectionHistories.add(initializeInspectionHistory22());
        mInspectionHistories.add(initializeInspectionHistory23());
        mInspectionHistories.add(initializeInspectionHistory24());
    }

    //region Inspection Initializations
    private Inspection initializeInspection1() {
        return new Inspection(1, 1, "Thurgood Stubbs\n555-5555", 1, "Final", "Notes go here. Notes go here. Notes go here. Notes go here.");
    }
    private Inspection initializeInspection2() {
        return new Inspection(2, 1, "Thurgood Stubbs\n555-5555", 2, "Final", "Notes go here. Notes go here. Notes go here. Notes go here.");
    }
    private Inspection initializeInspection3() {
        return new Inspection(3, 2, "Thurgood Stubbs\n555-5555", 3, "Final", "Notes go here. Notes go here. Notes go here. Notes go here.");
    }
    private Inspection initializeInspection4() {
        return new Inspection(4, 2, "Thurgood Stubbs\n555-5555", 4, "Final", "Notes go here. Notes go here. Notes go here. Notes go here.");
    }
    private Inspection initializeInspection5() {
        return new Inspection(5, 1, "Remy Kuber\n777-7777", 5, "Final", "Notes go here. Notes go here. Notes go here. Notes go here.");
    }
    private Inspection initializeInspection6() {
        return new Inspection(6, 1, "Remy Kuber\n777-7777", 6, "Final", "Notes go here. Notes go here. Notes go here. Notes go here.");
    }
    private Inspection initializeInspection7() {
        return new Inspection(7, 2, "Remy Kuber\n777-7777", 7, "Final", "Notes go here. Notes go here. Notes go here. Notes go here.");
    }
    private Inspection initializeInspection8() {
        return new Inspection(8, 2, "Remy Kuber\n777-7777", 8, "Final", "Notes go here. Notes go here. Notes go here. Notes go here.");
    }
    //endregion

    //region Builder Initializations
    private Builder initializeBuilder1() {
        return new Builder(1, "Redhawk Builders");
    }
    private Builder initializeBuilder2() {
        return new Builder(2, "Miami U Builders");
    }
    //endregion

    //region Location Initializations
    private Location initializeLocation1() { return new Location(1, "123 One Road, Dallas, TX", 75068, "Dallas", "TX", "Test Community 1"); }
    private Location initializeLocation2() { return new Location(2, "234 One Road, Dallas, TX", 75068, "Dallas", "TX", "Test Community 1"); }
    private Location initializeLocation3() { return new Location(3, "345 One Road, Dallas, TX", 75068, "Dallas", "TX", "Test Community 1"); }
    private Location initializeLocation4() { return new Location(4, "456 One Road, Dallas, TX", 75068, "Dallas", "TX", "Test Community 1"); }
    private Location initializeLocation5() { return new Location(5, "123 Two Street, Dallas, TX", 75068, "Dallas", "TX", "Test Community 2"); }
    private Location initializeLocation6() { return new Location(6, "234 Two Street, Dallas, TX", 75068, "Dallas", "TX", "Test Community 2"); }
    private Location initializeLocation7() { return new Location(7, "345 Two Street, Dallas, TX", 75068, "Dallas", "TX", "Test Community 2"); }
    private Location initializeLocation8() { return new Location(8, "456 Two Street, Dallas, TX", 75068, "Dallas", "TX", "Test Community 2"); }
    //endregion

    //region Inspector Initializations
    private Inspector initializeInspector1() { return new Inspector(1, "Jerry Williams"); }
    private Inspector initializeInspector2() { return new Inspector(2, "Ricky Sandlin"); }
    private Inspector initializeInspector3() { return new Inspector(3, "Joe Bob"); }
    private Inspector initializeInspector4() { return new Inspector(4, "Sean John"); }
    //endregion

    //region InspectionHistory Initializations
    private InspectionHistory initializeInspectionHistory1() { return new InspectionHistory(1, 1, new Date(), "Pre-Drywall - loc 1", "Craig Brooks", "Passed"); }
    private InspectionHistory initializeInspectionHistory2() { return new InspectionHistory(2, 1, new Date(), "Pre-Drywall - loc 1", "Craig Brooks", "Failed"); }
    private InspectionHistory initializeInspectionHistory3() { return new InspectionHistory(3, 1, new Date(), "Pre-Pour - loc 1", "Craig Brooks", "Passed"); }
    private InspectionHistory initializeInspectionHistory4() { return new InspectionHistory(4, 2, new Date(), "Pre-Drywall - loc 2", "Craig Brooks", "Passed"); }
    private InspectionHistory initializeInspectionHistory5() { return new InspectionHistory(5, 2, new Date(), "Pre-Drywall - loc 2", "Craig Brooks", "Failed"); }
    private InspectionHistory initializeInspectionHistory6() { return new InspectionHistory(6, 2, new Date(), "Pre-Pour - loc 2", "Craig Brooks", "Passed"); }
    private InspectionHistory initializeInspectionHistory7() { return new InspectionHistory(7, 3, new Date(), "Pre-Drywall - loc 3", "Craig Brooks", "Passed"); }
    private InspectionHistory initializeInspectionHistory8() { return new InspectionHistory(8, 3, new Date(), "Pre-Drywall - loc 3", "Craig Brooks", "Failed"); }
    private InspectionHistory initializeInspectionHistory9() { return new InspectionHistory(9, 3, new Date(), "Pre-Pour - loc 3", "Craig Brooks", "Passed"); }
    private InspectionHistory initializeInspectionHistory10() { return new InspectionHistory(10, 4, new Date(), "Pre-Drywall - loc 4", "Craig Brooks", "Passed"); }
    private InspectionHistory initializeInspectionHistory11() { return new InspectionHistory(11, 4, new Date(), "Pre-Drywall - loc 4", "Craig Brooks", "Failed"); }
    private InspectionHistory initializeInspectionHistory12() { return new InspectionHistory(12, 4, new Date(), "Pre-Pour - loc 4", "Craig Brooks", "Passed"); }
    private InspectionHistory initializeInspectionHistory13() { return new InspectionHistory(13, 5, new Date(), "Pre-Drywall - loc 5", "Craig Brooks", "Passed"); }
    private InspectionHistory initializeInspectionHistory14() { return new InspectionHistory(14, 5, new Date(), "Pre-Drywall - loc 5", "Craig Brooks", "Failed"); }
    private InspectionHistory initializeInspectionHistory15() { return new InspectionHistory(15, 5, new Date(), "Pre-Pour - loc 5", "Craig Brooks", "Passed"); }
    private InspectionHistory initializeInspectionHistory16() { return new InspectionHistory(16, 6, new Date(), "Pre-Drywall - loc 6", "Craig Brooks", "Passed"); }
    private InspectionHistory initializeInspectionHistory17() { return new InspectionHistory(17, 6, new Date(), "Pre-Drywall - loc 6", "Craig Brooks", "Failed"); }
    private InspectionHistory initializeInspectionHistory18() { return new InspectionHistory(18, 6, new Date(), "Pre-Pour - loc 6", "Craig Brooks", "Passed"); }
    private InspectionHistory initializeInspectionHistory19() { return new InspectionHistory(19, 7, new Date(), "Pre-Drywall - loc 7", "Craig Brooks", "Passed"); }
    private InspectionHistory initializeInspectionHistory20() { return new InspectionHistory(20, 7, new Date(), "Pre-Drywall - loc 7", "Craig Brooks", "Failed"); }
    private InspectionHistory initializeInspectionHistory21() { return new InspectionHistory(21, 7, new Date(), "Pre-Pour - loc 7", "Craig Brooks", "Passed"); }
    private InspectionHistory initializeInspectionHistory22() { return new InspectionHistory(22, 8, new Date(), "Pre-Drywall - loc 8", "Craig Brooks", "Passed"); }
    private InspectionHistory initializeInspectionHistory23() { return new InspectionHistory(23, 8, new Date(), "Pre-Drywall - loc 8", "Craig Brooks", "Failed"); }
    private InspectionHistory initializeInspectionHistory24() { return new InspectionHistory(24, 8, new Date(), "Pre-Pour - loc 8", "Craig Brooks", "Passed"); }
    //endregion
}
