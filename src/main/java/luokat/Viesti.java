package luokat;

import java.time.LocalDate;

/**
 * Created by Administrator on 07/11/2017.
 */
public class Viesti {

    private String id;
    private String kirjoittaja;
    private String teksti;
    private String keskusteluId;

    public String getKeskusteluId() { return keskusteluId;}

    public void setKeskusteluId(String keskusteluId) { this.keskusteluId = keskusteluId; }

    public void setId(String id) { this.id = id; }

    public String getId() { return id; }

    public String getKirjoittaja() {
        return kirjoittaja;
    }

    public void setKirjoittaja(String kirjoittaja) {
        this.kirjoittaja = kirjoittaja;
    }

    public String getTeksti() {
        return teksti;
    }

    public void setTeksti(String teksti) {
        this.teksti = teksti;
    }

}
