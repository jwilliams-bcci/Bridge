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
    public int first_inspection_detail_id;
    public String lot_number;
    public String stage_of_construction;
    public boolean reinspection_required;
    public String picture_path;
    public byte[] attachment_data;
    public boolean is_uploaded;
    public boolean is_editable;

    public InspectionDefect_Table(int inspection_id, int defect_item_id, int defect_status_id,
                                  String comment, int prior_inspection_detail_id,
                                  int first_inspection_detail_id, String lot_number,
                                  String stage_of_construction, boolean reinspection_required,
                                  String picture_path, byte[] attachment_data) {
        this.inspection_id = inspection_id;
        this.defect_item_id = defect_item_id;
        this.defect_status_id = defect_status_id;
        this.comment = comment;
        this.prior_inspection_detail_id = prior_inspection_detail_id;
        this.first_inspection_detail_id = first_inspection_detail_id;
        this.lot_number = lot_number;
        this.stage_of_construction = stage_of_construction;
        this.reinspection_required = reinspection_required;
        this.picture_path = picture_path;
        this.attachment_data = attachment_data;
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
                prior_inspection_detail_id + ", FirstInspectionDetailId: " + first_inspection_detail_id +
                ", ReinspectionRequired: " + reinspection_required + ", PicturePath: " + picture_path;
    }
}