package com.wsinf.multitools.fragments.wifi;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.ScanResult;
import android.view.View;
import android.widget.EditText;

import com.wsinf.multitools.R;

interface CallBack {
    void onConnect(final ScanResult scanResult, final String password);
}

class ConnectionDialog extends AlertDialog {

    private ScanResult scanResult;
    private CallBack callBack;
    private EditText edtPassword;


    ConnectionDialog(Context context, ScanResult scanResult, CallBack callBack) {
        super(context);

        this.scanResult = scanResult;
        this.callBack = callBack;

        init();
    }

    private void init() {
        @SuppressLint("InflateParams") final View view = getLayoutInflater().inflate(R.layout.dialog_password, null);

        edtPassword = view.findViewById(R.id.edt_password);

        setView(view);
        setTitle(scanResult.SSID);
        setButton(BUTTON_POSITIVE, "Connect", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callBack.onConnect(scanResult, edtPassword.getText().toString());
            }
        });

        setButton(BUTTON_NEGATIVE, "Cancel", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
    }

}
