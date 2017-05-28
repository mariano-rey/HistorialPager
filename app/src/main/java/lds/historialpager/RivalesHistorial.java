package lds.historialpager;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RivalesHistorial extends AppCompatActivity {

    private List<Partido> listaPartidos = new ArrayList<>();
    private ResultadosAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rivales_historial);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_resultados);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ResultadosAdapter(this, listaPartidos);
        recyclerView.setAdapter(adapter);

        FloatingActionButton agregarResultado = (FloatingActionButton) findViewById(R.id.agregarPartido);
        agregarResultado.setOnClickListener(view -> agregarResultado());


    }

    private void agregarResultado() {
        View v = View.inflate(this, R.layout.dialog_resultado, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText editText = new EditText(this);
        final EditText editText1 = new EditText(this);
        final EditText editText2 = new EditText(this);
        final EditText editText3 = new EditText(this);
        alert.setView(v);
        alert.setPositiveButton("Guardar", (dialogInterface, i) -> {
                    String nombreLocal = editText.getText().toString();
                    String nombreVisitante = editText1.getText().toString();
                    String golesLocal = editText2.getText().toString();
                    String golesVisitante = editText3.getText().toString();

                    //ARREGLAR ESTO!!!

                    Partido partido = new Partido(nombreLocal, nombreVisitante, golesLocal, golesVisitante, null);
                    partido.save();

                    ActualizarLista();
                }
        );

        alert.show();
    }

    private void ActualizarLista() {
        listaPartidos.clear();
        listaPartidos.addAll(Partido.partidos());
        adapter.notifyDataSetChanged();
    }

    private class ResultadosAdapter extends RecyclerView.Adapter<ResultadosViewHolder> {

        private List<Partido> listaPartidos;

        ResultadosAdapter(RivalesHistorial rivalesHistorial, List<Partido> listaPartidos) {
            this.listaPartidos = listaPartidos;
        }

        @Override
        public ResultadosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_resultados, parent, false);
            return new ResultadosViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ResultadosViewHolder holder, int position) {
            holder.equipoLocal.setText(listaPartidos.get(position).getEquipoLocal());
            holder.golesLocal.setText(listaPartidos.get(position).getGolesLocal());
            holder.golesVisitante.setText(listaPartidos.get(position).getGolesVisitante());
            holder.equipoVisitante.setText(listaPartidos.get(position).getEquipoVisitante());
        }

        @Override
        public int getItemCount() {
            return listaPartidos.size();
        }
    }

    private class ResultadosViewHolder extends RecyclerView.ViewHolder {
        TextView equipoLocal, golesLocal, golesVisitante, equipoVisitante;

        ResultadosViewHolder(View itemView) {
            super(itemView);

            equipoLocal = (TextView) itemView.findViewById(R.id.textView1);
            golesLocal = (TextView) itemView.findViewById(R.id.textView2);
            golesVisitante = (TextView) itemView.findViewById(R.id.textView3);
            equipoVisitante = (TextView) itemView.findViewById(R.id.textView4);
        }
    }

}
