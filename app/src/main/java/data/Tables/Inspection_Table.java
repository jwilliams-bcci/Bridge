package data.Tables;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.time.OffsetDateTime;

import data.DateConverter;

@Entity(tableName = "inspection_table")
public class Inspection_Table {
    @PrimaryKey
    public int InspectionID;
    public int InspectionTypeID;
    @TypeConverters({DateConverter.class})
    public String InspectionDate = "";
    public int DivisionID;
    public int LocationID;
    public String BuilderName = "";
    public int BuilderID;
    public String SuperName = "";
    public int InspectorID;
    public String Inspector = "";
    public String Community = "";
    public int CommunityID;
    public String City = "";
    public int InspectionClass;
    public String InspectionType = "";
    public boolean ReInspect;
    public int InspectionOrder;
    public String Address = "";
    public int InspectionStatusID;
    public String InspectionStatus = "";
    public String SuperPhone = "";
    public String SuperEmailAddress = "";
    public int SuperintendentPresent;
    public String IncompleteReason = "";
    public int IncompleteReasonID;
    public String Notes = "";
    public String JobNumber = "";
    public boolean RequireRiskAssessment;
    public String EkotropeProjectID = "";
    public String EkotropePlanID = "";
    @TypeConverters({DateConverter.class})
    public OffsetDateTime StartTime;
    @TypeConverters({DateConverter.class})
    public OffsetDateTime EndTime;
    public boolean IsComplete;
    public boolean IsUploaded;
    public boolean IsFailed;
    public int RouteSheetOrder;
    public int TraineeID;
    public String JotformLink = "";

    public Inspection_Table () {}

    public Inspection_Table(int InspectionID, String InspectionDate, int DivisionID,
                            int LocationID, String BuilderName, int BuilderID, String SuperName,
                            int InspectorID, String Inspector, String Community, int CommunityID,
                            int InspectionClass, String City, int InspectionTypeID,
                            String InspectionType, boolean ReInspect, int InspectionOrder,
                            String Address, int InspectionStatusID, String InspectionStatus,
                            String SuperPhone, String SuperEmailAddress, int SuperintendentPresent,
                            String IncompleteReason, int IncompleteReasonID, String Notes,
                            String JobNumber, boolean RequireRiskAssessment,
                            String EkotropeProjectID, int RouteSheetOrder, String JotformLink) {
        this.InspectionID = InspectionID;
        this.InspectionDate = InspectionDate;
        this.DivisionID = DivisionID;
        this.BuilderName = BuilderName.isEmpty() ? "" : BuilderName;
        this.BuilderID = BuilderID;
        this.SuperName = SuperName.isEmpty() ? "" : SuperName;
        this.Community = Community.isEmpty() ? "" : Community;
        this.CommunityID = CommunityID;
        this.InspectionClass = InspectionClass;
        this.City = City.isEmpty() ? "" : City;
        this.InspectorID = InspectorID;
        this.Inspector = Inspector.isEmpty() ? "" : Inspector;
        this.LocationID = LocationID;
        this.InspectionTypeID = InspectionTypeID;
        this.InspectionType = InspectionType.isEmpty() ? "" : InspectionType;
        this.ReInspect = ReInspect;
        this.InspectionOrder = InspectionOrder;
        this.Address = Address.isEmpty() ? "" : Address;
        this.InspectionStatusID = InspectionStatusID;
        this.InspectionStatus = InspectionStatus.isEmpty() ? "" : InspectionStatus;
        this.SuperPhone = SuperPhone.isEmpty() ? "" : SuperPhone;
        this.SuperEmailAddress = SuperEmailAddress.isEmpty() ? "" : SuperEmailAddress;
        this.SuperintendentPresent = SuperintendentPresent;
        this.IncompleteReason = IncompleteReason.isEmpty() ? "" : IncompleteReason;
        this.IncompleteReasonID = IncompleteReasonID;
        this.Notes = Notes.isEmpty() ? "" : Notes;
        this.JobNumber = JobNumber.isEmpty() ? "" : JobNumber;
        this.RequireRiskAssessment = RequireRiskAssessment;
        this.EkotropeProjectID = EkotropeProjectID.isEmpty() ? "" : EkotropeProjectID;
        this.EkotropePlanID = null;
        this.StartTime = null;
        this.EndTime = null;
        this.IsComplete = false;
        this.IsUploaded = false;
        this.IsFailed = false;
        this.RouteSheetOrder = RouteSheetOrder;
        this.TraineeID = -1;
        this.JotformLink = JotformLink.isEmpty() ? "" : JotformLink;
    }
}
