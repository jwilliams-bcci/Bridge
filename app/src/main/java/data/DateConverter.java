package data;

import android.provider.SyncStateContract;

import androidx.room.TypeConverter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.Date;

public class DateConverter {
    static DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

    @TypeConverter
    public static Date fromString(String value) {
        if (value != null) {
            try {
                return df.parse(value);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @TypeConverter
    public static String toOffsetDateTime(OffsetDateTime value) {
        if (value != null) {
            try {
                return value.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
