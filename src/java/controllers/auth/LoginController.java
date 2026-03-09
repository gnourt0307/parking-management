/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controllers.auth;

import dal.UserDAO;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.User;

/**
 *
 * @author Admin
 */
public class LoginController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet LoginController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet LoginController at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        RequestDispatcher rd;
        if (user == null) {
            rd = request.getRequestDispatcher("views/auth/login.jsp");
        } else {
            if (user.getRoleID() == 1) {
                rd = request.getRequestDispatcher("views/admin/dashboard.jsp");
            } else if (user.getRoleID() == 2) {
                rd = request.getRequestDispatcher("views/staff/vehicle_in.jsp");
            } else if (user.getRoleID() == 3) {
                rd = request.getRequestDispatcher("views/customer/profile.jsp");
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
        }
        rd.forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UserDAO dao = new UserDAO();
        User user = dao.getUserByUsername(username);

        if (user != null && username.equals(user.getUsername()) && password.equals(user.getPassword())) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);

            if (user.getRoleID() == 1) {
                response.sendRedirect(request.getContextPath() + "/Dashboard");
            } else if (user.getRoleID() == 2) {
                response.sendRedirect(request.getContextPath() + "/VehicleIn");
            } else if (user.getRoleID() == 3 || user.getRoleID() == 2 || user.getRoleID() == 1) {
                response.sendRedirect(request.getContextPath() + "/Profile");
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
