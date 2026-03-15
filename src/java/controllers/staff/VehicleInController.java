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

        // Lấy danh sách slot để staff chọn khi check-in
        SlotDAO slotDAO = new SlotDAO();
        List<Slot> slots = slotDAO.getAllSlots(null, null);
        request.setAttribute("slots", slots);

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
                Ticket ticket = new Ticket();

                // Đơn giản tạo mã vé theo timestamp
                ticket.setTicketCode("VEX-" + System.currentTimeMillis());
                ticket.setLicensePlate(licensePlate);
                ticket.setTypeID(typeID);
                ticket.setSlotID(slotID);
                ticket.setCustomerID(null); // khách vãng lai
                ticket.setCreatedBy(user.getUserID());

                boolean created = ticketDAO.createTicket(ticket);

                if (created) {
                    // Đánh dấu slot đang được sử dụng
                    SlotDAO slotDAO = new SlotDAO();
                    slotDAO.setSlotStatus(slotID, "OCCUPIED");
                    session.setAttribute("successMsg", "Check-in thành công.");
                } else {
                    session.setAttribute("errorMsg", "Check-in thất bại. Vui lòng thử lại.");
                }
            } catch (Exception e) {
                session.setAttribute("errorMsg", "Dữ liệu không hợp lệ.");
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
