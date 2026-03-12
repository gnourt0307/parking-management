<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="en">

    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>Manage Users</title>
        <link rel="stylesheet" href="static/css/style.css" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />
    </head>

    <body>
        <div class="admin-layout">
            <!-- Left Sidebar -->
            <jsp:include page="../includes/sidebar_admin.jsp" />

            <!-- Main Content Area -->
            <main class="admin-main">
                <div class="container main-content full-width">
                    <div class="page-header">
                        <h2><i class="fa-solid fa-users"></i> User Management</h2>
                        <div class="d-flex gap-10 align-items-center">
                            <form action="Users" method="GET" style="display: flex; gap: 5px;">
                                <input type="text" name="searchKeyword" value="${searchKeyword}" placeholder="Enter Username..." class="search-input" />

                                <button type="submit" class="btn btn-search"><i class="fa-solid fa-magnifying-glass"></i> Search</button>
                                <a href="Users" class="btn btn-secondary" style="text-decoration: none; padding: 8px 12px; border-radius:4px; border:1px solid #ccc; background:#f4f4f4; color:#333;">Clear</a>
                            </form>
                            <button class="btn btn-success" onclick="openAddModal()"><i class="fa-solid fa-plus"></i> Add New User</button>
                        </div>
                    </div>

                    <table class="data-table">
                        <c:if test="${not empty sessionScope.successMsg}">
                            <div class="alert alert-success">${sessionScope.successMsg}</div>
                            <c:remove var="successMsg" scope="session" />
                        </c:if>
                        <c:if test="${not empty sessionScope.errorMsg}">
                            <div class="alert alert-danger">${sessionScope.errorMsg}</div>
                            <c:remove var="errorMsg" scope="session" />
                        </c:if>
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Full Name</th>
                                <th>Username</th>
                                <th>Password</th>
                                <th>Role</th>
                                <th>Status</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${empty users}">
                                    <tr>
                                        <td colspan="7" style="text-align: center;">No users found.</td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach items="${users}" var="user">
                                        <tr>
                                            <td>${user.userID}</td>
                                            <td>${user.fullName}</td>
                                            <td>${user.username}</td>
                                            <td>${user.password}</td>
                                            <td>${user.role.roleName}</td>
                                            <td>${user.status}</td>
                                            <td>
                                                <button class="btn btn-sm" onclick="openEditModal('${user.userID}','${user.fullName}','${user.username}','${user.password}','${user.role.roleID}','${user.status}')"><i class="fa-solid fa-pen-to-square"></i> Edit</button>
                                                <button class="btn btn-danger btn-sm"><i class="fa-solid fa-trash"></i> Delete</button>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>
                </div>

                <jsp:include page="../includes/footer.jsp" />
            </main>
        </div>

        <!-- Add User Modal -->
        <div id="addModal" class="modal">
            <div class="modal-content">
                <span class="close-btn" onclick="closeModal('addModal')">&times;</span>
                <h3>Add New User</h3>
                <form action="Users" method="POST">
                    <input type="hidden" name="action" value="add">
                    <div class="form-group">
                        <label>Full Name</label>
                        <input type="text" name="fullName" required>
                    </div>
                    <div class="form-group">
                        <label>User Name</label>
                        <input type="text" name="username" required>
                    </div>
                    <div class="form-group">
                        <label>Password</label>
                        <input type="password" name="password" required>
                    </div>
                    <div class="form-group">
                        <label>Role</label>
                        <select name="role">
                            <c:forEach items="${roles}" var="r">
                                <option value="${r.roleID}">${r.roleName}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>Status</label>
                        <select name="status" required>
                            <option value="Active">Active</option>
                            <option value="Inactive">Inactive</option>
                        </select>
                    </div>
                    <button type="submit" class="btn btn-success" style="width: 100%;">Save User</button>
                </form>
            </div>
        </div>  

        <!-- Edit User Modal -->
        <div id="editModal" class="modal">
            <div class="modal-content">
                <span class="close-btn" onclick="closeModal('editModal')">&times;</span>
                <h3>Edit User</h3>
                <form action="Users" method="POST">
                    <input type="hidden" name="action" value="edit">
                    <input type="hidden" name="userID" id="editUser">
                    <div class="form-group">
                        <label>Full name</label>
                        <input type="text" name="fullName" id="fullName" required>
                    </div>
                    <div class="form-group">
                        <label>Username</label>
                        <input type="text" name="username" id="username" required>
                    </div>

                    <div class="form-group">
                        <label>Password</label>
                        <input type="text" name="password" id="password" required>
                    </div>
                    <div class="form-group">
                        <label>Role</label>
                        <select name="role" id="role" required>
                            <c:forEach items="${roles}" var="r">
                                <option value="${r.roleID}">${r.roleName}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>Status</label>
                        <select name="status" id="status" required>
                            <option value="Active">Active</option>
                            <option value="Inactive">Inactive</option>
                        </select>
                    </div>
                    <button type="submit" class="btn btn-success" style="width: 100%;">Update User</button>
                </form>
            </div>
        </div>
        <script src="static/js/admin_edit.js"></script>
    </body>

</html>

