package com.example.mostafa.talents;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {

RecyclerItemTouchHelper RecyclerItemTouchHelper;
    /**

     */
    public RecyclerItemTouchHelper(int dragDirs, int swipeDirs,RecyclerItemTouchHelper re) {
        super(dragDirs, swipeDirs);
        RecyclerItemTouchHelper=re;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }
}
