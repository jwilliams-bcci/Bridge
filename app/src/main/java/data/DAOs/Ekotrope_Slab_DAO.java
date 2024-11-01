package data.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import data.Tables.Ekotrope_Slab_Table;

@Dao
public interface Ekotrope_Slab_DAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Ekotrope_Slab_Table slab);

    @Query("DELETE FROM ekotrope_slab_table")
    void deleteAll();

    @Query("DELETE FROM ekotrope_slab_table WHERE plan_id = :plan_id AND [index] = :index")
    void delete(String plan_id, int index);

    @Query("SELECT * FROM ekotrope_slab_table WHERE plan_id = :plan_id AND [index] = :index")
    Ekotrope_Slab_Table getSlab(String plan_id, int index);

    @Query("SELECT * FROM ekotrope_slab_table WHERE plan_id = :plan_id")
    LiveData<List<Ekotrope_Slab_Table>> getSlabs(String plan_id);

    @Query("SELECT * FROM ekotrope_slab_table WHERE plan_id = :plan_id")
    List<Ekotrope_Slab_Table> getSlabsSync(String plan_id);

    @Query("UPDATE ekotrope_slab_table SET " +
            "name = :name, typeName = :typeName, underslabInsulationR = :underslabInsulationR, " +
            "isFullyInsulated = :isFullyInsulated, underslabInsulationWidth = :underslabInsulationWidth, " +
            "perimeterInsulationDepth = :perimeterInsulationDepth, perimeterInsulationR = :perimeterInsulationR, " +
            "thermalBreak = :thermalBreak " +
            "WHERE plan_id = :plan_id AND [index] = :index")
    void update(String plan_id, int index, String name, String typeName, double underslabInsulationR,
                boolean isFullyInsulated, double underslabInsulationWidth, double perimeterInsulationDepth,
                double perimeterInsulationR, boolean thermalBreak);
}
