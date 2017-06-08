package lds.historialpager;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bignerdranch.android.multiselector.ModalMultiSelectorCallback;
import com.bignerdranch.android.multiselector.MultiSelector;
import com.bignerdranch.android.multiselector.SwappingHolder;

import java.util.List;


class PerfilesAdapter extends RecyclerView.Adapter<PerfilesAdapter.ViewHolder> {
    private List<Historial> listaPerfiles;
    private MultiSelector multiSelector = new MultiSelector();
    private ModalMultiSelectorCallback multiSelectorCallback = new ModalMultiSelectorCallback(multiSelector) {
        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            multiSelectorCallback.onCreateActionMode(actionMode, menu);
            MenuInflater inflater = actionMode.getMenuInflater();
            inflater.inflate(R.menu.menu_delete, menu);
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if (item.getItemId() == R.id.borrarRival) {
                mode.finish();

                for (int i = listaPerfiles.size(); i >= 0; i--) {
                    if (multiSelector.isSelected(i, 0)) {
                        // remove item from list
                        Historial historial = Historial.load(Historial.class, 1);
                        historial.delete();
                        listaPerfiles.clear();
                        listaPerfiles.addAll(Historial.perfiles());
                        notifyDataSetChanged();
                        notifyItemRemoved(i);
                    }
                }

                multiSelector.clearSelections();
                return true;
            }
            return false;
        }
    };

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

    class ViewHolder extends SwappingHolder {
        TextView nombre;

        ViewHolder(View itemView, Context context) {
            super(itemView, multiSelector);
            itemView.setLongClickable(true);

            itemView.setOnLongClickListener(view -> {
                if (!multiSelector.isSelectable()) {
                    ((AppCompatActivity) context).startSupportActionMode(multiSelectorCallback);
                    multiSelector.setSelectable(true);
                    multiSelector.setSelected(ViewHolder.this, true);
                    return true;
                }
                return false;
            });

            nombre = (TextView) itemView.findViewById(R.id.nombre);
            itemView.setOnClickListener(view -> {
                if (!multiSelector.tapSelection(ViewHolder.this)) {
                    Intent rivalesHistorial = new Intent(context, RivalesHistorial.class);
                    rivalesHistorial.putExtra("nombre", nombre.getText());
                    context.startActivity(rivalesHistorial);
                }
            });

//            borrar.setOnClickListener(view -> {
//                new Delete().from(Historial.class).where("rival = ?", nombre.getText()).execute();
//                listaPerfiles.clear();
//                listaPerfiles.addAll(Historial.perfiles());
//                notifyDataSetChanged();
//            });
        }
    }
}
