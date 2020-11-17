package data;

import java.lang.reflect.Array;
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
    private List<InspectionResolution> mInspectionResolutions = new ArrayList<>();
    private List<DefectCategory> mDefectCategories = new ArrayList<>();
    private List<DefectItem> mDefectItems = new ArrayList<>();
    private List mInspectDefectListItems = new ArrayList<>();
    private List<CannedComment> mCannedComments = new ArrayList<>();
    private List<Room> mRooms = new ArrayList<>();
    private List<Direction> mDirections = new ArrayList<>();

    public static DataManager getInstance() {
        if(ourInstance == null) {
            ourInstance = new DataManager();
            ourInstance.initializeInspections();
            ourInstance.initializeBuilders();
            ourInstance.initializeLocations();
            ourInstance.initializeInspectors();
            ourInstance.initializeInspectionHistories();
            ourInstance.initializeInspectionResolutions();
            ourInstance.initializeDefectCategories();
            ourInstance.initializeDefectItems();
            ourInstance.initializeInspectDefectList();
            ourInstance.initializeCannedComments();
            ourInstance.initializeRooms();
            ourInstance.initializeDirections();
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
        for (InspectionHistory inspectionHistory : mInspectionHistories) {
            if (id == inspectionHistory.getInspectionHistoryId()) {
                return inspectionHistory;
            }
        }
        return null;
    }

    public List<InspectionResolution> getInspectionResolutions() {
        return mInspectionResolutions;
    }
    public InspectionResolution getInspectionResolution(int id) {
        for (InspectionResolution inspectionResolution : mInspectionResolutions) {
            if (id == inspectionResolution.getInspectionResolutionId()) {
                return inspectionResolution;
            }
        }
        return null;
    }

    public List<DefectCategory> getDefectCategories() {
        return mDefectCategories;
    }
    public DefectCategory getDefectCategory(int id) {
        for (DefectCategory defectCategory : mDefectCategories) {
            if (id == defectCategory.getDefectCategoryId()) {
                return defectCategory;
            }
        }
        return null;
    }

    public List<DefectItem> getDefectItems() {
        return mDefectItems;
    }
    public List<DefectItem> getDefectItems(int categoryId) {
        return mDefectItems.stream().filter(p -> p.getDefectCategoryId() == categoryId).collect(Collectors.toList());
    }
    public DefectItem getDefectItem(int id) {
        for (DefectItem defectItem : mDefectItems) {
            if (id == defectItem.getDefectItemId()) {
                return defectItem;
            }
        }
        return null;
    }

    public List getInspectDefectList() {
        List listItems = new ArrayList();

        for (Object object : mDefectCategories) {
            DefectCategory category = (DefectCategory) object;
            listItems.add(category);
            listItems.addAll(getDefectItems(category.getDefectCategoryId()));
        }
        return listItems;
    }

    public List<CannedComment> getCannedComments() {
        return mCannedComments;
    }
    public CannedComment getCannedComment(int id) {
        for (CannedComment cannedComment : mCannedComments) {
            if (id == cannedComment.getCannedCommentId()) {
                return cannedComment;
            }
        }
        return null;
    }

    public List<Room> getRooms() {
        return mRooms;
    }
    public Room getRoom(int id) {
        for (Room room : mRooms) {
            if (id == room.getRoomId()) {
                return room;
            }
        }
        return null;
    }

    public List<Direction> getDirections() {
        return mDirections;
    }
    public Direction getDirection(int id) {
        for (Direction direction : mDirections) {
            if (id == direction.getDirectionId()) {
                return direction;
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

    private void initializeInspectionResolutions() {
        mInspectionResolutions.add(initializeInspectionResolution1());
        mInspectionResolutions.add(initializeInspectionResolution2());
        mInspectionResolutions.add(initializeInspectionResolution3());
    }

    private void initializeDefectCategories() {
        mDefectCategories.add(initializeDefectCategory1());
        mDefectCategories.add(initializeDefectCategory2());
        mDefectCategories.add(initializeDefectCategory3());
        mDefectCategories.add(initializeDefectCategory4());
        mDefectCategories.add(initializeDefectCategory5());
        mDefectCategories.add(initializeDefectCategory6());
        mDefectCategories.add(initializeDefectCategory7());
    }

    private void initializeDefectItems() {
        mDefectItems.add(initializeDefectItem1());
        mDefectItems.add(initializeDefectItem2());
        mDefectItems.add(initializeDefectItem3());
        mDefectItems.add(initializeDefectItem4());
        mDefectItems.add(initializeDefectItem5());
        mDefectItems.add(initializeDefectItem6());
        mDefectItems.add(initializeDefectItem7());
        mDefectItems.add(initializeDefectItem8());
        mDefectItems.add(initializeDefectItem9());
        mDefectItems.add(initializeDefectItem10());
        mDefectItems.add(initializeDefectItem11());
        mDefectItems.add(initializeDefectItem12());
        mDefectItems.add(initializeDefectItem13());
        mDefectItems.add(initializeDefectItem14());
        mDefectItems.add(initializeDefectItem15());
        mDefectItems.add(initializeDefectItem16());
        mDefectItems.add(initializeDefectItem17());
        mDefectItems.add(initializeDefectItem18());
        mDefectItems.add(initializeDefectItem19());
        mDefectItems.add(initializeDefectItem20());
        mDefectItems.add(initializeDefectItem21());
    }

    private void initializeInspectDefectList() {
        mInspectDefectListItems.addAll(mDefectCategories);
        mInspectDefectListItems.addAll(getDefectItems(1));
    }

    private void initializeCannedComments() {
        mCannedComments.add(initializeCannedComment1());
        mCannedComments.add(initializeCannedComment2());
        mCannedComments.add(initializeCannedComment3());
    }

    private void initializeRooms() {
        mRooms.add(initializeRoom1());
        mRooms.add(initializeRoom2());
        mRooms.add(initializeRoom3());
        mRooms.add(initializeRoom4());
        mRooms.add(initializeRoom5());
        mRooms.add(initializeRoom6());
        mRooms.add(initializeRoom7());
        mRooms.add(initializeRoom8());
        mRooms.add(initializeRoom9());
        mRooms.add(initializeRoom10());
        mRooms.add(initializeRoom11());
        mRooms.add(initializeRoom12());
        mRooms.add(initializeRoom13());
        mRooms.add(initializeRoom14());
        mRooms.add(initializeRoom15());
        mRooms.add(initializeRoom16());
        mRooms.add(initializeRoom17());
        mRooms.add(initializeRoom18());
        mRooms.add(initializeRoom19());
        mRooms.add(initializeRoom20());
        mRooms.add(initializeRoom21());
        mRooms.add(initializeRoom22());
        mRooms.add(initializeRoom23());
        mRooms.add(initializeRoom24());
        mRooms.add(initializeRoom25());
        mRooms.add(initializeRoom26());
        mRooms.add(initializeRoom27());
        mRooms.add(initializeRoom28());
        mRooms.add(initializeRoom29());
        mRooms.add(initializeRoom30());
        mRooms.add(initializeRoom31());
        mRooms.add(initializeRoom32());
        mRooms.add(initializeRoom33());
        mRooms.add(initializeRoom34());
        mRooms.add(initializeRoom35());
        mRooms.add(initializeRoom36());
        mRooms.add(initializeRoom37());
        mRooms.add(initializeRoom38());
        mRooms.add(initializeRoom39());
        mRooms.add(initializeRoom40());
        mRooms.add(initializeRoom41());
        mRooms.add(initializeRoom42());
        mRooms.add(initializeRoom43());
        mRooms.add(initializeRoom44());
        mRooms.add(initializeRoom45());
        mRooms.add(initializeRoom46());
        mRooms.add(initializeRoom47());
        mRooms.add(initializeRoom48());
        mRooms.add(initializeRoom49());
        mRooms.add(initializeRoom50());
        mRooms.add(initializeRoom51());
        mRooms.add(initializeRoom52());
        mRooms.add(initializeRoom53());
        mRooms.add(initializeRoom54());
        mRooms.add(initializeRoom55());
        mRooms.add(initializeRoom56());
        mRooms.add(initializeRoom57());
        mRooms.add(initializeRoom58());
        mRooms.add(initializeRoom59());
        mRooms.add(initializeRoom60());
        mRooms.add(initializeRoom61());
        mRooms.add(initializeRoom62());
        mRooms.add(initializeRoom63());
        mRooms.add(initializeRoom64());
        mRooms.add(initializeRoom65());
        mRooms.add(initializeRoom66());
        mRooms.add(initializeRoom68());
        mRooms.add(initializeRoom69());
        mRooms.add(initializeRoom70());
        mRooms.add(initializeRoom71());
        mRooms.add(initializeRoom72());
        mRooms.add(initializeRoom73());
        mRooms.add(initializeRoom74());
        mRooms.add(initializeRoom75());
        mRooms.add(initializeRoom76());
        mRooms.add(initializeRoom77());
        mRooms.add(initializeRoom78());
        mRooms.add(initializeRoom79());
        mRooms.add(initializeRoom80());
        mRooms.add(initializeRoom81());
        mRooms.add(initializeRoom86());
        mRooms.add(initializeRoom67());
        mRooms.add(initializeRoom82());
        mRooms.add(initializeRoom83());
        mRooms.add(initializeRoom84());
        mRooms.add(initializeRoom85());
    }

    private void initializeDirections() {
        mDirections.add(initializeDirection1());
        mDirections.add(initializeDirection2());
        mDirections.add(initializeDirection3());
        mDirections.add(initializeDirection4());
        mDirections.add(initializeDirection5());
        mDirections.add(initializeDirection6());
        mDirections.add(initializeDirection7());
        mDirections.add(initializeDirection8());
        mDirections.add(initializeDirection9());
        mDirections.add(initializeDirection10());
        mDirections.add(initializeDirection11());
        mDirections.add(initializeDirection12());
        mDirections.add(initializeDirection13());
        mDirections.add(initializeDirection14());
        mDirections.add(initializeDirection15());
        mDirections.add(initializeDirection16());
        mDirections.add(initializeDirection17());
        mDirections.add(initializeDirection18());
        mDirections.add(initializeDirection19());
        mDirections.add(initializeDirection20());
        mDirections.add(initializeDirection21());
        mDirections.add(initializeDirection22());
        mDirections.add(initializeDirection23());
        mDirections.add(initializeDirection24());
        mDirections.add(initializeDirection25());
        mDirections.add(initializeDirection26());
        mDirections.add(initializeDirection27());
        mDirections.add(initializeDirection28());
        mDirections.add(initializeDirection29());
        mDirections.add(initializeDirection30());
        mDirections.add(initializeDirection31());
        mDirections.add(initializeDirection32());
        mDirections.add(initializeDirection33());
        mDirections.add(initializeDirection34());
        mDirections.add(initializeDirection35());
        mDirections.add(initializeDirection36());
        mDirections.add(initializeDirection37());
        mDirections.add(initializeDirection38());
        mDirections.add(initializeDirection39());
        mDirections.add(initializeDirection40());
        mDirections.add(initializeDirection41());
        mDirections.add(initializeDirection42());
        mDirections.add(initializeDirection43());
        mDirections.add(initializeDirection44());
        mDirections.add(initializeDirection45());
        mDirections.add(initializeDirection46());
        mDirections.add(initializeDirection47());
        mDirections.add(initializeDirection48());
        mDirections.add(initializeDirection49());
        mDirections.add(initializeDirection50());
        mDirections.add(initializeDirection51());
        mDirections.add(initializeDirection52());
        mDirections.add(initializeDirection53());
        mDirections.add(initializeDirection54());
        mDirections.add(initializeDirection55());
        mDirections.add(initializeDirection56());
        mDirections.add(initializeDirection57());
        mDirections.add(initializeDirection58());
        mDirections.add(initializeDirection59());
        mDirections.add(initializeDirection60());
        mDirections.add(initializeDirection61());
        mDirections.add(initializeDirection62());
        mDirections.add(initializeDirection63());
        mDirections.add(initializeDirection64());
        mDirections.add(initializeDirection65());
        mDirections.add(initializeDirection66());
        mDirections.add(initializeDirection67());
        mDirections.add(initializeDirection68());
        mDirections.add(initializeDirection69());
        mDirections.add(initializeDirection70());
        mDirections.add(initializeDirection71());
        mDirections.add(initializeDirection72());
        mDirections.add(initializeDirection73());
        mDirections.add(initializeDirection74());
        mDirections.add(initializeDirection75());
        mDirections.add(initializeDirection76());
        mDirections.add(initializeDirection77());
        mDirections.add(initializeDirection78());
        mDirections.add(initializeDirection79());
        mDirections.add(initializeDirection80());
        mDirections.add(initializeDirection81());
        mDirections.add(initializeDirection82());
        mDirections.add(initializeDirection83());
        mDirections.add(initializeDirection84());
        mDirections.add(initializeDirection85());
        mDirections.add(initializeDirection86());
        mDirections.add(initializeDirection87());
        mDirections.add(initializeDirection88());
        mDirections.add(initializeDirection89());
        mDirections.add(initializeDirection90());
        mDirections.add(initializeDirection91());
        mDirections.add(initializeDirection92());
        mDirections.add(initializeDirection93());
        mDirections.add(initializeDirection94());
        mDirections.add(initializeDirection95());
        mDirections.add(initializeDirection96());
        mDirections.add(initializeDirection97());
        mDirections.add(initializeDirection98());
        mDirections.add(initializeDirection99());
        mDirections.add(initializeDirection100());
        mDirections.add(initializeDirection101());
        mDirections.add(initializeDirection102());
        mDirections.add(initializeDirection103());
        mDirections.add(initializeDirection104());
        mDirections.add(initializeDirection105());
        mDirections.add(initializeDirection106());
        mDirections.add(initializeDirection107());
        mDirections.add(initializeDirection108());
        mDirections.add(initializeDirection109());
        mDirections.add(initializeDirection110());
        mDirections.add(initializeDirection111());
        mDirections.add(initializeDirection112());
        mDirections.add(initializeDirection113());
        mDirections.add(initializeDirection114());
        mDirections.add(initializeDirection115());
        mDirections.add(initializeDirection116());
        mDirections.add(initializeDirection117());
        mDirections.add(initializeDirection118());
        mDirections.add(initializeDirection119());
        mDirections.add(initializeDirection120());
        mDirections.add(initializeDirection121());
        mDirections.add(initializeDirection122());
        mDirections.add(initializeDirection123());
        mDirections.add(initializeDirection124());
        mDirections.add(initializeDirection125());
        mDirections.add(initializeDirection126());
        mDirections.add(initializeDirection127());
        mDirections.add(initializeDirection128());
        mDirections.add(initializeDirection129());
        mDirections.add(initializeDirection130());
        mDirections.add(initializeDirection131());
        mDirections.add(initializeDirection132());
        mDirections.add(initializeDirection133());
        mDirections.add(initializeDirection134());
        mDirections.add(initializeDirection135());
        mDirections.add(initializeDirection136());
        mDirections.add(initializeDirection137());
        mDirections.add(initializeDirection138());
        mDirections.add(initializeDirection139());
        mDirections.add(initializeDirection140());
        mDirections.add(initializeDirection142());
        mDirections.add(initializeDirection143());
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

    //region InspectionResolution Initializations
    private InspectionResolution initializeInspectionResolution1() { return new InspectionResolution(1, "Cancel"); }
    private InspectionResolution initializeInspectionResolution2() { return new InspectionResolution(2, "Defer"); }
    private InspectionResolution initializeInspectionResolution3() { return new InspectionResolution(3, "Not Ready"); }
    //endregion

    //region DefectCategory Initializations
    private DefectCategory initializeDefectCategory1() { return new DefectCategory(1, "Job Site"); }
    private DefectCategory initializeDefectCategory2() { return new DefectCategory(1, "Frame"); }
    private DefectCategory initializeDefectCategory3() { return new DefectCategory(1, "Cornice/Deck"); }
    private DefectCategory initializeDefectCategory4() { return new DefectCategory(1, "Rough Roofing"); }
    private DefectCategory initializeDefectCategory5() { return new DefectCategory(1, "Rough Plumbing"); }
    private DefectCategory initializeDefectCategory6() { return new DefectCategory(1, "Rough HVAC"); }
    private DefectCategory initializeDefectCategory7() { return new DefectCategory(1, "Rough Electrical"); }
    //endregion

    //region DefectItem Initializations
    private DefectItem initializeDefectItem1() { return new DefectItem(1, 1, 1, "Address posted."); }
    private DefectItem initializeDefectItem2() { return new DefectItem(2, 1, 6, "Cable pockets grouted"); }
    private DefectItem initializeDefectItem3() { return new DefectItem(3, 1, 5, "Cables stressed"); }
    private DefectItem initializeDefectItem4() { return new DefectItem(4, 2, 1, "Manually Entered Inspection Item"); }
    private DefectItem initializeDefectItem5() { return new DefectItem(5, 2, 37, "Attic access sized properly"); }
    private DefectItem initializeDefectItem6() { return new DefectItem(6, 2, 51, "Attic Insulation baffles"); }
    private DefectItem initializeDefectItem7() { return new DefectItem(7, 3, 1, "Manually Entered Inspection Item"); }
    private DefectItem initializeDefectItem8() { return new DefectItem(8, 3, 63, "Brick frieze installed properly"); }
    private DefectItem initializeDefectItem9() { return new DefectItem(9, 3, 55, "Brick returns"); }
    private DefectItem initializeDefectItem10() { return new DefectItem(10, 4, 1, "Manually Entered Inspection Item"); }
    private DefectItem initializeDefectItem11() { return new DefectItem(11, 4, 77, "Air vents"); }
    private DefectItem initializeDefectItem12() { return new DefectItem(12, 4, 73, "Boots"); }
    private DefectItem initializeDefectItem13() { return new DefectItem(13, 5, 1, "Manually Entered Inspection Item"); }
    private DefectItem initializeDefectItem14() { return new DefectItem(14, 5, 86, "Clean-outs capped"); }
    private DefectItem initializeDefectItem15() { return new DefectItem(15, 5, 87, "Drain arms have fall"); }
    private DefectItem initializeDefectItem16() { return new DefectItem(16, 6, 1, "Manually Entered Inspection Item"); }
    private DefectItem initializeDefectItem17() { return new DefectItem(17, 6, 101, "Catwalk 24 in. wide to furnace"); }
    private DefectItem initializeDefectItem18() { return new DefectItem(18, 6, 95, "Ducts installed properly"); }
    private DefectItem initializeDefectItem19() { return new DefectItem(19, 7, 1, "Manually Entered Inspection Item"); }
    private DefectItem initializeDefectItem20() { return new DefectItem(20, 7, 105, "Boxes"); }
    private DefectItem initializeDefectItem21() { return new DefectItem(21, 7, 113, "Outlets must be >18 in. from firebox"); }
    //endregion

    //region CannedComment Initializations
    private CannedComment initializeCannedComment1() { return new CannedComment(1, "Canned Comment 1"); }
    private CannedComment initializeCannedComment2() { return new CannedComment(2, "Canned Comment 2"); }
    private CannedComment initializeCannedComment3() { return new CannedComment(3, "Canned Comment 3"); }
    //endregion

    //region Room Initializations
    private Room initializeRoom1() { return new Room(1, "Arch"); }
    private Room initializeRoom2() { return new Room(2, "Attic-Drop Down"); }
    private Room initializeRoom3() { return new Room(3, "Attic-Walk-In"); }
    private Room initializeRoom4() { return new Room(4, "Bar"); }
    private Room initializeRoom5() { return new Room(5, "Basement"); }
    private Room initializeRoom6() { return new Room(6, "Bath"); }
    private Room initializeRoom7() { return new Room(7, "Bath-Hall"); }
    private Room initializeRoom8() { return new Room(8, "Bath-J&J"); }
    private Room initializeRoom9() { return new Room(9, "Bath-Mstr"); }
    private Room initializeRoom10() { return new Room(10, "Bath-Powder"); }
    private Room initializeRoom11() { return new Room(11, "Bedroom"); }
    private Room initializeRoom12() { return new Room(12, "Bedroom-Mstr"); }
    private Room initializeRoom13() { return new Room(13, "Bonus Room"); }
    private Room initializeRoom14() { return new Room(14, "Breakfast Nook"); }
    private Room initializeRoom15() { return new Room(15, "Breezeway"); }
    private Room initializeRoom16() { return new Room(16, "Butler's Pantry"); }
    private Room initializeRoom17() { return new Room(17, "Cantilever"); }
    private Room initializeRoom18() { return new Room(18, "Childrens Retreat"); }
    private Room initializeRoom19() { return new Room(19, "Closet"); }
    private Room initializeRoom20() { return new Room(20, "Closet-Linen"); }
    private Room initializeRoom21() { return new Room(21, "Closet-Mstr"); }
    private Room initializeRoom22() { return new Room(22, "Column"); }
    private Room initializeRoom23() { return new Room(23, "Crawl Space"); }
    private Room initializeRoom24() { return new Room(24, "Deck"); }
    private Room initializeRoom25() { return new Room(25, "Dining Room"); }
    private Room initializeRoom26() { return new Room(26, "Door-Front"); }
    private Room initializeRoom27() { return new Room(27, "Door-Garage"); }
    private Room initializeRoom28() { return new Room(28, "Door-Patio"); }
    private Room initializeRoom29() { return new Room(29, "Dormer"); }
    private Room initializeRoom30() { return new Room(30, "Driveway"); }
    private Room initializeRoom31() { return new Room(31, "Entry"); }
    private Room initializeRoom32() { return new Room(32, "Exterior"); }
    private Room initializeRoom33() { return new Room(33, "Family Room"); }
    private Room initializeRoom34() { return new Room(34, "Floor"); }
    private Room initializeRoom35() { return new Room(35, "Foyer"); }
    private Room initializeRoom36() { return new Room(36, "Gameroom"); }
    private Room initializeRoom37() { return new Room(37, "Garage Door"); }
    private Room initializeRoom38() { return new Room(38, "Garage"); }
    private Room initializeRoom39() { return new Room(39, "Garage-1 Car"); }
    private Room initializeRoom40() { return new Room(40, "Garage-2 Car"); }
    private Room initializeRoom41() { return new Room(41, "Guest House"); }
    private Room initializeRoom42() { return new Room(42, "Hallway"); }
    private Room initializeRoom43() { return new Room(43, "Interior"); }
    private Room initializeRoom44() { return new Room(44, "Island"); }
    private Room initializeRoom45() { return new Room(45, "Kitchen"); }
    private Room initializeRoom46() { return new Room(46, "Landing"); }
    private Room initializeRoom47() { return new Room(47, "Living Room"); }
    private Room initializeRoom48() { return new Room(48, "Media Room"); }
    private Room initializeRoom49() { return new Room(49, "Microlam"); }
    private Room initializeRoom50() { return new Room(50, "Niche"); }
    private Room initializeRoom51() { return new Room(51, "Nook"); }
    private Room initializeRoom52() { return new Room(52, "Office"); }
    private Room initializeRoom53() { return new Room(53, "OSB"); }
    private Room initializeRoom54() { return new Room(54, "Pantry"); }
    private Room initializeRoom55() { return new Room(55, "Patio"); }
    private Room initializeRoom56() { return new Room(56, "Plywood"); }
    private Room initializeRoom57() { return new Room(57, "Porch"); }
    private Room initializeRoom58() { return new Room(58, "Room"); }
    private Room initializeRoom59() { return new Room(59, "Stairway"); }
    private Room initializeRoom60() { return new Room(60, "Study"); }
    private Room initializeRoom61() { return new Room(61, "Sub-Floor"); }
    private Room initializeRoom62() { return new Room(62, "Sun Room"); }
    private Room initializeRoom63() { return new Room(63, "Utility"); }
    private Room initializeRoom64() { return new Room(64, "Walkway"); }
    private Room initializeRoom65() { return new Room(65, "Wet Bar"); }
    private Room initializeRoom66() { return new Room(66, "Wine Cellar"); }
    private Room initializeRoom68() { return new Room(68, "2x6"); }
    private Room initializeRoom69() { return new Room(69, "2x8"); }
    private Room initializeRoom70() { return new Room(70, "2x10"); }
    private Room initializeRoom71() { return new Room(71, "2x12"); }
    private Room initializeRoom72() { return new Room(72, "2x4"); }
    private Room initializeRoom73() { return new Room(73, "Bedroom Closet"); }
    private Room initializeRoom74() { return new Room(74, "Closet-Bedroom"); }
    private Room initializeRoom75() { return new Room(75, "Laundry"); }
    private Room initializeRoom76() { return new Room(76, "Attic"); }
    private Room initializeRoom77() { return new Room(77, "Bath-Mstr Commode Closet"); }
    private Room initializeRoom78() { return new Room(78, "Front Door"); }
    private Room initializeRoom79() { return new Room(79, "Patio Door"); }
    private Room initializeRoom80() { return new Room(80, "Service Door"); }
    private Room initializeRoom81() { return new Room(81, "Overhead Door"); }
    private Room initializeRoom86() { return new Room(86, "Loft"); }
    private Room initializeRoom67() { return new Room(67, "Bedroom bath"); }
    private Room initializeRoom82() { return new Room(82, "Kitchen island"); }
    private Room initializeRoom83() { return new Room(83, "Kitchen pantry"); }
    private Room initializeRoom84() { return new Room(84, "Garage service door"); }
    private Room initializeRoom85() { return new Room(85, "Interior doors"); }
    //endregion

    //region Direction Initializations
    private Direction initializeDirection1() { return new Direction(1, "Up & Down"); }
    private Direction initializeDirection2() { return new Direction(2, "Above"); }
    private Direction initializeDirection3() { return new Direction(3, "and"); }
    private Direction initializeDirection4() { return new Direction(4, "Left"); }
    private Direction initializeDirection5() { return new Direction(5, "Right"); }
    private Direction initializeDirection6() { return new Direction(6, "Left & Right"); }
    private Direction initializeDirection7() { return new Direction(7, "Front"); }
    private Direction initializeDirection8() { return new Direction(8, "Rear"); }
    private Direction initializeDirection9() { return new Direction(9, "Front & Rear"); }
    private Direction initializeDirection10() { return new Direction(10, "Up"); }
    private Direction initializeDirection11() { return new Direction(11, "Down"); }
    private Direction initializeDirection12() { return new Direction(12, "Middle"); }
    private Direction initializeDirection13() { return new Direction(13, "Top"); }
    private Direction initializeDirection14() { return new Direction(14, "Bottom"); }
    private Direction initializeDirection15() { return new Direction(15, "Center"); }
    private Direction initializeDirection16() { return new Direction(16, "Over"); }
    private Direction initializeDirection17() { return new Direction(17, "Under"); }
    private Direction initializeDirection18() { return new Direction(18, "Upper"); }
    private Direction initializeDirection19() { return new Direction(19, "Side"); }
    private Direction initializeDirection20() { return new Direction(20, "Inside"); }
    private Direction initializeDirection21() { return new Direction(21, "All"); }
    private Direction initializeDirection22() { return new Direction(22, "Adjacent"); }
    private Direction initializeDirection23() { return new Direction(23, "Outside"); }
    private Direction initializeDirection24() { return new Direction(24, "of"); }
    private Direction initializeDirection25() { return new Direction(25, "at"); }
    private Direction initializeDirection26() { return new Direction(26, "at 45°"); }
    private Direction initializeDirection27() { return new Direction(27, "between"); }
    private Direction initializeDirection28() { return new Direction(28, "A/C"); }
    private Direction initializeDirection29() { return new Direction(29, "Air Barrier"); }
    private Direction initializeDirection30() { return new Direction(30, "Access Cover"); }
    private Direction initializeDirection31() { return new Direction(31, "Arch Window"); }
    private Direction initializeDirection32() { return new Direction(32, "Art Niche"); }
    private Direction initializeDirection33() { return new Direction(33, "Attic Access"); }
    private Direction initializeDirection34() { return new Direction(34, "Backside"); }
    private Direction initializeDirection35() { return new Direction(35, "Backsplash"); }
    private Direction initializeDirection36() { return new Direction(36, "Balcony"); }
    private Direction initializeDirection37() { return new Direction(37, "Base"); }
    private Direction initializeDirection38() { return new Direction(38, "Behind"); }
    private Direction initializeDirection39() { return new Direction(39, "Box"); }
    private Direction initializeDirection40() { return new Direction(40, "Brick"); }
    private Direction initializeDirection41() { return new Direction(41, "Brick Pocket"); }
    private Direction initializeDirection42() { return new Direction(42, "Cabinet"); }
    private Direction initializeDirection43() { return new Direction(43, "Casing"); }
    private Direction initializeDirection44() { return new Direction(44, "Catwalk"); }
    private Direction initializeDirection45() { return new Direction(45, "Ceiling"); }
    private Direction initializeDirection46() { return new Direction(46, "Chase"); }
    private Direction initializeDirection47() { return new Direction(47, "Chimney"); }
    private Direction initializeDirection48() { return new Direction(48, "Chimney Cap"); }
    private Direction initializeDirection49() { return new Direction(49, "City walk"); }
    private Direction initializeDirection50() { return new Direction(50, "Clean-out"); }
    private Direction initializeDirection51() { return new Direction(51, "Coach Light"); }
    private Direction initializeDirection52() { return new Direction(52, "Cold"); }
    private Direction initializeDirection53() { return new Direction(53, "Commode"); }
    private Direction initializeDirection54() { return new Direction(54, "Condenser"); }
    private Direction initializeDirection55() { return new Direction(55, "Cooktop"); }
    private Direction initializeDirection56() { return new Direction(56, "Corner"); }
    private Direction initializeDirection57() { return new Direction(57, "Counter"); }
    private Direction initializeDirection58() { return new Direction(58, "Crown"); }
    private Direction initializeDirection59() { return new Direction(59, "Dishwasher"); }
    private Direction initializeDirection60() { return new Direction(60, "Door"); }
    private Direction initializeDirection61() { return new Direction(61, "Dormer"); }
    private Direction initializeDirection62() { return new Direction(62, "Drain"); }
    private Direction initializeDirection63() { return new Direction(63, "East"); }
    private Direction initializeDirection64() { return new Direction(64, "Electrical Panel"); }
    private Direction initializeDirection65() { return new Direction(65, "Entertainment Center"); }
    private Direction initializeDirection66() { return new Direction(66, "Exterior"); }
    private Direction initializeDirection67() { return new Direction(67, "Faucet"); }
    private Direction initializeDirection68() { return new Direction(68, "Fireplace"); }
    private Direction initializeDirection69() { return new Direction(69, "Framing Material"); }
    private Direction initializeDirection70() { return new Direction(70, "Furnace"); }
    private Direction initializeDirection71() { return new Direction(71, "Furr Out"); }
    private Direction initializeDirection72() { return new Direction(72, "Gable"); }
    private Direction initializeDirection73() { return new Direction(73, "Gusset Plate"); }
    private Direction initializeDirection74() { return new Direction(74, "Hall"); }
    private Direction initializeDirection75() { return new Direction(75, "Head Wall"); }
    private Direction initializeDirection76() { return new Direction(76, "Header"); }
    private Direction initializeDirection77() { return new Direction(77, "Hers"); }
    private Direction initializeDirection78() { return new Direction(78, "Hip"); }
    private Direction initializeDirection79() { return new Direction(79, "His"); }
    private Direction initializeDirection80() { return new Direction(80, "Hose bib"); }
    private Direction initializeDirection81() { return new Direction(81, "Hot"); }
    private Direction initializeDirection82() { return new Direction(82, "House Wrap"); }
    private Direction initializeDirection83() { return new Direction(83, "Jump walk"); }
    private Direction initializeDirection84() { return new Direction(84, "Lead walk"); }
    private Direction initializeDirection85() { return new Direction(85, "Light"); }
    private Direction initializeDirection86() { return new Direction(86, "Linen"); }
    private Direction initializeDirection87() { return new Direction(87, "Lintel"); }
    private Direction initializeDirection88() { return new Direction(88, "Lock"); }
    private Direction initializeDirection89() { return new Direction(89, "Log Lighter"); }
    private Direction initializeDirection90() { return new Direction(90, "Main"); }
    private Direction initializeDirection91() { return new Direction(91, "Manifold"); }
    private Direction initializeDirection92() { return new Direction(92, "Microwave"); }
    private Direction initializeDirection93() { return new Direction(93, "Mirror"); }
    private Direction initializeDirection94() { return new Direction(94, "Near"); }
    private Direction initializeDirection95() { return new Direction(95, "North"); }
    private Direction initializeDirection96() { return new Direction(96, "Overflow Drain"); }
    private Direction initializeDirection97() { return new Direction(97, "Parallel"); }
    private Direction initializeDirection98() { return new Direction(98, "Phone Jack"); }
    private Direction initializeDirection99() { return new Direction(99, "Rake"); }
    private Direction initializeDirection100() { return new Direction(100, "Recessed Can"); }
    private Direction initializeDirection101() { return new Direction(101, "Refrigerator"); }
    private Direction initializeDirection102() { return new Direction(102, "Return"); }
    private Direction initializeDirection103() { return new Direction(103, "Return Air"); }
    private Direction initializeDirection104() { return new Direction(104, "Ridge"); }
    private Direction initializeDirection105() { return new Direction(105, "Rim Board"); }
    private Direction initializeDirection106() { return new Direction(106, "Roof"); }
    private Direction initializeDirection107() { return new Direction(107, "Screws"); }
    private Direction initializeDirection108() { return new Direction(108, "Shingles"); }
    private Direction initializeDirection109() { return new Direction(109, "Shoe Moulding"); }
    private Direction initializeDirection110() { return new Direction(110, "Shower"); }
    private Direction initializeDirection111() { return new Direction(111, "Shower pan"); }
    private Direction initializeDirection112() { return new Direction(112, "Sink"); }
    private Direction initializeDirection113() { return new Direction(113, "South"); }
    private Direction initializeDirection114() { return new Direction(114, "Splice"); }
    private Direction initializeDirection115() { return new Direction(115, "Step"); }
    private Direction initializeDirection116() { return new Direction(116, "Stringer"); }
    private Direction initializeDirection117() { return new Direction(117, "Supply"); }
    private Direction initializeDirection118() { return new Direction(118, "Switch Plate"); }
    private Direction initializeDirection119() { return new Direction(119, "Third Floor"); }
    private Direction initializeDirection120() { return new Direction(120, "Throughout"); }
    private Direction initializeDirection121() { return new Direction(121, "Toilet Paper"); }
    private Direction initializeDirection122() { return new Direction(122, "Towel Bar"); }
    private Direction initializeDirection123() { return new Direction(123, "Tread"); }
    private Direction initializeDirection124() { return new Direction(124, "Trim"); }
    private Direction initializeDirection125() { return new Direction(125, "Tub"); }
    private Direction initializeDirection126() { return new Direction(126, "Under Cabinet"); }
    private Direction initializeDirection127() { return new Direction(127, "Unit 1"); }
    private Direction initializeDirection128() { return new Direction(128, "Unit 2"); }
    private Direction initializeDirection129() { return new Direction(129, "Unit 3"); }
    private Direction initializeDirection130() { return new Direction(130, "Valley"); }
    private Direction initializeDirection131() { return new Direction(131, "Vanity"); }
    private Direction initializeDirection132() { return new Direction(132, "Vent"); }
    private Direction initializeDirection133() { return new Direction(133, "Wall"); }
    private Direction initializeDirection134() { return new Direction(134, "Washer Box"); }
    private Direction initializeDirection135() { return new Direction(135, "Water Heater"); }
    private Direction initializeDirection136() { return new Direction(136, "West"); }
    private Direction initializeDirection137() { return new Direction(137, "Window"); }
    private Direction initializeDirection138() { return new Direction(138, "PVC"); }
    private Direction initializeDirection139() { return new Direction(139, "T&P"); }
    private Direction initializeDirection140() { return new Direction(140, "Vacuum Breaker"); }
    private Direction initializeDirection142() { return new Direction(142, "Smoke Detector"); }
    private Direction initializeDirection143() { return new Direction(143, "Doorbell"); }
    //endregion
}