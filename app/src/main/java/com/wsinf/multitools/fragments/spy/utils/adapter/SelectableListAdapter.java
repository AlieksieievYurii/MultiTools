package com.wsinf.multitools.fragments.spy.utils.adapter;

import android.content.Context;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.List;

public abstract class SelectableListAdapter<T> extends BaseAdapter {

    private Context context;

    OnSelection<T> onSelection;
    boolean isSelectableMode = false;
    ListView listView;
    List<T> data;

    SelectableListAdapter(Context context, ListView listView, List<T> data) {
        this.context = context;
        this.listView = listView;
        this.data = data;

        this.listView.setAdapter(this);
    }

    public Context getContext() {
        return context;
    }

    public void setOnSelection(OnSelection<T> onSelection) {
        this.onSelection = onSelection;
    }

    public abstract void onCancelSelection();
    public abstract void onSelect();
    public abstract void onCheckAll();
}
