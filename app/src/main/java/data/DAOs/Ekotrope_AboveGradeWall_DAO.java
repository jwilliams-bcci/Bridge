package data.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import data.Tables.Ekotrope_AboveGradeWall_Table;

@Dao
public interface Ekotrope_AboveGradeWall_DAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Ekotrope_AboveGradeWall_Table aboveGradeWall);

    @Query("DELETE FROM ekotrope_above_grade_wall_table")
    void deleteAll();

    @Query("DELETE FROM ekotrope_above_grade_wall_table WHERE plan_id = :plan_id AND [index] = :index")
    void delete(String plan_id, int index);

    @Query("SELECT * FROM ekotrope_above_grade_wall_table WHERE plan_id = :plan_id AND [index] = :index")
    Ekotrope_AboveGradeWall_Table getAboveGradeWall(String plan_id, int index);

    @Query("SELECT * FROM ekotrope_above_grade_wall_table WHERE plan_id = :plan_id")
    LiveData<List<Ekotrope_AboveGradeWall_Table>> getAboveGradeWalls(String plan_id);

    @Query("SELECT * FROM ekotrope_above_grade_wall_table WHERE plan_id = :plan_id")
    List<Ekotrope_AboveGradeWall_Table> getAboveGradeWallsSync(String plan_id);

    @Query("SELECT * FROM ekotrope_above_grade_wall_table WHERE plan_id = :plan_id AND isChanged = 1")
    List<Ekotrope_AboveGradeWall_Table> getAboveGradeWallsForUpdate(String plan_id);

    @Query("UPDATE ekotrope_above_grade_wall_table SET " +
            "studSpacing = :studSpacing, studWidth = :studWidth, studDepth = :studDepth, " +
            "studMaterial = :studMaterial, cavityInsulationGrade = :cavityInsulationGrade, " +
            "cavityInsulationR = :cavityInsulationR, continuousInsulationR = :continuousInsulationR, " +
            "isChanged = 1 " +
            "WHERE plan_id = :plan_id AND [index] = :index")
    void update(String plan_id, int index, String cavityInsulationGrade, double cavityInsulationR,
                double studSpacing, double continuousInsulationR, double studWidth,
                double studDepth, String studMaterial);
}
