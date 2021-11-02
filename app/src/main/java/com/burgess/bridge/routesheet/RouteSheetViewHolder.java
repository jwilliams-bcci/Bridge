package com.burgess.bridge.routesheet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.burgess.bridge.BridgeLogger;
import com.burgess.bridge.inspectiondetails.InspectionDetailsActivity;
import com.burgess.bridge.R;
import com.burgess.bridge.reviewandsubmit.ReviewAndSubmitActivity;

public class RouteSheetViewHolder extends RecyclerView.ViewHolder {
    public static final String TAG = "ROUTE_SHEET";

    public int mInspectionId;
    public int mInspectionTypeId;
    public boolean isComplete;
    public boolean isReinspection;
    public int numberUploaded;
    public int numberToUpload;

    private final CardView mCardView;
    private final TextView mTextInspectionCommunity;
    private final TextView mTextInspectionAddress;
    private final TextView mTextInspectionType;
    //private final TextView mTextInspectionNotes;
    private final TextView mTextInspectionUploaded;
    public final ImageView mReorderHandle;
    public final ImageView mReupload;

    @SuppressLint("ClickableViewAccessibility")
    public RouteSheetViewHolder(@NonNull View itemView) {
        super(itemView);
        mCardView = itemView.findViewById(R.id.item_inspection_list_card_view);
        mTextInspectionCommunity = itemView.findViewById(R.id.text_recycler_community);
        mTextInspectionAddress = itemView.findViewById(R.id.text_recycler_address);
        mTextInspectionType = itemView.findViewById(R.id.text_recycler_inspection_type);
        //mTextInspectionNotes = itemView.findViewById(R.id.text_recycler_inspection_notes);
        mTextInspectionUploaded = itemView.findViewById(R.id.text_recycler_uploaded);
        mReorderHandle = itemView.findViewById(R.id.item_inspection_list_imageview_reorder_handle);
        mReupload = itemView.findViewById(R.id.item_inspection_list_imageview_reupload);

        itemView.setOnClickListener(v -> {
            if (!isComplete) {
                Intent intent = new Intent(v.getContext(), InspectionDetailsActivity.class);
                intent.putExtra(InspectionDetailsActivity.INSPECTION_ID, mInspectionId);
                v.getContext().startActivity(intent);
            } else {
                Toast.makeText(itemView.getContext(), "Cannot edit inspections while uploading!", Toast.LENGTH_LONG).show();
            }
        });

        if (isComplete) {
            itemView.setBackgroundColor(Color.YELLOW);
        }

        mReupload.setOnClickListener(v -> {
            BridgeLogger.log('I', TAG, "Trying reupload for ID: " + mInspectionId);
            Intent reviewAndSubmitIntent = new Intent(v.getContext(), ReviewAndSubmitActivity.class);
            reviewAndSubmitIntent.putExtra(ReviewAndSubmitActivity.INSPECTION_ID, mInspectionId);
            v.getContext().startActivity(reviewAndSubmitIntent);
        });
    }

    public void bind(String community, String address, String inspectionType, String notes) {
        mTextInspectionCommunity.setText(community);
        mTextInspectionAddress.setText(" - " + address);
        mTextInspectionType.setText(inspectionType);
        //mTextInspectionNotes.setText(notes.equalsIgnoreCase("null") ? "No additional notes" : notes);
        if (isComplete) {
            setBackgroundColor(Color.YELLOW);
            mTextInspectionUploaded.setVisibility(View.VISIBLE);
            mTextInspectionUploaded.setText(String.format("Uploaded %d of %d defects.", numberUploaded, numberToUpload));
            mReupload.setVisibility(View.VISIBLE);
        }
    }

    public void setBackgroundColor(int color) {
        mCardView.setCardBackgroundColor(color);
    }

    public static RouteSheetViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inspection_list, parent, false);
        return new RouteSheetViewHolder(view);
    }
}
