package data;

import androidx.room.DatabaseView;

@DatabaseView("SELECT id.id AS inspection_defect_id, d.item_number, d.item_description, id.comment " +
                "FROM inspection_defect_table id " +
                "INNER JOIN defect_item_table d ON id.defect_item_id = d.id")
public class ReviewAndSubmit_View {
    public int inspection_defect_id;
    public int item_number;
    public String item_description;
    public String comment;
}
