package com.wsinf.spytracker;

import java.util.List;

public interface PromiseOnList<T> {
    void onReceiveAll(List<T> list);
}
