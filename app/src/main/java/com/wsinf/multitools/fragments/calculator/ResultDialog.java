package com.wsinf.multitools.fragments.calculator;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.wsinf.multitools.R;

class ResultDialog extends Dialog {

    private CalculationModule calculationModule;

    ResultDialog(@NonNull Context context, CalculationModule calculationModule) {
        super(context);
        this.calculationModule = calculationModule;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_results);

        final TextView tvDistance = findViewById(R.id.tv_distance);
        final TextView tvMetricSystem = findViewById(R.id.tv_distance_metric_system);
        final TextView tvConsumption = findViewById(R.id.tv_consumption);
        final TextView tvCostFuel = findViewById(R.id.tv_cost_fuel);
        final TextView tvFuelSpent = findViewById(R.id.tv_fuel_spent);
        final TextView tvMoneySpent = findViewById(R.id.tv_money_spent);

        tvMetricSystem.setText(this.calculationModule.getDistanceMetricSystem().toString());
        tvDistance.setText(String.valueOf(this.calculationModule.getDistance()));
        tvConsumption.setText(String.valueOf(this.calculationModule.getFuelConsumption()));
        tvCostFuel.setText(String.valueOf(this.calculationModule.getCostFuel()));
        tvFuelSpent.setText(String.valueOf(this.calculationModule.getFuelSpent()));
        tvMoneySpent.setText(String.valueOf(this.calculationModule.getMoneySpent()));

    }


}
