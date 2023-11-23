package com.aishwarya.mymateapp;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
    private final int spanCount;
    private final int spacing;
    private final boolean includeEdge;

    public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        // Cast the view's layout params to StaggeredGridLayoutManager.LayoutParams
        StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
        int position = parent.getChildAdapterPosition(view); // Get the adapter position of the view
        int column = layoutParams.getSpanIndex(); // Get the span index of the view

        if (includeEdge) {
            // Apply spacing for items
            outRect.left = spacing - column * spacing / spanCount;  // Calculate the left spacing for the item
            outRect.right = (column + 1) * spacing / spanCount;   // Calculate the right spacing for the item

            if (position < spanCount) {
                outRect.top = spacing;  // Apply top spacing for the first row
            }
            outRect.bottom = spacing;  // Apply bottom spacing for all rows
        } else {

            // Apply spacing for items with includeEdge set to false
            outRect.left = column * spacing / spanCount;
            outRect.right = spacing - (column + 1) * spacing / spanCount;
            if (position >= spanCount) {
                outRect.top = spacing;  // Apply top spacing for rows after the first row
            }
        }
    }
}