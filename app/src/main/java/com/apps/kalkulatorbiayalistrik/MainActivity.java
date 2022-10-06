package com.apps.kalkulatorbiayalistrik;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;


public class MainActivity extends AppCompatActivity{

    private EditText etWatt,etJam;
    private TextView tvBiayaListrikHariAmount;
    private TextView tvBiayaListrikBulanAmount;
    private TextView tvBiayaListrikTahunAmount;
    private Spinner spinnerGolongan, spinnerDayaListrik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etWatt = findViewById(R.id.etWatt);
        etJam = findViewById(R.id.etJam);
        tvBiayaListrikHariAmount = findViewById(R.id.tvBiayaListrikHariAmount);
        tvBiayaListrikBulanAmount = findViewById(R.id.tvBiayaListrikBulanAmount);
        tvBiayaListrikTahunAmount = findViewById(R.id.tvBiayaListrikTahunAmount);
        spinnerGolongan = findViewById(R.id.spinnerGolongan);
        spinnerDayaListrik = findViewById(R.id.spinnerDayaListrik);

        ArrayAdapter<CharSequence> adapterGol = ArrayAdapter.createFromResource(this,
        R.array.array_golongan, android.R.layout.simple_spinner_item);
        adapterGol.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGolongan.setAdapter(adapterGol);

        spinnerGolongan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                if(pos == 0){
                    ArrayAdapter<CharSequence> adapterDaya= ArrayAdapter.createFromResource(getApplicationContext(),
                            R.array.daya_rumah_tangga, android.R.layout.simple_spinner_item);
                    adapterDaya.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerDayaListrik.setAdapter(adapterDaya);

                }
                else if(pos == 1){
                    ArrayAdapter<CharSequence> adapterDaya= ArrayAdapter.createFromResource(getApplicationContext(),
                            R.array.daya_bisnis, android.R.layout.simple_spinner_item);
                    adapterDaya.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerDayaListrik.setAdapter(adapterDaya);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

       spinnerDayaListrik.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
               int posDaya = spinnerDayaListrik.getSelectedItemPosition();
               int posGolongan = spinnerGolongan.getSelectedItemPosition();
               computeBiayaListrik(posDaya, posGolongan);
           }

           @Override
           public void onNothingSelected(AdapterView<?> adapterView) {

           }
       });

    }

    public void computeBiayaListrik(int posDaya, int posGolongan) {

        String Watt = etWatt.getText().toString();
        String Jam = etJam.getText().toString();

        if(TextUtils.isEmpty(Watt) || TextUtils.isEmpty(Jam)){
            tvBiayaListrikHariAmount.setText("");
            tvBiayaListrikBulanAmount.setText("");
            tvBiayaListrikTahunAmount.setText("");
        }

        final double[] biayaKWHRumahTangga = { 169, 274, 1352, 1444.70 };
        final double[] biayaKWHBisnis = { 254, 420, 966, 1100, 1444.70, 1114,74};
        int wattNum = 0;
        int jamNum = 0;
        double daya = 0;

        try {
            wattNum = Integer.parseInt(etWatt.getText().toString());
            jamNum = Integer.parseInt(etJam.getText().toString());
        }
        catch(NumberFormatException e){
            wattNum = 0;
            jamNum = 0;
        }

        if(posGolongan == 0){
            switch(posDaya){
                case 0:
                    daya = biayaKWHRumahTangga[0];
                    break;
                case 1:
                    daya = biayaKWHRumahTangga[1];
                    break;
                case 2:
                    daya = biayaKWHRumahTangga[2];
                    break;
                default:
                    daya = biayaKWHRumahTangga[3];
                    break;
            }
        }

        else if(posGolongan == 1){
            switch(posDaya){
                case 0:
                    daya = biayaKWHBisnis[0];
                    break;
                case 1:
                    daya = biayaKWHBisnis[1];
                    break;
                case 2:
                    daya = biayaKWHBisnis[2];
                    break;
                case 3:
                    daya = biayaKWHBisnis[3];
                    break;
                case 5:
                    daya = biayaKWHBisnis[5];
                    break;
                default:
                    daya = biayaKWHBisnis[4];
                    break;

            }
        }
        double biayaHarian = (double) (wattNum * jamNum)/1000 * daya;
        double biayaBulanan = biayaHarian * 30;
        double biayaTahunan = biayaBulanan * 12;


        NumberFormat format = NumberFormat.getInstance(new Locale("in","ID"));
        format.setMaximumFractionDigits(2);

        tvBiayaListrikHariAmount.setText("Rp" + format.format(biayaHarian));
        tvBiayaListrikBulanAmount.setText("Rp" + format.format(biayaBulanan));
        tvBiayaListrikTahunAmount.setText("Rp" + format.format(biayaTahunan));
    }

}
