/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controllers.admin;

import dal.ZoneDAO;
import dal.VehicleTypeDAO;
import models.VehicleType;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.User;
import models.Zone;

public class ZonesController extends HttpServlet {



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

        // Initialize DAOs
        ZoneDAO zoneDAO = new ZoneDAO();
        VehicleTypeDAO typeDAO = new VehicleTypeDAO();

        String zoneFilter = request.getParameter("zoneFilter");
        String vehicleTypeSearch = request.getParameter("search");

        List<Zone> allZones = zoneDAO.getAllZones();
        List<Zone> zones = zoneDAO.getZonesByFilters(zoneFilter, vehicleTypeSearch);
        List<VehicleType> vehicleTypesList = typeDAO.getAllTypes();

        request.setAttribute("vehicleTypesList", vehicleTypesList);

        request.setAttribute("allZones", allZones);
        request.setAttribute("zones", zones);
        request.setAttribute("searchKeyword", vehicleTypeSearch);

        rd = request.getRequestDispatcher("views/admin/zone_list.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null || user.getRoleID() != 1) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        // Initialize DAOs
        ZoneDAO zoneDAO = new ZoneDAO();
        VehicleTypeDAO typeDAO = new VehicleTypeDAO();

        String action = request.getParameter("action");
        if (action == null) {
            response.sendRedirect("Zones");
            return;
        }

        try {
            if (action.equals("add")) {
                String zoneName = request.getParameter("zoneName");
                String description = request.getParameter("description");
                String vehicleTypeName = request.getParameter("vehicleTypeName");

                int typeID = typeDAO.findOrCreateType(vehicleTypeName);

                if (zoneDAO.checkZoneExist(zoneName)) {
                    session.setAttribute("errorMsg", "Zone Name already exists.");
                } else {
                    Zone newZone = new Zone(0, zoneName, description);
                    newZone.setTypeID(typeID);
                    boolean success = zoneDAO.addZone(newZone);
                    if (success) {
                        session.setAttribute("successMsg", "Zone added successfully.");
                    } else {
                        session.setAttribute("errorMsg", "Failed to add zone.");
                    }
                }
            } else if (action.equals("edit")) {
                int zoneID = Integer.parseInt(request.getParameter("zoneID"));
                String zoneName = request.getParameter("zoneName");
                String description = request.getParameter("description");
                String vehicleTypeName = request.getParameter("vehicleTypeName");

                int typeID = typeDAO.findOrCreateType(vehicleTypeName);

                if (zoneDAO.checkZoneExistForUpdate(zoneName, zoneID)) {
                    session.setAttribute("errorMsg", "Zone Name already exists in another record.");
                } else {
                    Zone updatedZone = new Zone(zoneID, zoneName, description);
                    updatedZone.setTypeID(typeID);
                    boolean success = zoneDAO.editZone(updatedZone);
                    if (success) {
                        session.setAttribute("successMsg", "Zone updated successfully.");
                    } else {
                        session.setAttribute("errorMsg", "Failed to update zone.");
                    }
                }
            } else if (action.equals("delete")) {
                int zoneID = Integer.parseInt(request.getParameter("zoneID"));
                String resultMsg = zoneDAO.deleteZone(zoneID);
                if ("success".equals(resultMsg)) {
                    session.setAttribute("successMsg", "Zone deleted successfully.");
                } else {
                    session.setAttribute("errorMsg", resultMsg);
                }
            }
        } catch (Exception e) {
            session.setAttribute("errorMsg", "Invalid input Data.");
        }

        response.sendRedirect("Zones");
    }

    @Override
    public String getServletInfo() {
        return "Zone Management Controller";
    }
}
