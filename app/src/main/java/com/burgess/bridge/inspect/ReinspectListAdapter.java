package com.burgess.bridge.inspect;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.burgess.bridge.R;
import com.burgess.bridge.defectitem.DefectItemActivity;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import data.Tables.InspectionHistory_Table;
import data.Tables.Inspection_Table;

public class ReinspectListAdapter extends ListAdapter<InspectionHistory_Table, InspectViewHolder> {
    public static final String TAG = "REINSPECT_LIST";

    private List<InspectionHistory_Table> currentList;
    private Inspection_Table inspection;

    public int mInspectionId;
    public int mInspectionHistoryId;
    public int mInspectionTypeId;

    protected ReinspectListAdapter(@NonNull @NotNull DiffUtil.ItemCallback<InspectionHistory_Table> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @NotNull
    @Override
    public InspectViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_defect_item, parent, false);
        return new InspectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull InspectViewHolder holder, int position) {
        InspectionHistory_Table inspectionHistory = getCurrentList().get(position);
        Group group = holder.itemView.findViewById(R.id.item_defect_item_group);

        TextView textSection = holder.getTextDefectItemSection();
        TextView textNumber = holder.getTextDefectItemNumber();
        TextView textDescription = holder.getTextDefectItemDescription();

        // Set text fields
        textSection.setVisibility(View.GONE);
        textNumber.setText(Integer.toString(inspectionHistory.ItemNumber));
        textDescription.setText(inspectionHistory.Comment + "\n" + inspectionHistory.ItemDescription);

        // Set the background according to whether it's reviewed or not
        if (!inspectionHistory.IsReviewed) {
            textNumber.setTextColor(Color.WHITE);
            textDescription.setTextColor(Color.WHITE);
            group.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.defect_border_not_reviewed));
        } else {
            switch (inspectionHistory.ReviewedStatus) {
                case 2:
                    group.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.defect_border_reviewed_failed));
                    textDescription.setTextColor(Color.WHITE);
                    textNumber.setTextColor(Color.WHITE);
                    break;
                default:
                    group.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.defect_border_reviewed_complete));
                    textDescription.setTextColor(Color.BLACK);
                    textNumber.setTextColor(Color.BLACK);
                    break;
            }
        }

        // Add click listener for the RecyclerView items
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), DefectItemActivity.class);
            intent.putExtra(DefectItemActivity.INSPECTION_ID, inspectionHistory.InspectionID);
            intent.putExtra(DefectItemActivity.DEFECT_ID, inspectionHistory.DefectItemID);
            intent.putExtra(DefectItemActivity.INSPECTION_HISTORY_ID, inspectionHistory.InspectionDetailID);
            intent.putExtra(DefectItemActivity.FIRST_DETAIL_ID, inspectionHistory.FirstDefectInspectionDetailID);
            intent.putExtra(DefectItemActivity.INSPECTION_DEFECT_ID, inspectionHistory.InspectionDefectID);
            holder.itemView.getContext().startActivity(intent);
        });

        // Set the variables for swipe functionality in Activity
        holder.setInspectionHistoryId(inspectionHistory.InspectionDetailID);
        holder.setInspectionDefectId(inspectionHistory.InspectionDefectID);
        holder.setDefectItemId(inspectionHistory.DefectItemID);
        holder.setComment(inspectionHistory.Comment);
    }

    @Override
    public int getItemCount() {
        return currentList == null ? 0 : currentList.size();
    }
    public List<InspectionHistory_Table> getCurrentList() {
        return currentList;
    }
    public void setCurrentList(List<InspectionHistory_Table> list) {
        currentList = list;
        notifyDataSetChanged();
    }

    public static class ReinspectDiff extends DiffUtil.ItemCallback<InspectionHistory_Table> {
        @Override
        public boolean areItemsTheSame(@NonNull InspectionHistory_Table oldItem, @NonNull InspectionHistory_Table newItem) {
            return oldItem.InspectionDetailID == newItem.InspectionDetailID;
        }

        @Override
        public boolean areContentsTheSame(@NonNull InspectionHistory_Table oldItem, @NonNull InspectionHistory_Table newItem) {
            return Objects.equals(oldItem.ReviewedStatus, newItem.ReviewedStatus);
        }
    }
}