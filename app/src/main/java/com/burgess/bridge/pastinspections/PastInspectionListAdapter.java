package com.burgess.bridge.pastinspections;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.burgess.bridge.R;

import java.time.format.DateTimeFormatter;
import java.util.List;

import data.Tables.PastInspection_Table;

public class PastInspectionListAdapter extends ListAdapter<PastInspection_Table, PastInspectionsViewHolder> {
    public static final String TAG = "INSPECTION_HISTORY";

    private List<PastInspection_Table> currentList;

    protected PastInspectionListAdapter(@NonNull DiffUtil.ItemCallback<PastInspection_Table> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public PastInspectionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_past_inspections_list, parent,false);
        return new PastInspectionsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PastInspectionsViewHolder holder, int position) {
        PastInspection_Table pastInspection = currentList.get(position);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");

        //holder.getTextInspectionDate().setText(formatter.format(pastInspection.InspectionSubmitTime));
        holder.getTextInspectorName().setText(pastInspection.Inspector);
        holder.getTextInspectionType().setText(pastInspection.InspectionType);
        holder.getTextInspectionStatus().setText(pastInspection.IncompleteReason);
    }

    public void setCurrentList(List<PastInspection_Table> list) {
        currentList = list;
        submitList(currentList);
    }

    public static class PastInspectionsDiff extends DiffUtil.ItemCallback<PastInspection_Table> {

        @Override
        public boolean areItemsTheSame(@NonNull PastInspection_Table oldItem, @NonNull PastInspection_Table newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull PastInspection_Table oldItem, @NonNull PastInspection_Table newItem) {
            return oldItem.InspectionID == newItem.InspectionID;
        }
    }
}
