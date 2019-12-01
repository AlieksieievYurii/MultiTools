package com.wsinf.multitools.fragments.spy.device;

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

public class FbDevice extends FbObject<Device> {

    public FbDevice(DatabaseReference root) {
        super(root);
    }

    @Override
    public Device create(Device device) {
        final DatabaseReference reference = getRoot().child(device.getId());
        reference.setValue(device.toMap());
        return device;
    }

    @Override
    public void getAll(final PromiseOnList<Device> promise) {
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
    public void get(String id, final PromiseOnObject<Device> promise) {
        getRoot().orderByKey().equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0)
                    promise.onReceive(null);
                else {
                    final DataSnapshot dssDevice = dataSnapshot.getChildren().iterator().next();
                    final Device device = dssDevice.getValue(Device.class);
                    if (device == null)
                        return;

                    device.setId(dssDevice.getKey());
                    promise.onReceive(device);
                }

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
