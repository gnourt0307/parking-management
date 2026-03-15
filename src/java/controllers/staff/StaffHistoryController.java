package controllers.staff;

import dal.TicketDAO;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import models.StaffTicketHistory;
import models.User;

public class StaffHistoryController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRoleID() != 2) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        int staffID = user.getUserID();
        String search = request.getParameter("search");

        TicketDAO ticketDAO = new TicketDAO();
        List<StaffTicketHistory> histories = ticketDAO.getStaffHistory(staffID, search);

        request.setAttribute("histories", histories);
        request.setAttribute("searchKeyword", search);

        RequestDispatcher rd = request.getRequestDispatcher("views/staff/history.jsp");
        rd.forward(request, response);
    }
}

