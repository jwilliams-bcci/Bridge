package com.burgess.bridge.inspect;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.burgess.bridge.BridgeLogger;
import com.burgess.bridge.R;
import com.burgess.bridge.defectitem.DefectItemActivity;
import com.burgess.bridge.reviewandsubmit.ReviewAndSubmitActivity;
import com.burgess.bridge.routesheet.RouteSheetActivity;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import data.Tables.InspectionDefect_Table;
import data.Tables.Inspection_Table;

public class InspectActivity extends AppCompatActivity {
    public static final String INSPECTION_ID = "com.burgess.bridge.INSPECTION_ID";
    public static final int INSPECTION_ID_NOT_FOUND = -1;
    public static final String FILTER_OPTION = "com.burgess.bridge.FILTER_OPTION";
    public static final String TAG = "INSPECT";

    public int mInspectionId;
    public int mInspectionTypeId;
    public boolean mReinspection;
    public String mFilter;
    private ConstraintLayout mConstraintLayout;
    private Spinner mSpinnerDefectCategories;
    private InspectViewModel mInspectViewModel;
    private Inspection_Table mInspection;
    private Button mButtonSaveAndExit;
    private Button mButtonSortItemNumber;
    private Button mButtonSortDescription;
    private Button mButtonAddNote;
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
        mInspectViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(InspectViewModel.class);

        Intent intent = getIntent();
        mInspectionId = intent.getIntExtra(INSPECTION_ID, INSPECTION_ID_NOT_FOUND);
        mFilter = intent.getStringExtra(FILTER_OPTION) != null ? intent.getStringExtra(FILTER_OPTION) : "ALL";
        mInspection = mInspectViewModel.getInspectionSync(mInspectionId);
        mReinspection = mInspectViewModel.getReinspect(mInspectionId);
        mInspectionTypeId = mInspection.inspection_type_id;

        initializeViews();
        initializeButtonListeners();
        initializeDisplayContent();
    }

    private void initializeViews() {
        mConstraintLayout = findViewById(R.id.inspect_constraint_layout);
        mTextAddress = findViewById(R.id.inspect_text_inspection_address);
        mTextTotalDefectCountLabel = findViewById(R.id.inspect_text_total_defect_count_label);
        mTextTotalDefectCount = findViewById(R.id.inspect_text_total_defect_count);
        mButtonAddNote = findViewById(R.id.inspect_button_add_note);
        mButtonSaveAndExit = findViewById(R.id.inspect_button_save_and_exit);
        mButtonReviewAndSubmit = findViewById(R.id.inspect_button_review_and_submit);
        mSpinnerDefectCategories = findViewById(R.id.inspect_spinner_defect_category);
        mButtonSortItemNumber = findViewById(R.id.inspect_button_sort_by_defect_number);
        mButtonSortDescription = findViewById(R.id.inspect_button_sort_by_description);
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
                    mInspectListAdapter.submitList(defectItems));
        });
        mButtonSortDescription.setOnClickListener(v -> {
            mInspectViewModel.getAllDefectItemsFilteredDescriptionSort(mSpinnerDefectCategories.getSelectedItem().toString(), mInspectionTypeId, mInspectionId).observe(this, defectItems ->
                    mInspectListAdapter.submitList(defectItems));
        });
    }
    private void initializeDisplayContent() {
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
            mInspectListAdapter.setInspectionId(mInspectionId);
            mInspectListAdapter.setInspectionTypeId(mInspectionTypeId);
            mInspectListAdapter.setFilter(mFilter);
            mRecyclerDefectItems.setAdapter(mInspectListAdapter);
            displayDefectItems(mFilter);
        }
        mRecyclerDefectItems.setLayoutManager(new LinearLayoutManager(this));

        fillCategorySpinner(mFilter);
    }

    private void fillCategorySpinner(String initialFilter) {
        mInspectViewModel.getDefectCategories(mInspectionTypeId).observe(this, defectCategories -> {
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, defectCategories);
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
                mInspectListAdapter.submitList(defectItems));
    }
    private void displayReinspectItems(int scrollPosition) {
        mInspectViewModel.getInspectionHistory(mInspectionId).observe(this, defectItems ->
                mReinspectListAdapter.submitList(defectItems, () -> {
                    mRecyclerDefectItems.scrollToPosition(scrollPosition);
                }));
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
        mRecyclerDefectItems.setAdapter(mReinspectListAdapter);
        displayReinspectItems(0);

        // Add swipe functionality to defect item list
        ItemTouchHelper.SimpleCallback touchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, @NonNull @NotNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {
                InspectViewHolder holder = (InspectViewHolder) viewHolder;
                final int selectedHistoryId = holder.mInspectionHistoryId;
                final int selectedDefectItemId = holder.mDefectItemId;
                final int selectedInspectionDefectId = holder.mInspectionDefectId;
                final String selectedComment = holder.mComment;
                final long newId;

                switch (direction) {
                    case ItemTouchHelper.LEFT:
                        BridgeLogger.log('I', TAG, "Marked Defect ID: " + selectedDefectItemId + " as NC using swipe.");
                        if (selectedInspectionDefectId > 0) {
                            InspectionDefect_Table inspectionDefectToUpdate = mInspectViewModel.getInspectionDefect(selectedInspectionDefectId);
                            if (inspectionDefectToUpdate.defect_status_id == 2) {
                                Snackbar.make(mConstraintLayout, "Defect is already marked NC!", Snackbar.LENGTH_SHORT).show();
                            } else {
                                inspectionDefectToUpdate.defect_status_id = 2;
                                mInspectViewModel.updateInspectionDefect(inspectionDefectToUpdate);
                                mInspectViewModel.updateReviewedStatus(2, selectedHistoryId);
                                mInspectViewModel.updateIsReviewed(selectedHistoryId);
                                mInspectViewModel.updateInspectionDefectId(selectedInspectionDefectId, selectedHistoryId);
                            }
                        } else {
                            InspectionDefect_Table newFailedDefect = new InspectionDefect_Table(mInspectionId, selectedDefectItemId, 2, selectedComment, selectedHistoryId, false, null);
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
                                mRecyclerDefectItems.notifyAll();
                            } else {
                                inspectionDefectToUpdate.defect_status_id = 3;
                                mInspectViewModel.updateInspectionDefect(inspectionDefectToUpdate);
                                mInspectViewModel.updateReviewedStatus(3, selectedHistoryId);
                                mInspectViewModel.updateIsReviewed(selectedHistoryId);
                                mInspectViewModel.updateInspectionDefectId(selectedInspectionDefectId, selectedHistoryId);
                            }
                        } else {
                            InspectionDefect_Table newCompleteDefect = new InspectionDefect_Table(mInspectionId, selectedDefectItemId, 3, selectedComment, selectedHistoryId, false, null);
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