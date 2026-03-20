<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="utils.FormatCurrency" %>
<!doctype html>
<html lang="en">

    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>Admin Dashboards</title>
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
                    <h2><i class="fa-solid fa-chart-pie"></i> Dashboard Overview</h2>

                    <div class="dashboard-cards">
                        <div class="card">
                            <h3>Total Capacity</h3>
                            <div class="value">${totalCapacity}</div> <!<!-- Fix for auto -->
                        </div>
                        <div class="card">
                            <h3>Vehicles Parked</h3>
                            <div class="value">${vehiclesParked}</div> <!<!-- Fix for auto -->
                        </div>
                        <div class="card">
                            <h3>Available Slots</h3>
                            <div class="value text-success">${availableSlots}</div> <!<!-- Fix for auto -->
                        </div>
                        <div class="card">
                            <h3>Today's Revenue</h3>
                            <div class="value">${FormatCurrency.formatVND(todaysRevenue)}</div> <!<!-- Fix for auto -->
                        </div>
                    </div>

                    <h3><i class="fa-solid fa-clock-rotate-left"></i> Recent Activity</h3>
                    
                    <form action="Dashboard" method="GET" class="filter-section d-flex align-items-center" style="gap: 15px; margin-bottom: 20px; flex-wrap: wrap;">
                        <div style="display:flex; align-items:center; gap:8px;">
                            <label for="staffFilter" style="margin:0; white-space:nowrap;">Staff:</label>
                            <select name="staffFilter" id="staffFilter" class="filter-select" style="min-width: 150px;">
                                <option value="all" ${empty param.staffFilter or param.staffFilter == 'all' ? 'selected' : ''}>All Staff</option>
                                <c:forEach items="${staffList}" var="st">
                                    <c:set var="strId" value="${st.userID}" />
                                    <option value="${st.userID}" ${param.staffFilter == strId ? 'selected' : ''}>${st.fullName}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div style="display:flex; align-items:center; gap:8px;">
                            <label for="actionFilter" style="margin:0; white-space:nowrap;">Activity:</label>
                            <select name="actionFilter" id="actionFilter" class="filter-select" style="min-width: 150px;">
                                <option value="all" ${empty param.actionFilter or param.actionFilter == 'all' ? 'selected' : ''}>All Types</option>
                                <option value="Check-In" ${param.actionFilter == 'Check-In' ? 'selected' : ''}>Check-In</option>
                                <option value="Check-Out" ${param.actionFilter == 'Check-Out' ? 'selected' : ''}>Check-Out</option>
                            </select>
                        </div>
                        <div style="display:flex; align-items:center; gap:8px;">
                            <label for="dateFrom" style="margin:0; white-space:nowrap;">From:</label>
                            <input type="date" name="dateFrom" id="dateFrom" class="filter-input" value="${param.dateFrom}">
                        </div>
                        <div style="display:flex; align-items:center; gap:8px;">
                            <label for="dateTo" style="margin:0; white-space:nowrap;">To:</label>
                            <input type="date" name="dateTo" id="dateTo" class="filter-input" value="${param.dateTo}">
                        </div>

                        <div style="display:flex; gap:10px;">
                            <button type="submit" class="btn btn-search">Filter</button>
                            <a href="Dashboard" class="btn btn-secondary" style="text-decoration: none; padding: 8px 12px; border-radius:4px; border:1px solid #ccc; background:#f4f4f4; color:#333; display: flex; align-items: center; height: 100%; box-sizing: border-box;">Clear</a>
                        </div>
                    </form>

                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>Time</th>
                                <th>License Plate</th>
                                <th>Vehicle Type</th>
                                <th>Slot</th>
                                <th>Zone</th>
                                <th>Action</th>
                                <th>Staff</th>
                                <th>Amount</th>
                            </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${empty recentActivities}">
                                <tr>
                                    <td colspan="8" style="text-align: center;">No recent activity found.</td>
                                </tr>
                            </c:when>
                            <c:otherwise>
                                <c:forEach items="${recentActivities}" var="activity">
                                    <tr>
                                        <td>${activity.formattedTime}</td>
                                        <td>${activity.licensePlate}</td>
                                        <td style="text-transform: capitalize;">${activity.vehicleType}</td>
                                        <td>${activity.slot}</td>
                                        <td>${activity.zone}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${activity.actionType == 'Check-In'}">
                                                    <span class="alert-success badge">${activity.actionType}</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="alert-danger badge">${activity.actionType}</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>${activity.staffName}</td>
                                        <td style="font-weight: bold; color: ${activity.actionType == 'Check-Out' ? '#e74c3c' : '#333'}">
                                            ${activity.formattedAmount}
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
        <script src="static/js/admin_edit.js"></script>
    </body>

</html>
