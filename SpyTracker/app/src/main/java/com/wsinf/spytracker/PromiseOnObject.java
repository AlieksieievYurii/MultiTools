package com.wsinf.spytracker;

import java.util.List;

public interface PromiseOnObject<T> {
    void onReceive(T object);
}
