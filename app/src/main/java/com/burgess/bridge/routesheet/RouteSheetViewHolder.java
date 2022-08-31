package com.burgess.bridge.routesheet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.burgess.bridge.BridgeLogger;
import com.burgess.bridge.inspectiondetails.InspectionDetailsActivity;
import com.burgess.bridge.R;
import com.burgess.bridge.reviewandsubmit.ReviewAndSubmitActivity;

public class RouteSheetViewHolder extends RecyclerView.ViewHolder {
    public static final String TAG = "ROUTE_SHEET";

    private final CardView mCardView;
    private final TextView mTextInspectionCommunity;
    private final TextView mTextInspectionAddress;
    private final TextView mTextInspectionType;
    private final TextView mTextCallbackNotes;
    private final TextView mTextInspectionUploaded;
    private final ImageView mImageViewReupload;
    private final ImageView mImageViewNoteAttached;

    public RouteSheetViewHolder(@NonNull View itemView) {
        super(itemView);
        mCardView = itemView.findViewById(R.id.item_inspection_list_card_view);
        mTextInspectionCommunity = itemView.findViewById(R.id.text_recycler_community);
        mTextInspectionAddress = itemView.findViewById(R.id.text_recycler_address);
        mTextInspectionType = itemView.findViewById(R.id.text_recycler_inspection_type);
        mTextCallbackNotes = itemView.findViewById(R.id.text_recycler_callback_notes);
        mTextInspectionUploaded = itemView.findViewById(R.id.text_recycler_uploaded);
        mImageViewReupload = itemView.findViewById(R.id.item_inspection_list_imageview_reupload);
        mImageViewNoteAttached = itemView.findViewById(R.id.item_inspection_list_imageview_note_attached);
    }

    //region GETTERS
    public CardView getCardView() {
        return mCardView;
    }

    public TextView getTextInspectionCommunity() {
        return mTextInspectionCommunity;
    }

    public TextView getTextInspectionAddress() {
        return mTextInspectionAddress;
    }

    public TextView getTextInspectionType() {
        return mTextInspectionType;
    }

    public TextView getTextCallbackNotes() { return mTextCallbackNotes; }

    public TextView getTextInspectionUploaded() {
        return mTextInspectionUploaded;
    }

    public ImageView getImageViewReupload() {
        return mImageViewReupload;
    }

    public ImageView getImageViewNoteAttached() {
        return mImageViewNoteAttached;
    }
    //endregion
}
