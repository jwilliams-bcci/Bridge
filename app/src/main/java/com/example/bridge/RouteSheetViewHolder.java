package com.example.bridge;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RouteSheetViewHolder extends RecyclerView.ViewHolder {
    private final TextView mInspectionCommunity;
    private final TextView mInspectionAddress;
    private final TextView mInspectionType;
    private final TextView mInspectionNotes;

    public RouteSheetViewHolder(@NonNull View itemView) {
        super(itemView);
        mInspectionCommunity = itemView.findViewById(R.id.text_recycler_community);
        mInspectionAddress = itemView.findViewById(R.id.text_recycler_address);
        mInspectionType = itemView.findViewById(R.id.text_recycler_inspection_type);
        mInspectionNotes = itemView.findViewById(R.id.text_recycler_inspection_notes);

        itemView.setOnClickListener(v -> {
        });
    }

    public void bind(String community, String address, String inspectionType, String notes) {
        mInspectionCommunity.setText(community);
        mInspectionAddress.setText(address);
        mInspectionType.setText(inspectionType);
        mInspectionNotes.setText(notes);
    }

    public static RouteSheetViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inspection_list, parent, false);
        return new RouteSheetViewHolder(view);
    }
}
