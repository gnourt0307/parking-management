<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
                            <div class="value">$1,250</div> <!<!-- Fix for auto -->
                        </div>
                    </div>

                    <h3><i class="fa-solid fa-clock-rotate-left"></i> Recent Activity</h3> <!<!-- Fix for auto -->
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>Time</th>
                                <th>License Plate</th>
                                <th>Action</th>
                                <th>Staff</th>
                            </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${empty recentActivities}">
                                <tr>
                                    <td colspan="4" style="text-align: center;">No recent activity found.</td>
                                </tr>
                            </c:when>
                            <c:otherwise>
                                <c:forEach items="${recentActivities}" var="activity">
                                    <tr>
                                        <td>${activity.formattedTime}</td>
                                        <td>${activity.licensePlate}</td>
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
