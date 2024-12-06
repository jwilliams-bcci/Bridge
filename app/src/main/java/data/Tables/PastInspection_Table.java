package data.Tables;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.time.OffsetDateTime;

import data.DateConverter;

@Entity(tableName = "past_inspection_table")
public class PastInspection_Table {
    @PrimaryKey
    public int InspectionID;
    @TypeConverters({DateConverter.class})
    public String InspectionSubmitTime;
    public int LocationID;
    public String Inspector;
    public String InspectionType;
    public int InspectionStatusID;
    public String IncompleteReason;

    public PastInspection_Table(){}

    public PastInspection_Table(int InspectionID, String InspectionSubmitTime,
                                int LocationID, String inspector, String InspectionType,
                                int InspectionStatusID, String IncompleteReason) {
        this.InspectionID = InspectionID;
        this.InspectionSubmitTime = InspectionSubmitTime;
        this.LocationID = LocationID;
        this.Inspector = inspector;
        this.InspectionType = InspectionType;
        this.InspectionStatusID = InspectionStatusID;
        this.IncompleteReason = IncompleteReason;
    }
}
