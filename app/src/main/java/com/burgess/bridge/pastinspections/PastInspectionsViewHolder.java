package com.burgess.bridge.pastinspections;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.burgess.bridge.R;

public class PastInspectionsViewHolder extends RecyclerView.ViewHolder {
    private final TextView mTextInspectionDate;
    private final TextView mTextInspectorName;
    private final TextView mTextInspectionType;
    private final TextView mTextInspectionStatus;

    public PastInspectionsViewHolder(@NonNull View itemView) {
        super(itemView);
        mTextInspectionDate = itemView.findViewById(R.id.item_inspection_history_list_item_text_date);
        mTextInspectorName = itemView.findViewById(R.id.item_inspection_history_list_item_text_inspector);
        mTextInspectionType = itemView.findViewById(R.id.item_inspection_history_list_item_text_inspection_type);
        mTextInspectionStatus = itemView.findViewById(R.id.item_inspection_history_list_item_text_status);
    }

    //region GETTERS
    public TextView getTextInspectionDate() {
        return mTextInspectionDate;
    }

    public TextView getTextInspectorName() {
        return mTextInspectorName;
    }

    public TextView getTextInspectionType() {
        return mTextInspectionType;
    }

    public TextView getTextInspectionStatus() {
        return mTextInspectionStatus;
    }
    //endregion
}
