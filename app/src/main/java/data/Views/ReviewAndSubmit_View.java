package data.Views;

import androidx.room.DatabaseView;

@DatabaseView("SELECT id.id AS inspection_defect_id, id.inspection_id, d.id AS defect_item_id, d.item_number, id.defect_status_id, d.item_description, id.comment, d.reinspection_required, id.picture_path " +
                "FROM inspection_defect_table id " +
                "INNER JOIN defect_item_table d ON id.defect_item_id = d.id")
public class ReviewAndSubmit_View {
    public int inspection_defect_id;
    public int inspection_id;
    public int defect_item_id;
    public int item_number;
    public int defect_status_id;
    public String item_description;
    public String comment;
    public boolean reinspection_required;
    public String picture_path;

    public boolean notAllCs() {
        return this.defect_status_id == 2 || this.defect_status_id != 6;
    }

    public boolean reinspectionRequired() {
        return this.reinspection_required && this.defect_status_id == 2;
    }
}
