package data.DAOs;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import data.Tables.DefectItem_InspectionType_XRef;

@Dao
public interface DefectItem_InspectionType_DAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DefectItem_InspectionType_XRef relation);
}
