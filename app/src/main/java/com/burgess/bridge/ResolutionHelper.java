package com.burgess.bridge;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResolutionHelper {
    private List<Resolution> resolutionList;
    private int mDivisionId;
    private int mInspectionClass;
    private String mInspectionType;

    public ResolutionHelper(int divisionId, int inspectionClass, String inspectionType) {
        resolutionList = new ArrayList<>();
        mDivisionId = divisionId;
        mInspectionClass = inspectionClass;
        mInspectionType = inspectionType;
    }

    public List<Resolution> buildList() {
        Pattern patternHERS = Pattern.compile("^HERS Thermal Bypass Rough");
        Pattern patternEnergy = Pattern.compile("^Energy Code Rough");
        Matcher matcherHERS = patternHERS.matcher(mInspectionType);
        Matcher matcherEnergy = patternEnergy.matcher(mInspectionType);

        resolutionList.clear();
        resolutionList.add(new Resolution(0, "Complete"));
        resolutionList.add(new Resolution(1, "Cancelled in field by Bldr"));
        if (mDivisionId == 20) {
            resolutionList.add(new Resolution(25, "Deficiencies Noted"));
            resolutionList.add(new Resolution(26, "No Deficiencies Observed"));
        } else {
            resolutionList.add(new Resolution(12, "Failed"));
            resolutionList.add(new Resolution(11, "Passed"));
        }
        resolutionList.add(new Resolution(3, "Not Ready"));
        resolutionList.add(new Resolution(15, "Cancelled before 7:30"));
        resolutionList.add(new Resolution(22, "Deferred by Consultant"));
        if (mInspectionClass == 7) {
            resolutionList.add(new Resolution(24, "Builder to Verify"));
            if (mInspectionType.startsWith("HERS Thermal Bypass Rough") || mInspectionType.startsWith("Energy Code Rough")) {
                resolutionList.add(new Resolution(32, "Correct & Proceed"));
            }
            if (mDivisionId == 5 || mDivisionId == 17) {
                resolutionList.add(new Resolution(21, "Batch"));
            }
        }
        resolutionList.add(new Resolution(27, "Performed"));
        resolutionList.add(new Resolution(30, "Deferred by Weather"));
        resolutionList.add(new Resolution(31, "Deferred Due to Cancellation"));

        return resolutionList;
    }

    public class Resolution {
        public int code;
        public String description;

        public Resolution(int code, String description) {
            this.code = code;
            this.description = description;
        }
    }
}
