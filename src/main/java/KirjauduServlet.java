import com.sun.org.apache.xpath.internal.operations.Bool;

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

@WebServlet(name = "KirjauduServlet", urlPatterns = {"/Kirjaudu"})
public class KirjauduServlet extends HttpServlet {
    Boolean virhe = false;
    @Resource(name = "jdbc/foorumi")
    DataSource ds;
    HttpSession istunto;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        istunto = request.getSession(false);
        String nimi = "";
        String salasana = "";
        if (istunto != null) {
            nimi = (String) istunto.getAttribute("nimi");
            salasana = (String) istunto.getAttribute("salasana");
        }
        if (nimi == null) {
            nimi = "";
        }
        response.setContentType("text/html");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html lang='fi'>");
            out.println("<head>");
            out.println("<meta charset='utf-8'/>");
            out.println("<title>Kirjaudu</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Kirjaudu</h1>");
            if (virhe) {
                out.println("<p>Tarkista käyttäjätunnus ja salasana<p>");
            }
            out.println("<form method=POST>");
            out.println("<p>Nimi: <input name='nimi' value='" + nimi + "'></p>");
            out.println("<p>Salasana: <input name='salasana' type='password'></p>");
            out.println("<p><input type='submit' value='Kirjaudu'></p>");
            out.println("</form>");
            out.println("</body>");
            out.println("</html>");

        }
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {

        String nimi = request.getParameter("nimi");
        String salasana = request.getParameter("salasana");

        if (nimi == null) {
            PrintWriter out = response.getWriter();
            virhe = true;
            response.sendRedirect("KirjauduServlet");
        }
        istunto = request.getSession(false);

            try (Connection con = ds.getConnection()) {
                String sql = "SELECT * FROM kayttaja WHERE kayttajatunnus = ? AND salasana = ?";
                PreparedStatement lause = con.prepareStatement(sql);
                lause.setString(1, nimi);
                lause.setString(2, salasana);
                ResultSet rs = lause.executeQuery();
                if (!rs.next()) {
                    virhe = true;
                    doGet(request, response);
                } else {
                    istunto = request.getSession(true);
                    istunto.setAttribute("nimi", nimi);
                    response.setContentType("text/html");
                    PrintWriter out = response.getWriter();
                    out.println("<html>");
                    out.println("<head>");
                    out.println("<title>Siirrytään..</title>");
                    out.println("</head>");
                    out.println("<body>");
                    out.println("<h3>");
                    out.println("Olet nyt kirjautunut, sivun pitäisi siirtyä:");
                    out.println("<a href='/index.html'>");
                    out.println("Etusivulle</a>");
                    out.println("</h3>");
                    out.println("</body>");
                    out.println("</html>");
                    response.sendRedirect("/index.html");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

    }
}