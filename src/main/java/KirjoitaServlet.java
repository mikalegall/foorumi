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
import java.sql.SQLException;

/**
 * Tämä servletti kirjoittaa käyttäjän postauksen tietokantaan.
 */
@WebServlet(name = "KirjoitaServlet", urlPatterns = "/Kirjoita")
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
            ps.setInt(2, 3); // Käytetään käyttäjää vieras
            ps.setString(3, keskusteluId);
            ps.setString(4, request.getParameter("viestiTeksti"));
            ps.setString(5, "2000-01-01");  // To Be Implemented :)
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        response.sendRedirect("/Keskustelu?id=" + keskusteluId);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
