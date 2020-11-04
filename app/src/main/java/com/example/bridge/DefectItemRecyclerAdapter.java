package com.example.bridge;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import data.DefectItem;

public class DefectItemRecyclerAdapter extends RecyclerView.Adapter<DefectItemRecyclerAdapter.ViewHolder> {
    private final Context mContext;
    private final List<DefectItem> mDefectItemList;
    private final LayoutInflater mLayoutInflater;

    public DefectItemRecyclerAdapter(Context context, List<DefectItem> defectItemList) {
        mContext = context;
        mDefectItemList = defectItemList;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_defect_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DefectItem defectItem = mDefectItemList.get(position);
        holder.mDefectItemId = defectItem.getDefectItemId();
        holder.mDefectCategoryId = defectItem.getDefectCategoryId();
        holder.mItemNumber = defectItem.getItemNumber();
        holder.mItemDescription = defectItem.getItemDescription();
        holder.mTextItemNumber.setText(Integer.toString(defectItem.getItemNumber()));
        holder.mTextItemDescription.setText(defectItem.getItemDescription());
    }

    @Override
    public int getItemCount() {
        return mDefectItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public int mDefectItemId;
        public int mDefectCategoryId;
        public int mItemNumber;
        public String mItemDescription;
        public final TextView mTextItemNumber;
        public final TextView mTextItemDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextItemNumber = (TextView) itemView.findViewById(R.id.item_defect_item_text_number);
            mTextItemDescription = (TextView) itemView.findViewById(R.id.item_defect_item_text_description);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                }
//            });
        }
    }
}
