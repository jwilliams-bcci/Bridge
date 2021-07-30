package data.Views;

import androidx.room.DatabaseView;

@DatabaseView("SELECT h.id, d.*, h.inspection_id, h.comment FROM defect_item_table d INNER JOIN inspection_history_table h ON h.defect_item_id = d.id")
public class ReinspectDefectList_View {
    public int id;
    public int defect_category_id;
    public String defect_category_name;
    public int item_number;
    public int inspection_type_id;
    public String item_description;
    public String spanish_item_description;
    public int inspection_id;
    public String comment;
}
