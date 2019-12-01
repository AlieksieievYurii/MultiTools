package com.wsinf.multitools.fragments.spy.point;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.wsinf.multitools.fragments.spy.FbObject;
import com.wsinf.multitools.fragments.spy.PromiseOnList;
import com.wsinf.multitools.fragments.spy.PromiseOnObject;

import java.util.ArrayList;
import java.util.List;

public class FbPoint extends FbObject<Point> {

    public FbPoint(DatabaseReference root) {
        super(root);
    }

    @Override
    public Point create(Point object) {
        final DatabaseReference point = getRoot().push();
        object.setId(point.getKey());
        point.setValue(object.toMap());
        return object;
    }

    @Override
    public void getAll(final PromiseOnList<Point> promise) {
        getRoot().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final List<Point> points = new ArrayList<>();
                for (final DataSnapshot dsp : dataSnapshot.getChildren())
                    points.add(toObject(dsp));
                promise.onReceiveAll(points);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void get(String id, PromiseOnObject<Point> promise) {

    }

    @Override
    protected Point toObject(DataSnapshot dataSnapshot) {
        final String id = dataSnapshot.getKey();
        final Point point = dataSnapshot.getValue(Point.class);
        if (point == null)
            return null;
        point.setId(id);
        return point;
    }
}
