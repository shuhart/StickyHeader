package com.shuhart.stickyheader;

public class SectionHeader implements Section {

    private int section;

    public SectionHeader(int section) {
        this.section = section;
    }

    @Override
    public int type() {
        return HEADER;
    }

    @Override
    public int sectionPosition() {
        return section;
    }
}
