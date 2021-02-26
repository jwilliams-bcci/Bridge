package data;

import androidx.room.Embedded;
import androidx.room.Relation;

import data.Tables.Inspection_Table;
import data.Tables.Location_Table;

public class LocationWithInspections {
    @Embedded public Location_Table location;
    @Relation(
            parentColumn = "id",
            entityColumn = "locationId"
    )
    public Inspection_Table inspection;
}