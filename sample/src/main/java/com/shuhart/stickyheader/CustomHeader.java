package com.shuhart.stickyheader;

import android.support.v7.widget.RecyclerView;

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
