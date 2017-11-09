import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Tämä servletti kirjoittaa käyttäjän postauksen tietokantaan. Käyttäjän tulee olla kirjautunut
 * sisään käyttääkseen tätä servlettiä. KirjoitaServlet lisää joko uuden viestiketjun ja laittaa
 * siihen ensimmäisen viestin, tai lisää uuden viestin olemassaolevaan viestiketjuun.,
 */
@WebServlet(name = "KirjoitaServlet", urlPatterns = "/kirjoita")
public class KirjoitaServlet extends HttpServlet {
    @Resource(name = "jdbc/foorumi")
    DataSource dataSource;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession istunto = request.getSession(false);
        /*
        Jos käyttäjä ei ole kirjautunut, tänne ei ole mitään asiaa.
         */
        if (istunto == null || istunto.getAttribute("nimi") == null) {
            response.sendRedirect("/kirjaudu");
        }

        /*
        Tarkistetaan, mihin keskusteluun käyttäjä on vastaamassa. Mikäli käyttäjä on
        aloittamassa uutta keskustelua, keskusteluId == null.
         */
        String keskusteluId = request.getParameter("keskusteluId");

        /*
        Mikäli käyttäjä on aloittamassa uutta keskustelua, kategoriaId on asetettu.
        Tässä tapauksessa luodaan uusi keskustelu ja talletetaan keskustelun id
        muuttujaan keskusteluId.
         */
        String kategoriaId = request.getParameter("kategoriaId");
        String otsikko = request.getParameter("otsikko");
        if (kategoriaId != null) {
            keskusteluId = luoUusiKeskustelu(kategoriaId, otsikko);
        }

        /*
        Kirjoitetaan käyttäjän viesti keskusteluun. Tämä tehdään riippumatta siitä onko
        kyseessä uusi keskustelu vai jatko edelliseen keskusteluun.
         */
        try (Connection yhteys = dataSource.getConnection()) {
            /*
            SQL-taulun viesti schema (id primary key, kirjoittaja int, keskustelu int, teksti varchar, aikaleima timestamp)
             */
            String sql = "insert into viesti values (?, ?, ?, ?, ?)";
            PreparedStatement ps = yhteys.prepareStatement(sql);
            ps.setInt(1, 0); // Käytetään indeksiä nolla, jolloin SQL auto-increment hoitaa nimeämisen
            istunto = request.getSession(false);
            String nimi = (String) istunto.getAttribute("nimi");
            int id = 0;
            try (Connection con = dataSource.getConnection()) {
                PreparedStatement stmt = con.prepareStatement("SELECT id FROM kayttaja WHERE kayttajatunnus=?");
                stmt.setString(1, nimi);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    String ids = rs.getString("id");
                    id = Integer.parseInt(ids);
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
            ps.setInt(2, id);
            ps.setString(3, keskusteluId);
            ps.setString(4, request.getParameter("viestiTeksti"));
            ps.setString(5, timeStamp);  // To Be Implemented :)
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        response.sendRedirect("/keskustelu?id=" + keskusteluId);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    /*
    Metodi luoUusiKeskustelu luo tietokantaan tauluun keskustelu uuden rivin käyttäen
    SQL:n auto-increment ominaisuutta ja palauttaa uuden keskustelun id-tunnuksen
    String-muotoisena.
     */
    private String luoUusiKeskustelu(String kategoriaId, String otsikko) {
        String id =  null;
        try (Connection yhteys = dataSource.getConnection()) {
            String sql = "insert into keskustelu values(0, ?, ?)";
            PreparedStatement ps = yhteys.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, otsikko);
            ps.setString(2, kategoriaId);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }
}
