package lds.historialpager;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import co.dift.ui.SwipeToAction;
import lds.historialpager.MainActivity;
import lds.historialpager.R;


class PerfilesAdapter extends RecyclerView.Adapter<PerfilesAdapter.ViewHolder> {
    private List<Historial> listaPerfiles;

    PerfilesAdapter(Rivales rivales, List<Historial> listaPerfiles) {
        this.listaPerfiles = listaPerfiles;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item1, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.nombre.setText(listaPerfiles.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return listaPerfiles.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombre;

        ViewHolder(View itemView) {
            super(itemView);
            nombre = (TextView) itemView.findViewById(R.id.nombre);
        }
    }
}
