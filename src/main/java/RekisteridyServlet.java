
import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;


@WebServlet(name = "Rekisteröidy", urlPatterns = {"/Rekisteridy"})
public class RekisteridyServlet extends HttpServlet {
    Boolean tyhjä = false;
    Boolean salasanavirhe = false;
    Boolean varattu = false;
    Boolean lyhytsalasana = false;
    @Resource(name = "jdbc/foorumi") DataSource ds;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nimi = request.getParameter("nimi");
        String salasana1 = request.getParameter("salasana");
        String salasana2 = request.getParameter("salasana2");
       /*
        if (nimi == null) {
            response.sendRedirect("Rekisteridy");
        }
        */
        try (Connection con = ds.getConnection()) {
            String sql = "SELECT * FROM kayttaja WHERE kayttajatunnus = ?";
            PreparedStatement lause = con.prepareStatement(sql);
            lause.setString(1, nimi);
            ResultSet rs = lause.executeQuery();
            if (rs.next()) {
                if (rs.getString("kayttajatunnus").equals(nimi)) {
                    varattu = true;
                    doGet(request, response); // edellinen sivu
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (!salasana1.equals(salasana2)) {
            salasanavirhe = true;
            doGet(request, response);
        } else if (nimi.equals("") || salasana1.equals("")) {
            tyhjä = true;
            doGet(request, response);
        } else if (salasana1.length() < 5) {
            lyhytsalasana = true;
            doGet(request, response);
        }
        else {
            try (Connection con = ds.getConnection()) {
                PreparedStatement stmt = con.prepareStatement("INSERT INTO kayttaja(kayttajatunnus, salasana, rooli) VALUES (?, ? ,'tavis')");
                stmt.setString(1, nimi);
                stmt.setString(2, salasana1);
                stmt.executeUpdate();
                response.setContentType("text/html");
                PrintWriter out = response.getWriter();
                response.sendRedirect("/Kirjaudu");
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html lang='fi'>");
            out.println("<head>");
            out.println("<meta charset='utf-8'/>");
            out.println("<title>Rekisteröidy</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Rekisteröidy</h1>");
            if (tyhjä) {
                out.println("<p>Käyttäjätunnus tai salasana ei saa olla tyhjä<p>");
                tyhjä = false;
            }
            if (salasanavirhe) {
                out.println("<p>Salasanan vahvistus epäonnistui<p>");
                salasanavirhe = false;
            }
            if (varattu) {
                out.println("<p>Käyttäjätunnus on varattu<p>");
                varattu = false;
            }
            if (lyhytsalasana) {
                out.println("<p>Salasanan on oltava vähintään 5 merkkiä pitkä<p>");
                lyhytsalasana = false;
            }
            out.println("<form method=POST>");
            out.println("<p>Käyttäjätunnus: <input name='nimi'></p>");
            out.println("<p>Salasana: <input name='salasana' type='password'></p>");
            out.println("<p>Vahvista salasana: <input name='salasana2' type='password'></p>");
            out.println("<p><input type='submit' value='Rekisteröidy'></p>");
            out.println("</form>");
            out.println("</body>");
            out.println("</html>");

        }
    }
}
