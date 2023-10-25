package com.example.t1bicing;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.text.SimpleDateFormat;
import java.util.List;

public class AdaptadorEstacion extends RecyclerView.Adapter<AdaptadorEstacion.ViewHolder> {

    private List<Estacion> estaciones;
    private OnItemClickListener listener;

    public AdaptadorEstacion(List<Estacion> estaciones) {
        this.estaciones = estaciones;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public AdaptadorEstacion.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_estacion, parent, false);
//        return new ViewHolder(view, listener);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    public void setNotas(List<Estacion> notas) {
        this.estaciones = notas;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Estacion estacion = estaciones.get(position);


        if (!estacion.getEstadoEstacion().getStatus().equals("IN_SERVICE")){
            holder.textViewNombre.setBackgroundColor(Color.parseColor("#E0115F"));
            holder.textViewEstado.setBackgroundColor(Color.parseColor("#2b2b2b"));
            holder.textViewContenido.setBackgroundColor(Color.parseColor("#E0115F"));
        }
        else if (estacion.isFavorito()) {
            holder.textViewNombre.setBackgroundColor(Color.parseColor("#FFFF00"));
            holder.textViewEstado.setBackgroundColor(Color.parseColor("#FFFF00"));
            holder.textViewContenido.setBackgroundColor(Color.parseColor("#FFFF00"));
        } else {
            holder.textViewNombre.setBackgroundColor(Color.parseColor("#E0115F"));
            holder.textViewEstado.setBackgroundColor(Color.parseColor("#E0115F"));
            holder.textViewContenido.setBackgroundColor(Color.parseColor("#E0115F"));
        }

        holder.textViewNombre.setText(estacion.getNombre());
        holder.textViewEstado.setText(estacion.getEstadoEstacion().getStatus());
        holder.textViewContenido.setText(estacion.getInfoNota());
    }

    public Estacion getEstacionPosition(int elementPosition){return this.estaciones.get(elementPosition);}

    @Override
    public int getItemCount() {
        return estaciones.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNombre;
        TextView textViewEstado;
        TextView textViewContenido;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNombre = itemView.findViewById(R.id.textViewNombreNota);
            textViewEstado = itemView.findViewById(R.id.textViewEstadoNota);
            textViewContenido = itemView.findViewById(R.id.textViewContinidoNota);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (listener != null) {
//                        int position = getAdapterPosition();
//                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(v, getAdapterPosition());
//                        }
//                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int elementPosition);
    }



}


