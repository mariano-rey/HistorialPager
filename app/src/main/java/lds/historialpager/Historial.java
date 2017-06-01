package lds.historialpager;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by Tano on 27/4/2017.
 * No Fue Magia
 */

@Table(name = "Historial", id = "_id")
public class Historial extends Model {

    @Column(name = "Rival", unique = true)
    public String rival;

    public Historial() {
        super();
    }

    /**
     * En realidad es un Rival
     * @param rival el nombre del rival
     */
    public Historial(String rival) {
        super();
        this.rival = rival;
    }

    public static List<Historial> perfiles() {
        return new Select()
                .from(Historial.class)
                .execute();
    }

    public String getRival() {
        return rival;
    }

    public static Historial actual(String nombreRival) {
        return new Select()
                .from(Historial.class)
                .where("rival = ?", nombreRival)
                .executeSingle();
    }
}
