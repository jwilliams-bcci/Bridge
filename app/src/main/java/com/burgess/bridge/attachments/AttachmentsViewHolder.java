package com.burgess.bridge.attachments;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.burgess.bridge.R;

public class AttachmentsViewHolder extends RecyclerView.ViewHolder {
    private final TextView mTextFileName;
    private final TextView mTextAttachmentType;

    public AttachmentsViewHolder(@NonNull View itemView) {
        super(itemView);
        mTextFileName = itemView.findViewById(R.id.item_attachments_list_text_filename);
        mTextAttachmentType = itemView.findViewById(R.id.item_attachments_list_text_attachment_type);
    }

    //region GETTERS
    public TextView getTextFileName() { return mTextFileName; }
    public TextView getTextAttachmentType() { return mTextAttachmentType; }
    //endregion
}
