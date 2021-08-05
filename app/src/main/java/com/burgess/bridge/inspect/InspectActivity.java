package com.burgess.bridge.inspect;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.burgess.bridge.R;
import com.burgess.bridge.defectitem.DefectItemActivity;
import com.burgess.bridge.reviewandsubmit.ReviewAndSubmitActivity;
import com.burgess.bridge.routesheet.RouteSheetActivity;

import org.jetbrains.annotations.NotNull;

import data.Tables.Inspection_Table;

import static com.burgess.bridge.Constants.PREF;

public class InspectActivity extends AppCompatActivity {
    public static final String INSPECTION_ID = "com.burgess.bridge.INSPECTION_ID";
    public static final String INSPECTION_TYPE_ID = "com.burgess.bridge.INSPECTION_TYPE_ID";
    public static final String LOCATION_ID = "com.burgess.bridge.LOCATION_ID";
    public static final String INSPECTION_HISTORY_ID = "com.burgess.bridge.INSPECTION_HISTORY_ID";
    public static final int INSPECTION_ID_NOT_FOUND = -1;
    public static final int INSPECTION_TYPE_ID_NOT_FOUND = -1;
    public static final int LOCATION_ID_NOT_FOUND = -1;
    public static final int INSPECTION_HISTORY_ID_NOT_FOUND = -1;
    public static final String TAG = "INSPECT";

    public int mInspectionId;
    private int mLocationId;
    public int mInspectionTypeId;
    public boolean mReinspection;
    private Spinner mSpinnerDefectCategories;
    private InspectViewModel mInspectViewModel;
    private SharedPreferences mSharedPreferences;
    private LiveData<Inspection_Table> mInspection;
    private Button mButtonSaveAndExit;
    private Button mButtonSortItemNumber;
    private Button mButtonSortDescription;
    private Button mButtonAddNote;
    private RecyclerView mRecyclerDefectItems;
    private InspectListAdapter mInspectListAdapter;
    private ReinspectListAdapter mReinspectListAdapter;
    private AddNoteFragment mAddNoteFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspect);
        setSupportActionBar(findViewById(R.id.inspect_toolbar));
        mSharedPreferences = getSharedPreferences(PREF, Context.MODE_PRIVATE);

        Intent intent = getIntent();
        TextView textAddress = findViewById(R.id.inspect_text_inspection_address);

        mInspectionId = intent.getIntExtra(INSPECTION_ID, INSPECTION_ID_NOT_FOUND);
        mInspectionTypeId = intent.getIntExtra(INSPECTION_TYPE_ID, INSPECTION_ID_NOT_FOUND);
        mLocationId = intent.getIntExtra(LOCATION_ID, LOCATION_ID_NOT_FOUND);
        mSpinnerDefectCategories = findViewById(R.id.inspect_spinner_defect_category);
        mInspectViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(InspectViewModel.class);
        mInspection = mInspectViewModel.getInspection(mInspectionId);
        mReinspection = mInspectViewModel.getReinspect(mInspectionId);
        mButtonSaveAndExit = findViewById(R.id.inspect_button_save_and_exit);
        mButtonSortItemNumber = findViewById(R.id.inspect_button_sort_by_defect_number);
        mButtonSortDescription = findViewById(R.id.inspect_button_sort_by_description);
        mButtonAddNote = findViewById(R.id.inspect_button_add_note);
        mRecyclerDefectItems = findViewById(R.id.inspect_list_defect_items);

        displayAddress(textAddress);
        fillSpinner(mSpinnerDefectCategories);

        if (mReinspection) {
            Log.i(TAG, "Going into reinspect list adapter...");
            mRecyclerDefectItems.setItemViewCacheSize(300);
            mReinspectListAdapter = new ReinspectListAdapter(new ReinspectListAdapter.InspectDiff());
            mReinspectListAdapter.setInspectionId(mInspectionId);
            mReinspectListAdapter.setInspectionTypeId(mInspectionTypeId);
            mRecyclerDefectItems.setAdapter(mReinspectListAdapter);
            displayReinspectItems("ALL");
        } else {
            Log.i(TAG, "Going into new inspection list adapter...");
            mInspectListAdapter = new InspectListAdapter(new InspectListAdapter.InspectDiff());
            mInspectListAdapter.setInspectionId(mInspectionId);
            mInspectListAdapter.setInspectionTypeId(mInspectionTypeId);
            mRecyclerDefectItems.setAdapter(mInspectListAdapter);
            displayDefectItems("ALL");
        }
        mRecyclerDefectItems.setLayoutManager(new LinearLayoutManager(this));

        Button buttonReviewAndSubmit = findViewById(R.id.inspect_button_review_and_submit);
        buttonReviewAndSubmit.setOnClickListener(v -> {
            boolean allGood;
            int numberToReview = mInspectViewModel.getItemsToReview(mInspectionId);
            if (mReinspection) {
                allGood = numberToReview < 1 ? true : false;
            } else {
                allGood = true;
            }

            if (allGood) {
                Intent reviewAndSubmitIntent = new Intent(InspectActivity.this, ReviewAndSubmitActivity.class);
                reviewAndSubmitIntent.putExtra(ReviewAndSubmitActivity.INSPECTION_ID, mInspectionId);
                startActivity(reviewAndSubmitIntent);
            } else {
                Toast.makeText(this, "Please review all items.", Toast.LENGTH_LONG).show();
            }
        });

        mButtonSaveAndExit.setOnClickListener(v -> {
            finish();
            Intent routeSheetIntent = new Intent(InspectActivity.this, RouteSheetActivity.class);
            routeSheetIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(routeSheetIntent);
        });

        if (mReinspection) {
            mButtonSortDescription.setVisibility(View.INVISIBLE);
            mButtonSortItemNumber.setVisibility(View.INVISIBLE);
        }
        mButtonSortItemNumber.setOnClickListener(v -> {
            mInspectViewModel.getAllDefectItemsFilteredNumberSort(mSpinnerDefectCategories.getSelectedItem().toString(), mInspectionTypeId, mInspectionId).observe(this, defectItems ->
                    mInspectListAdapter.submitList(defectItems));
        });

        mButtonSortDescription.setOnClickListener(v -> {
            mInspectViewModel.getAllDefectItemsFilteredDescriptionSort(mSpinnerDefectCategories.getSelectedItem().toString(), mInspectionTypeId, mInspectionId).observe(this, defectItems ->
                    mInspectListAdapter.submitList(defectItems));
        });

        mButtonAddNote.setOnClickListener(v -> {
            Intent defectItemIntent = new Intent(this, DefectItemActivity.class);
            defectItemIntent.putExtra(DefectItemActivity.INSPECTION_ID, mInspectionId);
            defectItemIntent.putExtra(DefectItemActivity.INSPECTION_TYPE_ID, mInspectionTypeId);
            defectItemIntent.putExtra(DefectItemActivity.DEFECT_ID, 1);
            startActivity(defectItemIntent);
        });

