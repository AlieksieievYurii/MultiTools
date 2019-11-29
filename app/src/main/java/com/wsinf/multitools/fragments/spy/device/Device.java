package com.wsinf.multitools.fragments.spy.device;

import com.wsinf.multitools.fragments.spy.Serializer;

import java.util.HashMap;
import java.util.Map;

public class Device implements Serializer
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
}
