package com.shuhart.stickyheader.sample;

import androidx.recyclerview.widget.RecyclerView;

public class CustomHeader implements Section {
    @Override
    public int type() {
        return CUSTOM_HEADER;
    }

    @Override
    public int sectionPosition() {
        return RecyclerView.NO_POSITION;
    }
}
