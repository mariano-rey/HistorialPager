package lds.historialpager;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.List;


public class Rivales extends Fragment {
    View rootView;

    private RecyclerView rV1;
    private FloatingActionButton agregarperfil;

    private List<Historial> listaPerfiles;
    private PerfilesAdapter adapter;

    public Rivales() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_rivales, container, false);

        listaPerfiles = Historial.perfiles();

        rV1 = (RecyclerView) rootView.findViewById(R.id.rV1);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rV1.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rV1.getContext(), linearLayoutManager.getOrientation());
        rV1.addItemDecoration(dividerItemDecoration);
        rV1.setHasFixedSize(true);
        adapter = new PerfilesAdapter(this, listaPerfiles);
        rV1.setAdapter(adapter);

        agregarperfil = (FloatingActionButton) rootView.findViewById(R.id.agregarPerfil);
        agregarperfil.setOnClickListener(view -> agregarRival());

        // HAY QUE PENSAR
        // COMO ELIMINAR LOS RIVALES
        // UN DISEÑO PIOLA PARA EL RECYCLER

        rV1.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    agregarperfil.hide();
                } else {
                    agregarperfil.show();
                }

                super.onScrolled(recyclerView, dx, dy);
            }
        });



        return rootView;
    }

    private void agregarRival() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        final EditText edittext = new EditText(getActivity());
        edittext.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        alert.setTitle("Nuevo Rival");
        alert.setMessage("Ingrese Nombre");
        alert.setView(edittext);
        alert.setPositiveButton("Guardar", (dialogInterface, i) -> {
            String respuesta = edittext.getText().toString();
            if (TextUtils.isEmpty(respuesta)) {
                Snackbar snackbar = Snackbar.make(rV1, "Completa gato!", Snackbar.LENGTH_LONG);
                snackbar.setAction("Reintentar", view -> agregarRival());
                snackbar.show();

            } else {

                Historial historial = new Historial(respuesta);
                historial.save();

                ActualizarLista();
            }
        });
        alert.show();
    }

    private void ActualizarLista() {
        listaPerfiles.clear();
        listaPerfiles.addAll(Historial.perfiles());
        adapter.notifyDataSetChanged();
    }
}
