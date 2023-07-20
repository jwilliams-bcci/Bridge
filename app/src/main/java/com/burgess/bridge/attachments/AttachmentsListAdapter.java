package com.burgess.bridge.attachments;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.burgess.bridge.R;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

import data.Tables.Attachment_Table;

public class AttachmentsListAdapter extends ListAdapter<Attachment_Table, AttachmentsViewHolder> {
    public static final String TAG = "ATTACHMENTS";
    private List<Attachment_Table> currentList;

    protected AttachmentsListAdapter(@NonNull DiffUtil.ItemCallback<Attachment_Table> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public AttachmentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attachments_list, parent, false);
        return new AttachmentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttachmentsViewHolder holder, int position) {
        Attachment_Table attachment = currentList.get(position);

        holder.getTextFileName().setText(attachment.file_name);
        holder.getTextAttachmentType().setText(attachment.attachment_type);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            File file = new File(attachment.file_path);
            Uri apkUri = FileProvider.getUriForFile(v.getContext(), v.getContext().getApplicationContext().getPackageName(), file);
            intent.setDataAndType(apkUri, "*/*");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            v.getContext().startActivity(intent);
        });
    }

    public void setCurrentList(List<Attachment_Table> list) {
        currentList = list;
        submitList(currentList);
    }

    public static class AttachmentsDiff extends DiffUtil.ItemCallback<Attachment_Table> {
        @Override
        public boolean areItemsTheSame(@NonNull Attachment_Table oldItem, @NonNull Attachment_Table newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Attachment_Table oldItem, @NonNull Attachment_Table newItem) {
            return oldItem.id == newItem.id;
        }
    }
}
