package data.Enums;

import androidx.annotation.NonNull;

public enum IncompleteReason {
    COMPLETE("Complete", 0),
    CANCELLED_IN_FIELD_BY_BLDR("Cancelled in field by Bldr", 1),
    NOT_READY("Not Ready", 3),
    FAILED("Failed", 12),
    CANCELLED_BEFORE_730("Cancelled Before 7:30", 15),
    BATCH("Batch", 21),
    DEFERRED_BY_CONSULTANT("Deferred by Consultant", 22),
    BUILDER_TO_VERIFY("Builder to Verify", 24),
    PERFORMED("Performed", 27),
    DEFERRED_BY_WEATHER("Deferred by Weather", 30),
    DEFERRED_DUE_TO_CANCELLATION("Deferred Due to Cancellation", 31)
    ;

    private String description;
    private int code;

    IncompleteReason(String description, int code) {
        this.description = description;
        this.code = code;
    }

    @NonNull
    @Override
    public String toString() {
        return description;
    }
}