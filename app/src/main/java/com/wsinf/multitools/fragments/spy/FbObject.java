package com.wsinf.multitools.fragments.spy;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

public abstract class FbObject<T> {
    private DatabaseReference root;

    public FbObject(DatabaseReference root) {
        this.root = root;
    }

    protected DatabaseReference getRoot() {
        return root;
    }

    public abstract T create(T object);
    public abstract void getAll(final PromiseOnList<T> promise);
    public abstract void get(final String id, final PromiseOnObject<T> promise);
    protected abstract T toObject(DataSnapshot dataSnapshot);
}
