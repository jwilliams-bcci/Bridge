package com.burgess.bridge.inspect;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.burgess.bridge.R;
import com.burgess.bridge.defectitem.DefectItemActivity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import data.Tables.DefectItem_Table;
import data.Tables.InspectionDefect_Table;
import data.Tables.Inspection_Table;

public class InspectListAdapter extends ListAdapter<DefectItem_Table, InspectViewHolder> {
    public static final String TAG = "INSPECT_LIST";

    private List<DefectItem_Table> currentList;
    private Inspection_Table inspection;
    private Set<Integer> mEnteredDefectIds = new HashSet<>();

    private int mInspectionId;
    private int mInspectionTypeId;
    private String mFilter;

    protected InspectListAdapter(@NonNull DiffUtil.ItemCallback<DefectItem_Table> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public InspectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_defect_item, parent, false);
        return new InspectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InspectViewHolder holder, int position) {
        DefectItem_Table defectItem = getItem(position);

        ConstraintLayout itemLayout = holder.getConstraintLayout();

        // Get text fields
        TextView textSection = holder.getTextDefectItemSection();
        TextView textItemNumber = holder.getTextDefectItemNumber();
        TextView textItemDescription = holder.getTextDefectItemDescription();
        TextView textStatusIcon = holder.getTextStatusIcon();

        // Set text fields
        textSection.setText(defectItem.CategoryName);
        textItemDescription.setText(defectItem.ItemDescription);
        textItemNumber.setText(Integer.toString(defectItem.ItemNumber));

        // Hide the section if needed
        if (position == 0) {
            // ALWAYS show the header for the first item
            textSection.setVisibility(View.VISIBLE);
        } else {
            // For other items, check against the previous one
            DefectItem_Table previous = getItem(position-1);
            if (previous.CategoryName.equals(defectItem.CategoryName)) {
                textSection.setVisibility(View.GONE);
            } else {
                textSection.setVisibility(View.VISIBLE);
            }
        }

        boolean isEntered = mEnteredDefectIds.contains(defectItem.DefectItemID);
        if (isEntered) {
            // Item has been entered, show a checkmark
            textStatusIcon.setText("✓");
            textStatusIcon.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.holo_green_dark));
            textStatusIcon.setVisibility(View.VISIBLE);
        } else if (defectItem.RequiresEntry) {
            // Item requires entry but is not entered, show asterisk
            textStatusIcon.setText("*");
            textStatusIcon.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.holo_red_dark));
            textStatusIcon.setVisibility(View.VISIBLE);
        } else {
            // No special status, hide the icon
            textStatusIcon.setVisibility(View.GONE);
        }

        itemLayout.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.defect_border));

        // Also, explicitly set text color to black, since the
        // reinspect adapter changes it to white.
        textItemNumber.setTextColor(Color.BLACK);
        textItemDescription.setTextColor(Color.BLACK);

        // Set the click listener
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), DefectItemActivity.class);
            intent.putExtra(DefectItemActivity.INSPECTION_ID, inspection.InspectionID);
            intent.putExtra(DefectItemActivity.DEFECT_ID, defectItem.DefectItemID);
            intent.putExtra(DefectItemActivity.INSPECTION_HISTORY_ID, -1);
            intent.putExtra(DefectItemActivity.FIRST_DETAIL_ID, -1);
            intent.putExtra(DefectItemActivity.FILTER_OPTION, mFilter);
            intent.putExtra(DefectItemActivity.SCROLL_POSITION, position);
            holder.itemView.getContext().startActivity(intent);
        });
    }

//    @Override
//    public int getItemCount() {
//        return currentList == null ? 0 : currentList.size();
//    }
//    public List<DefectItem_Table> getCurrentList() {
//        return currentList;
//    }

    public void setCurrentList(List<DefectItem_Table> list) {
        currentList = list;
        submitList(list);
    }
    public void setEnteredDefects(List<InspectionDefect_Table> enteredDefects) {
        mEnteredDefectIds.clear();
        if (enteredDefects != null) {
            for (InspectionDefect_Table defect : enteredDefects) {
                mEnteredDefectIds.add(defect.DefectItemID);
            }
        }
        // Notify the adapter that the data has changed so it redraws the icons
        // This is necessary because we're not re-submitting the whole list
        notifyDataSetChanged();
    }
    public void setInspection(Inspection_Table i) {
        inspection = i;
    }
    public void setFilter (String filter) {
        mFilter = filter;
    }

    public static class InspectDiff extends DiffUtil.ItemCallback<DefectItem_Table> {
        @Override
        public boolean areItemsTheSame(@NonNull DefectItem_Table oldItem, @NonNull DefectItem_Table newItem) {
            return oldItem.DefectItemID == newItem.DefectItemID;
        }

        @Override
        public boolean areContentsTheSame(@NonNull DefectItem_Table oldItem, @NonNull DefectItem_Table newItem) {
            return oldItem.DefectItemID == newItem.DefectItemID &&
                    oldItem.RequiresEntry == newItem.RequiresEntry;
        }
    }
}
