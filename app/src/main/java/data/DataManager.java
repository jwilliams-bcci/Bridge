package data;

import java.lang.reflect.Array;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import data.Tables.CannedComment_Table;
import data.Tables.DefectItem_Table;

public class DataManager {
    private static DataManager ourInstance = null;

    private List<Room> mRooms = new ArrayList<>();
    private List<Direction> mDirections = new ArrayList<>();
    private List<Fault> mFaults = new ArrayList<>();

    public static DataManager getInstance() {
        if(ourInstance == null) {
            ourInstance = new DataManager();
            ourInstance.initializeRooms();
            ourInstance.initializeDirections();
            ourInstance.initializeFaults();
        }
        return ourInstance;
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
        mDirections.add(initializeDirection14());
        mDirections.add(initializeDirection15());
        mDirections.add(initializeDirection103());
        mDirections.add(initializeDirection20());
        mDirections.add(initializeDirection11());
        mDirections.add(initializeDirection10());
        mDirections.add(initializeDirection12());
        mDirections.add(initializeDirection19());
        mDirections.add(initializeDirection18());
        mDirections.add(initializeDirection17());
        mDirections.add(initializeDirection21());
        mDirections.add(initializeDirection6());
        mDirections.add(initializeDirection131());
        mDirections.add(initializeDirection55());
        mDirections.add(initializeDirection7());
        mDirections.add(initializeDirection13());
        mDirections.add(initializeDirection92());
        mDirections.add(initializeDirection8());
        mDirections.add(initializeDirection133());
        mDirections.add(initializeDirection143());
        mDirections.add(initializeDirection16());
        mDirections.add(initializeDirection30());
        mDirections.add(initializeDirection128());
        mDirections.add(initializeDirection129());
        mDirections.add(initializeDirection130());
        mDirections.add(initializeDirection2());
        mDirections.add(initializeDirection5());
        mDirections.add(initializeDirection3());
        mDirections.add(initializeDirection9());
        mDirections.add(initializeDirection1());
        mDirections.add(initializeDirection4());
        mDirections.add(initializeDirection22());
        mDirections.add(initializeDirection23());
        mDirections.add(initializeDirection24());
        mDirections.add(initializeDirection25());
        mDirections.add(initializeDirection26());
        mDirections.add(initializeDirection27());
        mDirections.add(initializeDirection28());
        mDirections.add(initializeDirection29());
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
        mDirections.add(initializeDirection132());
        mDirections.add(initializeDirection134());
        mDirections.add(initializeDirection135());
        mDirections.add(initializeDirection136());
        mDirections.add(initializeDirection137());
        mDirections.add(initializeDirection138());
        mDirections.add(initializeDirection139());
        mDirections.add(initializeDirection140());
        mDirections.add(initializeDirection141());
        mDirections.add(initializeDirection142());
    }
    private void initializeFaults() {
        mFaults.add(initializeFault1());
        mFaults.add(initializeFault2());
        mFaults.add(initializeFault3());
        mFaults.add(initializeFault4());
        mFaults.add(initializeFault5());
        mFaults.add(initializeFault6());
        mFaults.add(initializeFault7());
        mFaults.add(initializeFault8());
        mFaults.add(initializeFault9());
        mFaults.add(initializeFault10());
        mFaults.add(initializeFault11());
        mFaults.add(initializeFault12());
        mFaults.add(initializeFault13());
        mFaults.add(initializeFault14());
        mFaults.add(initializeFault15());
        mFaults.add(initializeFault16());
        mFaults.add(initializeFault17());
        mFaults.add(initializeFault18());
        mFaults.add(initializeFault19());
        mFaults.add(initializeFault20());
        mFaults.add(initializeFault21());
        mFaults.add(initializeFault22());
        mFaults.add(initializeFault23());
        mFaults.add(initializeFault24());
        mFaults.add(initializeFault25());
        mFaults.add(initializeFault26());
        mFaults.add(initializeFault27());
        mFaults.add(initializeFault28());
        mFaults.add(initializeFault29());
        mFaults.add(initializeFault30());
        mFaults.add(initializeFault31());
        mFaults.add(initializeFault32());
        mFaults.add(initializeFault33());
        mFaults.add(initializeFault34());
        mFaults.add(initializeFault35());
        mFaults.add(initializeFault36());
        mFaults.add(initializeFault37());
        mFaults.add(initializeFault38());
        mFaults.add(initializeFault39());
        mFaults.add(initializeFault40());
        mFaults.add(initializeFault41());
        mFaults.add(initializeFault42());
        mFaults.add(initializeFault43());
        mFaults.add(initializeFault44());
        mFaults.add(initializeFault45());
        mFaults.add(initializeFault46());
        mFaults.add(initializeFault47());
        mFaults.add(initializeFault48());
        mFaults.add(initializeFault49());
        mFaults.add(initializeFault50());
        mFaults.add(initializeFault51());
        mFaults.add(initializeFault52());
        mFaults.add(initializeFault53());
        mFaults.add(initializeFault54());
        mFaults.add(initializeFault55());
        mFaults.add(initializeFault56());
        mFaults.add(initializeFault57());
        mFaults.add(initializeFault58());
        mFaults.add(initializeFault59());
        mFaults.add(initializeFault60());
        mFaults.add(initializeFault61());
        mFaults.add(initializeFault62());
        mFaults.add(initializeFault63());
        mFaults.add(initializeFault64());
        mFaults.add(initializeFault65());
        mFaults.add(initializeFault66());
        mFaults.add(initializeFault67());
        mFaults.add(initializeFault68());
        mFaults.add(initializeFault69());
        mFaults.add(initializeFault70());
        mFaults.add(initializeFault71());
        mFaults.add(initializeFault72());
        mFaults.add(initializeFault73());
        mFaults.add(initializeFault74());
        mFaults.add(initializeFault75());
        mFaults.add(initializeFault76());
        mFaults.add(initializeFault77());
        mFaults.add(initializeFault78());
        mFaults.add(initializeFault79());
        mFaults.add(initializeFault80());
        mFaults.add(initializeFault81());
        mFaults.add(initializeFault82());
        mFaults.add(initializeFault83());
        mFaults.add(initializeFault84());
        mFaults.add(initializeFault85());
        mFaults.add(initializeFault86());
        mFaults.add(initializeFault87());
        mFaults.add(initializeFault88());
        mFaults.add(initializeFault89());
        mFaults.add(initializeFault90());
        mFaults.add(initializeFault91());
        mFaults.add(initializeFault92());
        mFaults.add(initializeFault93());
        mFaults.add(initializeFault94());
        mFaults.add(initializeFault95());
        mFaults.add(initializeFault96());
        mFaults.add(initializeFault97());
        mFaults.add(initializeFault98());
        mFaults.add(initializeFault99());
        mFaults.add(initializeFault100());
        mFaults.add(initializeFault101());
        mFaults.add(initializeFault102());
        mFaults.add(initializeFault103());
        mFaults.add(initializeFault104());
        mFaults.add(initializeFault105());
        mFaults.add(initializeFault106());
        mFaults.add(initializeFault107());
        mFaults.add(initializeFault108());
        mFaults.add(initializeFault109());
        mFaults.add(initializeFault110());
        mFaults.add(initializeFault111());
        mFaults.add(initializeFault112());
        mFaults.add(initializeFault113());
        mFaults.add(initializeFault114());
        mFaults.add(initializeFault115());
        mFaults.add(initializeFault116());
        mFaults.add(initializeFault117());
        mFaults.add(initializeFault118());
        mFaults.add(initializeFault119());
        mFaults.add(initializeFault120());
        mFaults.add(initializeFault121());
        mFaults.add(initializeFault122());
        mFaults.add(initializeFault123());
        mFaults.add(initializeFault124());
        mFaults.add(initializeFault125());
        mFaults.add(initializeFault127());
        mFaults.add(initializeFault128());
        mFaults.add(initializeFault129());
        mFaults.add(initializeFault130());
        mFaults.add(initializeFault159());
        mFaults.add(initializeFault134());
        mFaults.add(initializeFault135());
        mFaults.add(initializeFault136());
        mFaults.add(initializeFault137());
        mFaults.add(initializeFault138());
        mFaults.add(initializeFault139());
        mFaults.add(initializeFault140());
        mFaults.add(initializeFault141());
        mFaults.add(initializeFault142());
        mFaults.add(initializeFault143());
        mFaults.add(initializeFault144());
        mFaults.add(initializeFault145());
        mFaults.add(initializeFault146());
        mFaults.add(initializeFault147());
        mFaults.add(initializeFault148());
        mFaults.add(initializeFault149());
        mFaults.add(initializeFault150());
        mFaults.add(initializeFault151());
        mFaults.add(initializeFault152());
        mFaults.add(initializeFault153());
        mFaults.add(initializeFault154());
        mFaults.add(initializeFault155());
        mFaults.add(initializeFault156());
        mFaults.add(initializeFault157());
        mFaults.add(initializeFault158());
        mFaults.add(initializeFault160());
        mFaults.add(initializeFault161());
        mFaults.add(initializeFault162());
        mFaults.add(initializeFault163());
        mFaults.add(initializeFault164());
        mFaults.add(initializeFault165());
        mFaults.add(initializeFault166());
        mFaults.add(initializeFault167());
        mFaults.add(initializeFault168());
        mFaults.add(initializeFault169());
        mFaults.add(initializeFault170());
        mFaults.add(initializeFault171());
        mFaults.add(initializeFault172());
        mFaults.add(initializeFault173());
        mFaults.add(initializeFault174());
        mFaults.add(initializeFault175());
        mFaults.add(initializeFault176());
        mFaults.add(initializeFault177());
    }

