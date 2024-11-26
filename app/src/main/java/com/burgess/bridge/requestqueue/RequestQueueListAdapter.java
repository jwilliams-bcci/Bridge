package com.burgess.bridge.requestqueue;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.burgess.bridge.R;

import java.util.List;
import java.util.Locale;

import data.Tables.SubmitRequest_Table;

public class RequestQueueListAdapter extends ListAdapter<SubmitRequest_Table, RequestQueueViewHolder> {
    private List<SubmitRequest_Table> currentList;
    private RequestQueueViewModel viewModel;

    public RequestQueueListAdapter(@NonNull RequestQueueDiff diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public RequestQueueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_request_queue, parent, false);
        return new RequestQueueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestQueueViewHolder holder, int position) {
        SubmitRequest_Table item = currentList.get(position);
        holder.textTitle.setText((String.format(Locale.US, "Request ID: %d", item.id)));
        holder.textDescription.setText((String.format(Locale.US, "Inspection ID: %s", item.inspection_id)));

        if (item.complete) {
            holder.imageStatus.setBackgroundResource(android.R.drawable.checkbox_on_background);
        } else if (item.failed) {
            holder.imageStatus.setBackgroundResource(android.R.drawable.ic_menu_close_clear_cancel);
        } else {
            holder.imageStatus.setBackgroundResource(android.R.drawable.ic_menu_help);
        }
    }

    @Override
    public int getItemCount() {
        return currentList == null ? 0 : currentList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setCurrentList(List<SubmitRequest_Table> list) {
        currentList = list;
        submitList(currentList);
    }

    public List<SubmitRequest_Table> getCurrentList() {
        return currentList;
    }

    public static class RequestQueueDiff extends DiffUtil.ItemCallback<SubmitRequest_Table> {

        @Override
        public boolean areItemsTheSame(@NonNull SubmitRequest_Table oldItem, @NonNull SubmitRequest_Table newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull SubmitRequest_Table oldItem, @NonNull SubmitRequest_Table newItem) {
            return oldItem.complete == newItem.complete &&
                    oldItem.failed == newItem.failed;
        }
    }
}
