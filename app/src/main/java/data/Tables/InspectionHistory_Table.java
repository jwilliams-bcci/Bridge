package data.Tables;

import androidx.annotation.Nullable;
import androidx.room.Entity;

@Entity(tableName = "inspection_history_table", primaryKeys = {"InspectionDetailID", "InspectionID"})
public class InspectionHistory_Table {
    public int InspectionDetailID;
    public int InspectionID;
    public int FirstDefectInspectionDetailID;
    public int PreviousInspectionID;
    public int DefectItemID;
    public int ItemNumber;
    public int DefectStatusID;
    public int DefectCategoryID;
    public String CategoryName;
    public String ItemDescription;
    public String Comment;
    public boolean IsReviewed;
    @Nullable
    public Integer ReviewedStatus;
    @Nullable
    public Integer InspectionDefectID;

    public InspectionHistory_Table() {
        this.InspectionDetailID = 0;
        this.InspectionID = 0;
        this.FirstDefectInspectionDetailID = 0;
        this.PreviousInspectionID = 0;
        this.DefectItemID = 0;
        this.ItemNumber = 0;
        this.DefectStatusID = 0;
        this.DefectCategoryID = 0;
        this.CategoryName = "";
        this.ItemDescription = "";
        this.Comment = "";
        this.IsReviewed = false;
        this.ReviewedStatus = null;
        this.InspectionDefectID = null;
    }

    public InspectionHistory_Table(int InspectionDetailID, int InspectionID, int FirstDefectInspectionDetailID,
                                   int PreviousInspectionID, int DefectItemID, int ItemNumber,
                                   int DefectStatusID, int DefectCategoryID, String CategoryName,
                                   String ItemDescription, String Comment) {
        this.InspectionDetailID = InspectionDetailID;
        this.FirstDefectInspectionDetailID = FirstDefectInspectionDetailID;
        this.InspectionID = 0;
        this.PreviousInspectionID = PreviousInspectionID;
        this.DefectItemID = DefectItemID;
        this.ItemNumber = ItemNumber;
        this.DefectStatusID = DefectStatusID;
        this.DefectCategoryID = DefectCategoryID;
        this.CategoryName = CategoryName;
        this.ItemDescription = ItemDescription;
        this.Comment = Comment;
        this.IsReviewed = false;
        this.ReviewedStatus = null;
        this.InspectionDefectID = null;
    }
}