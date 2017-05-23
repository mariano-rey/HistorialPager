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

    public Historial(String rival) {
        super();
        this.rival = rival;
    }

    public static List<Historial> perfiles() {
        return new Select()
                .from(Historial.class)
                .execute();
    }

    @Override
    public String toString() {
        return rival;
    }

}
