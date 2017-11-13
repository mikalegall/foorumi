import luokat.Viesti;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Kirjoittaja: Jari
 *
 * KeskusteluServlet käsittelee pyynnöt, jotka tulevat osoitteeseen /Keskustelu.
 * Pyynnön odotetaan sisältävän get-attribuutti id, joka tulee vastata tietokannassa
 * olevan keskustelun id-numeroa. Mikäli id-numeroa ei ole tai se on virheellinen,
 * käyttäjä ohjataan virheilmoitukseen. KeskusteluServlet hakee keskustelun
 * näyttämisksi tarvittavat tiedot tietokannasta ja lähettää ne eteenpäin
 * keskustelu.jsp -tiedostolle.
 *
 */
@WebServlet(name = "KeskusteluServlet", urlPatterns = "/keskustelu")
public class KeskusteluServlet extends HttpServlet {
    @Resource(name = "jdbc/foorumi")
    DataSource dataSource;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String keskusteluId = request.getParameter("id");
        if (keskusteluId == null) {
            response.getWriter().println("käyttäjä ei antanut keskustelua, ohjaa etusivulle");
            return;
        }


        /*
        Jotta keskustelu voidaan näyttää, tarvitaan tietokannasta kaikki keskusteluun liittyvät
        viestit, keskustelun otsikko sekä kategoria, johon keskustelu kuuluu. Nämä kaikki haetaan
        tietokannasta.
         */
        ArrayList<Viesti> viestit = new ArrayList<>();
        String keskusteluOtsikko = "";
        String keskusteluKategoriaNimi = "";
        String keskusteluKategoriaId = "";
        try (Connection yhteys = dataSource.getConnection()) {

            /*
            Haetaan tietokannasta kaikki viestit, jotka liittyvät kyseiseen keskusteluun.
            Samalla haetaan jokaisen viestin kirjoittajan käyttäjätunnus ja talletetaan
            nämä tiedot Viesti-olioon, joka lisätään viestit-listaan.
             */
            String sql = "select viesti.id as viestiid, kayttajatunnus, teksti, aikaleima " +
                    "from kayttaja join viesti on kayttaja.id = viesti.kirjoittaja " +
                    "where keskustelu=? order by aikaleima";
            PreparedStatement ps = yhteys.prepareStatement(sql);
            ps.setString(1, keskusteluId);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                Viesti v = new Viesti();
                v.setTeksti(resultSet.getString("teksti"));
                v.setKirjoittaja(resultSet.getString("kayttajatunnus"));
                v.setId(resultSet.getString("viestiid"));
                v.setAikaleima(resultSet.getString("aikaleima"));
                viestit.add(v);
            }

            if (viestit.isEmpty()) {
                response.getWriter().println("keskustelua ei löydy, ohjaa erroriin");
            }

            /*
            Hae keskustelun otsikko  ja kategoria tietokannasta
             */
            sql = "select otsikko as keskusteluotsikko, nimi as kategorianimi, kategoria.id as kategoriaid " +
                    "from keskustelu join kategoria on keskustelu.kategoria = kategoria.id " +
                    "where keskustelu.id = ?";

            ps = yhteys.prepareStatement(sql);
            ps.setString(1, keskusteluId);
            resultSet = ps.executeQuery();
            resultSet.next();
            keskusteluOtsikko = resultSet.getString("keskusteluotsikko");
            keskusteluKategoriaNimi = resultSet.getString("kategorianimi");
            keskusteluKategoriaId = resultSet.getString("kategoriaid");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        /*
        Talletetaan haetut tiedot request-attribuuttiin ja ohjataan pyyntö eteenpän jsp.-tiedostolle.
         */
        request.setAttribute("keskusteluId", keskusteluId);
        request.setAttribute("keskusteluOtsikko", keskusteluOtsikko);
        request.setAttribute("keskusteluKategoriaNimi", keskusteluKategoriaNimi);
        request.setAttribute("keskusteluKategoriaId", keskusteluKategoriaId);
        request.setAttribute("viestit", viestit);
        request.getRequestDispatcher("keskustelu.jsp").forward(request, response);

    }
}
