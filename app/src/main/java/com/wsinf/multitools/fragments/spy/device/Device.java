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
    private String serial;
    private String model;
    private String buildId;
    private String manufacture;
    private String brand;
    private String type;
    private String user;
    private int base;
    private String incremental;
    private String sdk;
    private String board;
    private String host;
    private String versionCode;

    public Device() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBuildId() {
        return buildId;
    }

    public void setBuildId(String buildId) {
        this.buildId = buildId;
    }

    public String getManufacture() {
        return manufacture;
    }

    public void setManufacture(String manufacture) {
        this.manufacture = manufacture;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getBase() {
        return base;
    }

    public void setBase(int base) {
        this.base = base;
    }

    public String getIncremental() {
        return incremental;
    }

    public void setIncremental(String incremental) {
        this.incremental = incremental;
    }

    public String getSdk() {
        return sdk;
    }

    public void setSdk(String sdk) {
        this.sdk = sdk;
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    private Device(Parcel in) {
        id = in.readString();
        serial = in.readString();
        model = in.readString();
        buildId = in.readString();
        manufacture = in.readString();
        brand = in.readString();
        type = in.readString();
        user = in.readString();
        base = in.readInt();
        incremental = in.readString();
        sdk = in.readString();
        board = in.readString();
        host = in.readString();
        versionCode = in.readString();
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

    @Override
    public Map<String, Object> toMap() {
        final Map<String, Object> data = new HashMap<>();
        data.put("model", model);
        data.put("serial", serial);
        data.put("buildId", buildId);
        data.put("manufacture", manufacture);
        data.put("brand", brand);
        data.put("type", type);
        data.put("user", user);
        data.put("base", base);
        data.put("incremental", incremental);
        data.put("sdk", sdk);
        data.put("board", board);
        data.put("host", host);
        data.put("versionCode", versionCode);
        return data;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "Device{" +
                "id='" + id + '\'' +
                ", serial='" + serial + '\'' +
                ", model='" + model + '\'' +
                ", buildId='" + buildId + '\'' +
                ", manufacture='" + manufacture + '\'' +
                ", brand='" + brand + '\'' +
                ", type='" + type + '\'' +
                ", user='" + user + '\'' +
                ", base=" + base +
                ", incremental='" + incremental + '\'' +
                ", sdk='" + sdk + '\'' +
                ", board='" + board + '\'' +
                ", host='" + host + '\'' +
                ", versionCode='" + versionCode + '\'' +
                '}';
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(serial);
        dest.writeString(model);
        dest.writeString(buildId);
        dest.writeString(manufacture);
        dest.writeString(brand);
        dest.writeString(type);
        dest.writeString(user);
        dest.writeInt(base);
        dest.writeString(incremental);
        dest.writeString(sdk);
        dest.writeString(board);
        dest.writeString(host);
        dest.writeString(versionCode);
    }
}
