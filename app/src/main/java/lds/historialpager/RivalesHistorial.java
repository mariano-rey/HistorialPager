package lds.historialpager;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

public class RivalesHistorial extends AppCompatActivity {

    private List<Partido> listaPartidos;
    private RecyclerView recyclerView;
    private PartidosAdapter adapter;
    private String nombreRival;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rivales_historial);

        nombreRival = getIntent().getExtras().getString("nombre");

        recyclerView = (RecyclerView) findViewById(R.id.rv_resultados);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        listaPartidos = Partido.partidos(nombreRival);

        adapter = new PartidosAdapter(this, listaPartidos);
        recyclerView.setAdapter(adapter);

        FloatingActionButton agregarResultado = (FloatingActionButton) findViewById(R.id.agregarPartido);
        agregarResultado.setOnClickListener(view -> agregarResultado());


    }


    private void agregarResultado() {
        View v = View.inflate(this, R.layout.dialog_resultado, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        EditText equipoLocal = (EditText) v.findViewById(R.id.equipoLocal);
        equipoLocal.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        EditText golesL = (EditText) v.findViewById(R.id.golesLocal);
        EditText golesV = (EditText) v.findViewById(R.id.golesVisitante);
        EditText equipoVisitante = (EditText) v.findViewById(R.id.equipoVisitante);
        equipoVisitante.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        Switch swQuienSos = (Switch) v.findViewById(R.id.switch1);


        golesL.setOnKeyListener((view, i, keyEvent) -> {
            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                if (golesL.getText().length() == 1) {
                    if (i == KeyEvent.KEYCODE_NUM)
                        golesV.requestFocus();
                }
            }
            return true;
        });


        alert.setView(v);
        alert.setPositiveButton("Guardar", (dialogInterface, i) -> {
            String nombreLocal = equipoLocal.getText().toString();
            String gLocal = golesL.getText().toString();
            if (TextUtils.isEmpty(gLocal)) {
                Snackbar snackbar = Snackbar.make(recyclerView, "Ingrese Goles Local", Snackbar.LENGTH_LONG);
                snackbar.show();
                agregarResultado();
            }
            int golesLocal = Integer.parseInt((gLocal));
            String gVisitante = golesV.getText().toString();
            if (TextUtils.isEmpty(gVisitante)) {
                Snackbar snackbar = Snackbar.make(recyclerView, "Ingrese Goles Visitante", Snackbar.LENGTH_LONG);
                snackbar.show();
                agregarResultado();
            }
            int golesVisitante = Integer.parseInt(gVisitante);
            String nombreVisitante = equipoVisitante.getText().toString();

            Historial historial = Historial.actual(nombreRival);

            if (TextUtils.isEmpty(nombreLocal)) {
                Snackbar snackbar = Snackbar.make(recyclerView, "Ingrese Equipo Local", Snackbar.LENGTH_LONG);
                snackbar.show();
                agregarResultado();

            } else if (TextUtils.isEmpty(nombreVisitante)) {
                Snackbar snackbar = Snackbar.make(recyclerView, "Ingrese Equipo Visitante", Snackbar.LENGTH_LONG);
                snackbar.show();
                agregarResultado();
            } else {

                Partido partido = new Partido(nombreLocal, nombreVisitante, golesLocal, golesVisitante, swQuienSos.isChecked() ? Partido.QUIENES.Vistitante.ordinal() : Partido.QUIENES.Local.ordinal(), historial);
                partido.save();

                ActualizarLista();
            }
        });

        alert.show();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    private void ActualizarLista() {
        listaPartidos.clear();
        listaPartidos.addAll(Partido.partidos(nombreRival));
        adapter.notifyDataSetChanged();
    }

    private class PartidosAdapter extends RecyclerView.Adapter<PartidosViewHolder> {

        private List<Partido> listaPartidos;

        PartidosAdapter(RivalesHistorial rivalesHistorial, List<Partido> listaPartidos) {
            this.listaPartidos = listaPartidos;
        }

        @Override
        public PartidosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_resultados, parent, false);
            return new PartidosViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(PartidosViewHolder holder, int position) {

            Partido actual = listaPartidos.get(position);

            holder.equipoLocal.setText(actual.getEquipoLocal());
            holder.golesLocal.setText(String.valueOf(actual.getGolesLocal()));
            holder.golesVisitante.setText(String.valueOf(actual.getGolesVisitante()));
            holder.equipoVisitante.setText(actual.getEquipoVisitante());

            Drawable punto = AppCompatResources.getDrawable(getApplicationContext(), R.drawable.ic_yo);
            if (actual.soyLocal())
                holder.equipoLocal.setCompoundDrawablesWithIntrinsicBounds(punto, null, null, null);
            else
                holder.equipoVisitante.setCompoundDrawablesWithIntrinsicBounds(null, null, punto, null);

        }

        @Override
        public int getItemCount() {
            return listaPartidos.size();
        }
    }

    private class PartidosViewHolder extends RecyclerView.ViewHolder {
        TextView equipoLocal, golesLocal, golesVisitante, equipoVisitante;

        PartidosViewHolder(View itemView) {
            super(itemView);

            equipoLocal = (TextView) itemView.findViewById(R.id.textView1);
            golesLocal = (TextView) itemView.findViewById(R.id.textView2);
            golesVisitante = (TextView) itemView.findViewById(R.id.textView3);
            equipoVisitante = (TextView) itemView.findViewById(R.id.textView4);
        }
    }

}
