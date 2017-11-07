package luokat;

import java.time.LocalDate;

/**
 * Created by Administrator on 07/11/2017.
 */
public class Viesti {

    private String kirjoittaja, teksti;
    private LocalDate aikaleima;

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

    public LocalDate getAikaleima() {
        return aikaleima;
    }

    public void setAikaleima(LocalDate aikaleima) {
        this.aikaleima = aikaleima;
    }


}
