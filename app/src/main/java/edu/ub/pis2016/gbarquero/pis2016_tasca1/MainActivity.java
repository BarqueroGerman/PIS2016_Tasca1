package edu.ub.pis2016.gbarquero.pis2016_tasca1;

import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private int from = 0;
    private int to = 1;

    private double[][] exchangeValues = {
            {1, 1.059}, //euro
            {1./1.059, 1} //dollar
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        updateExchangeValueText();

        EditText from_txt = (EditText) findViewById(R.id.from_value);
        from_txt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateValues();
            }
        });

        final EditText exchange_value = (EditText) findViewById(R.id.exchange_value);
        exchange_value.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String newValueStr = s.toString();
                double newValue = 0;
                if(newValueStr != null && !newValueStr.equals("")){
                    newValue = Double.parseDouble(newValueStr);
                }
                exchangeValues[from][to] = newValue;
                exchangeValues[to][from] = 1/newValue;
                updateValues();
            }
        });

        EditText feesText = (EditText) findViewById(R.id.fees_percentage);
        feesText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateValues();
            }
        });

        SensorEventListener m_sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                feesCheckChange((CheckBox) findViewById(R.id.fees_checkbox));
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {}
        };

        SensorManager sm = (SensorManager)getSystemService(SENSOR_SERVICE);
        sm.registerListener(m_sensorEventListener, sm.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR), SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void updateValues(){
        EditText toTxt = (EditText) findViewById(R.id.to_value);
        double toValue = getResult();
        toTxt.setText(getRoundedString(toValue,2));
    }

    private void updateExchangeValueText(){
        EditText editText = (EditText)findViewById(R.id.exchange_value);
        editText.setText(getRoundedString(exchangeValues[from][to], 3));
    }

    private void updateCurrenciesDisplay(){
        TextView fromTxt = (TextView) findViewById(R.id.from_txt);
        TextView toTxt = (TextView) findViewById(R.id.to_txt);
        fromTxt.setText(Currencies.getFullStringId(from));
        toTxt.setText(Currencies.getFullStringId(to));
    }

    private double getResult(){
        EditText fromTxt = (EditText) findViewById(R.id.from_value);
        String fromValueStr = fromTxt.getText().toString();
        double fromValue = 0;
        if(fromValueStr != null && !fromValueStr.equals("")){
            fromValue = Double.parseDouble(fromValueStr);
        }
        double result = getEquivalenceWithoutFees(fromValue);
        return applyFees(result);
    }

    private double getEquivalenceWithoutFees(double fromValue){
        return fromValue*exchangeValues[from][to];
    }

    private double applyFees(double fromValue){
        CheckBox checkbox = (CheckBox) findViewById(R.id.fees_checkbox);
        EditText edittext = (EditText) findViewById(R.id.fees_percentage);
        if(checkbox.isChecked()){
            double percentage = 0;
            String percentageStr = edittext.getText().toString();
            if(percentageStr != null && !percentageStr.equals("")){
                percentage = Double.parseDouble(percentageStr);
            }
            return fromValue - fromValue * (percentage / 100);
        }
        return fromValue;
    }

    public void reverseCurrencies(View v){
        int aux = from;
        from = to;
        to = aux;

        // Ara actualitzem els textos de l'aplicacio
        updateCurrenciesDisplay();
        updateValues();
        updateExchangeValueText();
    }

    public void feesCheckChange(View v){
        CheckBox checkbox = (CheckBox) v;
        EditText edittext = (EditText) findViewById(R.id.fees_percentage);
        if(checkbox.isChecked()){
            edittext.setVisibility(View.VISIBLE);
        }else{
            edittext.setVisibility(View.GONE);
        }
        updateValues();
    }

    public String getRoundedString(double value, int decimals){
        if (decimals < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, decimals);
        value = value * factor;
        long tmp = Math.round(value);
        double rounded = (double) tmp / factor;
        return Double.toString(rounded);
    }
}
