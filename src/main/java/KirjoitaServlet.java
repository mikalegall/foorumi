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
 * Tämä servletti kirjoittaa käyttäjän postauksen tietokantaan.
 */
@WebServlet(name = "KirjoitaServlet", urlPatterns = "/kirjoita")
public class KirjoitaServlet extends HttpServlet {
    @Resource(name = "jdbc/foorumi")
    DataSource dataSource;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String keskusteluId = request.getParameter("keskusteluId");

        try (Connection yhteys = dataSource.getConnection()) {
            /*
            SQL-taulun viesti schema (id primary key, kirjoittaja int, keskustelu int, teksti varchar, aikaleima timestamp)
             */
            String sql = "insert into viesti values (?, ?, ?, ?, ?)";
            PreparedStatement ps = yhteys.prepareStatement(sql);
            ps.setInt(1, 0); // Käytetään indeksiä nolla, jolloin SQL auto-increment hoitaa nimeämisen
            HttpSession istunto = request.getSession(false);
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
}
