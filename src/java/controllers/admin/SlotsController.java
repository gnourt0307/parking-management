/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controllers.admin;

import dal.SlotDAO;
import dal.VehicleTypeDAO;
import dal.ZoneDAO;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Slot;
import models.Ticket;
import models.User;
import models.VehicleType;
import models.Zone;
import java.util.Map;

/**
 *
 * @author Admin
 */
public class SlotsController extends HttpServlet {

    private SlotDAO slotDAO;
    private ZoneDAO zoneDAO;
    private VehicleTypeDAO typeDAO;
    private dal.TicketDAO ticketDAO;

    @Override
    public void init() throws ServletException {
        slotDAO = new SlotDAO();
        zoneDAO = new ZoneDAO();
        typeDAO = new VehicleTypeDAO();
        ticketDAO = new dal.TicketDAO();
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     * 
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
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
            if (user.getRoleID() != 1) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        }

        // Handle search and filter parameters
        String searchKeyword = request.getParameter("search");
        String zoneFilter = request.getParameter("zoneFilter");

        List<Slot> slots = slotDAO.getAllSlots(searchKeyword, zoneFilter);
        List<Zone> zones = zoneDAO.getAllZones();
        List<VehicleType> types = typeDAO.getAllTypes();
        Map<Integer, Ticket> activeTickets = ticketDAO.getActiveTicketsMap();

        request.setAttribute("slots", slots);
        request.setAttribute("zones", zones);
        request.setAttribute("types", types);
        request.setAttribute("activeTickets", activeTickets);
        request.setAttribute("searchKeyword", searchKeyword);
        request.setAttribute("zoneFilter", zoneFilter);

        rd = request.getRequestDispatcher("views/admin/slot_list.jsp");
        rd.forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     * 
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRoleID() != 1) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String action = request.getParameter("action");
        if (action == null) {
            response.sendRedirect("Slots");
            return;
        }

        try {
            if (action.equals("add")) {
                int zoneID = Integer.parseInt(request.getParameter("zoneID"));
                String slotName = request.getParameter("slotName");
                int typeID = Integer.parseInt(request.getParameter("typeID"));
                String status = request.getParameter("status");

                if (slotDAO.checkSlotExist(zoneID, slotName)) {
                    session.setAttribute("errorMsg", "Slot '" + slotName + "' already exists in this Zone.");
                } else {
                    boolean success = slotDAO.addSlot(zoneID, slotName, typeID, status);
                    if (success) {
                        session.setAttribute("successMsg", "Slot added successfully.");
                    } else {
                        session.setAttribute("errorMsg", "Failed to add slot.");
                    }
                }
            } else if (action.equals("edit")) {
                int slotID = Integer.parseInt(request.getParameter("slotID"));
                int zoneID = Integer.parseInt(request.getParameter("zoneID"));
                String slotName = request.getParameter("slotName");
                int typeID = Integer.parseInt(request.getParameter("typeID"));
                String status = request.getParameter("status");

                if (slotDAO.checkSlotExistForUpdate(zoneID, slotName, slotID)) {
                    session.setAttribute("errorMsg", "Slot '" + slotName + "' already exists in this Zone.");
                } else {
                    boolean success = slotDAO.updateSlot(slotID, zoneID, slotName, typeID, status);
                    if (success) {
                        session.setAttribute("successMsg", "Slot updated successfully.");
                    } else {
                        session.setAttribute("errorMsg", "Failed to update slot.");
                    }
                }
            } else if (action.equals("delete")) {
                int slotID = Integer.parseInt(request.getParameter("slotID"));
                boolean success = slotDAO.deleteSlot(slotID);
                if (success) {
                    session.setAttribute("successMsg", "Slot deleted successfully.");
                } else {
                    session.setAttribute("errorMsg", "Failed to delete slot, it might be in use.");
                }
            }
        } catch (Exception e) {
            session.setAttribute("errorMsg", "Invalid input Data.");
        }

        response.sendRedirect("Slots");
    }

    /**
     * Returns a short description of the servlet.
     * 
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Slot Management Controller";
    }
}
