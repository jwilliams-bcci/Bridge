package com.burgess.bridge.ekotrope_doorslist;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.burgess.bridge.R;
import com.burgess.bridge.ekotrope_doors.Ekotrope_DoorsActivity;

import java.util.List;

import data.Tables.Ekotrope_Door_Table;

public class Ekotrope_DoorsListAdapter extends ListAdapter<Ekotrope_Door_Table, Ekotrope_DoorsListViewHolder> {
    public static final String TAG = "DOORS_LIST";

    private List<Ekotrope_Door_Table> currentList;

    protected Ekotrope_DoorsListAdapter(@NonNull DiffUtil.ItemCallback<Ekotrope_Door_Table> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public Ekotrope_DoorsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_door, parent, false);
        return new Ekotrope_DoorsListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Ekotrope_DoorsListViewHolder holder, int position) {
        Ekotrope_Door_Table door = currentList.get(position);

        // Get text fields
        TextView textIndex = holder.getTextIndex();
        TextView textName = holder.getTextName();

        // Set text fields
        holder.setTextIndex(Integer.toString(door.index));
        holder.setTextName(door.name);

        // Set the click listener
        holder.itemView.setOnClickListener(v -> {
            Intent doorIntent = new Intent(holder.itemView.getContext(), Ekotrope_DoorsActivity.class);
            doorIntent.putExtra(Ekotrope_DoorsActivity.PLAN_ID, door.plan_id);
            doorIntent.putExtra(Ekotrope_DoorsActivity.DOOR_INDEX, door.index);
            holder.itemView.getContext().startActivity(doorIntent);
        });
    }
    @Override
    public int getItemCount() {
        return currentList == null ? 0 : currentList.size();
    }
    public List<Ekotrope_Door_Table> getCurrentList() {
        return currentList;
    }
    public void setCurrentList(List<Ekotrope_Door_Table> list) {
        currentList = list;
        submitList(list);
    }

    public static class Ekotrope_DoorsDiff extends DiffUtil.ItemCallback<Ekotrope_Door_Table> {
        @Override
        public boolean areItemsTheSame(@NonNull Ekotrope_Door_Table oldItem, @NonNull Ekotrope_Door_Table newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Ekotrope_Door_Table oldItem, @NonNull Ekotrope_Door_Table newItem) {
            return oldItem.index == newItem.index;
        }
    }
}
