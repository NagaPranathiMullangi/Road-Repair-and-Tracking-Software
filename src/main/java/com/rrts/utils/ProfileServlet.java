package com.rrts.utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.sql.SQLException;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfileServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        String supervisorId = request.getParameter("supervisorId");
        if (supervisorId == null || supervisorId.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing supervisor ID");
            return;
        }

        try {
            // Database connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Fetch approved complaints count
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/rrts", "root", "Pranu@2020")) {
                // Fetch approved complaints count
                String query = "SELECT COUNT(*) FROM complaints WHERE status = 'Approved' AND verified_by = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, Integer.parseInt(supervisorId));
                ResultSet rs = stmt.executeQuery();
                
                int approvedCount = 0;
                if (rs.next()) {
                    approvedCount = rs.getInt(1);
                }
                
                // Create JSON response
                JSONObject json = new JSONObject();
                json.put("approvedComplaints", approvedCount);
                
                out.print(json.toString());
            }
        } catch (ClassNotFoundException | NumberFormatException | SQLException | JSONException e) {
        }
    }
}
