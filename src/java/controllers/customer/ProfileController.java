/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controllers.customer;

import dal.CustomerVehicleDAO;
import dal.UserDAO;
import dal.VehicleTypeDAO;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import models.CustomerVehicle;
import models.User;
import models.VehicleType;

/**
 *
 * @author Admin
 */
public class ProfileController extends HttpServlet {

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
            out.println("<title>Servlet ProfileController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ProfileController at " + request.getContextPath() + "</h1>");
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
        User onSessionUser = (User) session.getAttribute("user");
        RequestDispatcher rd;
        if (onSessionUser == null) {
            rd = request.getRequestDispatcher("views/auth/login.jsp");
            rd.forward(request, response);
            return;
        }

        CustomerVehicleDAO customerVehicleDao = new CustomerVehicleDAO();
        VehicleTypeDAO vehicleTypeDao = new VehicleTypeDAO();
        List<VehicleType> vehicleList = vehicleTypeDao.getAllTypes();
        ArrayList<CustomerVehicle> customerVehicleList = customerVehicleDao.getCustomerVehicles(onSessionUser);

        session.setAttribute("vehicleList", vehicleList);
        session.setAttribute("customerVehicleList", customerVehicleList);
        rd = request.getRequestDispatcher("views/customer/profile.jsp");
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
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        String action = request.getParameter("action");
        UserDAO userDao = new UserDAO();
        VehicleTypeDAO vehicleTypeDao = new VehicleTypeDAO();
        CustomerVehicleDAO customerVehicleDao = new CustomerVehicleDAO();

        if ("updateProfile".equals(action)) {
            String fullname = request.getParameter("fullname");
            String username = request.getParameter("username");
            String phone = request.getParameter("phone");

            user.setUsername(username);
            user.setFullName(fullname);
            user.setPhone(phone);
            boolean success = userDao.updateInformation(user);

            if (success) {
                session.setAttribute("user", user);
                request.setAttribute("successMsg", "Update successfully.");
            } else {
                request.setAttribute("errorMsg", "Update Failed.");
            }
        } else if ("changePassword".equals(action)) {
            String currentPassword = request.getParameter("current_password");
            String newPassword = request.getParameter("new_password");
            String confirmPassword = request.getParameter("confirm_password");

            if (!currentPassword.equals(user.getPassword())) {
                request.setAttribute("errorMsg", "Wrong current password.");
            } else if (!confirmPassword.equals(newPassword)) {
                request.setAttribute("errorMsg", "New password & confirm password must match.");
            } else {
                user.setPassword(newPassword);
                boolean success = userDao.changePassword(user);
                if (success) {
                    session.setAttribute("user", user);
                    request.setAttribute("successMsg", "Change password successfully.");
                } else {
                    request.setAttribute("errorMsg", "Change password failed.");
                }
            }
        } else if ("add".equals(action)) {

            String typeName = request.getParameter("vehicleTypeName");
            String licensePlate = request.getParameter("licensePlate");
            int typeId = vehicleTypeDao.findTypeIdByName(typeName);
            VehicleType vehicleType = new VehicleType(typeId, typeName);
            CustomerVehicle customerVehicle = new CustomerVehicle(user.getUserID(), licensePlate, typeId, vehicleType);

            boolean success = customerVehicleDao.addCustomerVehicle(customerVehicle);

            if (success) {
                ArrayList<CustomerVehicle> customerVehicleList = customerVehicleDao.getCustomerVehicles(user);
                request.setAttribute("successMsg", "Added new vehicle successfully.");
                session.setAttribute("customerVehicleList", customerVehicleList);
            } else {
                request.setAttribute("errorMsg", "Added new vehicle failed.");
            }

        }
        request.getRequestDispatcher("views/customer/profile.jsp").forward(request, response);
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
