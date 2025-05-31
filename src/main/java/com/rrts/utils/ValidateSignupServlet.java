package com.rrts.utils;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/ValidateSignupServlet")
public class ValidateSignupServlet extends HttpServlet {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/newrrts"; // Ensure the correct DB name
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Pranu@2020";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String fullname = request.getParameter("username");  
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String area = request.getParameter("area");

        /*System.out.println("Received Data:");
        System.out.println("Full Name: " + fullname);
        System.out.println("Email: " + email);
        System.out.println("Password: " + password);
        System.out.println("Area Assigned: " + area);*/

        // Validate input
        if (fullname == null || fullname.isEmpty() ||
            email == null || email.isEmpty() ||
            password == null || password.length() < 6 ||
            area == null || area.isEmpty()) {
            System.out.println("Validation failed. Redirecting...");
            response.sendRedirect("supervisor-auth.html?error=Invalid input");
            return;
        }
        
        
        
        
      

        // Insert into database
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            int rowsInserted;
            // **Fixing Table Name**
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                // **Fixing Table Name**
              String sql = "INSERT INTO supervisors (fullname, email, password, area_assigned) VALUES (?, ?, ?, ?)";

                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, fullname);
                    pstmt.setString(2, email);
                    pstmt.setString(3, password);
                    pstmt.setString(4, area);
                    rowsInserted = pstmt.executeUpdate();
                   // System.out.println("Rows inserted: " + rowsInserted);
                }
            }

            if (rowsInserted > 0) {
    HttpSession session = request.getSession();
    session.setAttribute("supervisorUsername", fullname);
    session.setAttribute("supervisorArea", area);

    response.sendRedirect("success.html?fullname=" + URLEncoder.encode(fullname, "UTF-8") +
                          "&area=" + URLEncoder.encode(area, "UTF-8") +
                          "&email=" + URLEncoder.encode(email, "UTF-8"));
    return; // ✅ Stop execution after redirect
}
             else {
                System.out.println("Failed to insert into database.");
                response.sendRedirect("supervisor-auth.html?error=Database error");
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            response.sendRedirect("supervisor-auth.html?error=Database error");
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver Not Found!");
            response.sendRedirect("supervisor-auth.html?error=Driver error");
        }
    }
}
