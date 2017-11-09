import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Kirjoittaja: Anna-Reetta
 *
 * PoistaServlet käsittelee pyynnöt, jotka tulevat osoitteeseen /poista.
 * Pyynnön odotetaan sisältävän post-attribuutin viestiID, keskusteluID tai kayttajaID.
 *
 * ViestiID ohjaa viestin poistavaan metodiin ja palauttaa takaisin keskustelunäkymään.
 * KeskusteluID ohjaa keskustelun poistavaan metodiin ja palauttaa saman kategorian keskustelut listaavaan näkymään.
 * KayttajaID ohjaa käyttäjän poistavaan metodiin, joka antaa poistetun käyttäjän viesteille kirjoittajaksi
 * "Poistetun käyttäjän", ja palauttaa takaisin käyttäjät listaavalle sivulle.
 */

@WebServlet(name = "PoistaServlet", urlPatterns = "/poista")
public class PoistaServlet extends HttpServlet {
    @Resource(name = "jdbc/foorumi")
    DataSource ds;
    private String keskusteluID;
    private String kategoriaID;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id;
        //Tarkistetaan, onko parametrina viestiID, keskusteluID vai kayttajaID ja ohjataan oikeaan metodiin
        if (request.getParameter("viestiID") != null) {
            id = request.getParameter("viestiID");
            poistaViesti(id);
            response.sendRedirect("/keskustelu?id=" + keskusteluID);
        } else if (request.getParameter("keskusteluID") != null) {
            id = request.getParameter("keskusteluID");
            poistaKeskustelu(id);
            response.sendRedirect("/kategoria?id=" + kategoriaID);
        } else if (request.getParameter("kayttajaID") != null) {
            id = request.getParameter("kayttajaID");
            poistaKayttaja(id);
            response.sendRedirect("/kayttajat.jsp");
        }
    }

    protected void poistaViesti(String id) {
        try (Connection con = ds.getConnection()) {
            //haetaan viestin keskustelun id uudelleenohjausta varten
            String haeKeskusteluID = "SELECT keskustelu FROM viesti WHERE id=?";
            PreparedStatement ps1 = con.prepareStatement(haeKeskusteluID);
            ps1.setString(1, id);
            ResultSet rs = ps1.executeQuery();
            while (rs.next()) {
                keskusteluID = rs.getString(1);
            }
            //poistetaan viesti tietokannasta
            String poistoSQL = "DELETE from viesti WHERE id=?";
            PreparedStatement ps2 = con.prepareStatement(poistoSQL);
            ps2.setString(1, id);
            ps2.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void poistaKeskustelu(String id) {
        //haetaan keskustelun kategorian id uudelleenohjausta varten
        try (Connection con = ds.getConnection()) {
            String haeKategoriaID = "SELECT kategoria FROM keskustelu WHERE id=?";
            PreparedStatement ps1 = con.prepareStatement(haeKategoriaID);
            ps1.setString(1, id);
            ResultSet rs = ps1.executeQuery();
            while (rs.next()) {
                kategoriaID = rs.getString(1);
            }
            //poistetaan keskustelu ja sen alaiset viestit tietokannasta
            String poistaViestiSQL = "DELETE FROM viesti WHERE keskustelu=?";
            String poistaKeskusteluSQL = "DELETE FROM keskustelu WHERE id=?";
            PreparedStatement ps2 = con.prepareStatement(poistaViestiSQL);
            ps2.setString(1, id);
            ps2.executeUpdate();
            PreparedStatement ps3 = con.prepareStatement(poistaKeskusteluSQL);
            ps3.setString(1, id);
            ps3.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    protected void poistaKayttaja(String id) {
        try (Connection con = ds.getConnection()) {
            //muuta poistettavan käyttäjän viestien kirjoittajaksi Poistettu käyttäjä
            String nimenMuutos = "UPDATE viesti SET kirjoittaja=11 WHERE kirjoittaja=?";
            PreparedStatement ps1 = con.prepareStatement(nimenMuutos);
            ps1.setString(1, id);
            ps1.executeUpdate();
            //poista käyttäjä tietokannasta
            String poistoSQL = "DELETE FROM kayttaja WHERE id=?";
            PreparedStatement ps2 = con.prepareStatement(poistoSQL);
            ps2.setString(1, id);
            ps2.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
