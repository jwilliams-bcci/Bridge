package com.burgess.bridge.routesheet;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.burgess.bridge.RouteSheetDragEventListener;

public class RouteSheetTouchHelper extends ItemTouchHelper.Callback {
    private final RouteSheetListAdapter adapter;

    public RouteSheetTouchHelper(RouteSheetListAdapter adapter) {
        super();
        this.adapter = adapter;
    }

    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        return makeMovementFlags(dragFlags, 0);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        adapter.moveItem(viewHolder.getAbsoluteAdapterPosition(), target.getAbsoluteAdapterPosition());
        return true;
    }

    @Override
    public void onMoved(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, int fromPos, @NonNull RecyclerView.ViewHolder target, int toPos, int x, int y) {
        super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        // do nothing, no swiping on Route Sheet for right now.
    }
}
