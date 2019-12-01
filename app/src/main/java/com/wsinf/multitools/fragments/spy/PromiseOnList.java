package com.wsinf.multitools.fragments.spy;

import java.util.List;

public interface PromiseOnList<T> {
    void onReceiveAll(List<T> list);
}
