package com.example.t1bicing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class LlistaActivity extends AppCompatActivity implements AdaptadorEstacion.OnItemClickListener{


    private RecyclerView recyclerView;

    private AdaptadorEstacion adaptadorEstacion;

    private List<Estacion> estaciones;

    private FloatingActionButton buttonViewMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llista);

        recyclerView = findViewById(R.id.recyclerView);
        buttonViewMap = this.findViewById(R.id.floatingActionButtonMap);

        Intent intent = getIntent();
        estaciones = (List<Estacion>) intent.getSerializableExtra("estaciones");

        configRecicleView();

        buttonViewMap.setOnClickListener(view -> {
            this.finish();
        });
    }

    @Override
    public void onItemClick(View view, int elementPosition) {
        Intent intent = new Intent(this, MasInfoActivity.class);
        Estacion estacion = this.adaptadorEstacion.getEstacionPosition(elementPosition);
        intent.putExtra("estacion", estacion);
        startActivity(intent);
    }

    public void configRecicleView() {
        adaptadorEstacion = new AdaptadorEstacion(estaciones);
        adaptadorEstacion.setOnItemClickListener(this);
        recyclerView.setAdapter(adaptadorEstacion);
        //configButoonView(estatButton);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


}