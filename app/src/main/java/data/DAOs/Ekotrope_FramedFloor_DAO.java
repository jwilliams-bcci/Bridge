package data.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import data.Tables.Ekotrope_FramedFloor_Table;

@Dao
public interface Ekotrope_FramedFloor_DAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Ekotrope_FramedFloor_Table framedFloor);

    @Query("DELETE FROM ekotrope_framed_floor_table")
    void deleteAll();

    @Query("DELETE FROM ekotrope_framed_floor_table WHERE plan_id = :plan_id AND [index] = :index")
    void delete(String plan_id, int index);

    @Query("SELECT * FROM ekotrope_framed_floor_table WHERE plan_id = :plan_id AND [index] = :index")
    Ekotrope_FramedFloor_Table getFramedFloor(String plan_id, int index);

    @Query("SELECT * FROM ekotrope_framed_floor_table WHERE plan_id = :plan_id")
    LiveData<List<Ekotrope_FramedFloor_Table>> getFramedFloors(String plan_id);

    @Query("SELECT * FROM ekotrope_framed_floor_table WHERE plan_id = :plan_id")
    List<Ekotrope_FramedFloor_Table> getFramedFloorsSync(String plan_id);

    @Query("UPDATE ekotrope_framed_floor_table SET " +
            "studSpacing = :studSpacing, studWidth = :studWidth, studDepth = :studDepth, " +
            "studMaterial = :studMaterial, cavityInsulationGrade = :cavityInsulationGrade, " +
            "cavityInsulationR = :cavityInsulationR, continuousInsulationR = :continuousInsulationR " +
            "WHERE plan_id = :plan_id AND [index] = :index")
    void update(String plan_id, int index, String cavityInsulationGrade, double cavityInsulationR,
                double studSpacing, double continuousInsulationR, double studWidth,
                double studDepth, String studMaterial);
}
