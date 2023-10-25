package com.example.t1bicing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;

public class MasInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mas_info);

        TextView textViewtitol = this.findViewById(R.id.textViewTitolMasInfo);
        TextView textViewInfo = this.findViewById(R.id.textViewMasInfo);
        Button button = this.findViewById(R.id.buttonAtras);

        Intent intent = getIntent();
        Estacion estacion = (Estacion) intent.getSerializableExtra("estacion");

        textViewtitol.setText(estacion.getNombre());
        textViewInfo.setText(estacion.stringEstacion());

        button.setOnClickListener(view -> {
            this.finish();
        });
    }
}