package com.wsinf.multitools.fragments.spy.device;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.wsinf.multitools.fragments.spy.Promise;

import java.util.ArrayList;
import java.util.List;

public class FbDevice {
    private DatabaseReference root;

    public FbDevice(DatabaseReference root) {
        this.root = root;
    }

    public Device create(Device device) {
        final DatabaseReference reference = root.push();
        device.setId(reference.getKey());
        reference.setValue(device.toMap());
        return device;
    }

    public void getAllDevices(final Promise<Device> promise) {
        root.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final List<Device> list = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren())
                    list.add(toDevice(ds));
                promise.onReceiveAll(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private static Device toDevice(final DataSnapshot dataSnapshot) {
        final String id = dataSnapshot.getKey();
        final Device device = dataSnapshot.getValue(Device.class);
        if (device == null)
            return null;
        device.setId(id);
        return device;
    }
}
