package data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import data.DAOs.Builder_DAO;
import data.DAOs.CannedComment_DAO;
import data.DAOs.DefectCategory_DAO;
import data.DAOs.DefectItem_DAO;
import data.DAOs.Direction_DAO;
import data.DAOs.InspectionDefect_DAO;
import data.DAOs.InspectionResolution_DAO;
import data.DAOs.Inspection_DAO;
import data.DAOs.Location_DAO;
import data.DAOs.Room_DAO;
import data.Tables.Builder_Table;
import data.Tables.CannedComment_Table;
import data.Tables.DefectCategory_Table;
import data.Tables.DefectItem_Table;
import data.Tables.Direction_Table;
import data.Tables.InspectionDefect_Table;
import data.Tables.InspectionResolution_Table;
import data.Tables.Inspection_Table;
import data.Tables.Location_Table;
import data.Tables.Room_Table;

@Database(entities = {
        Builder_Table.class,
        CannedComment_Table.class,
        DefectCategory_Table.class,
        DefectItem_Table.class,
        Direction_Table.class,
        Inspection_Table.class,
        InspectionDefect_Table.class,
        InspectionResolution_Table.class,
        Location_Table.class,
        Room_Table.class
        }, views = {
        RouteSheet_View.class,
        ReviewAndSubmit_View.class
        }, version = 1, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class BridgeRoomDatabase extends RoomDatabase {
    public abstract Builder_DAO mBuilderDao();
    public abstract CannedComment_DAO mCannedCommentDao();
    public abstract DefectCategory_DAO mDefectCategoryDao();
    public abstract DefectItem_DAO mDefectItemDao();
    public abstract Direction_DAO mDirectionDao();
    public abstract Inspection_DAO mInspectionDao();
    public abstract InspectionDefect_DAO mInspectionDefectDao();
    public abstract InspectionResolution_DAO mInspectionResolutionDao();
    public abstract Location_DAO mLocationDao();
    public abstract Room_DAO mRoomDao();
    private static volatile BridgeRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static BridgeRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (BridgeRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), BridgeRoomDatabase.class, "bridge_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // If you want to keep data through app restarts, comment out the following block
            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background. If you want to start with more data, just add it!
//                Location_DAO locationDao = INSTANCE.mLocationDao();
//                locationDao.deleteAll();
//                Location_Table newLocation = new Location_Table(1, "1111 One Avenue", 75068, "Dallas", "TX", "Two Tree Estates");
//                locationDao.insert(newLocation);
//
//                Inspection_DAO inspectionDao = INSTANCE.mInspectionDao();
//                inspectionDao.deleteAll();
//                Inspection_Table newInspection = new Inspection_Table(1, 1001, new Date(System.currentTimeMillis()), 1, "Sup Williams", 1, "Pre-Drywall", "Notes go here", false, false);
//                inspectionDao.insert(newInspection);
            });
        }
    };
}
