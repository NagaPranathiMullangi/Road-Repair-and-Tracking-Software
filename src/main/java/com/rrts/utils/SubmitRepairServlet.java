package com.rrts.utils;

import java.io.IOException;
import java.sql.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "SubmitRepairServlet", urlPatterns = {"/SubmitRepairServlet"})
public class SubmitRepairServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int complaintId = Integer.parseInt(request.getParameter("complaint_id"));
            int supervisorId = Integer.parseInt(request.getParameter("supervisor_id"));
            String priority = request.getParameter("priority");
            
            int machinesEstimated = Integer.parseInt(request.getParameter("machines_estimated"));
            int personnelEstimated = Integer.parseInt(request.getParameter("personnel_estimated"));
            
            String rawMaterials = String.join(", ", request.getParameterValues("raw_materials"));
            String scheduledDate = request.getParameter("scheduled_date");
            Date repairDate = Date.valueOf(scheduledDate);
            
            try (Connection connection = getConnection()) {
                String insertRepairSQL = "INSERT INTO repairs (complaint_id, supervisor_id, priority, raw_materials_required, machines_required, personnel_required, scheduled_date) VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement repairStmt = connection.prepareStatement(insertRepairSQL);
                repairStmt.setInt(1, complaintId);
                repairStmt.setInt(2, supervisorId);
                repairStmt.setString(3, priority);
                repairStmt.setString(4, rawMaterials);
                repairStmt.setInt(5, machinesEstimated);
                repairStmt.setInt(6, personnelEstimated);
                repairStmt.setDate(7, repairDate);
                repairStmt.executeUpdate();
                
                String updateComplaintSQL = "UPDATE complaints SET status = 'Verified' WHERE complaint_id = ?";
                PreparedStatement complaintStmt = connection.prepareStatement(updateComplaintSQL);
                complaintStmt.setInt(1, complaintId);
                complaintStmt.executeUpdate();
                
                response.getWriter().println("Repair request submitted successfully.");
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid number format: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid input provided: " + e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unexpected error occurred: " + e.getMessage());
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
