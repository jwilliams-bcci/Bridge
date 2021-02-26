package data;

import androidx.room.DatabaseView;

import java.util.Date;

@DatabaseView("SELECT i.id AS inspection_id, l.community, l.address, i.inspection_type, i.notes, i.inspection_date " +
        "FROM location_table l " +
        "INNER JOIN inspection_table i ON i.location_id = l.id")
public class RouteSheet_View {
    public int inspection_id;
    public String community;
    public String address;
    public String inspection_type;
    public String notes;
    public Date inspection_date;
}
