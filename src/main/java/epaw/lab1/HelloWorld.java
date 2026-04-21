package epaw.lab1;

import epaw.lab1.util.DBManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/hello")
public class HelloWorld extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head><title>Users List</title></head>");
        out.println("<body>");
        out.println("<h1>Users from Database</h1>");
        out.println("<table border='1'>");
        out.println("<tr><th>ID</th><th>Name</th><th>Description</th></tr>");

        try (DBManager db = new DBManager()) {
            PreparedStatement stmt = db.prepareStatement("SELECT id, name, description FROM users");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                
                out.println("<tr>");
                out.println("<td>" + id + "</td>");
                out.println("<td>" + name + "</td>");
                out.println("<td>" + description + "</td>");
                out.println("</tr>");
            }
        } catch (Exception e) {
            out.println("<tr><td colspan='3'>Error: " + e.getMessage() + "</td></tr>");
            e.printStackTrace();
        }

        out.println("</table>");
        out.println("<h2> Add a new user </h2>");
        out.println("<form action=\"/hello\" method=\"post\">");
        out.println("<label for=\"name\">Name:</label><br>");
        out.println("<input type=\"text\" id=\"name\" name=\"name\"><br>");
        out.println("<label for=\"description\">Description:</label><br>");
        out.println("<input type=\"text\" id=\"description\" name=\"description\"><br><br>");
        out.println("<input type=\"submit\" value=\"Add User\">");
        out.println("</form>");
        out.println("</body>");
        out.println("</html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String description = request.getParameter("description");

        if (name != null && description != null && !name.isBlank() && !description.isBlank()) {
            try (DBManager db = new DBManager()) {
                PreparedStatement stmt = db.prepareStatement(
                        "INSERT INTO users (name, description) VALUES (?, ?)");
                stmt.setString(1, name.trim());
                stmt.setString(2, description.trim());
                stmt.executeUpdate();
            } catch (Exception e) {
                throw new ServletException("Failed to save user", e);
            }
        }

        response.sendRedirect(request.getContextPath() + "/hello");
    }

}