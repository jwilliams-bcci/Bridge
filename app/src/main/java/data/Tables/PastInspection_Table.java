package data.Tables;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "past_inspection_table")
public class PastInspection_Table {
    @PrimaryKey
    public int InspectionID;
    public String InspectionSubmitDate;
    public int LocationID;
    public String Inspector;
    public String InspectionType;
    public int InspectionStatusID;
    public String IncompleteReason;

    public PastInspection_Table(){}

    public PastInspection_Table(int InspectionID, String InspectionSubmitDate,
                                int LocationID, String inspector, String InspectionType,
                                int InspectionStatusID, String IncompleteReason) {
        this.InspectionID = InspectionID;
        this.InspectionSubmitDate = InspectionSubmitDate;
        this.LocationID = LocationID;
        this.Inspector = inspector;
        this.InspectionType = InspectionType;
        this.InspectionStatusID = InspectionStatusID;
        this.IncompleteReason = IncompleteReason;
    }
}
