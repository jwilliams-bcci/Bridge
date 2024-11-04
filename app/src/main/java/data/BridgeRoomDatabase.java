package data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.burgess.bridge.BridgeAPIQueue;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import data.DAOs.Builder_DAO;
import data.DAOs.CannedComment_DAO;
import data.DAOs.DefectItem_DAO;
import data.DAOs.DefectItem_InspectionType_DAO;
import data.DAOs.Direction_DAO;
import data.DAOs.Ekotrope_AboveGradeWall_DAO;
import data.DAOs.Ekotrope_Ceiling_DAO;
import data.DAOs.Ekotrope_Door_DAO;
import data.DAOs.Ekotrope_FramedFloor_DAO;
import data.DAOs.Ekotrope_RimJoist_DAO;
import data.DAOs.Ekotrope_Slab_DAO;
import data.DAOs.Ekotrope_Window_DAO;
import data.DAOs.Fault_DAO;
import data.DAOs.Attachment_DAO;
import data.DAOs.InspectionDefect_DAO;
import data.DAOs.InspectionHistory_DAO;
import data.DAOs.Inspection_DAO;
import data.DAOs.Inspector_DAO;
import data.DAOs.MultifamilyDetails_DAO;
import data.DAOs.PastInspection_DAO;
import data.DAOs.Room_DAO;
import data.Tables.Builder_Table;
import data.Tables.CannedComment_Table;
import data.Tables.DefectItem_InspectionType_XRef;
import data.Tables.DefectItem_Table;
import data.Tables.Direction_Table;
import data.Tables.Ekotrope_AboveGradeWall_Table;
import data.Tables.Ekotrope_Ceiling_Table;
import data.Tables.Ekotrope_Door_Table;
import data.Tables.Ekotrope_FramedFloor_Table;
import data.Tables.Ekotrope_RimJoist_Table;
import data.Tables.Ekotrope_Slab_Table;
import data.Tables.Ekotrope_Window_Table;
import data.Tables.Fault_Table;
import data.Tables.Attachment_Table;
import data.Tables.InspectionDefect_Table;
import data.Tables.InspectionHistory_Table;
import data.Tables.InspectionResolution_Table;
import data.Tables.Inspection_Table;
import data.Tables.Inspector_Table;
import data.Tables.Location_Table;
import data.Tables.MultifamilyDetails_Table;
import data.Tables.PastInspection_Table;
import data.Tables.Room_Table;
import data.Views.ReviewAndSubmit_View;
import data.Views.RouteSheet_View;

@Database(entities = {
        Builder_Table.class,
        CannedComment_Table.class,
        DefectItem_Table.class,
        Direction_Table.class,
        Inspection_Table.class,
        InspectionDefect_Table.class,
        InspectionResolution_Table.class,
        Location_Table.class,
        Room_Table.class,
        DefectItem_InspectionType_XRef.class,
        InspectionHistory_Table.class,
        MultifamilyDetails_Table.class,
        PastInspection_Table.class,
        Inspector_Table.class,
        Fault_Table.class,
        Attachment_Table.class,
        Ekotrope_FramedFloor_Table.class,
        Ekotrope_AboveGradeWall_Table.class,
        Ekotrope_Window_Table.class,
        Ekotrope_Door_Table.class,
        Ekotrope_Ceiling_Table.class,
        Ekotrope_Slab_Table.class,
        Ekotrope_RimJoist_Table.class
        }, views = {
        RouteSheet_View.class,
        ReviewAndSubmit_View.class
        }, version = 76, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class BridgeRoomDatabase extends RoomDatabase {
    private static Context mContext;
    public abstract Builder_DAO mBuilderDao();
    public abstract CannedComment_DAO mCannedCommentDao();
    public abstract DefectItem_DAO mDefectItemDao();
    public abstract Direction_DAO mDirectionDao();
    public abstract Inspection_DAO mInspectionDao();
    public abstract InspectionDefect_DAO mInspectionDefectDao();
    public abstract Room_DAO mRoomDao();
    public abstract DefectItem_InspectionType_DAO mDefectItem_InspectionTypeDao();
    public abstract InspectionHistory_DAO mInspectionHistoryDao();
    public abstract MultifamilyDetails_DAO mMultifamilyDetailsDao();
    public abstract PastInspection_DAO mPastInspectionDao();
    public abstract Inspector_DAO mInspectorDao();
    public abstract Fault_DAO mFaultDao();
    public abstract Attachment_DAO mInspectionAttachmentDao();
    public abstract Ekotrope_FramedFloor_DAO mEkotropeFramedFloorDao();
    public abstract Ekotrope_AboveGradeWall_DAO mEkotropeAboveGradeWallsDao();
    public abstract Ekotrope_Window_DAO mWindowDao();
    public abstract Ekotrope_Door_DAO mDoorDao();
    public abstract Ekotrope_Ceiling_DAO mCeilingDao();
    public abstract Ekotrope_Slab_DAO mSlabDao();
    public abstract Ekotrope_RimJoist_DAO mRimJoistDao();
    private static volatile BridgeRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static BridgeRoomDatabase getDatabase(final Context context) {
        String isProd = BridgeAPIQueue.getInstance(context).isProd() ? "bridge_database_prod.db" : "bridge_database_stage.db";
        mContext = context;

        if (INSTANCE == null) {
            synchronized (BridgeRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), BridgeRoomDatabase.class, "bridge_database")
                            .addCallback(sRoomDatabaseCallback)
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
        }
    };
}
