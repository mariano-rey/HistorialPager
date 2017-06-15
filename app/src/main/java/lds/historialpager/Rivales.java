package lds.historialpager;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;


public class Rivales extends Fragment {
    View rootView;

    private List<Historial> listaPerfiles;
    private PerfilesAdapter adapter;
    private RecyclerView rV1;


    public Rivales() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_rivales, container, false);
        setHasOptionsMenu(true);

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


        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem searchItem = menu.findItem(R.id.buscarRival);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        SearchManager searchManager = (SearchManager) getContext().getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.agregarRival:
                agregarRival();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void agregarRival() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

        final EditText edittext = new EditText(getContext());
        edittext.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        alert.setTitle("Nuevo Rival");
        alert.setMessage("Ingrese Nombre");
        alert.setView(edittext);
        alert.setCancelable(false);
        alert.setPositiveButton("Guardar", (dialogInterface, i) -> {
            String respuesta = edittext.getText().toString();
            if (TextUtils.isEmpty(respuesta)) {
                Snackbar snackbar = Snackbar.make(rV1, "Ingrese Nombre del Rival", Snackbar.LENGTH_LONG);
                snackbar.setAction("Reintentar", v -> agregarRival());
                snackbar.show();

            } else {

                Historial historial = new Historial(respuesta);
                historial.save();

                ActualizarLista();

                Toast.makeText(getContext(), "Rival Guardado", Toast.LENGTH_LONG).show();

            }
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        });
        alert.setNegativeButton("Cancelar", (dialogInterface, i) -> {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        });

        alert.show();
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    private void ActualizarLista() {
        listaPerfiles.clear();
        listaPerfiles.addAll(Historial.perfiles());
        adapter.notifyDataSetChanged();
    }
}
