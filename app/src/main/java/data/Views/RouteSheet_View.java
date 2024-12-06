package data.Views;

import androidx.room.DatabaseView;

import java.time.OffsetDateTime;

@DatabaseView("SELECT i.InspectionID, i.LocationID, i.DivisionID, i.InspectorID, i.InspectionTypeID," +
                "i.InspectionStatusID, i.ReInspect, i.Community, i.Address, i.InspectionType," +
                "i.Notes, i.JobNumber, i.RequireRiskAssessment, i.EkotropeProjectID," +
                "i.InspectionDate, i.IsComplete, i.IsFailed, i.IsUploaded," +
                "SUM(CASE WHEN id.IsUploaded = 1 THEN 1 ELSE 0 END) AS NumUploaded, " +
                "COUNT(id.ID) AS NumTotal, i.StartTime, RouteSheetOrder " +
                "FROM inspection_table i " +
                "LEFT JOIN inspection_defect_table id ON id.InspectionID = i.InspectionID GROUP BY i.InspectionID")
public class RouteSheet_View {
    public int InspectionID;
    public int LocationID;
    public int DivisionID;
    public int InspectorID;
    public int InspectionTypeID;
    public int InspectionStatusID;
    public boolean ReInspect;
    public String Community;
    public String Address;
    public String InspectionType;
    public String Notes;
    public String JobNumber;
    public boolean RequireRiskAssessment;
    public String EkotropeProjectID;
    public OffsetDateTime InspectionDate;
    public boolean IsComplete;
    public boolean IsFailed;
    public boolean IsUploaded;
    public int NumUploaded;
    public int NumTotal;
    public OffsetDateTime StartTime;
    public int RouteSheetOrder;
}
