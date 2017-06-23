package lds.historialpager;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class RivalesHistorial extends AppCompatActivity {

    private List<Partido> listaPartidos;
    private RecyclerView recyclerView;
    private PartidosAdapter adapter;
    private String nombreRival;
    private int contadorGanados, contadorPerdidos, contadorEmpatados;
    private TextView partidosGanados, partidosEmpatados, partidosPerdidos, partidosJugados, diferencia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rivales_historial);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nombreRival = getIntent().getExtras().getString("nombre");
        setTitle(nombreRival);

        recyclerView = (RecyclerView) findViewById(R.id.rv_resultados);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        listaPartidos = Partido.partidos(nombreRival);

        adapter = new PartidosAdapter(this, listaPartidos);
        recyclerView.setAdapter(adapter);

        ContarPartidos();
    }

    private void ContarPartidos() {
        partidosJugados = (TextView) findViewById(R.id.partidosJugados);
        partidosGanados = (TextView) findViewById(R.id.partidosGanados);
        partidosPerdidos = (TextView) findViewById(R.id.partidosPerdidos);
        partidosEmpatados = (TextView) findViewById(R.id.partidosEmpatados);
        diferencia = (TextView) findViewById(R.id.diferenciaHistorialRivales);

        for (Partido partido : listaPartidos) {
            if (partido.soyLocal() && partido.getGolesLocal() > partido.getGolesVisitante() || !partido.soyLocal() && partido.getGolesLocal() < partido.getGolesVisitante())
                contadorGanados++;
            else if (partido.soyLocal() && partido.getGolesLocal() < partido.getGolesVisitante() || !partido.soyLocal() && partido.getGolesLocal() > partido.getGolesVisitante())
                contadorPerdidos++;
            else
                contadorEmpatados++;
        }

        partidosJugados.setText("Total Jugados: " + recyclerView.getAdapter().getItemCount());
        partidosGanados.setText("PG: " + contadorGanados);
        partidosPerdidos.setText("PP: " + contadorPerdidos);
        partidosEmpatados.setText("PE: " + contadorEmpatados);
        diferencia.setText("Diferencia: " + (contadorGanados - contadorPerdidos));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_rivales_historial, menu);

        MenuItem searchItem = menu.findItem(R.id.buscarPartido);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        SearchManager searchManager = (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.agregarPartido:
                agregarResultado();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

        golesL.setFocusableInTouchMode(true);
        golesL.setOnKeyListener((view, i, keyEvent) -> {
            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                if (golesL.getText().length() == 1) {
                    golesV.requestFocusFromTouch();
                }
            }
            return true;
        });

        alert.setCancelable(false);
        alert.setView(v);
        alert.setPositiveButton("Guardar", (dialogInterface, i) -> {
            String nombreLocal = equipoLocal.getText().toString();
            String gLocal = golesL.getText().toString();
            String nombreVisitante = equipoVisitante.getText().toString();
            String gVisitante = golesV.getText().toString();

            if (TextUtils.isEmpty(nombreLocal)) {
                Toast.makeText(this, "Ingrese Equipo Local", Toast.LENGTH_LONG).show();
                agregarResultado();
            } else if (TextUtils.isEmpty(nombreVisitante)) {
                Toast.makeText(this, "Ingrese Equipo Visitante", Toast.LENGTH_LONG).show();
                agregarResultado();
            } else {

                Historial historial = Historial.actual(nombreRival);

                Partido partido = new Partido(nombreLocal, nombreVisitante, (TextUtils.isEmpty(gLocal)) ? Integer.parseInt("0") : Integer.parseInt(gLocal), TextUtils.isEmpty(gVisitante) ? Integer.parseInt("0") : Integer.parseInt(gVisitante), swQuienSos.isChecked() ? Partido.QUIENES.Vistitante.ordinal() : Partido.QUIENES.Local.ordinal(), historial);
                partido.save();

                ActualizarLista();
                if (partido.soyLocal() && partido.getGolesLocal() > partido.getGolesVisitante() || !partido.soyLocal() && partido.getGolesLocal() < partido.getGolesVisitante()) {
                    contadorGanados++;
                    partidosGanados.setText("PG: " + contadorGanados);
                } else if (partido.soyLocal() && partido.getGolesLocal() < partido.getGolesVisitante() || !partido.soyLocal() && partido.getGolesLocal() > partido.getGolesVisitante()) {
                    contadorPerdidos++;
                    partidosPerdidos.setText("PG: " + contadorPerdidos);
                } else {
                    contadorEmpatados++;
                    partidosEmpatados.setText("PG: " + contadorEmpatados);
                }
                partidosJugados.setText("Total Jugados: " + recyclerView.getAdapter().getItemCount());
                diferencia.setText("Diferencia: " + (contadorGanados - contadorPerdidos));

            }

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        });

        alert.setNegativeButton("Cancelar", (dialogInterface, i) -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
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

            if (actual.getGolesLocal() > actual.getGolesVisitante()) {
                holder.golesLocal.setTypeface(null, Typeface.BOLD);
                holder.equipoLocal.setTypeface(null, Typeface.BOLD);
            } else if (actual.getGolesLocal() < actual.getGolesVisitante()) {
                holder.golesVisitante.setTypeface(null, Typeface.BOLD);
                holder.equipoVisitante.setTypeface(null, Typeface.BOLD);
            }
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
