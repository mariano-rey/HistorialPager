
package lds.historialpager;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Resultados", id = "_id")
public class Resultados extends Model {

    @Column(name = "Local")
    public String Local;

    @Column(name = "Visitantes")
    public String Visitantes;

    @Column(name = "GolesLocal")
    public int GolesLocal;

    @Column(name = "GolesVisitante")
    public int GolesVisitante;

    @Column(name = "historial")
    public Historial historial;


    public Resultados(String lanus, String racing, String s, String s1) {
    }

    public Resultados(String local, String visitantes, int golesLocal, int golesVisitante, Historial historial) {
        Local = local;
        Visitantes = visitantes;
        GolesLocal = golesLocal;
        GolesVisitante = golesVisitante;
        this.historial = historial;
    }
}
