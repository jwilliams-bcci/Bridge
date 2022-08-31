package com.burgess.bridge.inspect;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.burgess.bridge.defectitem.DefectItemActivity;
import com.burgess.bridge.R;

public class InspectViewHolder extends RecyclerView.ViewHolder {
    private final TextView mTextDefectItemSection;
    private final TextView mTextDefectItemNumber;
    private final TextView mTextDefectItemDescription;

    public InspectViewHolder(@NonNull View itemView) {
        super(itemView);
        mTextDefectItemSection = itemView.findViewById(R.id.item_defect_item_text_section);
        mTextDefectItemNumber = itemView.findViewById(R.id.item_defect_item_text_number);
        mTextDefectItemDescription = itemView.findViewById(R.id.item_defect_item_text_description);
    }

    //region GETTERS
    public TextView getTextDefectItemSection() {
        return mTextDefectItemSection;
    }

    public TextView getTextDefectItemNumber() {
        return mTextDefectItemNumber;
    }

    public TextView getTextDefectItemDescription() {
        return mTextDefectItemDescription;
    }
    //endregion
}
