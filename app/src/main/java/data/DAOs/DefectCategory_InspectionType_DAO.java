package data.DAOs;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import data.Tables.DefectCategory_InspectionType_XRef;

@Dao
public interface DefectCategory_InspectionType_DAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(DefectCategory_InspectionType_XRef defectCategory_inspectionType);
}
