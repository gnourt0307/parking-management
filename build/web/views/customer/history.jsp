<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="utils.FormatCurrency" %>

<!doctype html>
<html lang="en">

    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>Parking History</title>
        <link rel="stylesheet" href="static/css/style.css" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />
    </head>

    <body>
        <div class="admin-layout">
            <!-- Left Sidebar -->
            <jsp:include page="../includes/sidebar_customer.jsp" />

            <!-- Main Content Area -->
            <main class="admin-main">
                <div class="container main-content width-full-max-none">
                    <h2><i class="fa-solid fa-clipboard-list"></i> Parking History & Invoices</h2>

                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>Ticket ID</th>
                                <th>Date</th>
                                <th>License Plate</th>
                                <th>Check-In</th>
                                <th>Check-Out</th>
                                <th>Fee</th>
                                <th>Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="t" items="${ticketHistory}">
                                <tr>
                                    <td>${t.ticketCode}</td>
                                    <td>
                                        ${t.entryTime.toLocalDate()}
                                    </td>
                                    <td>${t.licensePlate}</td>
                                    <td>
                                        ${t.entryTime.toLocalTime().withNano(0)}
                                    </td>
                                    <td>
                                        ${not empty t.transaction ? t.transaction.exitTime.toLocalTime().withNano(0) : '--:--'}
                                    </td>
                                    <td>
                                        <c:if test="${not empty t.transaction}">
                                            ${FormatCurrency.formatVND(t.transaction.totalAmount)}
                                        </c:if>
                                        <c:if test="${empty t.transaction}">
                                            -
                                        </c:if>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${t.status eq 'COMPLETED' || t.status eq 'Completed'}">
                                                <span class="text-success fw-bold">Paid</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="text-danger fw-bold">Parking</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty ticketHistory}">
                                <tr>
                                    <td colspan="7" class="text-center">No history found.</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </main>
        </div>
        <script src="static/js/admin_edit.js"></script>
        <jsp:include page="../includes/footer.jsp" />
    </body>

</html>