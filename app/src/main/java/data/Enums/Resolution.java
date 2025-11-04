package data.Enums;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum Resolution {
    COMPLETE("Complete", 0, false),
    CANCELLED_IN_FIELD_BY_BLDR("Cancelled in field by Bldr", 1, true),
    NOT_READY("Not Ready", 3, false),
    FAILED("Failed", 12, false),
    PASSED("Passed", 11, false),
    CANCELLED_BEFORE_730("Cancelled Before 7:30", 15, true),
    BATCH("Batch", 21, false),
    DEFERRED_BY_CONSULTANT("Deferred by Consultant", 22, true),
    BUILDER_TO_VERIFY("Builder to Verify", 24, false),
    DEFICIENCIES_NOTED("Deficiencies Noted", 25, false),
    NO_DEFICIENCIES_OBSERVED("No Deficiencies Observed", 26, false),
    PERFORMED("Performed", 27, false),
    DEFERRED_BY_WEATHER("Deferred by Weather", 30, true),
    DEFERRED_DUE_TO_CANCELLATION("Deferred Due to Cancellation", 31, true),
    CORRECT_AND_PROCEED("Correct & Proceed", 32, false),
    REVIEW("Review", 33, false)
    ;

    public String description;
    public int code;
    public boolean editResolutionOnly;
    private static final Map<Integer, Resolution> map;
    static {
        map = new HashMap<>();
        for (Resolution r : Resolution.values()) {
            map.put(r.code, r);
        }
    }

    Resolution(String description, int code, boolean editResolutionOnly) {
        this.description = description;
        this.code = code;
        this.editResolutionOnly = editResolutionOnly;
    }

    @NonNull
    @Override
    public String toString() {
        return description;
    }

    public static Resolution findByCode(int i) {
        return map.get(i);
    }
}