package lds.historialpager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


class RivalesAdapter extends RecyclerView.Adapter<RivalesAdapter.ViewHolder> {
    private List<Historial> listaPerfiles;

    RivalesAdapter(Rivales rivales, List<Historial> listaPerfiles) {
        this.listaPerfiles = listaPerfiles;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rivales, parent, false);
        return new ViewHolder(itemView, parent.getContext());
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.nombre.setText(listaPerfiles.get(position).getRival());
        List<Partido> listaPartidos = Partido.partidos(listaPerfiles.get(position).getRival());
        int contadorGanados = 0;
        int contadorPerdidos = 0;
        for (Partido partido : listaPartidos) {
            if (partido.soyLocal() && partido.getGolesLocal() > partido.getGolesVisitante() || !partido.soyLocal() && partido.getGolesLocal() < partido.getGolesVisitante())
                contadorGanados++;
            else if (partido.soyLocal() && partido.getGolesLocal() < partido.getGolesVisitante() || !partido.soyLocal() && partido.getGolesLocal() > partido.getGolesVisitante())
                contadorPerdidos++;
        }

        int dif = contadorGanados - contadorPerdidos;
        holder.diferencia.setText("Diferencia: " + dif);
    }

    @Override
    public int getItemCount() {
        return listaPerfiles.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombre;
        TextView diferencia;

        ViewHolder(View itemView, Context context) {
            super(itemView);

            diferencia = (TextView) itemView.findViewById(R.id.diferencia);
            nombre = (TextView) itemView.findViewById(R.id.nombre);
            nombre.setTypeface(null, Typeface.BOLD);
            itemView.setOnClickListener(view -> {
                Intent rivalesHistorial = new Intent(context, RivalesHistorial.class);
                rivalesHistorial.putExtra("nombre", nombre.getText());
                context.startActivity(rivalesHistorial);
            });
        }
    }
}