    //region Room Initializations
    private Room initializeRoom1() { return new Room(1, "Arch"); }
    private Room initializeRoom2() { return new Room(2, "Attic-Drop Down"); }
    private Room initializeRoom3() { return new Room(3, "Attic-Walk-In"); }
    private Room initializeRoom4() { return new Room(4, "Bar"); }
    private Room initializeRoom5() { return new Room(5, "Basement"); }
    private Room initializeRoom6() { return new Room(6, "Bath"); }
    private Room initializeRoom7() { return new Room(7, "Bath-Hall"); }
    private Room initializeRoom8() { return new Room(8, "Bath-J&J"); }
    private Room initializeRoom9() { return new Room(9, "Bath-Primary"); }
    private Room initializeRoom10() { return new Room(10, "Bath-Powder"); }
    private Room initializeRoom11() { return new Room(11, "Bedroom"); }
    private Room initializeRoom12() { return new Room(12, "Bedroom-Primary"); }
    private Room initializeRoom13() { return new Room(13, "Bonus Room"); }
    private Room initializeRoom14() { return new Room(14, "Breakfast Nook"); }
    private Room initializeRoom15() { return new Room(15, "Breezeway"); }
    private Room initializeRoom16() { return new Room(16, "Butler's Pantry"); }
    private Room initializeRoom17() { return new Room(17, "Cantilever"); }
    private Room initializeRoom18() { return new Room(18, "Childrens Retreat"); }
    private Room initializeRoom19() { return new Room(19, "Closet"); }
    private Room initializeRoom20() { return new Room(20, "Closet-Linen"); }
    private Room initializeRoom21() { return new Room(21, "Closet-Primary"); }
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
    private Room initializeRoom67() { return new Room(67, "1x4"); }
    private Room initializeRoom68() { return new Room(68, "2x6"); }
    private Room initializeRoom69() { return new Room(69, "2x8"); }
    private Room initializeRoom70() { return new Room(70, "2x10"); }
    private Room initializeRoom71() { return new Room(71, "2x12"); }
    private Room initializeRoom72() { return new Room(72, "2x4"); }
    private Room initializeRoom73() { return new Room(73, "Bedroom Closet"); }
    private Room initializeRoom74() { return new Room(74, "Closet-Bedroom"); }
    private Room initializeRoom75() { return new Room(75, "Laundry"); }
    private Room initializeRoom76() { return new Room(76, "Attic"); }
    private Room initializeRoom77() { return new Room(77, "Bath-Pri Commode Closet"); }
    private Room initializeRoom78() { return new Room(78, "Front Door"); }
    private Room initializeRoom79() { return new Room(79, "Patio Door"); }
    private Room initializeRoom80() { return new Room(80, "Service Door"); }
    private Room initializeRoom81() { return new Room(81, "Overhead Door"); }
    private Room initializeRoom86() { return new Room(86, "Loft"); }
    private Room initializeRoom87() { return new Room(67, "Bedroom bath"); }
    private Room initializeRoom82() { return new Room(82, "Kitchen island"); }
    private Room initializeRoom83() { return new Room(83, "Kitchen pantry"); }
    private Room initializeRoom84() { return new Room(84, "Garage service door"); }
    private Room initializeRoom85() { return new Room(85, "Interior doors"); }
    //endregion
    //region Direction Initializations
    private Direction initializeDirection14() { return new Direction(4, "Left",1); }
    private Direction initializeDirection15() { return new Direction(12, "Middle",2); }
    private Direction initializeDirection103() { return new Direction(5, "Right",3); }
    private Direction initializeDirection20() { return new Direction(13, "Top",4); }
    private Direction initializeDirection11() { return new Direction(15, "Center",5); }
    private Direction initializeDirection10() { return new Direction(14, "Bottom",6); }
    private Direction initializeDirection12() { return new Direction(7, "Front", 7); }
    private Direction initializeDirection19() { return new Direction(19, "Side",8); }
    private Direction initializeDirection18() { return new Direction(8, "Rear",9); }
    private Direction initializeDirection17() { return new Direction(16, "Over",10); }
    private Direction initializeDirection21() { return new Direction(17, "Under",11); }
    private Direction initializeDirection6() { return new Direction(3, "and",12); }
    private Direction initializeDirection131() { return new Direction(10, "Up",13); }
    private Direction initializeDirection55() { return new Direction(11, "Down",14); }
    private Direction initializeDirection7() { return new Direction(25, "at",15); }
    private Direction initializeDirection13() { return new Direction(20, "Inside",16); }
    private Direction initializeDirection92() { return new Direction(23, "Outside",17); }
    private Direction initializeDirection8() { return new Direction(26, "at 45°",18); }
    private Direction initializeDirection133() { return new Direction(18, "Upper",19); }
    private Direction initializeDirection143() { return new Direction(138, "Lower", 20); }
    private Direction initializeDirection16() { return new Direction(24, "of",21); }
    private Direction initializeDirection30() { return new Direction(38, "Behind",22); }
    private Direction initializeDirection128() { return new Direction(127, "Unit 1",23); }
    private Direction initializeDirection129() { return new Direction(128, "Unit 2",24); }
    private Direction initializeDirection130() { return new Direction(129, "Unit 3",25); }

