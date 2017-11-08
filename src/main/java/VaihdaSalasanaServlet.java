
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


@WebServlet(name = "VaihdaSalasana", urlPatterns = {"/VaihdaSalasana"})
public class VaihdaSalasanaServlet extends HttpServlet {
    Boolean salasanavirhe = false;
    Boolean lyhytsalasana = false;
    Boolean onnistui = false;
    @Resource(name = "jdbc/foorumi")
    DataSource ds;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String salasana1 = request.getParameter("salasana");
        String salasana2 = request.getParameter("salasana2");
        HttpSession istunto = request.getSession(false);
        String kayttajatunnus = (String) istunto.getAttribute("nimi");
        if (!salasana1.equals(salasana2)) {
            salasanavirhe = true;
            doGet(request, response);
        } else if (salasana1.length() < 5 || salasana1.equals("")) {
            lyhytsalasana = true;
            doGet(request, response);
        } else {
            try (Connection con = ds.getConnection()) {
                PreparedStatement stmt = con.prepareStatement("UPDATE kayttaja SET salasana=? where kayttajatunnus=?");
                stmt.setString(1, salasana1);
                stmt.setString(2, kayttajatunnus);
                stmt.executeUpdate();
                onnistui = true;
                doGet(request, response);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        HttpSession istunto = request.getSession(false);
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html lang='fi'>");
            out.println("<head>");
            out.println("<meta charset='utf-8'/>");
            out.println("<title>VaihdaSalasana</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Vaihda salasana</h1>");
            if (istunto == null || istunto.getAttribute("nimi") == null) {
                response.sendRedirect("/Kirjaudu");
            } else {
                if (salasanavirhe) {
                    out.println("<p>Salasanan vahvistus epäonnistui<p>");
                    salasanavirhe = false;
                }
                if (lyhytsalasana) {
                    out.println("<p>Salasanan on oltava vähintään 5 merkkiä pitkä<p>");
                    lyhytsalasana = false;
                }
                if (onnistui) {
                    out.println("<p>Salasanan vaihto onnistui<p>");
                    onnistui = false;
                }
                out.println("<form method=POST>");
                out.println("<p>Salasana: <input name='salasana' type='password'></p>");
                out.println("<p>Vahvista salasana: <input name='salasana2' type='password'></p>");
                out.println("<p><input type='submit' value='Vaihda salasana'></p>");
                out.println("</form>");
                out.println("</body>");
                out.println("</html>");
            }

        }
    }
}
