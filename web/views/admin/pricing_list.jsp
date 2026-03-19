<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="java.util.List" %>
<%@page import="java.util.ArrayList" %>
<%@page import="models.Pricing" %>
<%@page import="models.VehicleType" %>
<%@page import="utils.FormatCurrency" %>
<!doctype html>
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>Pricing Configuration</title>
        <link rel="stylesheet" href="static/css/style.css" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />
    </head>
    <body>
        <div class="admin-layout">
            <!-- Left Sidebar -->
            <jsp:include page="../includes/sidebar_admin.jsp"/>

            <!-- Main Content Area -->
            <main class="admin-main">
                <div class="container main-content full-width">
                    <div class="page-header">
                        <h2><i class="fa-solid fa-money-bill"></i> Pricing Configuration</h2>
                        <button class="btn btn-success" onclick="openAddModal()"><i class="fa-solid fa-plus"></i> Add New Pricing Rule</button>
                    </div>

                    <c:if test="${not empty successMsg}">
                        <div class="alert alert-success">${successMsg}</div>
                        <c:remove var="successMsg" scope="request" />
                    </c:if>
                    <c:if test="${not empty errorMsg}">
                        <div class="alert alert-danger">${errorMsg}</div>
                        <c:remove var="errorMsg" scope="request" />
                    </c:if>

                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>Vehicle Type</th>
                                <th>Hourly Rate</th>
                                <th>Daily Rate</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:if test="${requestScope.pricingList != null && !requestScope.pricingList.isEmpty()}">
                                <c:forEach var="pricing" items="${requestScope.pricingList}">
                                    <tr>
                                        <td>${pricing.vehicleType.typeName}</td>
                                        <td>${FormatCurrency.formatVND(pricing.hourlyRate)}</td>
                                        <td>${FormatCurrency.formatVND(pricing.dailyRate)}</td>
                                        <td>
                                            <button class="btn btn-sFm" onclick="openEditModal('${pricing.vehicleType.typeName}')"><i class="fa-solid fa-pen-to-square"></i> Edit</button>
                                            <form action="Pricing" method="POST" style="display:inline;"
                                                  onsubmit="return confirm('Delete this pricing?');">
                                                <input type="hidden" name="action" value="delete">
                                                <input type="hidden" name="hourlyRate" value="${pricing.hourlyRate}">
                                                <input type="hidden" name="dailyRate" value="${pricing.dailyRate}">
                                                <input type="hidden" name="vehicleTypeName" value="${pricing.vehicleType.typeName}">
                                                <button type="submit" class="btn btn-danger btn-sm"><i class="fa-solid fa-trash"></i>
                                                    Delete</button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:if>
                        </tbody>
                    </table>
                </div>

                <jsp:include page="../includes/footer.jsp"/>
            </main>
        </div>

        <!-- Add Pricing Modal -->
        <div id="addModal" class="modal">
            <div class="modal-content">
                <span class="close-btn" onclick="closeModal('addModal')">&times;</span>
                <h3>Add New Pricing</h3>
                <form action="Pricing" method="POST">
                    <input type="hidden" name="action" value="add">
                    <div class="form-group">
                        <label>Vehicle Type</label>
                        <input type="text" name="vehicleTypeName" class="filter-input" required style="width:100%;"
                               placeholder="Vehicle type...">
                    </div>
                    <div class="form-group">
                        <label>Hourly Rate</label>
                        <input type="number" name="hourlyRate" required>
                    </div>

                    <div class="form-group">
                        <label>Daily Rate</label>
                        <input type="number" name="dailyRate" required>
                    </div>
                    <button type="submit" class="btn btn-success" style="width: 100%;">Save Pricing</button>
                </form>
            </div>
        </div>

        <!-- Edit Pricing Modal -->
        <div id="editModal" class="modal">
            <div class="modal-content">
                <span class="close-btn" onclick="closeModal('editModal')">&times;</span>
                <h3>Edit Pricing</h3>
                <form action="Pricing" method="POST">
                    <input type="hidden" name="action" value="edit">
                    <input type="hidden" name="pricing" id="editPricing">
                    <div class="form-group">
                        <label>Vehicle Type</label>
                        <input type="text" name="vehicleTypeName" class="filter-input" required style="width:100%;"
                               placeholder="Vehicle type..." id="vehicleType" value="${requestScope.vehicleTypeName}" readonly>
                    </div>
                    <div class="form-group">
                        <label>Hourly Rate</label>
                        <input type="number" name="hourlyRate" id="hourlyRate" required>
                    </div>

                    <div class="form-group">
                        <label>Daily Rate</label>
                        <input type="number" name="dailyRate" id="dailyRate" required>
                    </div>
                    <button type="submit" class="btn btn-success" style="width: 100%;">Update Pricing</button>
                </form>
            </div>
        </div>   
        <script src="static/js/admin_edit.js"></script>
    </body>
</html>



