package com.wsinf.multitools.fragments.spy.device;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.wsinf.multitools.fragments.spy.FbObject;
import com.wsinf.multitools.fragments.spy.Promise;

import java.util.ArrayList;
import java.util.List;

public class FbDevice extends FbObject<Device> {

    public FbDevice(DatabaseReference root) {
        super(root);
    }

    @Override
    public Device create(Device device) {
        final DatabaseReference reference = getRoot().push();
        device.setId(reference.getKey());
        reference.setValue(device.toMap());
        return device;
    }

    @Override
    public void getAll(final Promise<Device> promise) {
        getRoot().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final List<Device> list = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren())
                    list.add(toObject(ds));
                promise.onReceiveAll(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected Device toObject(DataSnapshot dataSnapshot) {
        final String id = dataSnapshot.getKey();
        final Device device = dataSnapshot.getValue(Device.class);
        if (device == null)
            return null;
        device.setId(id);
        return device;
    }
}