//        if (mReinspection) {
//            ItemTouchHelper.SimpleCallback touchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
//                @Override
//                public boolean onMove(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, @NonNull @NotNull RecyclerView.ViewHolder target) {
//                    return false;
//                }
//
//                @Override
//                public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {
//                }
//
//                @Override
//                public int getSwipeDirs(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder) {
//                    return super.getSwipeDirs(recyclerView, viewHolder);
//                }
//
//                @Override
//                public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
//                    try {
//                        Bitmap icon;
//                        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
//                            View itemView = viewHolder.itemView;
//                            float height = (float) itemView.getBottom() - (float) itemView.getTop();
//                            float width = height / 5;
//                            viewHolder.itemView.setTranslationX(dX / 5);
//
//                            Paint paint = new Paint();
//                            int dir = getSwipeDirs(recyclerView, viewHolder);
//                            if (dX > 0) {
//                                paint.setColor(Color.GREEN);
//                            } else {
//                                paint.setColor(Color.RED);
//                            }
//                            RectF background = new RectF((float) itemView.getRight() + dX / 5, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
//                            c.drawRect(background, paint);
//                        } else {
//                            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            };
//            ItemTouchHelper touchHelper = new ItemTouchHelper(touchHelperCallback);
//            touchHelper.attachToRecyclerView(mRecyclerDefectItems);
//        }
    }

    private void displayAddress(TextView textAddress) {
        textAddress.setText("");
        mInspection.observe(this, inspection -> {
            textAddress.append(inspection.community + "\n");
            textAddress.append(inspection.address + "\n");
            textAddress.append(inspection.inspection_type);
        });
    }

    private void fillSpinner(Spinner spinnerDefectCategories) {
        mInspectViewModel.getDefectCategories(mInspectionTypeId).observe(this, defectCategories -> {
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, defectCategories);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerDefectCategories.setAdapter(adapter);
        });

        spinnerDefectCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mReinspection) {
                    displayReinspectItems(parent.getSelectedItem().toString());
                } else {
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

    private void displayReinspectItems(String filter) {
        mInspectViewModel.getInspectionHistory(mInspectionId).observe(this, defectItems ->
                mReinspectListAdapter.submitList(defectItems));
    }
}