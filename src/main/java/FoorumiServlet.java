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
 * Created by Administrator on 07/11/2017.
 */
@WebServlet(name = "FoorumiServlet", urlPatterns = "/index.html")
public class FoorumiServlet extends HttpServlet {
    @Resource(name = "jdbc/foorumi")
    DataSource dataSource;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String kategoria = request.getParameter("kategoria");
        if (kategoria != null) {
            response.setContentType("text/html");
            PrintWriter writer = response.getWriter();

            String keskustelu = request.getParameter("keskustelu");
            if (keskustelu != null) {
                ArrayList<Viesti> viestit = new ArrayList<>();
                String keskusteluNimi = "";
                try (Connection yhteys = dataSource.getConnection()) {
                    String sql = "select teksti from viesti";
                    PreparedStatement ps = yhteys.prepareStatement(sql);
                    ResultSet resultSet = ps.executeQuery();
                    while (resultSet.next()) {
                        Viesti v = new Viesti();
                        v.setTeksti(resultSet.getString("teksti"));
                        viestit.add(v);
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                request.setAttribute("viestit", viestit);
                request.getRequestDispatcher("keskustelu.jsp").forward(request, response);
            } else {
                writer.println("uudelleenohjaus kategoriaan " + kategoria);
            }

            writer.close();
        } else {
            // haetaan tietokannasta kategorioiden nimet
            ArrayList<String> nimet = new ArrayList<>();
            try (Connection yhteys = dataSource.getConnection()) {
                String sql = "select nimi from kategoria";
                PreparedStatement ps = yhteys.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    nimet.add(rs.getString("nimi"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            request.setAttribute("kategorianimet", nimet);
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }

    }

    private ResultSet query(String sql) {
        ResultSet resultSet = null;
        try (Connection yhteys = dataSource.getConnection()) {
            PreparedStatement ps = yhteys.prepareStatement(sql);
            resultSet = ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }


}
