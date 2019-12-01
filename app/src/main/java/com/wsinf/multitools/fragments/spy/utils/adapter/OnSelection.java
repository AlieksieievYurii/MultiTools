package com.wsinf.multitools.fragments.spy.utils.adapter;

import java.util.List;

public interface OnSelection<T> {
    void onSelectableMode();
    void onSelectedDevices(final List<T> list);
}
