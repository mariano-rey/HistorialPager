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


class PerfilesAdapter extends RecyclerView.Adapter<PerfilesAdapter.ViewHolder> {
    private List<Historial> listaPerfiles;
    private int contadorGanados;
    private int contadorPerdidos;

    PerfilesAdapter(Rivales rivales, List<Historial> listaPerfiles) {
        this.listaPerfiles = listaPerfiles;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item1, parent, false);
        return new ViewHolder(itemView, parent.getContext());
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.nombre.setText(listaPerfiles.get(position).getRival());
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

            nombre = (TextView) itemView.findViewById(R.id.nombre);
            itemView.setOnClickListener(view -> {
                Intent rivalesHistorial = new Intent(context, RivalesHistorial.class);
                rivalesHistorial.putExtra("nombre", nombre.getText());
                context.startActivity(rivalesHistorial);
            });

            diferencia = (TextView) itemView.findViewById(R.id.diferencia);
            nombre.setTypeface(null, Typeface.BOLD);

            List<Partido> listaPartidos = Partido.partidos("nombre");
            for (Partido partido : listaPartidos) {
                if (partido.soyLocal() && partido.getGolesLocal() > partido.getGolesVisitante() || !partido.soyLocal() && partido.getGolesLocal() < partido.getGolesVisitante())
                    contadorGanados++;
                else if (partido.soyLocal() && partido.getGolesLocal() < partido.getGolesVisitante() || !partido.soyLocal() && partido.getGolesLocal() > partido.getGolesVisitante())
                    contadorPerdidos++;
            }
            diferencia.setText("Diferencia: " + (contadorGanados - contadorPerdidos));
        }
    }
}
