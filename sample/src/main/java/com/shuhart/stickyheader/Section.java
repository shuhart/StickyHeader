package com.shuhart.stickyheader;

public interface Section {
    int HEADER = 0;
    int ITEM = 1;

    int type();

    int section();
}
