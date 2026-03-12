/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controllers.admin;

import dal.RoleDAO;
import dal.UserDAO;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import models.Role;
import models.User;

/**
 *
 * @author Admin
 */
public class UsersController extends HttpServlet {

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
            out.println("<title>Servlet UsersController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UsersController at " + request.getContextPath() + "</h1>");
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

        // 1. Kiểm tra đăng nhập và quyền
        if (user == null) {
            rd = request.getRequestDispatcher("views/auth/login.jsp");
            rd.forward(request, response);
            return; // Phải có return ở đây để code dừng lại, không chạy tiếp xuống dưới
        } else if (user.getRoleID() != 1) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        // 2. Xử lý tìm kiếm
        UserDAO uDAO = new UserDAO();
        String searchKeyword = request.getParameter("searchKeyword"); // Lấy từ khóa từ giao diện
        List<User> users;

        // Nếu người dùng có nhập từ khóa tìm kiếm
        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            users = uDAO.getListUsersBySearch(searchKeyword.trim());
        } else {
            // Nếu không nhập gì thì lấy toàn bộ
            users = uDAO.getListUsers();
        }

        RoleDAO rDAO = new RoleDAO();
        List<Role> roles = rDAO.getAllRoles();

        // 3. Đẩy dữ liệu sang JSP
        request.setAttribute("users", users);
        request.setAttribute("roles", roles);
        request.setAttribute("searchKeyword", searchKeyword); // Gửi lại từ khóa để giữ chữ trên ô input

        rd = request.getRequestDispatcher("views/admin/user_list.jsp");
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
        if (user == null || user.getRoleID() != 1) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String action = request.getParameter("action");
        if (action == null) {
            response.sendRedirect("Users");
            return;
        }

        try {
            if (action.equals("add")) {

                // 1. Lấy dữ liệu từ form JSP gửi lên
                String fullName = request.getParameter("fullName");
                String userName = request.getParameter("userName"); // name trên form là userName
                String password = request.getParameter("password");
                String phone = request.getParameter("phone"); // Lấy phone (nếu bạn thêm vào form)

                // Nếu form chưa có phone thì cho tạm chuỗi rỗng để không bị lỗi null
                if (phone == null) {
                    phone = "";
                }

                int roleID = Integer.parseInt(request.getParameter("role"));
                String status = request.getParameter("status");

                // 2. Đóng gói vào object User
                User newUser = new User();
                newUser.setFullName(fullName);
                newUser.setUsername(userName);
                newUser.setPassword(password);
                newUser.setPhone(phone);
                newUser.setRoleID(roleID);     // Set RoleID
                newUser.setStatus(status);     // Set Status

                // 3. Gọi hàm insertUser có sẵn
                UserDAO uDAO = new UserDAO();
                boolean success = uDAO.insertUser(newUser);

                // 4. Báo kết quả
                if (success) {
                    session.setAttribute("successMsg", "Thêm User thành công!");
                } else {
                    session.setAttribute("errorMsg", "Thêm thất bại (Có thể Username đã tồn tại).");
                }
                response.sendRedirect("Users");
                return;
            } else if (action.equals("edit")) {
                // 1. Lấy dữ liệu từ form Edit JSP gửi lên
                int userID = Integer.parseInt(request.getParameter("userID"));
                String fullName = request.getParameter("fullName");
                String username = request.getParameter("username");
                String password = request.getParameter("password");
                int roleID = Integer.parseInt(request.getParameter("role"));
                String status = request.getParameter("status");

                // 2. Đóng gói vào object User
                User updateUser = new User();
                updateUser.setUserID(userID);
                updateUser.setFullName(fullName);
                updateUser.setUsername(username);
                updateUser.setPassword(password);
                updateUser.setRoleID(roleID);
                updateUser.setStatus(status);

                // 3. Gọi hàm DAO để update xuống DB
                UserDAO uDAO = new UserDAO();
                boolean success = uDAO.updateUser(updateUser);

                // 4. Báo kết quả và điều hướng
                if (success) {
                    session.setAttribute("successMsg", "Cập nhật User thành công!");
                } else {
                    session.setAttribute("errorMsg", "Cập nhật thất bại. Vui lòng thử lại!");
                }
                response.sendRedirect("Users");
                return; // Thêm return để kết thúc nhánh này
            } else if (action.equals("delete")) {
//                int slotID = Integer.parseInt(request.getParameter("slotID"));
//                boolean success = slotDAO.deleteSlot(slotID);
//                if (success) {
//                    session.setAttribute("successMsg", "Slot deleted successfully.");
//                } else {
//                    session.setAttribute("errorMsg", "Failed to delete slot, it might be in use.");
//                }
            }
        } catch (Exception e) {
            session.setAttribute("errorMsg", "Invalid input Data.");
        }

        response.sendRedirect("Users");
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
