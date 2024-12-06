package data.Tables;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "inspection_defect_table")
public class InspectionDefect_Table {
    @PrimaryKey(autoGenerate = true)
    public int ID;
    public int InspectionID;
    public int DefectItemID;
    public int DefectStatusID;
    public String Comment;
    public int PriorInspectionDetailID;
    public int FirstDefectInspectionDetailID;
    public String LotNumber;
    public String StageOfConstruction;
    public boolean ReinspectionRequired;
    public String PicturePath;
    public byte[] AttachmentData;
    public boolean IsUploaded;
    public boolean IsEditable;

    public InspectionDefect_Table() {}

    public InspectionDefect_Table(int InspectionID, int DefectItemID, int DefectStatusID,
                                  String Comment, int PriorInspectionDetailID,
                                  int FirstDefectInspectionDetailID, String LotNumber,
                                  String StageOfConstruction, boolean ReinspectionRequired,
                                  String PicturePath, byte[] AttachmentData) {
        this.InspectionID = InspectionID;
        this.DefectItemID = DefectItemID;
        this.DefectStatusID = DefectStatusID;
        this.Comment = Comment;
        this.PriorInspectionDetailID = PriorInspectionDetailID;
        this.FirstDefectInspectionDetailID = FirstDefectInspectionDetailID;
        this.LotNumber = LotNumber;
        this.StageOfConstruction = StageOfConstruction;
        this.ReinspectionRequired = ReinspectionRequired;
        this.PicturePath = PicturePath;
        this.AttachmentData = AttachmentData;
        this.IsUploaded = false;
        this.IsEditable = true;
    }

    @NonNull
    @Override
    public String toString() {
        return "ID: " + ID + ",InspectionId: " + InspectionID + ", DefectItemId: " + DefectItemID +
                ", DefectStatusId: " + DefectStatusID + ", Comment: " + Comment + ", PriorInspectionDetailId: " +
                PriorInspectionDetailID + ", FirstInspectionDetailId: " + FirstDefectInspectionDetailID +
                ", ReinspectionRequired: " + ReinspectionRequired + ", PicturePath: " + PicturePath;
    }
}