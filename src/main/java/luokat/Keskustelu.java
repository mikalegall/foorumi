package luokat;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Keskustelu {
    private int id;
    private String otsikko;
    private int kategoria;

    public Keskustelu() {
    }

    public Keskustelu(ResultSet rs) {
        try {
            this.id = rs.getInt(1);
            this.otsikko = rs.getString(2);
            this.kategoria = rs.getInt(3);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOtsikko() {
        return otsikko;
    }

    public void setOtsikko(String otsikko) {
        this.otsikko = otsikko;
    }

    public int getKategoria() {
        return kategoria;
    }

    public void setKategoria(int kategoria) {
        this.kategoria = kategoria;
    }
}
