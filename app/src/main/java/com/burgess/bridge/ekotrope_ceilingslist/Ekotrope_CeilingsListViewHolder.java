package com.burgess.bridge.ekotrope_ceilingslist;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.burgess.bridge.R;

public class Ekotrope_CeilingsListViewHolder extends RecyclerView.ViewHolder {
    private final TextView mTextIndex;
    private final TextView mTextName;

    public Ekotrope_CeilingsListViewHolder(@NonNull View itemView) {
        super(itemView);
        mTextIndex = itemView.findViewById(R.id.item_ceiling_text_index);
        mTextName = itemView.findViewById(R.id.item_ceiling_text_name);
    }

    //region GETTERS
    public TextView getTextIndex() {
        return mTextIndex;
    }
    public TextView getTextName() {
        return mTextName;
    }
    //endregion
    //region SETTERS
    public void setTextIndex(String index) {
        mTextIndex.setText(index);
    }
    public void setTextName(String name) {
        mTextName.setText(name);
    }
    //endregion
}
