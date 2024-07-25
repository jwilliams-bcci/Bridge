package com.burgess.bridge.inspect;

import static com.burgess.bridge.Constants.PREF;
import static com.burgess.bridge.Constants.PREF_IND_INSPECTIONS_REMAINING;
import static com.burgess.bridge.Constants.PREF_TEAM_INSPECTIONS_REMAINING;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.burgess.bridge.BridgeLogger;
import com.burgess.bridge.R;
import com.burgess.bridge.defectitem.DefectItemActivity;
import com.burgess.bridge.ekotropedata.EkotropeDataActivity;
import com.burgess.bridge.reviewandsubmit.ReviewAndSubmitActivity;
import com.burgess.bridge.routesheet.RouteSheetActivity;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import data.Tables.InspectionDefect_Table;
import data.Tables.Inspection_Table;

public class InspectActivity extends AppCompatActivity {
    public static final String INSPECTION_ID = "com.burgess.bridge.INSPECTION_ID";
    public static final int INSPECTION_ID_NOT_FOUND = -1;
    public static final String SCROLL_POSITION = "com.burgess.bridge.SCROLL_POSITION";
    public static final int SCROLL_POSITION_NOT_FOUND = -1;
    public static final String FILTER_OPTION = "com.burgess.bridge.FILTER_OPTION";
    public static final String TAG = "INSPECT";

