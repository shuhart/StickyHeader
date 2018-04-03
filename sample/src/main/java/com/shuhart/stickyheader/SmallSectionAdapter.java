package com.shuhart.stickyheader;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class SmallSectionAdapter extends SectionAdapter {
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == Section.HEADER) {
            RecyclerView.ViewHolder holder =
                    new HeaderViewholder(inflater.inflate(R.layout.recycler_view_header_item, parent, false));
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            lp.height = lp.height / 2;
            holder.itemView.setLayoutParams(lp);
            return holder;
        }
        return super.onCreateViewHolder(parent, viewType);
    }
}
