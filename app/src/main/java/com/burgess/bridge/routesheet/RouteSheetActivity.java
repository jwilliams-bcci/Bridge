package com.burgess.bridge.routesheet;

import static android.app.ProgressDialog.show;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.widget.DatePicker;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.burgess.bridge.BridgeLogger;
import com.burgess.bridge.R;
import com.burgess.bridge.SharedPreferencesRepository;
import com.burgess.bridge.inspectiondetails.InspectionDetailsActivity;
import com.burgess.bridge.login.LoginActivity;
import com.burgess.bridge.reviewandsubmit.ReviewAndSubmitActivity;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import static com.burgess.bridge.Constants.PREF_AUTH_TOKEN;
import static com.burgess.bridge.Constants.PREF_AUTH_TOKEN_AGE;
import static com.burgess.bridge.Constants.PREF_INSPECTOR_DIVISION_ID;
import static com.burgess.bridge.Constants.PREF_INSPECTOR_ID;
import static com.burgess.bridge.Constants.PREF_LOGIN_NAME;
import static com.burgess.bridge.Constants.PREF_LOGIN_PASSWORD;
import static com.burgess.bridge.Constants.PREF_SECURITY_USER_ID;

import data.Views.RouteSheet_View;

public class RouteSheetActivity extends AppCompatActivity
        implements RouteSheetViewHolder.OnItemClickListener,
                    RouteSheetViewHolder.OnItemButtonClickListener,
                    DatePickerDialog.OnDateSetListener {
    private static final String TAG = "ROUTE_SHEET";

    private RouteSheetViewModel routeSheetVM;
    private ConstraintLayout constraintLayout;
    private TextView tRefreshStatus;
    private TextView tSearchCommunity;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rInspections;
    private Toolbar toolbar;
    private TextView tToolbarIndividualRemaining;
    private TextView tToolbarTeamRemaining;

    private SharedPreferencesRepository sharedPreferences;
    private String inspectorId;
    private RouteSheetListAdapter listAdapter;

    private int selectedInspectionId = 0;
    private String selectedInspectionAddress = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_sheet);
        setSupportActionBar(findViewById(R.id.route_sheet_toolbar));
        routeSheetVM = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(RouteSheetViewModel.class);

        // Prepare shared preferences...
        sharedPreferences = new SharedPreferencesRepository(this);

        // Prepare Logger
        BridgeLogger.getInstance(this);

        inspectorId = sharedPreferences.getInspectorId();

        initializeViews();
        initializeButtonListeners();
        initializeTextListeners();
        initializeSwipeRefresh();
        initializeDisplayContent();

        routeSheetVM.getSnackbarMessage().observe(this, message -> {
            if (message != null) {
                Snackbar.make(constraintLayout, message, Snackbar.LENGTH_LONG).show();
            }
        });
        routeSheetVM.getStatusMessage().observe(this, message -> {
            if (message != null) {
                tRefreshStatus.setText(message);
            }
        });
        routeSheetVM.getIndividualRemainingMessage().observe(this, message -> {
            if (message != null) {
                tToolbarIndividualRemaining.setText(message);
            }
        });
        routeSheetVM.getTeamRemainingMessage().observe(this, message -> {
            if (message != null) {
                tToolbarTeamRemaining.setText(message);
            }
        });
        routeSheetVM.getShowResetConfirmation().observe(this, show -> {
            if (show != null) {
                if (show) {
                    showResetConfirmationDialog();
                }
            }
        });
        routeSheetVM.getReportLinkUrl().observe(this, url -> {
            if (url != null) {
                Intent showRouteSheetIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://portal.burgess-inc.com" + url));
                startActivity(showRouteSheetIntent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.route_sheet_menu, menu);
        return true;
    }

    private void initializeViews() {
        constraintLayout = findViewById(R.id.route_sheet_constraint_layout);
        tRefreshStatus = findViewById(R.id.route_sheet_text_refresh_status);
        tSearchCommunity = findViewById(R.id.route_sheet_text_search_community);
        swipeRefreshLayout = findViewById(R.id.route_sheet_swipe_refresh);
        rInspections = findViewById(R.id.route_sheet_list_inspections);
        toolbar = findViewById(R.id.route_sheet_toolbar);
        tToolbarIndividualRemaining = findViewById(R.id.toolbar_individual_inspections_remaining);
        tToolbarTeamRemaining = findViewById(R.id.toolbar_team_inspections_remaining);
    }
    private void initializeButtonListeners() {
        toolbar.setOnMenuItemClickListener(v -> {
            if (v.getItemId() == R.id.route_sheet_menu_send_activity_log) {
                Snackbar.make(constraintLayout, "Sending activity log...", Snackbar.LENGTH_SHORT).show();
                try {
                    Intent emailIntent = BridgeLogger.sendLogFile(inspectorId, getVersionName());
                    startActivity(Intent.createChooser(emailIntent, "Send activity log..."));
                } catch (Exception e) {
                    BridgeLogger.log('E', TAG, "ERROR in initializeButtonListeners: " + e.getMessage());
                }
            }
            else if (v.getItemId() == R.id.route_sheet_menu_print_route_sheet) {
                showGetRouteSheetReportDialog();
            }
            else if (v.getItemId() == R.id.route_sheet_menu_logout) {
                sharedPreferences.removePref(PREF_AUTH_TOKEN);
                sharedPreferences.removePref(PREF_SECURITY_USER_ID);
                sharedPreferences.removePref(PREF_INSPECTOR_ID);
                sharedPreferences.removePref(PREF_INSPECTOR_DIVISION_ID);
                sharedPreferences.removePref(PREF_LOGIN_NAME);
                sharedPreferences.removePref(PREF_LOGIN_PASSWORD);
                sharedPreferences.removePref(PREF_AUTH_TOKEN_AGE);

                Intent loginIntent = new Intent(RouteSheetActivity.this, LoginActivity.class);
                loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(loginIntent);
            }
            return false;
        });
    }
    private void initializeTextListeners() {
        tSearchCommunity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                listAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
    private void initializeSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener(this::updateRouteSheet);
    }
    private void initializeDisplayContent() {
        try {
            listAdapter = new RouteSheetListAdapter(new RouteSheetListAdapter.InspectionDiff(), this, this);
            rInspections.setAdapter(listAdapter);
            rInspections.setLayoutManager(new LinearLayoutManager(this));
            Objects.requireNonNull(rInspections.getItemAnimator()).setChangeDuration(0);
            RouteSheetTouchHelper callback = new RouteSheetTouchHelper(listAdapter);
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
            itemTouchHelper.attachToRecyclerView(rInspections);
            routeSheetVM.getAllInspectionsForRouteSheet(Integer.parseInt(inspectorId)).observe(this, inspections -> {
                listAdapter.submitOriginalList(inspections);
                routeSheetVM.setCurrentList(inspections);
                tToolbarIndividualRemaining.setText(Integer.toString(inspections.size()));
            });
        } catch (Exception e) {
            Snackbar.make(constraintLayout, "Error in loading route sheet, please send log.", Snackbar.LENGTH_LONG).show();
            BridgeLogger.log('E', TAG, "ERROR in initializeDisplayContent: " + e.getMessage());
        }
        updateRouteSheet();
    }

    private String getVersionName() {
        PackageInfo pInfo = null;
        try {
            pInfo = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            BridgeLogger.log('E', TAG, "ERROR in getVersionName: " + e.getMessage());
        }
        if (pInfo != null) {
            return pInfo.versionName != null ? pInfo.versionName : "VERSION NOT FOUND";
        } else {
            return "VERSION NOT FOUND";
        }
    }

    private void updateRouteSheet() {
        routeSheetVM.updateInspections(sharedPreferences.getAuthToken(), inspectorId);
        routeSheetVM.updateRouteSheetIndexes(listAdapter.getCurrentList());
        routeSheetVM.clearCompletedAndFutureDatedInspections(sharedPreferences.getAuthToken(), Integer.parseInt(inspectorId));
        routeSheetVM.updateInspectionsRemaining(sharedPreferences.getAuthToken(), Integer.parseInt(inspectorId));
        swipeRefreshLayout.setRefreshing(false);
    }

    public void showResetConfirmationDialog() {
        AlertDialog confirmationDialog = new AlertDialog.Builder(this)
                .setTitle("Reset Inspection")
                .setMessage(String.format("This will clear ALL items from the inspection at %s, are you sure?", selectedInspectionAddress))
                .setPositiveButton("Yes", (dialog, which) -> {
                    routeSheetVM.resetInspection(selectedInspectionId, true);
                })
                .setNegativeButton("No", (dialog, which) -> {
                    Snackbar.make(constraintLayout, "Cancelled reset.", Snackbar.LENGTH_SHORT).show();
                })
                .create();
        confirmationDialog.show();
    }
    public void showGetRouteSheetReportDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this, year, month, day);
        datePickerDialog.setTitle("Select date");
        datePickerDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        routeSheetVM.updateRouteSheetIndexes(listAdapter.getCurrentList());
    }
    @Override
    protected void onPause() {
        super.onPause();
        routeSheetVM.updateRouteSheetIndexes(listAdapter.getCurrentList());
    }

    @Override
    public void onButtonClick(RouteSheet_View inspection, int buttonId) {
        if (buttonId == R.id.item_inspection_list_button_reset_inspection) {
            selectedInspectionId = inspection.InspectionID;
            selectedInspectionAddress = inspection.Address;
            routeSheetVM.resetInspection(inspection.InspectionID, null);
        }
        else if (buttonId == R.id.item_inspection_list_button_reupload) {
            Intent intent = new Intent(this, ReviewAndSubmitActivity.class);
            intent.putExtra(ReviewAndSubmitActivity.INSPECTION_ID, inspection.InspectionID);
            startActivity(intent);
        }
    }
    @Override
    public void onItemClick(RouteSheet_View inspection) {
        if (inspection.IsComplete) {
            Snackbar.make(constraintLayout, "Inspection already completed", Snackbar.LENGTH_LONG).show();
        } else if (inspection.DivisionID == 20 && inspection.InspectionTypeID != 1154) {
            boolean reobPresent = false;
            for (int lcv = 0; lcv < listAdapter.getCurrentList().size(); lcv++) {
                RouteSheet_View i = listAdapter.getCurrentList().get(lcv);
                if (!i.IsUploaded && i.InspectionTypeID == 1154 && i.LocationID == inspection.LocationID) {
                    reobPresent = true;
                }
            }
            if (reobPresent) {
                Snackbar.make(constraintLayout, "There is a re-ob at this location, please complete that first.", Snackbar.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(this, InspectionDetailsActivity.class);
                intent.putExtra(InspectionDetailsActivity.INSPECTION_ID, inspection.InspectionID);
                startActivity(intent);
            }
        } else {
            Intent intent = new Intent(this, InspectionDetailsActivity.class);
            intent.putExtra(InspectionDetailsActivity.INSPECTION_ID, inspection.InspectionID);
            startActivity(intent);
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());
        String selectedDate = dateFormat.format(calendar.getTime());

        routeSheetVM.getRouteSheetReportlink(sharedPreferences.getAuthToken(), Integer.parseInt(inspectorId), selectedDate);
    }
}