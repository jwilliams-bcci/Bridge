package com.example.bridge;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class InspectionViewHolder extends RecyclerView.ViewHolder {
    private final TextView mInspectionCommunity;
    private final TextView mInspectionAddress;
    private final TextView mInspectionType;
    private final TextView mInspectionNotes;

    public InspectionViewHolder(@NonNull View itemView) {
        super(itemView);
        mInspectionCommunity = itemView.findViewById(R.id.text_recycler_community);
        mInspectionAddress = itemView.findViewById(R.id.text_recycler_address);
        mInspectionType = itemView.findViewById(R.id.text_recycler_inspection_type);
        mInspectionNotes = itemView.findViewById(R.id.text_recycler_inspection_notes);
    }

    public void bind(String community, String address, String inspectionType, String notes) {
        mInspectionCommunity.setText(community);
        mInspectionAddress.setText(address);
        mInspectionType.setText(inspectionType);
        mInspectionNotes.setText(notes);
    }

    public static InspectionViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inspection_list, parent, false);
        return new InspectionViewHolder(view);
    }
}
