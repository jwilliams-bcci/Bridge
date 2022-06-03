package data.Enums;

import androidx.annotation.NonNull;

public enum Resolution {
    COMPLETE("Complete", 0, false),
    CANCELLED_IN_FIELD_BY_BLDR("Cancelled in field by Bldr", 1, true),
    NOT_READY("Not Ready", 3, false),
    FAILED("Failed", 12, false),
    PASSED("Passed", 11, false),
    CANCELLED_BEFORE_730("Cancelled Before 7:30", 15, true),
    DEFICIENCIES_NOTED("Deficiencies Noted", 25, false),
    NO_DEFICIENCIES_OBSERVED("No Deficiencies Observed", 26, false),
    BATCH("Batch", 21, false),
    DEFERRED_BY_CONSULTANT("Deferred by Consultant", 22, true),
    BUILDER_TO_VERIFY("Builder to Verify", 24, false),
    PERFORMED("Performed", 27, false),
    DEFERRED_BY_WEATHER("Deferred by Weather", 30, true),
    DEFERRED_DUE_TO_CANCELLATION("Deferred Due to Cancellation", 31, true)
    ;

    public String description;
    public int code;
    public boolean editResolutionOnly;

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
}