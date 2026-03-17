<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!doctype html>
<html lang="en">

    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>Staff Ticket History</title>
        <link rel="stylesheet" href="static/css/style.css" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />
    </head>

    <body>
        <div class="admin-layout">
            <jsp:include page="../includes/sidebar_staff.jsp" />

            <main class="admin-main">
                <div class="container main-content width-full-max-none">
                    <h2><i class="fa-solid fa-clock-rotate-left"></i> My Ticket History</h2>

                    <c:if test="${not empty sessionScope.successMsg}">
                        <div class="alert alert-success">${sessionScope.successMsg}</div>
                        <c:remove var="successMsg" scope="session" />
                    </c:if>
                    <c:if test="${not empty sessionScope.errorMsg}">
                        <div class="alert alert-danger">${sessionScope.errorMsg}</div>
                        <c:remove var="errorMsg" scope="session" />
                    </c:if>

                    <div>
                        <form action="StaffHistory" method="get" style="display:flex; align-items:center; gap: 15px; margin-bottom: 20px; flex-grow: 1;">
                            <label for="search" style="font-weight:500;">Search:</label>
                            <input type="text" id="search" name="search" value="${searchKeyword}"
                                   placeholder="Ticket code or license plate..." class="search-input" />
                            <button type="submit" class="btn btn-search"><i class="fa-solid fa-magnifying-glass"></i> Search</button>
                        </form>
                    </div>

                    <div class="card">
                        <table class="data-table history-table">
                            <thead>
                                <tr>
                                    <th>STT</th>
                                    <th>Ticket Code</th>
                                    <th>License Plate</th>
                                    <th>Vehicle Type</th>
                                    <th>Entry Time</th>
                                    <th>Exit Time</th>
                                    <th>Action</th>
                                    <th>Status</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                    <c:when test="${empty histories}">
                                        <tr>
                                            <td colspan="9" style="text-align:center;">No records found.</td>
                                        </tr>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach items="${histories}" var="h" varStatus="st">
                                            <tr>
                                                <td>${st.index + 1}</td>
                                                <td>${h.ticketCode}</td>
                                                <td>${h.licensePlate}</td>
                                                <td>${h.vehicleType}</td>
                                                <td>${h.entryTimeFormatted}</td>
                                                <td>${h.exitTimeFormatted}</td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${h.actionType == 'CHECK-IN'}">
                                                            <span class="badge badge-info">Check-In</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="badge badge-success">Check-Out</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>${h.status}</td>
                                            </tr>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                            </tbody>
                        </table>
                    </div>
                </div>
            </main>
        </div>
        <jsp:include page="../includes/footer.jsp" />
    </body>

</html>

