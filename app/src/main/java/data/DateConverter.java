package data;

import android.provider.SyncStateContract;

import androidx.room.TypeConverter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateConverter {
    private static DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    @TypeConverter
    public static OffsetDateTime toOffsetDateTime(String value) {
        return value == null ? null : formatter.parse(value, OffsetDateTime::from);
    }

    @TypeConverter
    public static String fromOffsetDateTime(OffsetDateTime date) {
        return date == null ? null : date.toString();
    }
}
