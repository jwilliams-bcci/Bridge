package data.Enums;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.List;

public enum ResolutionEnergy {
    COMPLETE("Complete", 0),
    CANCELLED_IN_FIELD_BY_BLDR("Cancelled in field by Bldr", 1),
    NOT_READY("Not Ready", 3),
    FAILED("Failed", 12),
    PASSED("Passed", 11),
    CANCELLED_BEFORE_730("Cancelled Before 7:30", 15),
    DEFICIENCIES_NOTED("Deficiencies Noted", 25),
    NO_DEFICIENCIES_OBSERVED("No Deficiencies Observed", 26),
    BATCH("Batch", 21),
    DEFERRED_BY_CONSULTANT("Deferred by Consultant", 22),
    BUILDER_TO_VERIFY("Builder to Verify", 2),
    PERFORMED("Performed", 27),
    DEFERRED_BY_WEATHER("Deferred by Weather", 30),
    DEFERRED_DUE_TO_CANCELLATION("Deferred Due to Cancellation", 31),
    ;

    public String description;
    public int code;

    ResolutionEnergy(String description, int code) {
        this.description = description;
        this.code = code;
    }

    @NonNull
    @Override
    public String toString() {
        return description;
    }
}
