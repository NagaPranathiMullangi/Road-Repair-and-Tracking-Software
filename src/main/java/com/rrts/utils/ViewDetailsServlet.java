package com.rrts.utils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet(name = "ViewDetailsServlet", urlPatterns = {"/ViewDetailsServlet"})
public class ViewDetailsServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int complaintId = Integer.parseInt(request.getParameter("complaint_id"));
        int supervisorId = Integer.parseInt(request.getParameter("supervisor_id"));
        
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<html lang='en'>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            out.println("<title>Complaint Details</title>");
            out.println("<style>");
            out.println("body { font-family: Arial, sans-serif; background-color: #f4f6f9; margin: 0; padding: 0; display: flex; flex-direction: column; min-height: 100vh; }");
            out.println(".header { background-color: #343a40; color: white; text-align: center; padding: 15px 0; font-size: 1.8rem; font-weight: bold; }");
            out.println(".container { width: 50%; margin: 30px auto; background-color: white; padding: 20px; box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1); border-radius: 8px; flex-grow: 1; }");
            out.println(".form-group { display: flex; justify-content: space-between; padding: 10px; border-bottom: 1px solid #ddd; }");
            out.println(".form-group label { font-weight: bold; }");
            out.println(".footer { background-color: #343a40; color: white; text-align: center; padding: 10px 0; font-size: 0.9rem; position: relative; bottom: 0; width: 100%; }");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div class='header'>ROAD REPAIR AND TRACKING SOFTWARE</div>");
            out.println("<div class='container'>");
            out.println("<h2>Complaint Details</h2>");
            
            try (Connection connection = getConnection()) {
                String sql = "SELECT * FROM repairs WHERE complaint_id = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setInt(1, complaintId);
                ResultSet resultSet = statement.executeQuery();
                
                if (resultSet.next()) {
                    out.println("<div class='form-group'><label>Complaint ID:</label> <span>" + resultSet.getInt("complaint_id") + "</span></div>");
                    out.println("<div class='form-group'><label>Priority:</label> <span>" + resultSet.getString("priority") + "</span></div>");
                    out.println("<div class='form-group'><label>Raw Materials Required:</label> <span>" + resultSet.getString("raw_materials_required") + "</span></div>");
                    out.println("<div class='form-group'><label>Machines Required:</label> <span>" + resultSet.getString("machines_required") + "</span></div>");
                    out.println("<div class='form-group'><label>Personnel Required:</label> <span>" + resultSet.getString("personnel_required") + "</span></div>");
                    out.println("<div class='form-group'><label>Scheduled Date:</label> <span>" + resultSet.getString("scheduled_date") + "</span></div>");
                    out.println("<div class='form-group'><label>Status:</label> <span>" + resultSet.getString("status") + "</span></div>");
                } else {
                    out.println("<p class='no-records'>No details found for this complaint.</p>");
                }
            } catch (Exception e) {
                e.printStackTrace(out);
            }
            
            out.println("</div>");
            out.println("<div class='footer'>2024 RRTS. All rights reserved</div>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    private Connection getConnection() throws Exception {
        String jdbcUrl = "jdbc:mysql://localhost:3306/newrrts";
        String dbUser = "root";
        String dbPassword = "Pranu@2020";
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(jdbcUrl, dbUser, dbPassword);
    }
}
