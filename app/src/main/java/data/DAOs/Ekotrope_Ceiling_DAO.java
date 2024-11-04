package data.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import data.Tables.Ekotrope_Ceiling_Table;

@Dao
public interface Ekotrope_Ceiling_DAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Ekotrope_Ceiling_Table ceiling);

    @Query("DELETE FROM ekotrope_ceiling_table")
    void deleteAll();

    @Query("DELETE FROM ekotrope_ceiling_table WHERE plan_id = :plan_id AND [index] = :index")
    void delete(String plan_id, int index);

    @Query("SELECT * FROM ekotrope_ceiling_table WHERE plan_id = :plan_id AND [index] = :index")
    Ekotrope_Ceiling_Table getCeiling(String plan_id, int index);

    @Query("SELECT * FROM ekotrope_ceiling_table WHERE plan_id = :plan_id")
    LiveData<List<Ekotrope_Ceiling_Table>> getCeilings(String plan_id);

    @Query("SELECT * FROM ekotrope_ceiling_table WHERE plan_id = :plan_id")
    List<Ekotrope_Ceiling_Table> getCeilingsSync(String plan_id);

    @Query("SELECT * FROM ekotrope_ceiling_table WHERE plan_id = :plan_id AND isChanged = 1")
    List<Ekotrope_Ceiling_Table> getCeilingsForUpdate(String plan_id);

    @Query("UPDATE ekotrope_ceiling_table SET " +
            "name = :name, typeName = :typeName, cavityInsulationGrade = :cavityInsulationGrade, " +
            "cavityInsulationR = :cavityInsulationR, continuousInsulationR = :continuousInsulationR, " +
            "studSpacing = :studSpacing, studWidth = :studWidth, studDepth = :studDepth, " +
            "studMaterial = :studMaterial, hasRadiantBarrier = :hasRadiantBarrier, isChanged = 1 " +
            "WHERE plan_id = :plan_id AND [index] = :index")
    public void update(String plan_id, int index, String name, String typeName, String cavityInsulationGrade,
                       double cavityInsulationR, double continuousInsulationR, double studSpacing,
                       double studWidth, double studDepth, String studMaterial,
                       boolean hasRadiantBarrier);
}
