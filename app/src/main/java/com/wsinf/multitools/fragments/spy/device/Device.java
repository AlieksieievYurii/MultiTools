package com.wsinf.multitools.fragments.spy.device;

import android.os.Parcel;
import android.os.Parcelable;

import com.wsinf.multitools.fragments.spy.Serializer;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Device implements Serializer, Parcelable
{
    private String id;
    private String macAddress;
    private String model;

    public Device() {
    }

    public Device(String macAddress, String model) {
        this.macAddress = macAddress;
        this.model = model;
    }

    protected Device(Parcel in) {
        id = in.readString();
        macAddress = in.readString();
        model = in.readString();
    }

    public static final Creator<Device> CREATOR = new Creator<Device>() {
        @Override
        public Device createFromParcel(Parcel in) {
            return new Device(in);
        }

        @Override
        public Device[] newArray(int size) {
            return new Device[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public String getModel() {
        return model;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Override
    public String toString() {
        return "Device{" +
                "id='" + id + '\'' +
                ", macAddress='" + macAddress + '\'' +
                ", model='" + model + '\'' +
                '}';
    }

    @Override
    public Map<String, Object> toMap() {
        final Map<String, Object> data = new HashMap<>();
        data.put("macAddress", macAddress);
        data.put("model", model);
        return data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(macAddress);
        dest.writeString(model);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Device device = (Device) o;
        return Objects.equals(id, device.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