    public int mInspectionId;
    public int mScrollPosition;
    public int mInspectionTypeId;
    public boolean mReinspection;
    public String mFilter;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private ConstraintLayout mConstraintLayout;
    private TextView mTextToolbarIndividualRemaining;
    private TextView mTextToolbarTeamRemaining;
    private Spinner mSpinnerDefectCategories;
    private InspectViewModel mInspectViewModel;
    private Inspection_Table mInspection;
    private Button mButtonSaveAndExit;
    private Button mButtonSortItemNumber;
    private Button mButtonSortDescription;
    private Button mButtonAddNote;
    private Button mButtonViewEkotropeData;
    private RecyclerView mRecyclerDefectItems;
    private InspectListAdapter mInspectListAdapter;
    private ReinspectListAdapter mReinspectListAdapter;
    private TextView mTextAddress;
    private TextView mTextTotalDefectCountLabel;
    private TextView mTextTotalDefectCount;
    private Button mButtonReviewAndSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_inspect);
        setSupportActionBar(findViewById(R.id.inspect_toolbar));
        mInspectViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(InspectViewModel.class);

        mSharedPreferences = getSharedPreferences(PREF, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

        Intent intent = getIntent();
        mInspectionId = intent.getIntExtra(INSPECTION_ID, INSPECTION_ID_NOT_FOUND);
        mScrollPosition = intent.getIntExtra(SCROLL_POSITION, SCROLL_POSITION_NOT_FOUND);
        mFilter = intent.getStringExtra(FILTER_OPTION) != null ? intent.getStringExtra(FILTER_OPTION) : "ALL";
        mInspection = mInspectViewModel.getInspectionSync(mInspectionId);
        mReinspection = mInspectViewModel.getReinspect(mInspectionId);
        mInspectionTypeId = mInspection.inspection_type_id;

        initializeViews();
        initializeButtonListeners();
        initializeDisplayContent();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mTextTotalDefectCount.setText(Integer.toString(mInspectViewModel.getInspectionDefectCount(mInspectionId)));
    }

    private void initializeViews() {
        mConstraintLayout = findViewById(R.id.inspect_constraint_layout);
        mTextToolbarIndividualRemaining = findViewById(R.id.toolbar_individual_inspections_remaining);
        mTextToolbarTeamRemaining = findViewById(R.id.toolbar_team_inspections_remaining);
        mTextAddress = findViewById(R.id.inspect_text_inspection_address);
        mTextTotalDefectCountLabel = findViewById(R.id.inspect_text_total_defect_count_label);
        mTextTotalDefectCount = findViewById(R.id.inspect_text_total_defect_count);
        mButtonAddNote = findViewById(R.id.inspect_button_add_note);
        mButtonSaveAndExit = findViewById(R.id.inspect_button_save_and_exit);
        mButtonReviewAndSubmit = findViewById(R.id.inspect_button_review_and_submit);
        mSpinnerDefectCategories = findViewById(R.id.inspect_spinner_defect_category);
        mButtonSortItemNumber = findViewById(R.id.inspect_button_sort_by_defect_number);
        mButtonSortDescription = findViewById(R.id.inspect_button_sort_by_description);
        mButtonViewEkotropeData = findViewById(R.id.inpect_button_view_ekotrope_data);
        mRecyclerDefectItems = findViewById(R.id.inspect_list_defect_items);
    }
    private void initializeButtonListeners() {
        mButtonAddNote.setOnClickListener(v -> {
            Intent defectItemIntent = new Intent(this, DefectItemActivity.class);
            defectItemIntent.putExtra(DefectItemActivity.INSPECTION_ID, mInspectionId);
            defectItemIntent.putExtra(DefectItemActivity.DEFECT_ID, 1);
            startActivity(defectItemIntent);
        });
        mButtonSaveAndExit.setOnClickListener(v -> {
            finish();
            Intent routeSheetIntent = new Intent(InspectActivity.this, RouteSheetActivity.class);
            routeSheetIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(routeSheetIntent);
        });
        mButtonReviewAndSubmit.setOnClickListener(v -> {
            boolean allGood;
            int numberToReview = mInspectViewModel.getItemsToReview(mInspectionId);
            if (mReinspection) {
                allGood = numberToReview < 1;
            } else {
                allGood = true;
            }

            if (allGood) {
                Intent reviewAndSubmitIntent = new Intent(InspectActivity.this, ReviewAndSubmitActivity.class);
                reviewAndSubmitIntent.putExtra(ReviewAndSubmitActivity.INSPECTION_ID, mInspectionId);
                startActivity(reviewAndSubmitIntent);
            } else {
                Snackbar.make(mConstraintLayout, "Please review all items", Snackbar.LENGTH_LONG).show();
            }
        });
        mButtonSortItemNumber.setOnClickListener(v -> {
            mInspectViewModel.getAllDefectItemsFilteredNumberSort(mSpinnerDefectCategories.getSelectedItem().toString(), mInspectionTypeId, mInspectionId).observe(this, defectItems ->
                    mInspectListAdapter.setCurrentList(defectItems));
        });
        mButtonSortDescription.setOnClickListener(v -> {
            mInspectViewModel.getAllDefectItemsFilteredDescriptionSort(mSpinnerDefectCategories.getSelectedItem().toString(), mInspectionTypeId, mInspectionId).observe(this, defectItems ->
                    mInspectListAdapter.setCurrentList(defectItems));
        });
        mButtonViewEkotropeData.setOnClickListener(v -> {
            Intent viewEkotropeDataIntent = new Intent(InspectActivity.this, EkotropeDataActivity.class);
            startActivity(viewEkotropeDataIntent);
        });
    }
    private void initializeDisplayContent() {
        mTextToolbarIndividualRemaining.setText(String.valueOf(mSharedPreferences.getInt(PREF_IND_INSPECTIONS_REMAINING, -1)));
        mTextToolbarTeamRemaining.setText(String.valueOf(mSharedPreferences.getInt(PREF_TEAM_INSPECTIONS_REMAINING, -1)));

        // Set address label
        mTextAddress.setText("");
        mTextAddress.append(mInspection.community + "\n");
        mTextAddress.append(mInspection.address + "\n");
        mTextAddress.append(mInspection.inspection_type);

        mTextTotalDefectCount.setText(Integer.toString(mInspectViewModel.getInspectionDefectCount(mInspectionId)));

        if ((mReinspection && mInspection.division_id != 20) || (mInspectionTypeId == 1154)) {
            initializeReinspectDisplayContent();
        } else {
            // Set up defect list
            mInspectListAdapter = new InspectListAdapter(new InspectListAdapter.InspectDiff());
            mInspectListAdapter.setInspection(mInspection);
            mInspectListAdapter.setFilter(mFilter);
            mRecyclerDefectItems.setAdapter(mInspectListAdapter);
            displayDefectItems(mFilter);
        }
        mRecyclerDefectItems.setLayoutManager(new LinearLayoutManager(this));
        if(mScrollPosition > 0) {
            new Handler().postDelayed(() -> mRecyclerDefectItems.scrollToPosition(mScrollPosition), 1000);
        }
        if(mInspection.inspection_class != 7) {
            mButtonViewEkotropeData.setVisibility(View.GONE);
            ConstraintLayout layout = findViewById(R.id.inspect_list_defect_items);
            ConstraintSet newLayout = new ConstraintSet();
            newLayout.clone(layout);
            newLayout.connect(R.id.inspect_list_defect_items, ConstraintSet.TOP, R.id.inspect_button_sort_by_defect_number, ConstraintSet.BOTTOM);
            newLayout.applyTo(layout);
        }
        fillCategorySpinner(mFilter);
    }

    private void fillCategorySpinner(String initialFilter) {
        mInspectViewModel.getDefectCategories(mInspectionTypeId).observe(this, defectCategories -> {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, defectCategories);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpinnerDefectCategories.setAdapter(adapter);
            mSpinnerDefectCategories.setSelection(adapter.getPosition(initialFilter));
        });

        mSpinnerDefectCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if ((mReinspection && mInspection.division_id != 20) || (mInspection.inspection_type_id == 1154)) {
                    displayReinspectItems(0);
                } else {
                    mFilter = parent.getSelectedItem().toString();
                    mInspectListAdapter.setFilter(mFilter);
                    displayDefectItems(parent.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void displayDefectItems(String filter) {
        mInspectViewModel.getAllDefectItemsFilteredDescriptionSort(filter, mInspectionTypeId, mInspectionId).observe(this, defectItems ->
                mInspectListAdapter.setCurrentList(defectItems));
    }
    private void displayReinspectItems(int scrollPosition) {
        mInspectViewModel.getInspectionHistory(mInspectionId).observe(this, defectItems -> {
            Parcelable recyclerViewState = mRecyclerDefectItems.getLayoutManager().onSaveInstanceState();
            mReinspectListAdapter.setCurrentList(defectItems);
            mRecyclerDefectItems.getLayoutManager().onRestoreInstanceState(recyclerViewState);
        });
    }

    private void initializeReinspectDisplayContent() {
        // Hide sort buttons and defect count
        mButtonSortDescription.setVisibility(View.GONE);
        mButtonSortItemNumber.setVisibility(View.GONE);
        mTextTotalDefectCountLabel.setVisibility(View.GONE);
        mTextTotalDefectCount.setVisibility(View.GONE);

        // Change constraints
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(mConstraintLayout);
        constraintSet.connect(R.id.inspect_list_defect_items, ConstraintSet.TOP, R.id.inspect_spinner_defect_category, ConstraintSet.BOTTOM);
        constraintSet.applyTo(mConstraintLayout);

        // Set up the defect list
        mReinspectListAdapter = new ReinspectListAdapter(new ReinspectListAdapter.ReinspectDiff());
        mReinspectListAdapter.mInspectionId = mInspectionId;
        mReinspectListAdapter.mInspectionTypeId = mInspectionTypeId;
        mReinspectListAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                super.onItemRangeMoved(fromPosition, toPosition, itemCount);
                if (fromPosition == 0 || toPosition == 0) {
                    mRecyclerDefectItems.scrollToPosition(0);
                }
            }
        });
        mRecyclerDefectItems.setAdapter(mReinspectListAdapter);

        // Add swipe functionality to defect item list
        ItemTouchHelper.SimpleCallback touchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, @NonNull @NotNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {
                InspectViewHolder holder = (InspectViewHolder) viewHolder;
                final int selectedHistoryId = holder.getInspectionHistoryId();
                final int selectedFirstDetailId = holder.getFirstDetailId();
                final int selectedDefectItemId = holder.getDefectItemId();
                final int selectedInspectionDefectId = holder.getInspectionDefectId();
                final String selectedComment = holder.getComment();
                final long newId;

                switch (direction) {
                    case ItemTouchHelper.LEFT:
                        BridgeLogger.log('I', TAG, "Marked Defect ID: " + selectedDefectItemId + " as NC using swipe.");
                        if (selectedInspectionDefectId > 0) {
                            InspectionDefect_Table inspectionDefectToUpdate = mInspectViewModel.getInspectionDefect(selectedInspectionDefectId);
                            if (inspectionDefectToUpdate.defect_status_id == 2) {
                                Snackbar.make(mConstraintLayout, "Defect is already marked NC!", Snackbar.LENGTH_SHORT).show();
                                mReinspectListAdapter.notifyItemChanged(viewHolder.getAbsoluteAdapterPosition());
                            } else {
                                inspectionDefectToUpdate.defect_status_id = 2;
                                mInspectViewModel.updateInspectionDefect(inspectionDefectToUpdate);
                                mInspectViewModel.updateReviewedStatus(2, selectedHistoryId);
                                mInspectViewModel.updateIsReviewed(selectedHistoryId);
                                mInspectViewModel.updateInspectionDefectId(selectedInspectionDefectId, selectedHistoryId);
                            }
                        } else {
                            InspectionDefect_Table newFailedDefect = new InspectionDefect_Table(mInspectionId, selectedDefectItemId, 2, selectedComment, selectedHistoryId, selectedFirstDetailId, null, null, false, null, null);
                            newId = mInspectViewModel.insertInspectionDefect(newFailedDefect);
                            mInspectViewModel.updateReviewedStatus(2, selectedHistoryId);
                            mInspectViewModel.updateIsReviewed(selectedHistoryId);
                            mInspectViewModel.updateInspectionDefectId((int) newId, selectedHistoryId);
                        }
                        break;
                    case ItemTouchHelper.RIGHT:
                        BridgeLogger.log('I', TAG, "Marked Defect ID: " + selectedDefectItemId + " as C using swipe.");
                        if (selectedInspectionDefectId > 0) {
                            InspectionDefect_Table inspectionDefectToUpdate = mInspectViewModel.getInspectionDefect(selectedInspectionDefectId);
                            if (inspectionDefectToUpdate.defect_status_id == 3) {
                                Snackbar.make(mConstraintLayout, "Defect is already marked C!", Snackbar.LENGTH_SHORT).show();
                                mReinspectListAdapter.notifyItemChanged(viewHolder.getAbsoluteAdapterPosition());
                            } else {
                                inspectionDefectToUpdate.defect_status_id = 3;
                                mInspectViewModel.updateInspectionDefect(inspectionDefectToUpdate);
                                mInspectViewModel.updateReviewedStatus(3, selectedHistoryId);
                                mInspectViewModel.updateIsReviewed(selectedHistoryId);
                                mInspectViewModel.updateInspectionDefectId(selectedInspectionDefectId, selectedHistoryId);
                            }
                        } else {
                            InspectionDefect_Table newCompleteDefect = new InspectionDefect_Table(mInspectionId, selectedDefectItemId, 3, selectedComment, selectedHistoryId, selectedFirstDetailId, null, null, false, null, null);
                            newId = mInspectViewModel.insertInspectionDefect(newCompleteDefect);
                            mInspectViewModel.updateReviewedStatus(3, selectedHistoryId);
                            mInspectViewModel.updateIsReviewed(selectedHistoryId);
                            mInspectViewModel.updateInspectionDefectId((int) newId, selectedHistoryId);
                        }
                        break;
                }
            }

            @Override
            public int getSwipeDirs(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder) {
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(touchHelperCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerDefectItems);
    }
}