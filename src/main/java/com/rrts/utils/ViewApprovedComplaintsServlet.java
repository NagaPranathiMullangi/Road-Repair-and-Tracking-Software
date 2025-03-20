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

@WebServlet(name = "ViewApprovedComplaintsServlet", urlPatterns = {"/ViewApprovedComplaintsServlet"})
public class ViewApprovedComplaintsServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String areaAssigned = request.getParameter("area_assigned"); 
        String name = request.getParameter("fullname"); // Supervisor's name

        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {
            out.println("<html lang='en'>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            out.println("<title>Approved Complaints</title>");
            out.println("<style>");
            out.println("body { font-family: Arial, sans-serif; background-color: #f4f6f9; margin: 0; padding: 0; display: flex; flex-direction: column; min-height: 100vh; }");
            out.println(".header { background-color: #343a40; color: white; text-align: center; padding: 15px 0; font-size: 1.8rem; font-weight: bold; }");
            out.println(".container { width: 80%; margin: 30px auto; background-color: white; padding: 20px; box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1); border-radius: 8px; flex-grow: 1; }");
            out.println(".table { width: 100%; border-collapse: collapse; margin-top: 20px; }");
            out.println(".table th, .table td { padding: 12px; text-align: left; border: 1px solid #ddd; }");
            out.println(".table th { background-color: #343a40; color: white; }");
            out.println(".table tr:nth-child(even) { background-color: #f2f2f2; }");
            out.println(".table tr:hover { background-color: #e0e0e0; }");
            out.println(".button { background-color: #28a745; color: white; padding: 10px 15px; border-radius: 5px; text-decoration: none; font-size: 14px; border: none; cursor: pointer; margin-right: 5px; }");
            out.println(".button:hover { background-color: #218838; }");
            out.println(".no-records { color: #D32F2F; font-size: 1.2em; text-align: center; }");
            out.println(".footer { background-color: #343a40; color: white; text-align: center; padding: 10px 0; font-size: 0.9rem; position: relative; bottom: 0; width: 100%; }");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");

            out.println("<div class='header'>ROAD REPAIR AND TRACKING SOFTWARE</div>");

            out.println("<div class='container'>");
            out.println("<h2>Approved Complaints for Area: " + areaAssigned + "</h2>");

            try (Connection connection = getConnection()) {
                // Fetch supervisor_id using name and area_assigned
                String supervisorQuery = "SELECT id FROM supervisors WHERE fullname = ? AND area_assigned = ?";
                PreparedStatement supervisorStmt = connection.prepareStatement(supervisorQuery);
                supervisorStmt.setString(1, name);
                supervisorStmt.setString(2, areaAssigned);
                ResultSet supervisorRs = supervisorStmt.executeQuery();

                int supervisorId = -1;
                if (supervisorRs.next()) {
                    supervisorId = supervisorRs.getInt("id");
                }

                // Fetch approved complaints for the area
                String sql = "SELECT complaint_id, complaint_text, severity FROM complaints WHERE area = ? AND verified = 'Yes' AND status = 'Verified'";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, areaAssigned);
                ResultSet resultSet = statement.executeQuery();

                if (!resultSet.isBeforeFirst()) {
                    out.println("<p class='no-records'>No approved complaints found for your area.</p>");
                } else {
                    out.println("<table class='table'>");
                    out.println("<tr><th>Complaint ID</th><th>Complaint Text</th><th>Severity</th><th>Actions</th></tr>");
                    while (resultSet.next()) {
                        int complaintId = resultSet.getInt("complaint_id");
                        out.println("<tr>");
                        out.println("<td>" + complaintId + "</td>");
                        out.println("<td>" + resultSet.getString("complaint_text") + "</td>");
                        out.println("<td>" + resultSet.getString("severity") + "</td>");
                       
                        out.println("<td>"+"<form action='ViewDetailsServlet' method='post' style='display:inline;'>");
                        out.println("<input type='hidden' name='complaint_id' value='" + complaintId + "'>");
                        out.println("<input type='hidden' name='supervisor_id' value='" + supervisorId + "'>");
                        out.println("<button type='submit' class='button' style='background-color: #007bff;'>View Details</button>");
                        out.println("</form>");
                        
                        out.println("</td>");
                        out.println("</tr>");
                    }
                    out.println("</table>");
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
