package data.Tables;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "inspection_defect_table")
public class InspectionDefect_Table {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int inspection_id;
    public int defect_item_id;
    public int defect_status_id;
    public String comment;
    public int prior_inspection_detail_id;
    public boolean reinspection_required;
    public String picture_path;
    public boolean is_uploaded;
    public boolean is_editable;

    public InspectionDefect_Table(int inspection_id, int defect_item_id, int defect_status_id, String comment, int prior_inspection_detail_id, boolean reinspection_required, String picture_path) {
        this.inspection_id = inspection_id;
        this.defect_item_id = defect_item_id;
        this.defect_status_id = defect_status_id;
        this.comment = comment;
        this.prior_inspection_detail_id = prior_inspection_detail_id;
        this.reinspection_required = reinspection_required;
        this.picture_path = picture_path;
        this.is_uploaded = false;
        this.is_editable = true;
    }

    public InspectionDefect_Table() {

    }

    @NonNull
    @Override
    public String toString() {
        return "ID: " + id + ",InspectionId: " + inspection_id + ", DefectItemId: " + defect_item_id +
                ", DefectStatusId: " + defect_status_id + ", Comment: " + comment + ", PriorInspectionDetailId: " +
                prior_inspection_detail_id + ", ReinspectionRequired: " + reinspection_required;
    }
}