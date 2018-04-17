package com.shuhart.stickyheader;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SectionAdapter extends StickyAdapter<RecyclerView.ViewHolder, RecyclerView.ViewHolder> {
    List<Section> items = new ArrayList<>();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == Section.HEADER || viewType == Section.CUSTOM_HEADER) {
            return new HeaderViewholder(inflater.inflate(R.layout.recycler_view_header_item, parent, false));
        }
        return new ItemViewHolder(inflater.inflate(R.layout.recycler_view_item, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = items.get(position).type();
        int section = items.get(position).sectionPosition();
        if (type == Section.HEADER) {
            ((HeaderViewholder) holder).textView.setText("Header " + section);
        } else if (type == Section.ITEM){
            ((ItemViewHolder) holder).textView.setText("Item " + section);
        } else {
            ((HeaderViewholder) holder).textView.setText("Custom header");
        }
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).type();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getHeaderPositionForItem(int itemPosition) {
        return items.get(itemPosition).sectionPosition();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int headerPosition) {
        ((HeaderViewholder) holder).textView.setText("Header " + headerPosition);
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return createViewHolder(parent, Section.HEADER);
    }

    public static class HeaderViewholder extends RecyclerView.ViewHolder {
        TextView textView;

        HeaderViewholder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_view);
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        ItemViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_view);
        }
    }
}
