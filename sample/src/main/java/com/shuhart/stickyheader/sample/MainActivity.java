package com.shuhart.stickyheader.sample;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.shuhart.stickyheader.StickyHeaderItemDecorator;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration dividerDecorator = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerDecorator);

        SectionAdapter adapter = new SectionAdapter();
        recyclerView.setAdapter(adapter);

        StickyHeaderItemDecorator decorator = new StickyHeaderItemDecorator(adapter);
        decorator.attachToRecyclerView(recyclerView);

        adapter.items = new ArrayList<Section>() {{
            int section = 0;
//            add(new CustomHeader());
            for (int i = 0; i < 28; i++) {
                if (i < 12) {
                    if (i % 4 == 0) {
                        section = i;
                        add(new SectionHeader(section));
                    } else {
                        add(new SectionItem(section));
                    }
                } else {
                    if (i % 8 == 0) {
                        section = i;
                        add(new SectionHeader(section));
                    } else {
                        add(new SectionItem(section));
                    }
                }
            }
        }};
        adapter.notifyDataSetChanged();
    }
}
