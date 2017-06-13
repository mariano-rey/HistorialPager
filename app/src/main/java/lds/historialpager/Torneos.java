package lds.historialpager;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


public class Torneos extends Fragment {
    private View rootView;

    private RecyclerView rV2;


    public Torneos() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_torneos, container, false);
        setHasOptionsMenu(true);

        rV2 = (RecyclerView) rootView.findViewById(R.id.rV2);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rV2.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rV2.getContext(), linearLayoutManager.getOrientation());
        rV2.addItemDecoration(dividerItemDecoration);
        rV2.setHasFixedSize(true);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_torneos, menu);
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem searchItem = menu.findItem(R.id.buscarTorneo);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        SearchManager searchManager = (SearchManager) getContext().getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.agregarTorneo:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
