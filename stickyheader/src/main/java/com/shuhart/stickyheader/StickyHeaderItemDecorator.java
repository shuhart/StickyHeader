package com.shuhart.stickyheader;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

public class StickyHeaderItemDecorator extends RecyclerView.ItemDecoration {

    private StickyAdapter adapter;
    private int currentStickyPosition = RecyclerView.NO_POSITION;
    private RecyclerView recyclerView;
    private RecyclerView.ViewHolder currentStickyHolder;

    public StickyHeaderItemDecorator(@NonNull StickyAdapter adapter) {
        this.adapter = adapter;
    }

    public void attachToRecyclerView(@Nullable RecyclerView recyclerView) {
        if (this.recyclerView == recyclerView) {
            return; // nothing to do
        }
        if (recyclerView != null) {
            destroyCallbacks(recyclerView);
        }
        this.recyclerView = recyclerView;
        if (recyclerView != null) {
            currentStickyHolder = adapter.onCreateHeaderViewHolder(recyclerView);
            fixLayoutSize();
            setupCallbacks();
        }
    }

    private void setupCallbacks() {
        recyclerView.addItemDecoration(this);
    }

    private void destroyCallbacks(RecyclerView recyclerView) {
        recyclerView.removeItemDecoration(this);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager == null) {
            return;
        }

        int topChildPosition = RecyclerView.NO_POSITION;
        if (layoutManager instanceof LinearLayoutManager) {
            topChildPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
        } else {
            View topChild = parent.getChildAt(0);
            if (topChild != null) {
                topChildPosition = parent.getChildAdapterPosition(topChild);
            }
        }

        if (topChildPosition == RecyclerView.NO_POSITION) {
            return;
        }

        RecyclerView.ViewHolder viewHolder = parent.findViewHolderForAdapterPosition(topChildPosition);
        if (viewHolder == null || viewHolder.itemView.getLayoutParams().width == 0) {
            return;
        }

        int contactPoint = viewHolder.itemView.getBottom();
        View childInContact = getChildInContact(parent, contactPoint);
        if (childInContact == null) {
            return;
        }

        int childPosition = parent.getChildAdapterPosition(childInContact);

        if (adapter.getHeaderPositionForItem(childPosition) == childPosition) {
            updateStickyHeader(topChildPosition, childPosition);
            moveHeader(c, childInContact);
            return;
        } else {
            updateStickyHeader(topChildPosition, RecyclerView.NO_POSITION);
        }

        drawHeader(c);
    }

    @SuppressWarnings("unchecked")
    private void updateStickyHeader(int topChildPosition, int contactChildPosition) {
        int headerPositionForItem = adapter.getHeaderPositionForItem(topChildPosition);
        if (headerPositionForItem != currentStickyPosition) {
            adapter.onBindHeaderViewHolder(currentStickyHolder, headerPositionForItem);
            currentStickyPosition = headerPositionForItem;
        } else if (contactChildPosition != RecyclerView.NO_POSITION) {
            adapter.onBindHeaderViewHolder(currentStickyHolder, headerPositionForItem);
        }
    }

    private void drawHeader(Canvas c) {
        c.save();
        c.translate(0, 0);
        currentStickyHolder.itemView.draw(c);
        c.restore();
    }

    private void moveHeader(Canvas c, View nextHeader) {
        c.save();
        c.translate(0, nextHeader.getTop() - nextHeader.getHeight());
        currentStickyHolder.itemView.draw(c);
        c.restore();
    }

    private View getChildInContact(RecyclerView parent, int contactPoint) {
        View childInContact = null;
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            if (child.getBottom() > contactPoint) {
                if (child.getTop() <= contactPoint) {
                    // This child overlaps the contactPoint
                    childInContact = child;
                    break;
                }
            }
        }
        return childInContact;
    }

    private void fixLayoutSize() {
        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                // Specs for parent (RecyclerView)
                int widthSpec = View.MeasureSpec.makeMeasureSpec(recyclerView.getWidth(), View.MeasureSpec.EXACTLY);
                int heightSpec = View.MeasureSpec.makeMeasureSpec(recyclerView.getHeight(), View.MeasureSpec.UNSPECIFIED);

                // Specs for children (headers)
                int childWidthSpec = ViewGroup.getChildMeasureSpec(
                        widthSpec,
                        recyclerView.getPaddingLeft() + recyclerView.getPaddingRight(),
                        currentStickyHolder.itemView.getLayoutParams().width);
                int childHeightSpec = ViewGroup.getChildMeasureSpec(
                        heightSpec,
                        recyclerView.getPaddingTop() + recyclerView.getPaddingBottom(),
                        currentStickyHolder.itemView.getLayoutParams().height);

                currentStickyHolder.itemView.measure(childWidthSpec, childHeightSpec);

                currentStickyHolder.itemView.layout(0, 0,
                        currentStickyHolder.itemView.getMeasuredWidth(),
                        currentStickyHolder.itemView.getMeasuredHeight());
            }
        });
    }

    /**
     * View types for a sticky header are not supported.
     */
    public abstract static class StickyAdapter<SVH extends RecyclerView.ViewHolder,
            VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

        /**
         * This method gets called by {@link StickyHeaderItemDecorator} to fetch
         * the position of the header item in the adapter that is used for
         * (represents) item at specified position.
         *
         * @param itemPosition int. Adapter's position of the item for which to do
         *                     the search of the position of the header item.
         * @return int. Position of the header item in the adapter.
         */
        abstract int getHeaderPositionForItem(int itemPosition);

        /**
         * This method gets called by {@link StickyHeaderItemDecorator} to setup the header View.
         *
         * @param holder         RecyclerView.ViewHolder. Holder to bind the data on.
         * @param headerPosition int. Position of the header item in the adapter.
         */
        abstract void onBindHeaderViewHolder(SVH holder, int headerPosition);

        /**
         * Called only twice when {@link StickyHeaderItemDecorator} needs
         * a new {@link RecyclerView.ViewHolder} to represent a sticky header item.
         * Those two instances will be cached and used to represent a current top sticky header
         * and the moving one.
         * <p>
         * You can either create a new View manually or inflate it from an XML layout file.
         * <p>
         * The new ViewHolder will be used to display items of the adapter using
         * {@link #onBindHeaderViewHolder(RecyclerView.ViewHolder, int)}. Since it will be re-used to display
         * different items in the data set, it is a good idea to cache references to sub views of
         * the View to avoid unnecessary {@link View#findViewById(int)} calls.
         *
         * @param parent The ViewGroup to resolve a layout params.
         * @return A new ViewHolder that holds a View of the given view type.
         * @see #onBindHeaderViewHolder(RecyclerView.ViewHolder, int)
         */
        abstract SVH onCreateHeaderViewHolder(ViewGroup parent);
    }
}
