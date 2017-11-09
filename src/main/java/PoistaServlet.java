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

@WebServlet(name = "PoistaServlet", urlPatterns = "/poista")
public class PoistaServlet extends HttpServlet {
    @Resource(name = "jdbc/foorumi")
    DataSource ds;
    private String keskusteluID;
    private String kategoriaID;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id;
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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void poistaViesti(String id) {
        try (Connection con = ds.getConnection()) {
            String haeKeskusteluID = "SELECT keskustelu FROM viesti WHERE id=?";
            PreparedStatement ps1 = con.prepareStatement(haeKeskusteluID);
            ps1.setString(1, id);
            ResultSet rs = ps1.executeQuery();
            while (rs.next()) {
                keskusteluID = rs.getString(1);
            }
            String poistoSQL = "DELETE from viesti WHERE id=?";
            PreparedStatement ps2 = con.prepareStatement(poistoSQL);
            ps2.setString(1, id);
            ps2.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void poistaKeskustelu(String id) {
        try (Connection con = ds.getConnection()) {
            String haeKategoriaID = "SELECT kategoria FROM keskustelu WHERE id=?";
            PreparedStatement ps1 = con.prepareStatement(haeKategoriaID);
            ps1.setString(1, id);
            ResultSet rs = ps1.executeQuery();
            while (rs.next()) {
                kategoriaID = rs.getString(1);
            }
            String poistoSQL = "DELETE from keskustelu WHERE id=?";
            PreparedStatement ps2 = con.prepareStatement(poistoSQL);
            ps2.setString(1, id);
            ps2.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    protected void poistaKayttaja(String id) {
        try (Connection con = ds.getConnection()) {
            String poistoSQL = "DELETE from kayttaja WHERE id=?";
            PreparedStatement ps = con.prepareStatement(poistoSQL);
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
