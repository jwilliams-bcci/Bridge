package data.Views;

import androidx.room.DatabaseView;

@DatabaseView("SELECT id.id AS inspection_defect_id, id.inspection_id, d.id AS defect_item_id," +
        "d.item_number, id.defect_status_id, d.item_description, id.comment," +
        "d.reinspection_required, id.picture_path, id.is_editable, 'DEFECT' AS item_type " +
        "FROM inspection_defect_table id " +
        "INNER JOIN defect_item_table d ON id.defect_item_id = d.id " +
        "UNION " +
        "SELECT a.id AS inspection_defect_id, a.inspection_id, -1 AS defect_item_id, " +
        "-1 AS item_number, -1 AS defect_status_id, a.file_name AS item_description, " +
        "'' AS comment, 0 AS reinspection_required, file_path as picture_path, 1 AS is_editable," +
        "'ATTACHMENT' AS item_type " +
        "FROM attachment_table a " +
        "WHERE a.is_uploaded = 0")
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
    public boolean is_editable;
    public String item_type;

    public boolean isNC() {
        return this.defect_status_id == 2;
    }

    public boolean isR() {
        return this.defect_status_id == 6;
    }

    public boolean reinspectionRequired() {
        return this.reinspection_required && this.defect_status_id == 2;
    }
}
