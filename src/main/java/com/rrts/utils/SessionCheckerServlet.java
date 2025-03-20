package com.rrts.utils;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/SessionCheckerServlet")
public class SessionCheckerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String jsonResponse;
        if (session != null && session.getAttribute("supervisorName") != null) {
            jsonResponse = "{"
                + "\"loggedIn\": true,"
                + "\"supervisorName\": \"" + session.getAttribute("supervisorName") + "\","
                + "\"supervisorEmail\": \"" + session.getAttribute("supervisorEmail") + "\","
                + "\"supervisorArea\": \"" + session.getAttribute("supervisorArea") + "\""
                + "}";
        } else {
            jsonResponse = "{\"loggedIn\": false}";
        }

        System.out.println("Session Data Sent: " + jsonResponse); // Debugging

        response.getWriter().write(jsonResponse);
    }
}
