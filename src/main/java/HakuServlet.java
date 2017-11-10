import com.sun.deploy.util.ArrayUtil;
import luokat.Viesti;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Administrator on 08/11/2017.
 */
@WebServlet(name = "HakuServlet", urlPatterns = "/haku")
public class HakuServlet extends HttpServlet {
    @Resource(name = "jdbc/foorumi")
    DataSource dataSource;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String hakusanat = request.getParameter("haku").trim();

        ArrayList<Viesti> hakutulokset = haeViestirungosta(hakusanat);
        if (hakutulokset.isEmpty()) {
            String[] hakusanoja = hakusanat.split(" ");
            for (String hakusana : hakusanoja) {
                hakutulokset.addAll(haeViestirungosta(hakusana));
            }
        }

        //debugPrint(hakutulokset, request, response);

        request.setAttribute("haku", hakusanat);
        request.setAttribute("hakutulokset", hakutulokset);
        request.getRequestDispatcher("hakutulokset.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
    /*
    Palauttaa annetusta tekstistä version jossa on (max) 10 merkkiä ennen
    hakusanaa, hakusana korostettuna <strong> tagilla ja lopussa (max)
    10 merkkiä hakusanan jälkeen alkuperäisestä tekstistä.
     */
    private String korostaHakusanaTekstistä(String teksti, String hakusana) {
        int hakusananAlkuIndeksi = teksti.toLowerCase().indexOf(hakusana.toLowerCase());
        if (hakusananAlkuIndeksi < 0) return null;
        int alkupiste = (hakusananAlkuIndeksi < 10) ? 0 : hakusananAlkuIndeksi - 10;
        int hakusananLoppuIndeksi = hakusananAlkuIndeksi + hakusana.length();
        int loppupiste = (hakusananLoppuIndeksi + 10 > teksti.length()) ? teksti.length() : hakusananLoppuIndeksi + 10;

        StringBuilder retval = new StringBuilder();
        retval.append(teksti.substring(alkupiste, hakusananAlkuIndeksi));
        retval.append("<em>");
        retval.append(teksti.substring(hakusananAlkuIndeksi, hakusananLoppuIndeksi));
        retval.append("</em>");
        retval.append(teksti.substring(hakusananLoppuIndeksi, loppupiste));
        return retval.toString();

    }

    /*
    Hakee tietokannan taulusta "viesti" kaikki viestit, joiden tekstissä
    esiintyy annettu hakusana. Palauttaa listan pseudoviestejä, joista
    jokaisen muttujassa "teksti" on se kohta alkuperäisestä tekstistä,
    jossa hakusana esiintyy, varsinainen hakusana korostettuna.
     */
    private ArrayList<Viesti> haeViestirungosta(String hakusana) {
        ArrayList<Viesti> hakutulokset = new ArrayList<>();
        try (Connection yhteys = dataSource.getConnection()) {
            String sql = "select id, keskustelu, teksti from viesti where teksti like ?";
            PreparedStatement ps = yhteys.prepareStatement(sql);
            ps.setString(1, "%" + hakusana + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Viesti viesti = new Viesti();
                viesti.setId(rs.getString("id"));
                viesti.setTeksti(korostaHakusanaTekstistä(rs.getString("teksti"), hakusana));
                viesti.setKeskusteluId(rs.getString("keskustelu"));
                hakutulokset.add(viesti);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hakutulokset;
    }

    private void debugPrint(ArrayList<Viesti> lista, HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html");
        for (Viesti v : lista) {
            out.println(v.getTeksti());
        }
    }
}
