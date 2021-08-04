package data;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.burgess.bridge.BridgeAPIQueue;
import com.burgess.bridge.login.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import data.DAOs.Builder_DAO;
import data.DAOs.CannedComment_DAO;
import data.DAOs.DefectCategory_DAO;
import data.DAOs.DefectCategory_InspectionType_DAO;
import data.DAOs.DefectItem_DAO;
import data.DAOs.DefectItem_InspectionType_DAO;
import data.DAOs.Direction_DAO;
import data.DAOs.InspectionDefect_DAO;
import data.DAOs.InspectionHistory_DAO;
import data.DAOs.InspectionResolution_DAO;
import data.DAOs.Inspection_DAO;
import data.DAOs.Location_DAO;
import data.DAOs.Room_DAO;
import data.Tables.Builder_Table;
import data.Tables.CannedComment_Table;
import data.Tables.DefectCategory_InspectionType_XRef;
import data.Tables.DefectCategory_Table;
import data.Tables.DefectItem_InspectionType_XRef;
import data.Tables.DefectItem_Table;
import data.Tables.Direction_Table;
import data.Tables.InspectionDefect_Table;
import data.Tables.InspectionHistory_Table;
import data.Tables.InspectionResolution_Table;
import data.Tables.Inspection_Table;
import data.Tables.Location_Table;
import data.Tables.Room_Table;
import data.Views.ReinspectDefectList_View;
import data.Views.ReviewAndSubmit_View;
import data.Views.RouteSheet_View;

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
        Room_Table.class,
        DefectCategory_InspectionType_XRef.class,
        DefectItem_InspectionType_XRef.class,
        InspectionHistory_Table.class
        }, views = {
        RouteSheet_View.class,
        ReviewAndSubmit_View.class
        }, version = 24, exportSchema = false)
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
    public abstract DefectCategory_InspectionType_DAO mDefectCategory_InspectionTypeDao();
    public abstract DefectItem_InspectionType_DAO mDefectItem_InspectionTypeDao();
    public abstract InspectionHistory_DAO mInspectionHistoryDao();
    private static volatile BridgeRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    private static final String roomUrl = "https://apistage.burgess-inc.com/api/Bridge/GetRooms";
    private static final String TAG = "DATABASE";
    private static RequestQueue queue;

    public static BridgeRoomDatabase getDatabase(final Context context) {
        queue = BridgeAPIQueue.getInstance(context).getRequestQueue();
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

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // If you want to keep data through app restarts, comment out the following block
            databaseWriteExecutor.execute(() -> {
                Direction_DAO directionDao = INSTANCE.mDirectionDao();
            });
        }
    };
}
