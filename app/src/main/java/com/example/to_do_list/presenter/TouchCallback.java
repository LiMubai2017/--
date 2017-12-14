package com.example.to_do_list.presenter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.example.to_do_list.view.MainView;

import java.util.Collections;

import static com.example.to_do_list.presenter.MainPresenter.tagList;

/**
 * Created by abc on 2017/12/14.
 */

public class TouchCallback extends ItemTouchHelper.Callback{
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        final int dragFlags;
        final int swipeFlags;

        if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            swipeFlags = 0;
        } else {
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            swipeFlags = 0;
        }
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int fromPosition = viewHolder.getAdapterPosition();//得到拖动ViewHolder的position
        int toPosition = target.getAdapterPosition();//得到目标ViewHolder的position
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(tagList, i, i + 1);
                int temp = tagList.get(i).getPriority();
                tagList.get(i).setPriority(tagList.get(i+1).getPriority());
                tagList.get(i+1).setPriority(temp);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(tagList, i, i - 1);
                int temp = tagList.get(i).getPriority();
                tagList.get(i).setPriority(tagList.get(i-1).getPriority());
                tagList.get(i-1).setPriority(temp);
            }
            MainPresenter.rankMode=3;
        }
        MainView.adapter.notifyItemMoved(fromPosition, toPosition);
        return true;
    }
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
    }
}
