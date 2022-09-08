package com.burgess.bridge.inspect;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.burgess.bridge.R;
import com.burgess.bridge.defectitem.DefectItemActivity;

import java.util.List;

import data.Tables.DefectItem_Table;
import data.Tables.Inspection_Table;

public class InspectListAdapter extends ListAdapter<DefectItem_Table, InspectViewHolder> {
    public static final String TAG = "INSPECT_LIST";

    private List<DefectItem_Table> currentList;
    private Inspection_Table inspection;

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
        DefectItem_Table defectItem = currentList.get(position);

        // Get text fields
        TextView textSection = holder.getTextDefectItemSection();
        TextView textItemNumber = holder.getTextDefectItemNumber();
        TextView textItemDescription = holder.getTextDefectItemDescription();

        // Set text fields
        textSection.setText(defectItem.defect_category_name);
        textItemDescription.setText(defectItem.item_description);
        textItemNumber.setText(Integer.toString(defectItem.item_number));

        // Hide the section if needed
        if (position > 0) {
            DefectItem_Table previous = getItem(position-1);
            if (previous.defect_category_name.equals(defectItem.defect_category_name)) {
                textSection.setVisibility(View.GONE);
            } else {
                textSection.setVisibility(View.VISIBLE);
            }
        }

        // Set the click listener
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), DefectItemActivity.class);
            intent.putExtra(DefectItemActivity.INSPECTION_ID, inspection.id);
            intent.putExtra(DefectItemActivity.DEFECT_ID, defectItem.id);
            intent.putExtra(DefectItemActivity.INSPECTION_HISTORY_ID, -1);
            intent.putExtra(DefectItemActivity.FILTER_OPTION, mFilter);
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return currentList == null ? 0 : currentList.size();
    }
    public List<DefectItem_Table> getCurrentList() {
        return currentList;
    }

    public void setCurrentList(List<DefectItem_Table> list) {
        currentList = list;
        submitList(list);
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
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull DefectItem_Table oldItem, @NonNull DefectItem_Table newItem) {
            return oldItem.id == newItem.id;
        }
    }
}
