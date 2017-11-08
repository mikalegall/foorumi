import luokat.Keskustelu;

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

@WebServlet(name = "KategoriaServlet", urlPatterns = "/kategoria")
public class KategoriaServlet extends HttpServlet {
    @Resource(name = "jdbc/foorumi")
    DataSource dataSource;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();
        String kategoriaid = request.getParameter("id");
        String kategoria = "";
        ArrayList<Keskustelu> keskustelut = new ArrayList<>();

        try (Connection yhteys = dataSource.getConnection()) {
            //haetaan kategorian mukaiset keskustelut tietokannasta
            String sql = "select ke.id, ke.otsikko, ke.kategoria from keskustelu ke join kategoria ka on ka.id=ke.kategoria and ka.id=?";
            PreparedStatement ps = yhteys.prepareStatement(sql);
            ps.setString(1, kategoriaid);
            ResultSet tulos = ps.executeQuery();
            //luodaan lista keskusteluista
            while (tulos.next()) {
                Keskustelu k = new Keskustelu(tulos);
                keskustelut.add(k);
            }
            //haetaan kategorian tiedot tietokannasta (sivun otsikoksi)
            String sql2 = "select nimi from kategoria where id=?";
            PreparedStatement ps2 = yhteys.prepareStatement(sql2);
            ps2.setString(1, kategoriaid);
            ResultSet tulos2 = ps2.executeQuery();
            while (tulos2.next()) {
                kategoria = tulos2.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        request.setAttribute("kategoria", kategoria);
        request.setAttribute("keskustelut", keskustelut);
        request.getRequestDispatcher("kategoriat.jsp").forward(request, response);

        writer.close();

    }
}
