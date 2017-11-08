package luokat;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Kategoria {
    private int id;
    private String nimi;

    public Kategoria() {
    }

    public Kategoria(ResultSet rs) {
        try {
            this.id = rs.getInt(1);
            this.nimi = rs.getString(2);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
