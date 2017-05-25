package lds.historialpager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RivalesHistorial extends AppCompatActivity {

    private List<Resultados> listaResultados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rivales_historial);

        listaResultados = new ArrayList<>();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_resultados);
        recyclerView.setAdapter(new ResultadosAdapter(this, listaResultados));

    }

    private class ResultadosAdapter extends RecyclerView.Adapter<ResultadosViewHolder> {

        private List<Resultados> listaResultados;

        ResultadosAdapter(RivalesHistorial rivalesHistorial, List<Resultados> listaResultados) {
            this.listaResultados = listaResultados;

            listaResultados.add(new Resultados("Lanus", "Racing", "2", "0"));
        }

        @Override
        public ResultadosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_resultados, parent, false);
            return new ResultadosViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ResultadosViewHolder holder, int position) {
            holder.textView1.setText(listaResultados.get(position).toString());
            holder.textView2.setText(listaResultados.get(position).toString());
            holder.textView3.setText(listaResultados.get(position).toString());
            holder.textView4.setText(listaResultados.get(position).toString());
        }

        @Override
        public int getItemCount() {
            return listaResultados.size();
        }
    }

    private class ResultadosViewHolder extends RecyclerView.ViewHolder {
        TextView textView1, textView2, textView3, textView4;

        ResultadosViewHolder(View itemView) {
            super(itemView);

            textView1 = (TextView) itemView.findViewById(R.id.textView1);
            textView2 = (TextView) itemView.findViewById(R.id.textView2);
            textView3 = (TextView) itemView.findViewById(R.id.textView3);
            textView4 = (TextView) itemView.findViewById(R.id.textView4);
        }
    }

}
