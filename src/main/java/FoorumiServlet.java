import luokat.Kategoria;
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
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();
        ArrayList<Kategoria> kategoriat = new ArrayList<>();

        try (Connection yhteys = dataSource.getConnection()) {
            //haetaan tietokannasta kaikki kategoriat
            String sql = "select id, nimi from kategoria";
            PreparedStatement ps = yhteys.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            //luodaan lista kategorioista
            while (rs.next()) {
                Kategoria k = new Kategoria(rs);
                kategoriat.add(k);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        request.setAttribute("kategoriat", kategoriat);
        request.getRequestDispatcher("index.jsp").forward(request, response);

        writer.close();
    }

}
