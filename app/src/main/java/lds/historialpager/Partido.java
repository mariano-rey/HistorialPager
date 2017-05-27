package lds.historialpager;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by Tano on 28/4/2017.
 * No Fue Magia
 */

@Table(name = "Partido", id = "_id")
public class Partido extends Model {

    @Column(name = "Local")
    public String Local;

    @Column(name = "Visitante")
    public String Visitante;

    @Column(name = "GolesLocal")
    public String GolesLocal;

    @Column(name = "GolesVisitante")
    public String GolesVisitante;

    @Column(name = "historial")
    public Historial historial;


    public Partido() {
    }

    public static List<Partido> partidos() {
        return new Select()
                .from(Partido.class)
                .execute();
    }

    public Partido(String local, String visitante, String golesLocal, String golesVisitante, Historial historial) {
        this.Local = local;
        this.Visitante = visitante;
        this.GolesLocal = golesLocal;
        this.GolesVisitante = golesVisitante;
        this.historial = historial;
    }

    String getEquipoLocal() {
        return Local;
    }

    String getEquipoVisitante() {
        return Visitante;
    }

    String getGolesLocal() {
        return GolesLocal;
    }

    String getGolesVisitante() {
        return GolesVisitante;
    }
}
