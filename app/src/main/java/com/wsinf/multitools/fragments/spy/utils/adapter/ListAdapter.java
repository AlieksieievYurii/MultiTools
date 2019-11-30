package com.wsinf.multitools.fragments.spy.utils.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.wsinf.multitools.R;
import com.wsinf.multitools.fragments.spy.device.Device;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends SelectableListAdapter<Device> {


    public ListAdapter(Context context,
                       ListView listView,
                       List<Device> data) {
        super(context, listView, data);
    }

    @Override
    public void onCancelSelection() {
        showCheckBoxes(false);
        isSelectableMode = false;
    }

    @Override
    public void onSelect() {
        final List<Device> selectedDevices = new ArrayList<>();
        for (int i = 0; i < listView.getChildCount(); i++) {
            final View view = listView.getChildAt(i);
            final CheckBox checkBox = view.findViewById(R.id.cb_checkbox);
            if (checkBox.isChecked())
                selectedDevices.add(getItem(i));
        }
        if (onSelection != null)
            onSelection.onSelectedDevices(selectedDevices);
    }

    @Override
    public void onCheckAll() {
        for (int i = 0; i < listView.getChildCount(); i++) {
            final View view = listView.getChildAt(i);
            final CheckBox checkBox = view.findViewById(R.id.cb_checkbox);
            checkBox.setChecked(true);
        }
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Device getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Device device = getItem(position);

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.device_item, parent, false);

        final TextView tvModel = convertView.findViewById(R.id.tv_model);
        final TextView tvMacAddress = convertView.findViewById(R.id.tc_mac_address);
        final CheckBox checkBox = convertView.findViewById(R.id.cb_checkbox);

        tvModel.setText(device.getModel());
        tvMacAddress.setText(device.getMacAddress());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSelectableMode)
                    checkBox.setChecked(!checkBox.isChecked());
                else if (onSelection != null) {
                    final List<Device> singleList = new ArrayList<>();
                    singleList.add(device);
                    onSelection.onSelectedDevices(singleList);
                }

            }
        });

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (isSelectableMode)
                    return false;

                if (onSelection != null)
                    onSelection.onSelectableMode();

                isSelectableMode = true;
                showCheckBoxes(true);
                checkBox.setChecked(true);

                return true;
            }
        });

        return convertView;
    }

    private void showCheckBoxes(boolean show) {
        for (int i = 0; i < listView.getChildCount(); i++) {
            final View view = listView.getChildAt(i);
            final CheckBox checkBox = view.findViewById(R.id.cb_checkbox);
            checkBox.setChecked(false);
            checkBox.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }
}
