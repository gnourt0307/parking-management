/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controllers.staff;

import dal.SlotDAO;
import dal.TicketDAO;
import dal.TransactionDAO;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import utils.FormatCurrency;
import models.Ticket;
import models.User;

/**
 *
 * @author Admin
 */
public class VehicleOutController extends HttpServlet {

    private static final DateTimeFormatter ENTRY_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss dd/M/yyyy");
    private static final BigDecimal LOST_TICKET_FEE = new BigDecimal("50000");

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
        TicketDAO ticketDAO = new TicketDAO();
        request.setAttribute("activeTickets", ticketDAO.getActiveTicketsList());
        
        dal.VehicleTypeDAO vtDAO = new dal.VehicleTypeDAO();
        request.setAttribute("vehicleTypes", vtDAO.getAllTypes());
        
        rd = request.getRequestDispatcher("views/staff/vehicle_out.jsp");
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
            response.sendRedirect("VehicleOut");
            return;
        }

        TicketDAO ticketDAO = new TicketDAO();

        try {
            if (action.equals("search")) {
                String plateSearch = request.getParameter("plateSearch");
                Ticket ticket = ticketDAO.findActiveTicketByPlate(plateSearch);

                if (ticket != null) {
                    // Tinh tien tam thoi theo so gio, lam tron len 1 gio
                    LocalDateTime now = LocalDateTime.now();
                    Duration duration = Duration.between(ticket.getEntryTime(), now);
                    long minutes = duration.toMinutes();
                    long hours = minutes / 60;
                    if (minutes % 60 != 0) {
                        hours++;
                    }
                    if (hours <= 0) {
                        hours = 1;
                    }

                    BigDecimal hourly = ticket.getHourlyRate();
                    if (hourly == null) {
                        hourly = BigDecimal.ZERO;
                    }
                    BigDecimal totalAmount = hourly.multiply(BigDecimal.valueOf(hours));

                    request.setAttribute("ticket", ticket);
                    request.setAttribute("baseAmount", FormatCurrency.formatVND(totalAmount));
                    request.setAttribute("lostFee", FormatCurrency.formatVND(LOST_TICKET_FEE));
                    request.setAttribute("totalWithLost", FormatCurrency.formatVND(totalAmount.add(LOST_TICKET_FEE)));
                    request.setAttribute("totalAmount", FormatCurrency.formatVND(totalAmount)); // giu tuong thich cho khac neu co
                    if (ticket.getEntryTime() != null) {
                        String formatted = ticket.getEntryTime().format(ENTRY_TIME_FORMATTER);
                        request.setAttribute("entryTimeFormatted", formatted);
                    }
                }
                request.setAttribute("activeTickets", ticketDAO.getActiveTicketsList());
                dal.VehicleTypeDAO vtDAO = new dal.VehicleTypeDAO();
                request.setAttribute("vehicleTypes", vtDAO.getAllTypes());
                
                RequestDispatcher rd = request.getRequestDispatcher("views/staff/vehicle_out.jsp");
                rd.forward(request, response);
                return;
            } else if (action.equals("confirm")) {
                int ticketID = Integer.parseInt(request.getParameter("ticketID"));
                boolean lostTicket = "true".equalsIgnoreCase(request.getParameter("lostTicket"));

                Ticket ticket = ticketDAO.getTicketById(ticketID);
                if (ticket == null || !"Active".equalsIgnoreCase(ticket.getStatus())) {
                    session.setAttribute("errorMsg", "Ticket not found or already completed.");
                    response.sendRedirect("VehicleOut");
                    return;
                }

                LocalDateTime now = LocalDateTime.now();
                Duration duration = Duration.between(ticket.getEntryTime(), now);
                long minutes = duration.toMinutes();
                long hours = minutes / 60;
                if (minutes % 60 != 0) {
                    hours++;
                }
                if (hours <= 0) {
                    hours = 1;
                }

                BigDecimal hourly = ticket.getHourlyRate();
                if (hourly == null) {
                    hourly = BigDecimal.ZERO;
                }
                BigDecimal totalAmount = hourly.multiply(BigDecimal.valueOf(hours));
                if (lostTicket) {
                    totalAmount = totalAmount.add(LOST_TICKET_FEE);
                }

                // Cap nhat trang thai ve va tao transaction
                boolean statusUpdated = ticketDAO.updateTicketStatus(ticketID, "Completed");
                TransactionDAO transDAO = new TransactionDAO();
                boolean transCreated = transDAO.createTransaction(ticketID, totalAmount, user.getUserID());

                // Mo lai slot cho xe khac
                SlotDAO slotDAO = new SlotDAO();
                slotDAO.setSlotStatus(ticket.getSlotID(), "Available");

                if (statusUpdated && transCreated) {
                    session.setAttribute("successMsg", "Payment completed successfully.");
                } else {
                    session.setAttribute("errorMsg", "Payment failed.");
                }
                response.sendRedirect("VehicleOut");
                return;
            } else if (action.equals("lost")) {
                int ticketID = Integer.parseInt(request.getParameter("ticketID"));
                Ticket ticket = ticketDAO.getTicketById(ticketID);
                if (ticket == null || !"Active".equalsIgnoreCase(ticket.getStatus())) {
                    session.setAttribute("errorMsg", "Ticket not found or already completed.");
                    response.sendRedirect("VehicleOut");
                    return;
                }

                // Tinh tien gui xe hien tai + phu phi mat ve
                LocalDateTime now = LocalDateTime.now();
                Duration duration = Duration.between(ticket.getEntryTime(), now);
                long minutes = duration.toMinutes();
                long hours = minutes / 60;
                if (minutes % 60 != 0) {
                    hours++;
                }
                if (hours <= 0) {
                    hours = 1;
                }

                BigDecimal hourly = ticket.getHourlyRate();
                if (hourly == null) {
                    hourly = BigDecimal.ZERO;
                }
                BigDecimal baseAmount = hourly.multiply(BigDecimal.valueOf(hours));
                BigDecimal totalAmount = baseAmount.add(LOST_TICKET_FEE);

                boolean statusUpdated = ticketDAO.updateTicketStatus(ticketID, "Completed");
                TransactionDAO transDAO = new TransactionDAO();
                boolean transCreated = transDAO.createTransaction(ticketID, totalAmount, user.getUserID());

                SlotDAO slotDAO = new SlotDAO();
                slotDAO.setSlotStatus(ticket.getSlotID(), "Available");

                if (statusUpdated && transCreated) {
                    session.setAttribute("successMsg", "Lost ticket processed successfully.");
                } else {
                    session.setAttribute("errorMsg", "Failed to process lost ticket.");
                }
                response.sendRedirect("VehicleOut");
                return;
            }
        } catch (Exception e) {
            session.setAttribute("errorMsg", "Invalid data provided.");
        }

        response.sendRedirect("VehicleOut");
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Vehicle check-out controller";
    }
}
