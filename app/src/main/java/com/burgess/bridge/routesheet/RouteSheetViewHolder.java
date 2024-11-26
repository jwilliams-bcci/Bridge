package com.burgess.bridge.routesheet;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.burgess.bridge.R;

public class RouteSheetViewHolder extends RecyclerView.ViewHolder {
    public static final String TAG = "ROUTE_SHEET";

    public final CardView cardView;
    public final TextView textCommunity;
    public final TextView textAddress;
    public final TextView textInspectionType;
    public final ImageView imageViewShowMenu;
    public final TextView textCallbackNotes;
    public final TextView textUploaded;
    public final ImageView imageViewReupload;
    public final ImageView imageViewNoteAttached;
    public final ConstraintLayout constraintLayoutMenu;
    public final View verticalDivider;
    public final Button buttonReupload;
    public final Button buttonResetInspection;
    public final Button buttonViewQueue;

    public RouteSheetViewHolder(@NonNull View itemView) {
        super(itemView);
        cardView = itemView.findViewById(R.id.item_inspection_list_card_view);
        textCommunity = itemView.findViewById(R.id.text_recycler_community);
        textAddress = itemView.findViewById(R.id.text_recycler_address);
        textInspectionType = itemView.findViewById(R.id.text_recycler_inspection_type);
        imageViewShowMenu = itemView.findViewById(R.id.item_inspection_list_imageview_show_menu);
        textCallbackNotes = itemView.findViewById(R.id.text_recycler_callback_notes);
        textUploaded = itemView.findViewById(R.id.text_recycler_uploaded);
        imageViewReupload = itemView.findViewById(R.id.item_inspection_list_imageview_reupload);
        imageViewNoteAttached = itemView.findViewById(R.id.item_inspection_list_imageview_note_attached);
        constraintLayoutMenu = itemView.findViewById(R.id.item_inspection_constraint_layout_menu);
        verticalDivider = itemView.findViewById(R.id.item_inspection_list_divider);
        buttonReupload = itemView.findViewById(R.id.item_inspection_list_button_reupload);
        buttonResetInspection = itemView.findViewById(R.id.item_inspection_list_button_reset_inspection);
        buttonViewQueue = itemView.findViewById(R.id.item_inspection_list_button_view_queue);
    }
}
