/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controllers.staff;

import dal.SlotDAO;
import dal.TicketDAO;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import models.Slot;
import models.Ticket;
import models.User;

/**
 *
 * @author Admin
 */
public class VehicleInController extends HttpServlet {

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
            rd.forward(request, response);
            return;
        } else {
            if (user.getRoleID() != 2) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        }

        String action = request.getParameter("action");
        if ("checkPlate".equals(action)) {
            String licensePlate = request.getParameter("plate");
            if (licensePlate == null) licensePlate = "";
            licensePlate = licensePlate.trim();
            
            dal.CustomerVehicleDAO cvDAO = new dal.CustomerVehicleDAO();
            models.CustomerVehicle cv = cvDAO.getVehicleByLicensePlate(licensePlate);
            
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            
            if (cv != null) {
                response.getWriter().write("{\"found\":true, \"typeID\":" + cv.getTypeID() + "}");
            } else {
                response.getWriter().write("{\"found\":false}");
            }
            return;
        }

        // Lay danh sach slot de staff chon khi check-in
        SlotDAO slotDAO = new SlotDAO();
        List<Slot> slots = slotDAO.getAllSlots(null, null);
        request.setAttribute("slots", slots);

        dal.VehicleTypeDAO vtDAO = new dal.VehicleTypeDAO();
        request.setAttribute("vehicleTypes", vtDAO.getAllTypes());

        rd = request.getRequestDispatcher("views/staff/vehicle_in.jsp");
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
        if (user == null || user.getRoleID() != 2) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String action = request.getParameter("action");
        if (action == null) {
            response.sendRedirect("VehicleIn");
            return;
        }

        if (action.equals("checkin")) {
            try {
                String licensePlate = request.getParameter("licensePlate");
                int typeID = Integer.parseInt(request.getParameter("vehicleType"));
                int slotID = Integer.parseInt(request.getParameter("assignedSlot"));

                TicketDAO ticketDAO = new TicketDAO();

                Ticket activeTicket = ticketDAO.findActiveTicketByPlate(licensePlate);
                if (activeTicket != null) {
                    session.setAttribute("errorMsg", "License plate " + licensePlate + " is already checked in.");
                    response.sendRedirect("VehicleIn");
                    return;
                }

                dal.CustomerVehicleDAO cvDAO = new dal.CustomerVehicleDAO();
                models.CustomerVehicle registeredVehicle = cvDAO.getVehicleByLicensePlate(licensePlate);

                if (registeredVehicle != null && registeredVehicle.getTypeID() != typeID) {
                    session.setAttribute("errorMsg", "License Plate " + licensePlate + " is registered as a " + registeredVehicle.getVehicleType().getTypeName() + ". Cannot check into a mismatched slot/type!");
                    response.sendRedirect("VehicleIn");
                    return;
                }

                Ticket ticket = new Ticket();

                // Tao ma ve theo format VEX-yyMMdd-0001
                ticket.setTicketCode(ticketDAO.generateNextTicketCode());
                ticket.setLicensePlate(licensePlate);
                ticket.setTypeID(typeID);
                ticket.setSlotID(slotID);
                ticket.setCustomerID(registeredVehicle != null ? registeredVehicle.getUserID() : null); // neu co dky thi map vao UserID, ko thi null
                ticket.setCreatedBy(user.getUserID());

                boolean created = ticketDAO.createTicket(ticket);

                if (created) {
                    // Danh dau slot dang duoc su dung
                    SlotDAO slotDAO = new SlotDAO();
                    slotDAO.setSlotStatus(slotID, "Occupied");
                    session.setAttribute("successMsg", "Check-in successful.");
                } else {
                    session.setAttribute("errorMsg", "Check-in failed. Please try again.");
                }
            } catch (Exception e) {
                session.setAttribute("errorMsg", "Invalid data provided.");
            }
            response.sendRedirect("VehicleIn");
        } else {
            response.sendRedirect("VehicleIn");
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Vehicle check-in controller";
    }
}
