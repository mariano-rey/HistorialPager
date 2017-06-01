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

    public boolean soyLocal() {
        return QuienSos == QUIENES.Local.ordinal();
    }

    public enum QUIENES {
        Local,
        Vistitante
    }

    @Column(name = "Local")
    public String Local;

    @Column(name = "Visitante")
    public String Visitante;

    @Column(name = "GolesLocal")
    public int GolesLocal;

    @Column(name = "GolesVisitante")
    public int GolesVisitante;

    @Column(name = "QuienSos")
    public  int QuienSos;

    @Column(name = "historial", onDelete = Column.ForeignKeyAction.CASCADE)
    public Historial historial;


    public Partido() {
    }

    public static List<Partido> partidos(String nombreRival) {
        return new Select()
                .from(Partido.class)
                .join(Historial.class)
                .on("Historial._id = Partido.historial")
                .where("Historial.rival = ?", nombreRival)
                .execute();
    }

    public Partido(String local, String visitante, int golesLocal, int golesVisitante, int quienSos, Historial historial) {
        this.Local = local;
        this.Visitante = visitante;
        this.GolesLocal = golesLocal;
        this.GolesVisitante = golesVisitante;
        this.QuienSos = quienSos;
        this.historial = historial;
    }

    String getEquipoLocal() {
        return Local;
    }

    String getEquipoVisitante() {
        return Visitante;
    }

    int getGolesLocal() {
        return GolesLocal;
    }

    int getGolesVisitante() {
        return GolesVisitante;
    }
}
