package com.wsinf.multitools.fragments.spy;

import java.util.List;

public interface Promise<T> {
    void onReceiveAll(List<T> list);
}
