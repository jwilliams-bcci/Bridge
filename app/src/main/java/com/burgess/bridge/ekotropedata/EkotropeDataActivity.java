package com.burgess.bridge.ekotropedata;

import static com.burgess.bridge.Constants.PREF;
import static com.burgess.bridge.Constants.PREF_INSPECTOR_ID;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.toolbox.JsonArrayRequest;
import com.burgess.bridge.BridgeAPIQueue;
import com.burgess.bridge.BridgeLogger;
import com.burgess.bridge.R;
import com.burgess.bridge.ServerCallback;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import data.Tables.Ekotrope_Data_Table;


public class EkotropeDataActivity extends AppCompatActivity {
    private SharedPreferences mSharedPreferences;
    private String mInspectorId;
    private Button mButtonEmail;
    private static final String TAG = "EKOTROPE_DATA";
    private long mLastClickTime = 0;

    public static final String INSPECTION_ID = "com.burgess.bridge.INSPECTION_ID";
    public static final int INSPECTION_ID_NOT_FOUND = -1;
    public int mInspectionId;
    private Ekotrope_Data_Table mInspection;
    private EkotropeDataViewModel mEkotropeDataViewModel;

    private static BridgeAPIQueue apiQueue;
    private JsonArrayRequest mUpdateEkotropeDataRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ekotrope_data);
        setSupportActionBar(findViewById(R.id.inspect_toolbar));

        apiQueue = BridgeAPIQueue.getInstance(this);

        mEkotropeDataViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(EkotropeDataViewModel.class);

        mSharedPreferences = getSharedPreferences(PREF, Context.MODE_PRIVATE);
        mInspectorId = mSharedPreferences.getString(PREF_INSPECTOR_ID, "NULL");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        String currentDate = formatter.format(OffsetDateTime.now());
        mUpdateEkotropeDataRequest = apiQueue.updateEkotropeData(mEkotropeDataViewModel, mInspectorId, currentDate, new ServerCallback() {
            @Override
            public void onSuccess(String message) {
            }

            @Override
            public void onFailure(String message) {
            }
        });
        apiQueue.getRequestQueue().add(mUpdateEkotropeDataRequest);

        Intent intent = getIntent();
        mInspectionId = intent.getIntExtra(INSPECTION_ID, INSPECTION_ID_NOT_FOUND);
        mInspection = mEkotropeDataViewModel.getInspectionSync(mInspectionId);

        initializeViews();
        initializeButtonListeners();
        initializeDisplayContent();
    }

    private void initializeViews() {
        mButtonEmail = findViewById(R.id.ekotrope_data_button_email);
    }

    private void initializeDisplayContent() {

    }

    private void initializeButtonListeners() {
        mButtonEmail.setOnClickListener(view -> {
            // Prevent double clicking
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();

            try {
                Intent emailIntent = sendEkotropeEmail();
                startActivity(Intent.createChooser(emailIntent, "Send Ekotrope Data..."));
            } catch (Exception e) {
                BridgeLogger.log('E', TAG, "ERROR in initializeButtonListeners: " + e.getMessage());
            }
        });
    }

    public Intent sendEkotropeEmail() {
        //Uri logFileUri = FileProvider.getUriForFile(ctx, "com.burgess.bridge", logFile);
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("vnd.android.cursor.dir/email");
        String[] to = {"jwilliams@burgess-inc.com", "rsandlin@burgess-inc.com", "bwallace@burgess-inc.com"};
        emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
        //emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(String.valueOf(logFileUri)));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Ekotrope Data for: " + mInspection.address);

        String getInfoExample = "Example Get Information Request\n" +
                "URI: https://api.ekotrope.com/api/v1/projects/" + mInspection.ekotrope_project_id + "\n" +
                "Header: Accept, application/json" + "\n" +
                "Header: Authorization, Basic 123";

        String getInfoReplyExample = "Example Get Information Reply\n" +
                "{\n" +
                "  \"id\": \"string\",\n" +
                "  \"name\": \"string\",\n" +
                "  \"createdAt\": \"2019-08-24T14:15:22Z\",\n" +
                "  \"createdBy\": \"string\",\n" +
                "  \"lastSavedAt\": \"2019-08-24T14:15:22Z\",\n" +
                "  \"lastSavedBy\": \"string\",\n" +
                "  \"selfOrPlanLastSavedAt\": \"2019-08-24T14:15:22Z\",\n" +
                "  \"isLocked\": true,\n" +
                "  \"status\": \"UNREGISTERED\",\n" +
                "  \"location\": {\n" +
                "    \"streetAddress\": \"string\",\n" +
                "    \"city\": \"string\",\n" +
                "    \"state\": \"DC\",\n" +
                "    \"zip\": \"strin\",\n" +
                "    \"zipWeather\": \"strin\",\n" +
                "    \"climateZone\": {\n" +
                "      \"zone\": 1,\n" +
                "      \"moistureRegime\": \"DRY\"\n" +
                "    },\n" +
                "    \"weatherStationName\": \"string\",\n" +
                "    \"county\": \"string\"\n" +
                "  },\n" +
                "  \"notes\": \"string\",\n" +
                "  \"model\": \"string\",\n" +
                "  \"community\": \"string\",\n" +
                "  \"lotNumber\": \"string\",\n" +
                "  \"builder\": \"string\",\n" +
                "  \"builderHomeId\": \"string\",\n" +
                "  \"builderPermitDateOrNumber\": \"\",\n" +
                "  \"constructionYear\": \"string\",\n" +
                "  \"isAffordableDevelopment\": true,\n" +
                "  \"submittedProjectId\": \"string\",\n" +
                "  \"hersRatingDetails\": {\n" +
                "    \"rater\": {\n" +
                "      \"name\": \"string\",\n" +
                "      \"phone\": \"string\",\n" +
                "      \"email\": \"user@example.com\",\n" +
                "      \"resnetRaterId\": \"string\",\n" +
                "      \"resnetProviderId\": \"string\",\n" +
                "      \"ratingCompany\": {\n" +
                "        \"id\": \"string\",\n" +
                "        \"name\": \"string\",\n" +
                "        \"address\": \"string\"\n" +
                "      }\n" +
                "    },\n" +
                "    \"registryId\": \"string\",\n" +
                "    \"ratingDate\": \"2019-08-24T14:15:22Z\",\n" +
                "    \"fieldRatingDate\": \"2019-08-24T14:15:22Z\",\n" +
                "    \"ratingType\": \"CONFIRMED\",\n" +
                "    \"sampledSetId\": \"string\",\n" +
                "    \"submittedPlanId\": \"stringst\",\n" +
                "    \"associatedUsers\": [\n" +
                "      {\n" +
                "        \"name\": \"string\",\n" +
                "        \"userType\": \"Rater\",\n" +
                "        \"resnetRaterId\": \"string\",\n" +
                "        \"fieldInspectorId\": \"string\"\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  \"mostRecentlySharedWithProviderAt\": \"2019-08-24T14:15:22Z\",\n" +
                "  \"plans\": [\n" +
                "    {\n" +
                "      \"id\": \"stringst\",\n" +
                "      \"name\": \"string\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"masterPlanId\": \"string\",\n" +
                "  \"resultsUnchangedSince\": \"2019-08-24T14:15:22Z\",\n" +
                "  \"utilityRates\": [\n" +
                "    [\n" +
                "      {\n" +
                "        \"rateApplicablePeriod\": \"reprehenderit officia cupidatat\",\n" +
                "        \"monthlyServiceCharge\": \"adipisicing consequat proident\"\n" +
                "      }\n" +
                "    ]\n" +
                "  ],\n" +
                "  \"targetEnergyStarVersion\": 2,\n" +
                "  \"algorithmVersion\": \"1.8.0\",\n" +
                "  \"builderPermitDate\": \"2019-08-24T14:15:22Z\",\n" +
                "  \"utilitySubmissionHistory\": [\n" +
                "    {\n" +
                "      \"efficiencyProgram\": \"string\",\n" +
                "      \"utilitySubmissionId\": \"string\",\n" +
                "      \"submissionStatus\": \"string\",\n" +
                "      \"submissionDate\": \"2019-08-24T14:15:22Z\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"thirdParty\": {\n" +
                "    \"icfProgram\": {\n" +
                "      \"submissionStatus\": \"UNDER_CONSTRUCTION\",\n" +
                "      \"builder\": \"string\",\n" +
                "      \"builderId\": \"string\",\n" +
                "      \"expectedStart\": \"2019-08-24T14:15:22Z\",\n" +
                "      \"expectedCloseIn\": \"2019-08-24T14:15:22Z\",\n" +
                "      \"expectedCompletion\": \"2019-08-24T14:15:22Z\",\n" +
                "      \"electricMeterNumber\": \"string\",\n" +
                "      \"gasMeterNumber\": \"string\",\n" +
                "      \"premiseNumber\": \"string\",\n" +
                "      \"gasUtility\": \"string\",\n" +
                "      \"electricUtility\": \"string\",\n" +
                "      \"isTwoOverTwoCondo\": true,\n" +
                "      \"isResnetRegisteredUsingThirdPartySoftware\": true,\n" +
                "      \"thirdPartyResnetRegistryId\": \"string\",\n" +
                "      \"isOverrideEnergyStar31\": true,\n" +
                "      \"dukeIncentivePath\": \"WHOLE_HOME\",\n" +
                "      \"seer14Ac\": 0,\n" +
                "      \"seer14Hp\": 0,\n" +
                "      \"seer15Ac\": 0,\n" +
                "      \"seer15Hp\": 0,\n" +
                "      \"seer16Ac\": 0,\n" +
                "      \"seer16Hp\": 0,\n" +
                "      \"heatPumpWaterHeater\": 0,\n" +
                "      \"seer15Qi\": 0,\n" +
                "      \"seer16Qi\": 0,\n" +
                "      \"isPartOfIqEpecPilot\": true,\n" +
                "      \"isPartOfIqPnmPilot\": true,\n" +
                "      \"isPermittedInAlbuquerque\": true,\n" +
                "      \"isEnergyStarCertified\": true,\n" +
                "      \"hasDirectVentFireplace\": true,\n" +
                "      \"communityOit\": \"string\",\n" +
                "      \"isPvReady\": true,\n" +
                "      \"isEvChargerInstalled\": true,\n" +
                "      \"isCommunityEvChargerInstalled\": true\n" +
                "    },\n" +
                "    \"epsProgram\": {\n" +
                "      \"referenceHomeType\": \"CENTRAL\",\n" +
                "      \"bestGuessReferenceHomeType\": \"CENTRAL\"\n" +
                "    },\n" +
                "    \"massSaveProgram\": {\n" +
                "      \"submissionStatus\": \"UNDER_CONSTRUCTION\",\n" +
                "      \"expeditedSubmission\": true,\n" +
                "      \"constructionStartDate\": \"2019-08-24T14:15:22Z\",\n" +
                "      \"firstInspectionDate\": \"2019-08-24T14:15:22Z\",\n" +
                "      \"finalInspectionDate\": \"2019-08-24T14:15:22Z\",\n" +
                "      \"estimatedCompletionDate\": \"2019-08-24T14:15:22Z\",\n" +
                "      \"isMultifamily\": true,\n" +
                "      \"totalUnitsInDevelopment\": 0,\n" +
                "      \"renovationAdditionType\": \"Renovation\",\n" +
                "      \"masterMeteredElectric\": true,\n" +
                "      \"electricMeterNumber\": \"string\",\n" +
                "      \"electricUtility\": \"string\",\n" +
                "      \"masterMeteredGas\": true,\n" +
                "      \"gasMeterNumber\": \"string\",\n" +
                "      \"gasUtility\": \"string\",\n" +
                "      \"contractorOrBuilderFirstName\": \"string\",\n" +
                "      \"contractorOrBuilderLastName\": \"string\",\n" +
                "      \"contractorOrBuilderCompanyName\": \"string\",\n" +
                "      \"contractorOrBuilderEmail\": \"string\",\n" +
                "      \"contractorOrBuilderPhoneNumber\": \"string\",\n" +
                "      \"participant\": {\n" +
                "        \"role\": \"Builder\",\n" +
                "        \"contactInformation\": {\n" +
                "          \"firstName\": \"string\",\n" +
                "          \"lastName\": \"string\",\n" +
                "          \"companyName\": \"string\",\n" +
                "          \"address\": \"string\",\n" +
                "          \"address2\": \"string\",\n" +
                "          \"city\": \"string\",\n" +
                "          \"state\": \"string\",\n" +
                "          \"zip\": \"string\",\n" +
                "          \"email\": \"string\",\n" +
                "          \"phone\": \"string\"\n" +
                "        }\n" +
                "      },\n" +
                "      \"massPermitType\": \"2009IECC\",\n" +
                "      \"isPassiveHouse\": true,\n" +
                "      \"isZeroEnergyModularAffordableHousing\": true,\n" +
                "      \"isModular\": true,\n" +
                "      \"isManufactured\": true,\n" +
                "      \"passesThermalEnclosureChecklist\": true,\n" +
                "      \"waterSenseFixtures\": true,\n" +
                "      \"allElectricHomeIncentive\": true,\n" +
                "      \"allElectricHomeIncentive2\": true,\n" +
                "      \"isWeatherized\": true,\n" +
                "      \"wasInfluencedAwayFromOilOrPropane\": true,\n" +
                "      \"influenceReasonClimateConcerns\": true,\n" +
                "      \"influenceReasonMassSaveIncentives\": true,\n" +
                "      \"influenceReasonEnergyBill\": true,\n" +
                "      \"influenceReasonBuilderInfluence\": true,\n" +
                "      \"influenceReasonRaterInfluence\": true,\n" +
                "      \"influenceReasonOther\": \"string\"\n" +
                "    },\n" +
                "    \"apsProgram\": {\n" +
                "      \"energyOrbitAddressId\": \"string\"\n" +
                "    },\n" +
                "    \"nipscoProgram\": {\n" +
                "      \"electricAccountNumber\": \"string\",\n" +
                "      \"gasAccountNumber\": \"string\",\n" +
                "      \"comboAccountNumber\": \"string\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"premiumReportsDownloaded\": true,\n" +
                "  \"willHomeBeRegisteredWithResnet\": true\n" +
                "}";

        String postInfoExample = "Example Post Information\n" +
                "URI: https://api.ekotrope.com/api/v1/projects/inspectionSync\n" +
                "Header: Content-Type, application/json" + "\n" +
                "Header: Accept, application/json" + "\n" +
                "Method: POST, {\n" +
                "    \"additionalEmail\": \"user@example.com\",\n" +
                "    \"inspectionSync\": {\n" +
                "        \"projectId\": \"a1B2c3D4\",\n" +
                "        \"ratingDate\": \"2021-10-01\",\n" +
                "        \"permitDate\": \"2019-08-24T14:15:22Z\",\n" +
                "        \"constructionYear\": \"2021\",\n" +
                "        \"builderHomeId\": \"XYZ ABC 123\",\n" +
                "        \"floorsOnOrAboveGrade\": 1,\n" +
                "        \"nBedrooms\": 3,\n" +
                "        \"conditionedFloorArea\": 1850.5,\n" +
                "        \"infiltrationVolume\": 21000.5,\n" +
                "        \"hasEvReadySpace\": true,\n" +
                "        \"hasGarage\": true,\n" +
                "        \"ratingType\": \"CONFIRMED\",\n" +
                "        \"raterOfRecordRTIN\": \"0123456\",\n" +
                "        \"otherRaterRxIN\": [\n" +
                "            \"1234567\",\n" +
                "            \"A1B2C3\"\n" +
                "        ], \n" +
                "        \"orientation\": \"North\",\n" +
                "        \"hasNaturalVentilation\": true,\n" +
                "        \"hasCeilingFan\": true,\n" +
                "        \"ceilingFanCfmPerWatt\": 0,\n" +
                "        \"infiltrationUnit\": \"CFM_50\",\n" +
                "        \"infiltrationValue\": 0,\n" +
                "        \"increaseInfiltrationThresholdAboveTested\": true,\n" +
                "        \"roofColor\": \"Reflective\",\n" +
                "        \"roofSolarAbsorptance\": 0,\n" +
                "        \"wallColor\": \"Reflective\",\n" +
                "        \"wallSolarAbsorptance\": 0,\n" +
                "        \"slabs\": [\n" +
                "            {\n" +
                "                \"index\": 0,\n" +
                "                \"name\": \"string\",\n" +
                "                \"underslabInsulationR\": 0,\n" +
                "                \"underslabInsulationWidth\": 0,\n" +
                "                \"perimeterInsulationDepth\": 0,\n" +
                "                \"perimeterInsulationR\": 0,\n" +
                "                \"thermalBreak\": true\n" +
                "            }\n" +
                "        ],\n" +
                "        \"aboveGradeWalls\": [\n" +
                "            {\n" +
                "                \"index\": 0,\n" +
                "                \"name\": \"string\",\n" +
                "                \"cavityInsulationGrade\": \"I\",\n" +
                "                \"cavityInsulationR\": 0,\n" +
                "                \"continuousInsulationR\": 0,\n" +
                "                \"studSpacing\": 7,\n" +
                "                \"studWidth\": 0,\n" +
                "                \"studDepth\": 0,\n" +
                "                \"studMaterial\": \"Wood\"\n" +
                "            }\n" +
                "        ],\n" +
                "        \"foundationWalls\": [\n" +
                "            {\n" +
                "                \"index\": 0,\n" +
                "                \"name\": \"string\",\n" +
                "                \"cavityInsulationGrade\": \"I\",\n" +
                "                \"cavityInsulationR\": 0,\n" +
                "                \"continuousInsulationR\": 0,\n" +
                "                \"studSpacing\": 7,\n" +
                "                \"studWidth\": 0,\n" +
                "                \"studDepth\": 0,\n" +
                "                \"studMaterial\": \"Wood\"\n" +
                "            }\n" +
                "        ],\n" +
                "        \"framedFloors\": [\n" +
                "            {\n" +
                "                \"index\": 0,\n" +
                "                \"name\": \"string\",\n" +
                "                \"cavityInsulationGrade\": \"I\",\n" +
                "                \"cavityInsulationR\": 0,\n" +
                "                \"continuousInsulationR\": 0,\n" +
                "                \"studSpacing\": 7,\n" +
                "                \"studWidth\": 0,\n" +
                "                \"studDepth\": 0,\n" +
                "                \"studMaterial\": \"Wood\"\n" +
                "            }\n" +
                "        ],\n" +
                "        \"ceilingsAndRoofs\": [\n" +
                "            {\n" +
                "                \"index\": 0,\n" +
                "                \"name\": \"string\",\n" +
                "                \"cavityInsulationGrade\": \"I\",\n" +
                "                \"cavityInsulationR\": 0,\n" +
                "                \"continuousInsulationR\": 0,\n" +
                "                \"studSpacing\": 7,\n" +
                "                \"studWidth\": 0,\n" +
                "                \"studDepth\": 0,\n" +
                "                \"studMaterial\": \"Wood\"\n" +
                "            }\n" +
                "        ],\n" +
                "        \"windows\": [\n" +
                "            {\n" +
                "                \"index\": 0,\n" +
                "                \"remove\": true,\n" +
                "                \"windowArea\": 0,\n" +
                "                \"orientation\": \"North\",\n" +
                "                \"installedWallIndex\": 0,\n" +
                "                \"installedFoundationWallIndex\": 0,\n" +
                "                \"overhangDepth\": 0,\n" +
                "                \"distanceOverhangToTop\": 0,\n" +
                "                \"distanceOverhangToBottom\": 0,\n" +
                "                \"SHGC\": 0,\n" +
                "                \"uFactor\": 0,\n" +
                "                \"adjacentSummerShading\": 0,\n" +
                "                \"adjacentWinterShading\": 0\n" +
                "            }\n" +
                "        ],\n" +
                "        \"newWindows\": [\n" +
                "            {\n" +
                "                \"windowArea\": 0,\n" +
                "                \"orientation\": \"North\",\n" +
                "                \"installedWallIndex\": 0,\n" +
                "                \"installedFoundationWallIndex\": 0,\n" +
                "                \"overhangDepth\": 0,\n" +
                "                \"distanceOverhangToTop\": 0,\n" +
                "                \"distanceOverhangToBottom\": 0,\n" +
                "                \"SHGC\": 0,\n" +
                "                \"uFactor\": 0,\n" +
                "                \"adjacentSummerShading\": 0,\n" +
                "                \"adjacentWinterShading\": 0\n" +
                "            }\n" +
                "        ],\n" +
                "        \"skylights\": [\n" +
                "            {\n" +
                "                \"surfaceArea\": 0,\n" +
                "                \"orientation\": \"North\",\n" +
                "                \"installedRoofIndex\": 0,\n" +
                "                \"pitch\": 0,\n" +
                "                \"index\": 0,\n" +
                "                \"remove\": true,\n" +
                "                \"SHGC\": 0,\n" +
                "                \"uFactor\": 0,\n" +
                "                \"adjacentSummerShading\": 0,\n" +
                "                \"adjacentWinterShading\": 0\n" +
                "            }\n" +
                "        ],\n" +
                "        \"newSkylights\": [\n" +
                "            {\n" +
                "                \"surfaceArea\": 0,\n" +
                "                \"orientation\": \"North\",\n" +
                "                \"installedRoofIndex\": 0,\n" +
                "                \"pitch\": 0,\n" +
                "                \"SHGC\": 0,\n" +
                "                \"uFactor\": 0,\n" +
                "                \"adjacentSummerShading\": 0,\n" +
                "                \"adjacentWinterShading\": 0\n" +
                "            }\n" +
                "        ],\n" +
                "        \"doors\": [\n" +
                "            {\n" +
                "                \"index\": 0,\n" +
                "                \"remove\": true,\n" +
                "                \"installedWallIndex\": 0,\n" +
                "                \"installedFoundationWallIndex\": 0,\n" +
                "                \"doorArea\": 0,\n" +
                "                \"uFactor\": 0\n" +
                "            }\n" +
                "        ],\n" +
                "        \"newDoors\": [\n" +
                "            {\n" +
                "                \"installedWallIndex\": 0,\n" +
                "                \"installedFoundationWallIndex\": 0,\n" +
                "                \"doorArea\": 0,\n" +
                "                \"uFactor\": 0\n" +
                "            }\n" +
                "        ],\n" +
                "        \"solarGenerations\": [\n" +
                "            {\n" +
                "                \"index\": 0,\n" +
                "                \"remove\": true,\n" +
                "                \"trackMode\": \"Fixed\",\n" +
                "                \"nameplateCapacity\": 0,\n" +
                "                \"derateFactor\": 0,\n" +
                "                \"azimuth\": 0,\n" +
                "                \"tilt\": 0\n" +
                "            }\n" +
                "        ],\n" +
                "        \"newSolarGenerations\": [\n" +
                "            {\n" +
                "                \"trackMode\": \"Fixed\",\n" +
                "                \"nameplateCapacity\": 0,\n" +
                "                \"derateFactor\": 0,\n" +
                "                \"azimuth\": 0,\n" +
                "                \"tilt\": 0\n" +
                "            }\n" +
                "        ],\n" +
                "        \"mechanicalEquipments\": [\n" +
                "            {\n" +
                "                \"index\": 0,\n" +
                "                \"modelNumber\": \"string\",\n" +
                "                \"secondModelNumber\": \"string\",\n" +
                "                \"thirdModelNumber\": \"string\",\n" +
                "                \"location\": \"Ambient\",\n" +
                "                \"percentHeatingLoad\": 0,\n" +
                "                \"percentCoolingLoad\": 0,\n" +
                "                \"percentHotWaterLoad\": 0,\n" +
                "                \"refrigerantCharge\": {\n" +
                "                    \"testConducted\": true,\n" +
                "                    \"testMethod\": \"NON_INVASIVE\",\n" +
                "                    \"meteringDevice\": \"PISTON_CAP_TUBE\",\n" +
                "                    \"differenceDtd\": 0,\n" +
                "                    \"differenceCtoa\": 0,\n" +
                "                    \"weightDeviation\": 0\n" +
                "                }\n" +
                "            }\n" +
                "        ],\n" +
                "        \"distributionSystems\": [\n" +
                "            {\n" +
                "                \"index\": 0,\n" +
                "                \"isLeakageToOutsideTested\": true,\n" +
                "                \"leakageToOutside\": 0,\n" +
                "                \"totalLeakage\": 0,\n" +
                "                \"increaseThresholdAboveTested\": true,\n" +
                "                \"leakageUnit\": \"CFM25\",\n" +
                "                \"totalDuctLeakageTestCondition\": \"NoTest\",\n" +
                "                \"numberOfReturns\": 0,\n" +
                "                \"sqFeetServed\": 0,\n" +
                "                \"useDefaultFlowRate\": true,\n" +
                "                \"heatingFlowRateCfm\": 0,\n" +
                "                \"coolingFlowRateCfm\": 0,\n" +
                "                \"ducts\": [\n" +
                "                    {\n" +
                "                        \"ductLocation\": \"ConditionedSpace\",\n" +
                "                        \"percentSupplyArea\": 0,\n" +
                "                        \"percentReturnArea\": 0\n" +
                "                    }\n" +
                "                ],\n" +
                "                \"totalDuctLeakageTestException\": true,\n" +
                "                \"withinAnsiAccaLimit\": true,\n" +
                "                \"blowerFanMeasuredAirflow\": 0,\n" +
                "                \"blowerFanWattDrawTested\": true,\n" +
                "                \"blowerFanMeasuredWattDraw\": 0\n" +
                "            }\n" +
                "        ],\n" +
                "        \"mechanicalVentilations\": [\n" +
                "            {\n" +
                "                \"index\": 0,\n" +
                "                \"ventilationType\": \"ExhaustOnly\",\n" +
                "                \"measuredFlowRate\": 0,\n" +
                "                \"fanWatts\": 0,\n" +
                "                \"operationalHoursPerDay\": 0,\n" +
                "                \"onceEveryThreeHours\": true,\n" +
                "                \"isInlineFan\": \"UNSPECIFIED\"\n" +
                "            }\n" +
                "        ],\n" +
                "        \"hvacGrading\": {\n" +
                "            \"hvacGradingConducted\": true,\n" +
                "            \"designReviewWithinTolerances\": true\n" +
                "        },\n" +
                "        \"lighting\": {\n" +
                "            \"percentInteriorFluorescent\": 0,\n" +
                "            \"percentInteriorLED\": 0,\n" +
                "            \"percentExteriorFluorescent\": 0,\n" +
                "            \"percentExteriorLED\": 0,\n" +
                "            \"percentGarageFluorescent\": 0,\n" +
                "            \"percentGarageLED\": 0\n" +
                "        },\n" +
                "        \"water\": {\n" +
                "            \"waterFixtureType\": \"STANDARD\",\n" +
                "            \"hotWaterPipeLength\": 10,\n" +
                "            \"atLeastR3PipeInsulation\": true,\n" +
                "            \"hasRecirculationSystem\": true,\n" +
                "            \"branchLength\": 0,\n" +
                "            \"pipeLoopLength\": 0,\n" +
                "            \"recirculationSystemControl\": \"NONE_OR_TIMER\",\n" +
                "            \"pumpPower\": 0,\n" +
                "            \"isShared\": true\n" +
                "        },\n" +
                "        \"mandatoryItems\": {\n" +
                "            \"iecc2006MandatoryChecklist\": true,\n" +
                "            \"iecc2009MandatoryChecklist\": true,\n" +
                "            \"iecc2012MandatoryChecklist\": true,\n" +
                "            \"iecc2015MandatoryChecklist\": true,\n" +
                "            \"iecc2018MandatoryChecklist\": true,\n" +
                "            \"iecc2021MandatoryChecklist\": true,\n" +
                "            \"ieccSlabEdgeInsulationException\": true,\n" +
                "            \"iecc2021WindowException\": \"WEATHER_STATION\",\n" +
                "            \"energyStarRaterDesignReviewChecklist\": true,\n" +
                "            \"energyStarHvacDesignReport\": true,\n" +
                "            \"energyStarHvacCommissioningChecklist\": true,\n" +
                "            \"energyStarWaterManagementSystemBuilderRequirements\": true,\n" +
                "            \"energyStarNextGenRaterChecklist\": true,\n" +
                "            \"indoorAirPlusRequirements\": true,\n" +
                "            \"epaWaterSenseCertification\": true\n" +
                "        },\n" +
                "        \"refrigeratorConsumption\": 0,\n" +
                "        \"dishwasherAvailable\": true,\n" +
                "        \"dishwasherDefaultsType\": \"EnergyStarCompact\",\n" +
                "        \"dishwasherSize\": \"Compact\",\n" +
                "        \"dishwasherEfficiencyType\": \"EF\",\n" +
                "        \"dishwasherEfficiency\": 0,\n" +
                "        \"dishwasherAnnualGasCost\": 0,\n" +
                "        \"dishwasherGasRate\": 0,\n" +
                "        \"dishwasherElectricRate\": 0,\n" +
                "        \"clothesDryer\": {\n" +
                "            \"available\": true,\n" +
                "            \"defaultsType\": \"Custom\",\n" +
                "            \"combinedEnergyFactor\": 0,\n" +
                "            \"utilizationFactor\": \"TIMER_CONTROLS\"\n" +
                "        },\n" +
                "        \"clothesWasher\": {\n" +
                "            \"available\": true,\n" +
                "            \"defaultsType\": \"Custom\",\n" +
                "            \"loadType\": \"FrontLoad\",\n" +
                "            \"labeledEnergyRating\": 0,\n" +
                "            \"integratedModifiedEnergyFactor\": 0\n" +
                "        },\n" +
                "        \"utilityRates\": [\n" +
                "            {\n" +
                "                \"fuel\": \"NATURAL_GAS\",\n" +
                "                \"name\": \"string\"\n" +
                "            }\n" +
                "        ],\n" +
                "        \"infiltrationMeasurementType\": \"Untested\",\n" +
                "        \"sampleSetId\": \"string\"\n" +
                "    }\n" +
                "}";

        String postInfoReplyExample = "Example Post Reply\n" +
                "{\n" +
                "    \"newMasterPlanId\": \"string\",\n" +
                "    \"status\": \"string\",\n" +
                "    \"message\": \"string\"\n" +
                "}";

        String currentDataExample = "Current Data Example\n" +
                "Address: " + mInspection.address + "\n" +
                "Ekotrope Project ID: " + mInspection.ekotrope_project_id + "\n" +
                "builder_name: " + mInspection.builder_name + "\n" +
                "city: " + mInspection.city + "\n" +
                "community: " + mInspection.community + "\n" +
                "incomplete_reason: " + mInspection.incomplete_reason + "\n" +
                "inspection_status: " + mInspection.inspection_status + "\n" +
                "inspection_type: " + mInspection.inspection_type + "\n" +
                "super_name: " + mInspection.super_name + "\n" +
                "super_email: " + mInspection.super_email + "\n" +
                "inspection_class: " + mInspection.inspection_class + "\n";

        emailIntent.putExtra(Intent.EXTRA_TEXT,
            //getInfoExample + "\n\n" +
            //getInfoReplyExample + "\n\n" +
            //postInfoExample + "\n\n" +
            //postInfoReplyExample + "\n\n" +
            currentDataExample
        );
        return emailIntent;
    }
}