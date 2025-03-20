/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.rrts.utils;

import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.sql.SQLException;

@WebServlet("/ValidateLoginServlet")
public class ValidateLoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String area = request.getParameter("area");

        // Check if any field is empty
        if (username == null || username.isEmpty()) {
            response.sendRedirect("supervisor-auth.html?type=login&login_username_error=Username is required");
            return;
        }
        if (password == null || password.isEmpty()) {
            response.sendRedirect("supervisor-auth.html?type=login&login_password_error=Password is required");
            return;
        }
        if (area == null || area.isEmpty()) {
            response.sendRedirect("supervisor-auth.html?type=login&login_area_error=Area is required");
            return;
        }

        // Database connection setup
        String dbURL = "jdbc:mysql://localhost:3306/newrrts";  // Update with your DB name
        String dbUser = "root";  // Update with your DB user
        String dbPass = "Pranu@2020";  // Update with your DB password

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPass)) {
                String sql = "SELECT * FROM supervisors WHERE fullname=? AND password=? AND area_assigned=?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, username);
                stmt.setString(2, password);
                stmt.setString(3, area);
                
                ResultSet rs = stmt.executeQuery();
                
                if (rs.next()) {
                    // Successful login, start session
                    HttpSession session = request.getSession();
                    session.setAttribute("supervisorUsername", username);
                    session.setAttribute("supervisorArea", area);
                     response.sendRedirect("success.html?fullname=" + URLEncoder.encode(username, "UTF-8") +
                                      
                                      "&area=" + URLEncoder.encode(area, "UTF-8"));
                   
               
                } else {
                    // Invalid credentials
                    response.sendRedirect("supervisor-auth.html?type=login&login_username_error=Invalid credentials, please try again");
                }
            }
        } catch (IOException | ClassNotFoundException | SQLException e) {
            response.sendRedirect("supervisor-auth.html?type=login&login_username_error=Database error, please try again");
        }
    }
}
