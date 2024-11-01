package com.burgess.bridge.ekotrope_framedfloorslist;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.burgess.bridge.R;
import com.burgess.bridge.ekotrope_framedfloors.Ekotrope_FramedFloorsActivity;

import java.util.List;

import data.Tables.Ekotrope_FramedFloor_Table;

public class Ekotrope_FramedFloorsListAdapter extends ListAdapter<Ekotrope_FramedFloor_Table, Ekotrope_FramedFloorsViewHolder> {
    public static final String TAG = "FRAMED_FLOORS_LIST";

    private List<Ekotrope_FramedFloor_Table> currentList;

    protected Ekotrope_FramedFloorsListAdapter(@NonNull DiffUtil.ItemCallback<Ekotrope_FramedFloor_Table> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public Ekotrope_FramedFloorsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_framed_floor, parent, false);
        return new Ekotrope_FramedFloorsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Ekotrope_FramedFloorsViewHolder holder, int position) {
        Ekotrope_FramedFloor_Table framedFloor = currentList.get(position);

        // Get text fields
        TextView textIndex = holder.getTextIndex();
        TextView textName = holder.getTextName();

        // Set text fields
        holder.setTextIndex(Integer.toString(framedFloor.index));
        holder.setTextName(framedFloor.name);

        // Set the click listener
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), Ekotrope_FramedFloorsActivity.class);
            intent.putExtra(Ekotrope_FramedFloorsActivity.PLAN_ID, framedFloor.plan_id);
            intent.putExtra(Ekotrope_FramedFloorsActivity.FRAMED_FLOOR_INDEX, framedFloor.index);
            holder.itemView.getContext().startActivity(intent);
        });
    }
    @Override
    public int getItemCount() {
        return currentList == null ? 0 : currentList.size();
    }
    public List<Ekotrope_FramedFloor_Table> getCurrentList() {
        return currentList;
    }
    public void setCurrentList(List<Ekotrope_FramedFloor_Table> list) {
        currentList = list;
        submitList(list);
    }

    public static class Ekotrope_FramedFloorsDiff extends DiffUtil.ItemCallback<Ekotrope_FramedFloor_Table> {
        @Override
        public boolean areItemsTheSame(@NonNull Ekotrope_FramedFloor_Table oldItem, @NonNull Ekotrope_FramedFloor_Table newItem) {
            return oldItem == newItem;
        }
        @Override
        public boolean areContentsTheSame(@NonNull Ekotrope_FramedFloor_Table oldItem, @NonNull Ekotrope_FramedFloor_Table newItem) {
            return oldItem.index == newItem.index;
        }
    }
}
