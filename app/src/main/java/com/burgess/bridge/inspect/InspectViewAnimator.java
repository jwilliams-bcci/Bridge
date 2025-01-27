package com.burgess.bridge.inspect;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

public class InspectViewAnimator extends DefaultItemAnimator {
    @Override
    public boolean animateAdd(RecyclerView.ViewHolder holder) {
        dispatchAddFinished(holder);
        return true;
    }
}
