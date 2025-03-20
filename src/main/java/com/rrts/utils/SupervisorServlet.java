/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.rrts.utils;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.PrintWriter;
import org.json.JSONObject;

@WebServlet("/SupervisorServlet")
public class SupervisorServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        // Retrieve session data
        HttpSession session = request.getSession();
        String name = (String) session.getAttribute("supervisorUsername");
        String area = (String) session.getAttribute("supervisorArea");

        // Default values if session attributes are not set
        if (name == null) name = "Supervisor Name";
        if (area == null) area = "Assigned Area";

        // Create JSON response
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("area", area);

        out.print(json);
        out.flush();
    }
}
