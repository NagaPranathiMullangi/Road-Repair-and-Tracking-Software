/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.rrts.utils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet(name = "ViewComplaintsServlet", urlPatterns = {"/ViewComplaintsServlet"})
public class ViewComplaintsServlet extends HttpServlet {

   @Override
   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String areaAssigned = request.getParameter("area_assigned");
        String name; // supervisor's area
       name = request.getParameter("fullname");
        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {
            out.println("<html lang='en'>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            out.println("<title>Verified Complaints</title>");
            out.println("<style>");
out.println("body { font-family: Arial, sans-serif; background-color: #f4f6f9; margin: 0; padding: 0; display: flex; flex-direction: column; min-height: 100vh; }");
out.println(".header { background-color: #343a40; color: white; text-align: center; padding: 15px 0; font-size: 1.8rem; font-weight: bold; }");
out.println(".container { width: 80%; margin: 30px auto; background-color: white; padding: 20px; box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1); border-radius: 8px; flex-grow: 1; }");
out.println(".table { width: 100%; border-collapse: collapse; margin-top: 20px; }");
out.println(".table th, .table td { padding: 12px; text-align: left; border: 1px solid #ddd; }");
out.println(".table th { background-color: #343a40; color: white; }");
out.println(".table tr:nth-child(even) { background-color: #f2f2f2; }");
out.println(".table tr:hover { background-color: #e0e0e0; }");
out.println(".button { background-color: #28a745; color: white; padding: 10px 20px; border-radius: 5px; text-decoration: none; font-size: 16px; border: none; cursor: pointer; }");
out.println(".button:hover { background-color: #218838; }");
out.println(".no-records { color: #D32F2F; font-size: 1.2em; text-align: center; }");
out.println(".footer { background-color: #343a40; color: white; text-align: center; padding: 10px 0; font-size: 0.9rem; position: relative; bottom: 0; width: 100%; }");
out.println("</style>");



            out.println("</head>");
            out.println("<body>");

            // Header Section
            out.println("<div class='header'>");
            out.println("ROAD REPAIR AND TRACKING SOFTWARE");
            out.println("</div>");

            out.println("<div class='container'>");

            out.println("<h2>Verified Complaints for Area: " + areaAssigned + "</h2>");
            

            try (Connection connection = getConnection()) {
                
                 String supervisorQuery = "SELECT id FROM supervisors WHERE fullname = ? AND area_assigned = ?";
                PreparedStatement supervisorStatement = connection.prepareStatement(supervisorQuery);
                supervisorStatement.setString(1, name);
                supervisorStatement.setString(2, areaAssigned);
                ResultSet supervisorResult = supervisorStatement.executeQuery();

    int supervisorId = -1; // Default value if no supervisor is found
    if (supervisorResult.next()) {
        supervisorId = supervisorResult.getInt("id");
    }
                
                
                
                String sql = "SELECT complaint_id, complaint_text, severity FROM complaints WHERE area = ? AND verified = 'Yes' AND status = 'Pending'";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, areaAssigned);
                ResultSet resultSet = statement.executeQuery();

                if (!resultSet.isBeforeFirst()) {
                    out.println("<p class='no-records'>No verified complaints found for your area.</p>");
                } else {
                    out.println("<table class='table'>");
                    out.println("<tr><th>Complaint ID</th><th>Complaint Text</th><th>Severity</th><th>Action</th></tr>");
                    while (resultSet.next()) {
                        int complaintId = resultSet.getInt("complaint_id");
                        out.println("<tr>");
                        out.println("<td>" + complaintId + "</td>");
                        out.println("<td>" + resultSet.getString("complaint_text") + "</td>");
                        out.println("<td>" + resultSet.getString("severity") + "</td>");
                       out.println("<td><form action='required_info.html' method='get'>");
                        out.println("<input type='hidden' name='complaint_id' value='" + complaintId + "'>");
                        out.println("<input type='hidden' name='supervisor_id' value='" + supervisorId + "'>"); // Pass supervisor_id
                        out.println("<button type='submit' class='button'>Request Repair</button>");
                        out.println("</form></td>");

                        out.println("</tr>");
                    }
                    out.println("</table>");
                }
            } catch (Exception e) {
                e.printStackTrace(out);
            }
            out.println("</div>");
            out.println("<div class='footer'>");
out.println("2024 RRTS. All rights reserved");
out.println("</div>");
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