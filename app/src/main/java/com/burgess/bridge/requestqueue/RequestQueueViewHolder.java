package com.burgess.bridge.requestqueue;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.burgess.bridge.R;

public class RequestQueueViewHolder extends RecyclerView.ViewHolder {
    public final TextView textTitle;
    public final TextView textDescription;
    public final ImageView imageStatus;

    public RequestQueueViewHolder(@NonNull View itemView) {
        super(itemView);
        textTitle = itemView.findViewById(R.id.item_request_queue_text_title);
        textDescription = itemView.findViewById(R.id.item_request_queue_text_description);
        imageStatus = itemView.findViewById(R.id.item_request_queue_status);
    }
}
