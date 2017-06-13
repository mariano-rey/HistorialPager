package lds.historialpager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {

    private static final String HISTORIAL = "Historial";
    private static final String PERFIL = "Perfil";
    private ViewPager viewPager;
    private SharedPreferences memoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        viewPager.setAdapter(new SectionPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        memoria = getSharedPreferences(HISTORIAL, MODE_PRIVATE);

        boolean existeUsuario = memoria.getString(PERFIL, null) != null;
        if (!existeUsuario)
            dialogPedirNombre();

    }


    private void dialogPedirNombre() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        final EditText edittext = new EditText(this);
        edittext.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        alert.setTitle("Nuevo Usuario");
        alert.setMessage("Ingrese Nombre: ");
        alert.setView(edittext);
        alert.setCancelable(false);
        alert.setPositiveButton("Guardar", (dialog, whichButton) -> {
            String respuesta = edittext.getText().toString();
            if (TextUtils.isEmpty(respuesta)) {
                Snackbar snackbar = Snackbar.make(viewPager, "Ingrese Nombre de Usuario", Snackbar.LENGTH_LONG);
                snackbar.show();
                dialogPedirNombre();
            } else {
                memoria.edit().putString(PERFIL, respuesta).apply();
            }
        });

        alert.show();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public class SectionPagerAdapter extends FragmentPagerAdapter {
        SectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new Rivales();
                case 1:
                    return new Torneos();
                default:
                    return new Rivales();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Rivales";
                case 1:
                    return "Torneos";
                default:
                    return "Lo que sea";
            }
        }
    }
}
