package data.Views;

import androidx.room.DatabaseView;

@DatabaseView("SELECT id.ID AS InspectionDefectID, id.InspectionID, d.DefectItemID AS DefectItemID," +
        "d.ItemNumber, id.DefectStatusID, d.ItemDescription, id.Comment," +
        "d.ReInspectionRequired AS ReinspectionRequired, id.PicturePath, id.IsEditable, 'DEFECT' AS ItemType " +
        "FROM inspection_defect_table id " +
        "INNER JOIN defect_item_table d ON id.DefectItemID = d.DefectItemID " +
        "UNION " +
        "SELECT a.AttachmentID AS InspectionDefectID, a.InspectionID, -1 AS DefectItemID, " +
        "-1 AS item_number, -1 AS DefectStatusID, a.FileName AS ItemDescription, " +
        "'' AS Comment, 0 AS ReinspectionRequired, FilePath as PicturePath, 1 AS IsEditable," +
        "'ATTACHMENT' AS ItemType " +
        "FROM attachment_table a " +
        "WHERE a.IsUploaded = 0")
public class ReviewAndSubmit_View {
    public int InspectionDefectID;
    public int InspectionID;
    public int DefectItemID;
    public int ItemNumber;
    public int DefectStatusID;
    public String ItemDescription;
    public String Comment;
    public boolean ReinspectionRequired;
    public String PicturePath;
    public boolean IsEditable;
    public String ItemType;

    public boolean isNC() {
        return this.DefectStatusID == 2;
    }

    public boolean isR() {
        return this.DefectStatusID == 6;
    }

    public boolean reinspectionRequired() {
        return this.ReinspectionRequired && this.DefectStatusID == 2;
    }
}