    private Direction initializeDirection2() { return new Direction(2, "Above",26); }
    private Direction initializeDirection5() { return new Direction(21, "All",27); }
    private Direction initializeDirection3() { return new Direction(22, "Adjacent",28); }
    private Direction initializeDirection9() { return new Direction(27, "between",29); }
    private Direction initializeDirection1() { return new Direction(28, "A/C",30); }
    private Direction initializeDirection4() { return new Direction(29, "Air Barrier",31); }
    private Direction initializeDirection22() { return new Direction(30, "Access Cover",32); }
    private Direction initializeDirection23() { return new Direction(31, "Arch Window",33); }
    private Direction initializeDirection24() { return new Direction(32, "Art Niche",34); }
    private Direction initializeDirection25() { return new Direction(33, "Attic Access",35); }
    private Direction initializeDirection26() { return new Direction(34, "Backside",36); }
    private Direction initializeDirection27() { return new Direction(35, "Backsplash",37); }
    private Direction initializeDirection28() { return new Direction(36, "Balcony",38); }
    private Direction initializeDirection29() { return new Direction(37, "Base",39); }
    private Direction initializeDirection31() { return new Direction(39, "Box",40); }
    private Direction initializeDirection32() { return new Direction(40, "Brick",41); }
    private Direction initializeDirection33() { return new Direction(41, "Brick Pocket",42); }
    private Direction initializeDirection34() { return new Direction(42, "Cabinet",43); }
    private Direction initializeDirection35() { return new Direction(43, "Casing",44); }
    private Direction initializeDirection36() { return new Direction(44, "Catwalk", 45); }
    private Direction initializeDirection37() { return new Direction(45, "Ceiling",46); }
    private Direction initializeDirection38() { return new Direction(46, "Chase",47); }
    private Direction initializeDirection39() { return new Direction(47, "Chimney",48); }
    private Direction initializeDirection40() { return new Direction(48, "Chimney Cap",49); }
    private Direction initializeDirection41() { return new Direction(49, "City walk",50); }
    private Direction initializeDirection42() { return new Direction(50, "Clean-out",51); }
    private Direction initializeDirection43() { return new Direction(51, "Coach Light",52); }
    private Direction initializeDirection44() { return new Direction(52, "Cold",53); }
    private Direction initializeDirection45() { return new Direction(53, "Commode",54); }
    private Direction initializeDirection46() { return new Direction(54, "Condenser",55); }
    private Direction initializeDirection47() { return new Direction(55, "Cooktop",56); }
    private Direction initializeDirection48() { return new Direction(56, "Corner",57); }
    private Direction initializeDirection49() { return new Direction(57, "Counter",58); }
    private Direction initializeDirection50() { return new Direction(58, "Crown",59); }
    private Direction initializeDirection51() { return new Direction(59, "Dishwasher",60); }
    private Direction initializeDirection52() { return new Direction(60, "Door",61); }
    private Direction initializeDirection53() { return new Direction(143, "Doorbell",62); }
    private Direction initializeDirection54() { return new Direction(61, "Dormer",63); }
    private Direction initializeDirection56() { return new Direction(62, "Drain",64); }
    private Direction initializeDirection57() { return new Direction(63, "East",65); }
    private Direction initializeDirection58() { return new Direction(64, "Electrical Panel",66); }
    private Direction initializeDirection59() { return new Direction(65, "Entertainment Center",67); }
    private Direction initializeDirection60() { return new Direction(66, "Exterior",68); }
    private Direction initializeDirection61() { return new Direction(67, "Faucet",69); }
    private Direction initializeDirection62() { return new Direction(68, "Fireplace",70); }
    private Direction initializeDirection63() { return new Direction(69, "Framing Material",71); }
    private Direction initializeDirection64() { return new Direction(9, "Front & Rear",72); }
    private Direction initializeDirection65() { return new Direction(70, "Furnace",73); }
    private Direction initializeDirection66() { return new Direction(71, "Furr Out",74); }
    private Direction initializeDirection67() { return new Direction(72, "Gable",75); }
    private Direction initializeDirection68() { return new Direction(73, "Gusset Plate",76); }
    private Direction initializeDirection69() { return new Direction(74, "Hall",77); }
    private Direction initializeDirection70() { return new Direction(75, "Head Wall",78); }
    private Direction initializeDirection71() { return new Direction(76, "Header",79); }
    private Direction initializeDirection72() { return new Direction(77, "Hers",80); }
    private Direction initializeDirection73() { return new Direction(78, "Hip",81); }
    private Direction initializeDirection74() { return new Direction(79, "His",82); }
    private Direction initializeDirection75() { return new Direction(80, "Hose bib",83); }
    private Direction initializeDirection76() { return new Direction(81, "Hot",84); }
    private Direction initializeDirection77() { return new Direction(82, "House Wrap",85); }
    private Direction initializeDirection78() { return new Direction(83, "Jump walk",86); }
    private Direction initializeDirection79() { return new Direction(84, "Lead walk",87); }
    private Direction initializeDirection80() { return new Direction(6, "Left & Right",88); }
    private Direction initializeDirection81() { return new Direction(85, "Light",89); }
    private Direction initializeDirection82() { return new Direction(86, "Linen",90); }
    private Direction initializeDirection83() { return new Direction(87, "Lintel",91); }
    private Direction initializeDirection84() { return new Direction(88, "Lock",92); }
    private Direction initializeDirection85() { return new Direction(89, "Log Lighter",93); }
    private Direction initializeDirection86() { return new Direction(90, "Main",94); }
    private Direction initializeDirection87() { return new Direction(91, "Manifold",95); }
    private Direction initializeDirection88() { return new Direction(92, "Microwave",96); }
    private Direction initializeDirection89() { return new Direction(93, "Mirror",97); }
    private Direction initializeDirection90() { return new Direction(94, "Near",98); }
    private Direction initializeDirection91() { return new Direction(95, "North",99); }
    private Direction initializeDirection93() { return new Direction(96, "Overflow Drain",100); }
    private Direction initializeDirection94() { return new Direction(97, "Parallel",101); }
    private Direction initializeDirection95() { return new Direction(98, "Phone Jack",102); }
    private Direction initializeDirection96() { return new Direction(138, "PVC",103); }
    private Direction initializeDirection97() { return new Direction(99, "Rake",104); }
    private Direction initializeDirection98() { return new Direction(100, "Recessed Can",105); }
    private Direction initializeDirection99() { return new Direction(101, "Refrigerator",106); }
    private Direction initializeDirection100() { return new Direction(102, "Return",107); }
    private Direction initializeDirection101() { return new Direction(103, "Return Air",108); }
    private Direction initializeDirection102() { return new Direction(104, "Ridge",109); }
    private Direction initializeDirection104() { return new Direction(105, "Rim Board",110); }
    private Direction initializeDirection105() { return new Direction(106, "Roof",111); }
    private Direction initializeDirection106() { return new Direction(107, "Screws",112); }
    private Direction initializeDirection107() { return new Direction(108, "Shingles",113); }
    private Direction initializeDirection108() { return new Direction(109, "Shoe Moulding",114); }
    private Direction initializeDirection109() { return new Direction(110, "Shower",115); }
    private Direction initializeDirection110() { return new Direction(111, "Shower pan",116); }
    private Direction initializeDirection111() { return new Direction(112, "Sink",117); }
    private Direction initializeDirection112() { return new Direction(142, "Smoke Detector",118); }
    private Direction initializeDirection113() { return new Direction(113, "South",119); }
    private Direction initializeDirection114() { return new Direction(114, "Splice",120); }
    private Direction initializeDirection115() { return new Direction(115, "Step",121); }
    private Direction initializeDirection116() { return new Direction(116, "Stringer",122); }
    private Direction initializeDirection117() { return new Direction(117, "Supply",123); }
    private Direction initializeDirection118() { return new Direction(118, "Switch Plate",124); }
    private Direction initializeDirection119() { return new Direction(139, "T&P",125); }
    private Direction initializeDirection120() { return new Direction(119, "Third Floor",126); }
    private Direction initializeDirection121() { return new Direction(120, "Throughout",127); }
    private Direction initializeDirection122() { return new Direction(121, "Toilet Paper",128); }
    private Direction initializeDirection123() { return new Direction(122, "Towel Bar",129); }
    private Direction initializeDirection124() { return new Direction(123, "Tread",130); }
    private Direction initializeDirection125() { return new Direction(124, "Trim",131); }
    private Direction initializeDirection126() { return new Direction(125, "Tub",132); }
    private Direction initializeDirection127() { return new Direction(126, "Under Cabinet",133); }
    private Direction initializeDirection132() { return new Direction(1, "Up & Down",134); }
    private Direction initializeDirection134() { return new Direction(140, "Vacuum Breaker",135); }
    private Direction initializeDirection135() { return new Direction(130, "Valley",136); }
    private Direction initializeDirection136() { return new Direction(131, "Vanity",137); }
    private Direction initializeDirection137() { return new Direction(132, "Vent",138); }
    private Direction initializeDirection138() { return new Direction(133, "Wall",139); }
    private Direction initializeDirection139() { return new Direction(134, "Washer Box",140); }
    private Direction initializeDirection140() { return new Direction(135, "Water Heater",141); }
    private Direction initializeDirection141() { return new Direction(136, "West",142); }
    private Direction initializeDirection142() { return new Direction(137, "Window",143); }
    //endregion
    //region Fault Initializations
    private Fault initializeFault1() { return new Fault(1,"Access Panel Missing","-Access panel missing"); }
    private Fault initializeFault2() { return new Fault(2,"Accessible-Not","-Not Accessible"); }
    private Fault initializeFault3() { return new Fault(3,"Aligned-Not","-Not aligned"); }
    private Fault initializeFault4() { return new Fault(4,"Anti-Tip Device","-Anti-Tip Device"); }
    private Fault initializeFault5() { return new Fault(5,"Balanced-Not","-Not Balanced"); }
    private Fault initializeFault6() { return new Fault(6,"Battery low","-Battery low"); }
    private Fault initializeFault7() { return new Fault(7,"Bent","-Bent"); }
    private Fault initializeFault8() { return new Fault(8,"Binds","-Binds"); }
    private Fault initializeFault9() { return new Fault(9,"Blocked-Not","-Not blocked"); }
    private Fault initializeFault10() { return new Fault(10,"Bowed","-Bowed"); }
    private Fault initializeFault11() { return new Fault(11,"Braced-Not","-Not Braced"); }
    private Fault initializeFault12() { return new Fault(12,"Broken","-Broken"); }
    private Fault initializeFault13() { return new Fault(13,"Bulbs out","-Bulb out"); }
    private Fault initializeFault14() { return new Fault(14,"Calibration","-Not Calibrated"); }
    private Fault initializeFault15() { return new Fault(15,"Cap Nuts","-Cap Nuts"); }
    private Fault initializeFault16() { return new Fault(16,"Caulk corners","-Caulk corners"); }
    private Fault initializeFault17() { return new Fault(17,"Caulked-Not","-Not Caulked"); }
    private Fault initializeFault18() { return new Fault(18,"Centered, Not","-Not Centered"); }
    private Fault initializeFault19() { return new Fault(19,"Check all","-Check all"); }
    private Fault initializeFault20() { return new Fault(20,"Chipped","-Chipped"); }
    private Fault initializeFault21() { return new Fault(21,"Clearance Insufficient","-Clearance Insufficient"); }
    private Fault initializeFault22() { return new Fault(22,"Connected-Not","-Not Connected"); }
    private Fault initializeFault23() { return new Fault(23,"Continuous-not","-Not Continuous"); }
    private Fault initializeFault24() { return new Fault(24,"Covered","-Covered"); }
    private Fault initializeFault25() { return new Fault(25,"Cracked","-Cracked"); }
    private Fault initializeFault26() { return new Fault(26,"Crooked","-Crooked"); }
    private Fault initializeFault27() { return new Fault(27,"Crown","-Crown"); }
    private Fault initializeFault28() { return new Fault(28,"Damaged","-Damaged"); }
    private Fault initializeFault29() { return new Fault(29,"Debris","-Debris"); }
    private Fault initializeFault30() { return new Fault(30,"Deflected","-Deflected"); }
    private Fault initializeFault31() { return new Fault(31,"Dips","-Dips"); }
    private Fault initializeFault32() { return new Fault(32,"Dirty","-Dirty"); }
    private Fault initializeFault33() { return new Fault(33,"Drags Carpet","-Drags Carpet"); }
    private Fault initializeFault34() { return new Fault(34,"Failed","-Failed"); }
    private Fault initializeFault35() { return new Fault(35,"Fall-no","-No Fall"); }
    private Fault initializeFault36() { return new Fault(36,"Fill Valve Leaks","-Fill Valve Leaks"); }
    private Fault initializeFault37() { return new Fault(37,"Flashed-Not","-Not Flashed"); }
    private Fault initializeFault38() { return new Fault(38,"Flush-Not","-Not Flush"); }
    private Fault initializeFault39() { return new Fault(39,"Flush-Not properly","-Proper flush"); }
    private Fault initializeFault40() { return new Fault(40,"Full Bearing-Not","-Not full-bearing"); }
    private Fault initializeFault41() { return new Fault(41,"Full throw-Not","-Not full throw"); }
    private Fault initializeFault42() { return new Fault(42,"Gap, no","-No gaps"); }
    private Fault initializeFault43() { return new Fault(43,"Gaps","-Gaps"); }
    private Fault initializeFault44() { return new Fault(44,"Gas off","-Gas off"); }
    private Fault initializeFault45() { return new Fault(45,"Ground","-Ground"); }
    private Fault initializeFault46() { return new Fault(46,"Grounded-Not","-Not Grounded"); }
    private Fault initializeFault47() { return new Fault(47,"Grout incomplete","-Grout incomplete"); }
    private Fault initializeFault48() { return new Fault(48,"Hanging","-Hanging"); }
    private Fault initializeFault49() { return new Fault(49,"High","-High"); }
    private Fault initializeFault50() { return new Fault(50,"Holes","-Holes"); }
    private Fault initializeFault51() { return new Fault(51,"Inadequate","-Inadequate"); }
    private Fault initializeFault52() { return new Fault(52,"Incomplete","-Incomplete"); }
    private Fault initializeFault53() { return new Fault(53,"Inoperative","-Inoperative"); }
    private Fault initializeFault54() { return new Fault(54,"Inspected-Not","-Not Inspected"); }
    private Fault initializeFault55() { return new Fault(55,"Installed-Not","-Not installed"); }
    private Fault initializeFault56() { return new Fault(56,"Labeled-Not","-Not labeled"); }
    private Fault initializeFault57() { return new Fault(57,"Lap Improper","-Improper lap"); }
    private Fault initializeFault58() { return new Fault(58,"Latch-won't","-Will not latch"); }
    private Fault initializeFault59() { return new Fault(59,"Leaks","-Leaks"); }
    private Fault initializeFault60() { return new Fault(60,"Level-Not","-Not level"); }
    private Fault initializeFault61() { return new Fault(61,"Long","-Too long"); }
    private Fault initializeFault62() { return new Fault(62,"Loose","-Loose"); }
    private Fault initializeFault63() { return new Fault(63,"Low","-Low"); }
    private Fault initializeFault64() { return new Fault(64,"Low Pressure","-Low Pressure"); }
    private Fault initializeFault65() { return new Fault(65,"Missing","-Missing"); }
    private Fault initializeFault66() { return new Fault(66,"Mutilated","-Mutilated"); }
    private Fault initializeFault67() { return new Fault(67,"Nail Pops","-Nail Pops"); }
    private Fault initializeFault68() { return new Fault(68,"Nailed Properly-Not","-Not nailed properly"); }
    private Fault initializeFault69() { return new Fault(69,"Narrow, too","-Too narrow"); }
    private Fault initializeFault70() { return new Fault(70,"Noisy","-Noisy"); }
    private Fault initializeFault71() { return new Fault(71,"Non-Corrosive Screws","-Non-Corrosive Screws"); }
    private Fault initializeFault72() { return new Fault(72,"Notched-Not","-Not notched"); }
    private Fault initializeFault73() { return new Fault(73,"Obstructed","-Obstructed"); }
    private Fault initializeFault74() { return new Fault(74,"Overcut","-Overcut"); }
    private Fault initializeFault75() { return new Fault(75,"Overlapped-Not","-Not Overlapped"); }
    private Fault initializeFault76() { return new Fault(76,"Overspanned","-Overspanned"); }
    private Fault initializeFault77() { return new Fault(77,"Painted-Not","-Not Painted"); }
    private Fault initializeFault78() { return new Fault(78,"Plumb-Not","-Not plumb"); }
    private Fault initializeFault79() { return new Fault(79,"Pops","-Pops"); }
    private Fault initializeFault80() { return new Fault(80,"Power-No","-No power"); }
    private Fault initializeFault81() { return new Fault(81,"Primed-Not","-Not Primed"); }
    private Fault initializeFault82() { return new Fault(82,"Removed-Not","-Not removed"); }
    private Fault initializeFault83() { return new Fault(83,"Required","-Required"); }
    private Fault initializeFault84() { return new Fault(84,"Responding-Not","-Not responding"); }
    private Fault initializeFault85() { return new Fault(85,"Reversed","-Reversed"); }
    private Fault initializeFault86() { return new Fault(86,"Safety Lines","-Safety Lines"); }
    private Fault initializeFault87() { return new Fault(87,"Sag","-Sag"); }
    private Fault initializeFault88() { return new Fault(88,"Scratched","-Scratched"); }
    private Fault initializeFault89() { return new Fault(89,"Sealed, Not","-Not Sealed"); }
    private Fault initializeFault90() { return new Fault(90,"Secured-Not","-Not Secured"); }
    private Fault initializeFault91() { return new Fault(91,"Shimmed-Not","-Not Shimmed"); }
    private Fault initializeFault92() { return new Fault(92,"Short","-Short"); }
    private Fault initializeFault93() { return new Fault(93,"Should be","-Should be"); }
    private Fault initializeFault94() { return new Fault(94,"Sized-Oversized","-Oversized"); }
    private Fault initializeFault95() { return new Fault(95,"Sized-Undersized","-Undersized"); }
    private Fault initializeFault96() { return new Fault(96,"Sleeved-Not","-Not Sleeved"); }
    private Fault initializeFault97() { return new Fault(97,"Spaced Properly-Not","-Not spaced properly"); }
    private Fault initializeFault98() { return new Fault(98,"Squeaks","-Squeaks"); }
    private Fault initializeFault99() { return new Fault(99,"Stained","-Stained"); }
    private Fault initializeFault100() { return new Fault(100,"Startup-Not","-No startup"); }
    private Fault initializeFault101() { return new Fault(101,"Studs- doubles needed","-Double studs needed"); }
    private Fault initializeFault102() { return new Fault(102,"Supported-Not","-Not Supported"); }
    private Fault initializeFault103() { return new Fault(103,"Tab(s) missing","Tab(s) missing"); }
    private Fault initializeFault104() { return new Fault(104,"Teed-Not","-Not teed"); }
    private Fault initializeFault105() { return new Fault(105,"Tested-Not","-Not Tested"); }
    private Fault initializeFault106() { return new Fault(106,"Tight, too","Too tight"); }
    private Fault initializeFault107() { return new Fault(107,"Touch-up-Needs","-Needs touch-up"); }
    private Fault initializeFault108() { return new Fault(108,"Twisted","-Twisted"); }
    private Fault initializeFault109() { return new Fault(109,"Uneven","-Uneven"); }
    private Fault initializeFault110() { return new Fault(110,"Unpacked-Not","-Not unpacked"); }
    private Fault initializeFault111() { return new Fault(111,"Verified-Not","-Not Verified"); }
    private Fault initializeFault112() { return new Fault(112,"Verify","-Verify"); }
    private Fault initializeFault113() { return new Fault(113,"Visible-Not","-Not Visible"); }
    private Fault initializeFault114() { return new Fault(114,"Water-No","-No water"); }
    private Fault initializeFault115() { return new Fault(115,"Water pressure held at","-Water Pressure test, PSI held at: "); }
    private Fault initializeFault116() { return new Fault(116,"Wavy","-Wavy"); }
    private Fault initializeFault117() { return new Fault(117,"Wide, too","-Too wide"); }
    private Fault initializeFault118() { return new Fault(118,"Wired Incorrectly","-Wired Incorrectly"); }
    private Fault initializeFault119() { return new Fault(119,"Workers present","-Workers present"); }
    private Fault initializeFault120() { return new Fault(120,"<60°, Not Tested","-<60°, Not Tested"); }
    private Fault initializeFault121() { return new Fault(121,"Voids","-Voids"); }
    private Fault initializeFault122() { return new Fault(122,"Compressed","-Compressed"); }
    private Fault initializeFault123() { return new Fault(123,"Contact, Not in","-Not in contact"); }
    private Fault initializeFault124() { return new Fault(124,"Fitted, Not","-Not fitted properly"); }
    private Fault initializeFault125() { return new Fault(125,"Pre-Cornice not passed","-Pre-Cornice not passed"); }
    private Fault initializeFault127() { return new Fault(127,"Overshot","-Overshot"); }
    private Fault initializeFault128() { return new Fault(128,"Undershot","-Undershot"); }
    private Fault initializeFault129() { return new Fault(129,"Improper","-Improper"); }
    private Fault initializeFault130() { return new Fault(130,"Covered, Not","-Not Covered"); }
    private Fault initializeFault131() { return new Fault(131,"Blower Door Results","-Blower Door Test results: CFM @ 50Pa"); }
    private Fault initializeFault159() { return new Fault(159,"Square, Not","Not squared"); }
    private Fault initializeFault133() { return new Fault(133,"Unable to fully inspect","-Unable to fully inspect"); }
    private Fault initializeFault134() { return new Fault(134,"Adjust","-Adjust"); }
    private Fault initializeFault135() { return new Fault(135,"Insulated, Not","-Not Insulated"); }
    private Fault initializeFault136() { return new Fault(136,"Protected, Not","-Not Protected"); }
    private Fault initializeFault137() { return new Fault(137,"Furnace Model Number","-Furnace Model Number: "); }
    private Fault initializeFault138() { return new Fault(138,"Compressor Model Number","-Compressor Model Number: "); }
    private Fault initializeFault139() { return new Fault(139,"Water Heater Model Number","-Water Heater Model Number: "); }
    private Fault initializeFault140() { return new Fault(140,"Within 18 in.","within 18 in."); }
    private Fault initializeFault141() { return new Fault(141,"Within 20 in.","within 20 in."); }
    private Fault initializeFault142() { return new Fault(142,"Verify at Final","-Will be verified at Final"); }
    private Fault initializeFault143() { return new Fault(143,"Steel splice plate missing","-Steel splice plate missing"); }
    private Fault initializeFault144() { return new Fault(144,"Splice block","-Splice block"); }
    private Fault initializeFault145() { return new Fault(145,"Wires, max 2 per staple","2 wires max per staple"); }
    private Fault initializeFault146() { return new Fault(146,"Wires, too close to nailing surface","Wires too close to nailing surface"); }
    private Fault initializeFault147() { return new Fault(147,"Tape Failing to adhere","Tape failing to adhere"); }
    private Fault initializeFault148() { return new Fault(148,"Material, improper","Improper material"); }
    private Fault initializeFault149() { return new Fault(149,"HVAC Contractor Checklist Defects:","HVAC Contractor Checklist Defects:"); }
    private Fault initializeFault150() { return new Fault(150,"Slow","-Slow"); }
    private Fault initializeFault151() { return new Fault(151,"Duct Lkg Unit 1 Outside Failed","Unit 1, to Outside, Failed @: , Should be: "); }
    private Fault initializeFault152() { return new Fault(152,"Duct Lkg Unit 2 Outside Failed","Unit 2, to Outside, Failed @: , Should be: "); }
    private Fault initializeFault153() { return new Fault(153,"Duct Lkg Unit 3 Outside Failed","Unit 3, to Outside, Failed @: , Should be: "); }
    private Fault initializeFault154() { return new Fault(154,"Duct Lkg Unit 1Total Failed","Unit 1, Total, Failed @: , Should be: "); }
    private Fault initializeFault155() { return new Fault(155,"Duct Lkg Unit 2 Total Failed","Unit 2, Total, Failed @: , Should be: "); }
    private Fault initializeFault156() { return new Fault(156,"Duct Lkg Unit 3 Total Failed","Unit 3, Total, Failed @: , Should be: "); }
    private Fault initializeFault157() { return new Fault(157,"Blower Door Failed","Failed @: , Should be:"); }
    private Fault initializeFault158() { return new Fault(158,"Blocked & Sealed, Not","Not Blocked or Sealed"); }
    private Fault initializeFault160() { return new Fault(160,"Inconsistent","Inconsistent"); }
    private Fault initializeFault161() { return new Fault(161,"Firestop chase tops","Firestop chase tops"); }
    private Fault initializeFault162() { return new Fault(162,"Firestop top plate penetrations","Firestop top plate penetrations"); }
    private Fault initializeFault163() { return new Fault(163,"Firestop dropped ceiling","Firestop dropped ceiling"); }
    private Fault initializeFault164() { return new Fault(164,"Firestop horizontal chase","Firestop horizontal chase"); }
    private Fault initializeFault165() { return new Fault(165,"Firestop rake walls","Firestop rake walls"); }
    private Fault initializeFault166() { return new Fault(166,"Firestop stairway","Firestop stairway"); }
    private Fault initializeFault167() { return new Fault(167,"Compressed by electrical","-Compressed by electrical"); }
    private Fault initializeFault168() { return new Fault(168,"Compressed by low voltage","-Compressed by low voltage"); }
    private Fault initializeFault169() { return new Fault(169,"Compressed by plumbing","-Compressed by plumbing"); }
    private Fault initializeFault170() { return new Fault(170,"Compressed by framing","-Compressed by framing"); }
    private Fault initializeFault171() { return new Fault(171,"Roof Decking not gapped properly","-Roof Decking not gapped properly"); }
    private Fault initializeFault172() { return new Fault(172,"Filled to overflow, Not","-Not filled to overflow"); }
    private Fault initializeFault173() { return new Fault(173,"Vented, Not","Not vented"); }
    private Fault initializeFault174() { return new Fault(174,"Secure and Seal","Secure and Seal"); }
    private Fault initializeFault175() { return new Fault(175,"Poly sealed, Not","Not Poly sealed"); }
    private Fault initializeFault176() { return new Fault(176,"per Builder specifications, Not","Not per Builder's Specifications"); }
    private Fault initializeFault177() { return new Fault(177,"ZIP System Properly rolled, Not","ZIP System Tape is not properly rolled"); }
    //endregion
}