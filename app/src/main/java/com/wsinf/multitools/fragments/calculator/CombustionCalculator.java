package com.wsinf.multitools.fragments.calculator;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.wsinf.multitools.R;

public class CombustionCalculator extends Fragment {

    private Context context;

    private EditText edtDistance;
    private EditText edtFuelConsumption;
    private EditText edtCostFuel;
    private Spinner spMetricDistance;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_combustion_calculator, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.edtDistance = view.findViewById(R.id.edt_distance);
        this.edtFuelConsumption = view.findViewById(R.id.edt_fuel_consumption);
        this.edtCostFuel = view.findViewById(R.id.edt_cost_fuel);
        this.spMetricDistance = view.findViewById(R.id.sp_distance_metric);

        this.spMetricDistance.setAdapter(new ArrayAdapter<>(this.context, android.R.layout.simple_spinner_item, DistanceMetricSystem.values()));

        final AppCompatButton btnCalculate = view.findViewById(R.id.btn_calculate);
        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculate();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private boolean isValidatedFields() {
        boolean validated = true;
        if (TextUtils.isEmpty(this.edtDistance.getText())) {
            this.edtDistance.setError(getResources().getString(R.string.error_empty));
            validated = false;
        }

        if (TextUtils.isEmpty(this.edtFuelConsumption.getText())) {
            this.edtFuelConsumption.setError(getResources().getString(R.string.error_empty));
            validated = false;
        }

        if (TextUtils.isEmpty(this.edtCostFuel.getText())) {
            this.edtCostFuel.setError(getResources().getString(R.string.error_empty));
            validated = false;
        }

        return validated;
    }

    private void calculate() {
        if (!isValidatedFields())
            return;

        final float distance = Float.parseFloat(this.edtDistance.getText().toString());
        final float fuelConsumption = Float.parseFloat(this.edtFuelConsumption.getText().toString());
        final float costFuel = Float.parseFloat(this.edtCostFuel.getText().toString());

        final CalculationModule calculationModule = CalculationModule.calculate(
                (DistanceMetricSystem) this.spMetricDistance.getSelectedItem(),
                distance, fuelConsumption, costFuel);

        ResultDialog resultDialog = new ResultDialog(this.context, calculationModule);
        resultDialog.show();

    }
}
