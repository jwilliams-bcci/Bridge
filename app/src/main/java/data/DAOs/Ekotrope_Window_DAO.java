package data.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import data.Tables.Ekotrope_Window_Table;

@Dao
public interface Ekotrope_Window_DAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Ekotrope_Window_Table window);

    @Query("DELETE FROM ekotrope_window_table")
    void deleteAll();

    @Query("DELETE FROM ekotrope_window_table WHERE plan_id = :plan_id AND [index] = :index")
    void delete(String plan_id, int index);

    @Query("SELECT * FROM ekotrope_window_table WHERE plan_id = :plan_id AND [index] = :index")
    Ekotrope_Window_Table getWindow(String plan_id, int index);

    @Query("SELECT * FROM ekotrope_window_table WHERE plan_id = :plan_id")
    LiveData<List<Ekotrope_Window_Table>> getWindows(String plan_id);

    @Query("SELECT * FROM ekotrope_window_table WHERE plan_id = :plan_id")
    List<Ekotrope_Window_Table> getWindowsSync(String plan_id);

    @Query("UPDATE ekotrope_window_table SET " +
            "name = :name, remove = :remove, windowArea = :windowArea, orientation = :orientation, " +
            "installedWallIndex = :installedWallIndex, installedFoundationWallIndex = :installedFoundationWallIndex, " +
            "overhangDepth = :overhangDepth, distanceOverhangToTop = :distanceOverhangToTop, " +
            "distanceOverhangToBottom = :distanceOverhangToBottom, SHGC = :SHGC, uFactor = :uFactor, " +
            "adjacentSummerShading = :adjacentSummerShading, adjacentWinterShading = :adjacentWinterShading " +
            "WHERE plan_id = :plan_id AND [index] = :index")
    void update(String plan_id, int index, String name, boolean remove, double windowArea,
                String orientation, int installedWallIndex, int installedFoundationWallIndex,
                double overhangDepth, double distanceOverhangToTop, double distanceOverhangToBottom,
                double SHGC, double uFactor, double adjacentSummerShading, double adjacentWinterShading);

}
