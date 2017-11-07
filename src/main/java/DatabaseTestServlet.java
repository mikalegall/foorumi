import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

/**
 * Created by Administrator on 07/11/2017.
 */
@WebServlet(name = "DatabaseTestServlet", urlPatterns = "/databasetest")
public class DatabaseTestServlet extends HttpServlet {
    @Resource(name ="jdbc/foorumi")
    DataSource dataSource;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();
        writer.println("Database test page<br><br>");
        try (Connection yhteys = dataSource.getConnection()) {
            Statement lause = yhteys.createStatement();
            ResultSet rs = lause.executeQuery("select * from kayttaja");
            writer.println("Taulun kayttaja nimet:<br>");
            while (rs.next()) {
                writer.println(rs.getString("nimi") + "<br>");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        writer.println("...done!");

        writer.close();
    }
}
